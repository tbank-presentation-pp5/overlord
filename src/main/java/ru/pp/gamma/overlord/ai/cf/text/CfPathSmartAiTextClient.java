package ru.pp.gamma.overlord.ai.cf.text;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import ru.pp.gamma.overlord.ai.account.CfAccountService;
import ru.pp.gamma.overlord.ai.account.dto.CfAccount;
import ru.pp.gamma.overlord.ai.cf.text.dto.pathopenai.CfPathOpenAiInputMessageDto;
import ru.pp.gamma.overlord.ai.cf.text.dto.pathopenai.CfPathOpenAiRequestDto;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Slf4j
@Component
public class CfPathSmartAiTextClient {

    private static final String URL_TEMPLATE =
            "https://api.cloudflare.com/client/v4/accounts/%s/ai/run/%s";

    private final RestClient client;
    private final CfAccountService accountService;
    private final ObjectMapper objectMapper;

    public CfPathSmartAiTextClient(
            @Qualifier("aiRestClient") RestClient client,
            CfAccountService accountService,
            ObjectMapper objectMapper
    ) {
        this.client = client;
        this.accountService = accountService;
        this.objectMapper = objectMapper;
    }

    public String generate(String systemPrompt, String userPrompt, String modelId, int maxTokens) {
        CfAccount account = accountService.getAccount();

        JsonNode root = client.post()
                .uri(buildUrl(account.accountId(), modelId))
                .header("Authorization", "Bearer " + account.authToken())
                .contentType(APPLICATION_JSON)
                .body(buildBody(systemPrompt, userPrompt, maxTokens))
                .retrieve()
                .body(JsonNode.class);

        if (root == null) {
            throw new RuntimeException("CF Smart API returned null for model: " + modelId);
        }

        log.debug("CF Smart raw response for model [{}]: {}", modelId, root);

        return extractText(root, modelId);
    }

    private String extractText(JsonNode root, String modelId) {
        JsonNode result = root.path("result");

        // пытался все апишки парснуть пока воркает
        // ешё тестирую и комментарии для себя оставлю
        // 1. result.response - строка
        String fromResultStr = textNode(result, "response");
        if (nonBlank(fromResultStr)) {
            log.debug("Model [{}]: extracted via result.response (string)", modelId);
            if (fromResultStr.startsWith("{") || fromResultStr.startsWith("[")) {
                return toSafeJsonString(fromResultStr);
            }
            return fromResultStr;
        }

        // 2. result.response - массив
        JsonNode responseNode = result.path("response");
        if (responseNode.isArray() && !responseNode.isEmpty()) {
            JsonNode first = responseNode.get(0);

            if (first.isTextual()) {
                // 2a. Массив строк - конкатенируем
                String joined = joinStringArray(responseNode);
                if (nonBlank(joined)) {
                    log.debug("Model [{}]: extracted via result.response (string array)", modelId);
                    return toSafeJsonString(joined);
                }
            } else {
                // 2b. массив объектов - модель вернула JSON-структуру прямо в response
                //     сериализуем весь массив обратно в JSON-строку
                try {
                    String json = objectMapper.writeValueAsString(responseNode);
                    log.debug("Model [{}]: extracted via result.response (object array → JSON string)", modelId);
                    return toSafeJsonString(json);
                } catch (JsonProcessingException e) {
                    log.warn("Model [{}]: failed to serialize response array to JSON", modelId, e);
                }
            }
        }

        // 2c. result.response - объект (например, qwen возвращает {name, slides})
        if (responseNode.isObject()) {
            try {
                String json = objectMapper.writeValueAsString(responseNode);
                log.debug("Model [{}]: extracted via result.response (object → JSON string)", modelId);
                return toSafeJsonString(json);
            } catch (JsonProcessingException e) {
                log.warn("Model [{}]: failed to serialize response object to JSON", modelId, e);
            }
        }

        // 3–5. result.choices[].message.*
        JsonNode resultChoices = result.path("choices");
        if (resultChoices.isArray() && !resultChoices.isEmpty()) {
            String fromChoices = extractFromChoices(resultChoices, modelId);
            if (fromChoices != null) return fromChoices;
        }

        JsonNode rootChoices = root.path("choices");
        if (rootChoices.isArray() && !rootChoices.isEmpty()) {
            String fromRootChoices = extractFromChoices(rootChoices, modelId);
            if (fromRootChoices != null) return fromRootChoices;
        }

        // 7. result - строка напрямую
        if (result.isTextual() && nonBlank(result.asText())) {
            log.debug("Model [{}]: extracted via result (text node)", modelId);
            String text = result.asText();
            if (text.startsWith("{") || text.startsWith("[")) {
                return toSafeJsonString(text);
            }
            return text;
        }

        throw new RuntimeException(
                "CF Smart API: cannot extract text for model [%s]. Response: %s"
                        .formatted(modelId, root));
    }

