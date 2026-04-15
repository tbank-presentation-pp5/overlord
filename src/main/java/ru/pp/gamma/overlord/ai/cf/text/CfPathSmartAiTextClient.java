package ru.pp.gamma.overlord.ai.cf.text;

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

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Slf4j
@Component
public class CfPathSmartAiTextClient {

    private static final String URL_TEMPLATE = "https://api.cloudflare.com/client/v4/accounts/%s/ai/run/%s";

    private final RestClient client;
    private final CfAccountService accountService;

    public CfPathSmartAiTextClient(
            @Qualifier("aiRestClient") RestClient client,
            CfAccountService accountService,
            ObjectMapper objectMapper
    ) {
        this.client = client;
        this.accountService = accountService;
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
            throw new RuntimeException("CF Smart API returned null response for model: " + modelId);
        }

        log.debug("CF Smart raw response for model [{}]: {}", modelId, root);

        return extractText(root, modelId);
    }

    private String extractText(JsonNode root, String modelId) {
        JsonNode result = root.path("result");

        // пытался все апишки парснуть пока воркает
        // ешё тестирую и комментарии для себя оставлю

        // 1. CF Path старый формат: { result: { response: "..." } }
        String fromResult = text(result, "response");
        if (nonBlank(fromResult)) {
            log.debug("Model [{}]: extracted via result.response", modelId);
            return fromResult;
        }

        // 2. OpenAI choices внутри result: { result: { choices: [...] } }
        JsonNode choices = result.path("choices");
        if (choices.isArray() && !choices.isEmpty()) {
            String fromChoices = extractFromChoices(choices, modelId);
            if (fromChoices != null) return fromChoices;
        }

        // 3. Fallback: choices прямо в корне (на случай если CF изменит формат)
        JsonNode rootChoices = root.path("choices");
        if (rootChoices.isArray() && !rootChoices.isEmpty()) {
            String fromRootChoices = extractFromChoices(rootChoices, modelId);
            if (fromRootChoices != null) return fromRootChoices;
        }

        // 4. result — просто строка
        if (result.isTextual() && nonBlank(result.asText())) {
            log.debug("Model [{}]: extracted via result (text node)", modelId);
            return result.asText();
        }

        throw new RuntimeException(
                "CF Smart API: cannot extract text from response for model [%s]. Full response: %s"
                        .formatted(modelId, root));
    }

    private String extractFromChoices(JsonNode choices, String modelId) {
        for (JsonNode choice : choices) {
            JsonNode message = choice.path("message");

            // content
            String content = text(message, "content");
            if (nonBlank(content)) {
                log.debug("Model [{}]: extracted via choices[].message.content", modelId);
                return content;
            }

            // reasoning_content (QwQ и другие thinking-модели, когда content пуст)
            String reasoning = text(message, "reasoning_content");
            if (nonBlank(reasoning)) {
                log.debug("Model [{}]: extracted via choices[].message.reasoning_content", modelId);
                return reasoning;
            }

            // reasoning (альтернативное поле у Nemotron и других)
            String reasoningAlt = text(message, "reasoning");
            if (nonBlank(reasoningAlt)) {
                log.debug("Model [{}]: extracted via choices[].message.reasoning", modelId);
                return reasoningAlt;
            }
        }
        return null;
    }

    private String text(JsonNode node, String... path) {
        JsonNode cur = node;
        for (String key : path) cur = cur.path(key);
        return cur.isTextual() ? cur.asText() : null;
    }

    private boolean nonBlank(String s) {
        return s != null && !s.isBlank();
    }

    private String buildUrl(String accountId, String modelId) {
        return URL_TEMPLATE.formatted(accountId, modelId);
    }

    private CfPathOpenAiRequestDto buildBody(String systemPrompt, String userPrompt, int maxTokens) {
        return new CfPathOpenAiRequestDto(List.of(
                new CfPathOpenAiInputMessageDto("system", systemPrompt),
                new CfPathOpenAiInputMessageDto("user", userPrompt)
        ));
    }
}