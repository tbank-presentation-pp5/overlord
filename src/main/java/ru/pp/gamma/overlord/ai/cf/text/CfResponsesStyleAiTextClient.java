package ru.pp.gamma.overlord.ai.cf.text;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import ru.pp.gamma.overlord.ai.account.CfAccountService;
import ru.pp.gamma.overlord.ai.account.dto.CfAccount;
import ru.pp.gamma.overlord.ai.cf.text.dto.response.*;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Component
public class CfResponsesStyleAiTextClient {

    private static final String URL_TEMPLATE = "https://api.cloudflare.com/client/v4/accounts/%s/ai/v1/responses";
    private static final int MAX_TOKENS = 130_964;
    private static final boolean STREAM = false;

    private final RestClient client;
    private final CfAccountService accountService;

    public CfResponsesStyleAiTextClient(
            @Qualifier("aiRestClient") RestClient client,
            CfAccountService accountService
    ) {
        this.client = client;
        this.accountService = accountService;
    }

    public String generate(String systemPrompt, String userPrompt, String modelId) {
        CfAccount account = accountService.getAccount();

        CfTextResponseDto response = client.post()
                .uri(buildUrl(account.accountId()))
                .header("Authorization", "Bearer " + account.authToken())
                .contentType(APPLICATION_JSON)
                .body(buildBody(systemPrompt, userPrompt, modelId))
                .retrieve()
                .body(CfTextResponseDto.class);

        if (response == null) {
            throw new RuntimeException("CF Responses API returned null response");
        }

        return extractText(response);
    }

    private String extractText(CfTextResponseDto response) {
        if (response.output() == null || response.output().isEmpty()) {
            throw new RuntimeException("No output in CF Responses API response");
        }

        for (CfOutput output : response.output()) {
            if (CfTextRole.assistant.equals(output.role()) &&
                    output.content() != null && !output.content().isEmpty()) {

                StringBuilder sb = new StringBuilder();
                output.content().forEach(c -> {
                    if (c.text() != null) sb.append(c.text());
                });

                String result = sb.toString();
                if (!result.isEmpty()) return result;
            }
        }

        throw new RuntimeException("No assistant message found in CF Responses API output");
    }

    private String buildUrl(String accountId) {
        return URL_TEMPLATE.formatted(accountId);
    }

    private CfTextRequestDto buildBody(String systemPrompt, String userPrompt, String modelId) {
        CfReasoning reasoning = new CfReasoning("high", "detailed");

        return new CfTextRequestDto(
                modelId,
                systemPrompt,
                userPrompt,
                reasoning,
                MAX_TOKENS,
                MAX_TOKENS,
                STREAM
        );
    }
}