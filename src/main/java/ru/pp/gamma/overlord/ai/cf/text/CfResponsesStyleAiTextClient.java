package ru.pp.gamma.overlord.ai.cf.text;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import ru.pp.gamma.overlord.ai.api.AiTextClient;
import ru.pp.gamma.overlord.ai.cf.text.dto.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Component
public class CfResponsesStyleAiTextClient implements AiTextClient {

    private static final String URL_TEMPLATE = "https://api.cloudflare.com/client/v4/accounts/%s/ai/v1/responses";
    private static final int MAX_TOKENS = 130964;
    private static final boolean STREAM = false;
    private static final String GPT_OSS_120B_MODEL = "@cf/openai/gpt-oss-120b";

    private final RestClient client;
    private final CfProps cfProps;

    public CfResponsesStyleAiTextClient(
            @Qualifier("aiRestClient") RestClient client,
            CfProps cfProps
    ) {
        this.client = client;
        this.cfProps = cfProps;
    }

    @Override
    public String generate(String systemPrompt, String userPrompt) {
        CfTextResponseDto response = client.post()
                .uri(getUrl())
                .header("Authorization", "Bearer " + cfProps.getAuthToken())
                .contentType(APPLICATION_JSON)
                .body(getBody(systemPrompt, userPrompt))
                .retrieve()
                .body(CfTextResponseDto.class);

        if (response == null) {
            throw new RuntimeException("Cloudflare AI API returned null response");
        }

        return extractTextFromResponse(response);
    }

    private String extractTextFromResponse(CfTextResponseDto response) {
        if (response.output() == null || response.output().isEmpty()) {
            throw new RuntimeException("No output in Cloudflare AI response");
        }

        for (CfOutput output : response.output()) {
            if (CfTextRole.assistant.equals(output.role())) {
                if (output.content() != null && !output.content().isEmpty()) {
                    StringBuilder textBuilder = new StringBuilder();
                    for (CfContent content : output.content()) {
                        if (content.text() != null) {
                            textBuilder.append(content.text());
                        }
                    }
                    String result = textBuilder.toString();
                    if (!result.isEmpty()) {
                        return result;
                    }
                }
            }
        }

        throw new RuntimeException("No assistant message found in response output");
    }

    private String getUrl() {
        return URL_TEMPLATE.formatted(cfProps.getIdAccount());
    }

    private CfTextRequestDto getBody(String systemPrompt, String userPrompt) {
        CfTextMessageElement systemMessage = new CfTextMessageElement(CfTextRole.system, systemPrompt);
        CfTextMessageElement userMessage = new CfTextMessageElement(CfTextRole.user, userPrompt);

        // Попозже могу тоже в dto сделать у Reasoning
        // effort и summary
        CfReasoning reasoning = new CfReasoning("high", "detailed");

        return new CfTextRequestDto(
                GPT_OSS_120B_MODEL,
                List.of(systemMessage, userMessage),
                reasoning,
                MAX_TOKENS,
                MAX_TOKENS,
                STREAM
        );
    }
}