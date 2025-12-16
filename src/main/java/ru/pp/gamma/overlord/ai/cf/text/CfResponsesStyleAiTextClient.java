package ru.pp.gamma.overlord.ai.cf.text;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import ru.pp.gamma.overlord.ai.account.CfAccountService;
import ru.pp.gamma.overlord.ai.account.dto.CfAccount;
import ru.pp.gamma.overlord.ai.api.AiTextClient;
import ru.pp.gamma.overlord.ai.cf.text.dto.response.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Component
public class CfResponsesStyleAiTextClient implements AiTextClient {

    private static final String URL_TEMPLATE = "https://api.cloudflare.com/client/v4/accounts/%s/ai/v1/responses";
    private static final int MAX_TOKENS = 130964;
    private static final boolean STREAM = false;

    private final RestClient client;
    private final CfProps cfProps;
    private final CfAccountService accountService;

    public CfResponsesStyleAiTextClient(
            @Qualifier("aiRestClient") RestClient client,
            CfProps cfProps,
            CfAccountService accountService
    ) {
        this.client = client;
        this.cfProps = cfProps;
        this.accountService = accountService;
    }

    @Override
    public String generate(String systemPrompt, String userPrompt) {
        CfAccount account = accountService.getAccount();

        CfTextResponseDto response = client.post()
                .uri(getUrl(account.accountId()))
                .header("Authorization", "Bearer " + account.authToken())
                .contentType(APPLICATION_JSON)
                .body(getBody(systemPrompt, userPrompt))
                .retrieve()
                .body(CfTextResponseDto.class);

        if (response == null) {
            throw new RuntimeException("CF AI API returned null response");
        }

        return extractTextFromResponse(response);
    }

    private String extractTextFromResponse(CfTextResponseDto response) {
        if (response.output() == null || response.output().isEmpty()) {
            throw new RuntimeException("No output in CF AI response");
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

    private String getUrl(String accountId) {
        return URL_TEMPLATE.formatted(accountId);
    }

    private CfTextRequestDto getBody(String systemPrompt, String userPrompt) {
        CfTextMessageElement systemMessage = new CfTextMessageElement(CfTextRole.system, systemPrompt);
        CfTextMessageElement userMessage = new CfTextMessageElement(CfTextRole.user, userPrompt);

        // Попозже могу тоже в dto сделать у Reasoning
        // effort и summary
        CfReasoning reasoning = new CfReasoning("high", "detailed");

        return new CfTextRequestDto(
                cfProps.getResponsesStyleModel(),
                List.of(systemMessage, userMessage),
                reasoning,
                MAX_TOKENS,
                MAX_TOKENS,
                STREAM
        );
    }
}