    private String extractFromChoices(JsonNode choices, String modelId) {
        for (JsonNode choice : choices) {
            JsonNode message = choice.path("message");

            String content = textNode(message, "content");
            if (nonBlank(content)) {
                log.debug("Model [{}]: extracted via choices[].message.content", modelId);
                return content;
            }

            String reasoningContent = textNode(message, "reasoning_content");
            if (nonBlank(reasoningContent)) {
                log.debug("Model [{}]: extracted via choices[].message.reasoning_content", modelId);
                return reasoningContent;
            }

            String reasoning = textNode(message, "reasoning");
            if (nonBlank(reasoning)) {
                log.debug("Model [{}]: extracted via choices[].message.reasoning", modelId);
                return reasoning;
            }
        }
        return null;
    }

    private String joinStringArray(JsonNode arrayNode) {
        StringBuilder sb = new StringBuilder();
        for (JsonNode element : arrayNode) {
            if (element.isTextual()) {
                sb.append(element.asText());
            }
        }
        return sb.toString();
    }

    private String toSafeJsonString(String raw) {
        try {
            ObjectMapper lenientMapper = objectMapper.copy();
            lenientMapper.configure(
                    JsonReadFeature.ALLOW_UNESCAPED_CONTROL_CHARS.mappedFeature(),
                    true
            );
            JsonNode node = lenientMapper.readTree(raw);
            return objectMapper.writeValueAsString(node);
        } catch (Exception e1) {
            log.warn("First pass of lenient JSON parse failed, trying manual escape of control chars", e1);
        }

        try {
            String escaped = escapeControlCharsInJsonStrings(raw);
            ObjectMapper lenientMapper = objectMapper.copy();
            lenientMapper.configure(
                    JsonReadFeature.ALLOW_UNESCAPED_CONTROL_CHARS.mappedFeature(),
                    true
            );
            JsonNode node = lenientMapper.readTree(escaped);
            return objectMapper.writeValueAsString(node);
        } catch (Exception e2) {
            log.warn("Second attempt failed, trying to add missing quotes around keys", e2);
        }

        try {
            String withQuotes = addMissingQuotesToKeys(raw);
            ObjectMapper lenientMapper = objectMapper.copy();
            lenientMapper.configure(
                    JsonReadFeature.ALLOW_UNESCAPED_CONTROL_CHARS.mappedFeature(),
                    true
            );
            JsonNode node = lenientMapper.readTree(withQuotes);
            return objectMapper.writeValueAsString(node);
        } catch (Exception e3) {
            log.warn("Third attempt also failed, returning raw string", e3);
        }

        try {
            String fixed = fixMissingObjectBraces(raw);
            ObjectMapper lenientMapper = objectMapper.copy();
            lenientMapper.configure(
                    JsonReadFeature.ALLOW_UNESCAPED_CONTROL_CHARS.mappedFeature(),
                    true
            );
            JsonNode node = lenientMapper.readTree(fixed);
            return objectMapper.writeValueAsString(node);
        } catch (Exception e4) {
            log.warn("Fourth attempt (fix missing braces) also failed, returning raw string", e4);
        }

        return raw;
    }

    private String escapeControlCharsInJsonStrings(String json) {
        Pattern pattern = Pattern.compile("\"((?:[^\"\\\\]|\\\\.)*)\"");
        Matcher matcher = pattern.matcher(json);
        StringBuilder sb = new StringBuilder();
        while (matcher.find()) {
            String content = matcher.group(1);
            String escapedContent = content
                    .replace("\n", "\\n")
                    .replace("\r", "\\r")
                    .replace("\t", "\\t");
            matcher.appendReplacement(sb, Matcher.quoteReplacement("\"" + escapedContent + "\""));
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    private String addMissingQuotesToKeys(String json) {
        Pattern pattern = Pattern.compile("(\\s*)([a-zA-Z_][a-zA-Z0-9_]*)\\s*:");
        Matcher matcher = pattern.matcher(json);
        StringBuilder sb = new StringBuilder();
        while (matcher.find()) {
            matcher.appendReplacement(sb, matcher.group(1) + "\"" + matcher.group(2) + "\":");
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    private String fixMissingObjectBraces(String json) {
        return json.replaceAll("(\\],)(\\s*)(\\{)", "$1}$2$3");
    }

    private String textNode(JsonNode node, String field) {
        JsonNode n = node.path(field);
        return n.isTextual() ? n.asText() : null;
    }

    private boolean nonBlank(String s) {
        return s != null && !s.isBlank();
    }

    private String buildUrl(String accountId, String modelId) {
        return URL_TEMPLATE.formatted(accountId, modelId);
    }

    private CfPathOpenAiRequestDto buildBody(String systemPrompt, String userPrompt, int maxTokens) {
        return new CfPathOpenAiRequestDto(
                List.of(
                        new CfPathOpenAiInputMessageDto("system", systemPrompt),
                        new CfPathOpenAiInputMessageDto("user", userPrompt)
                ),
                maxTokens,
                true
        );
    }
}