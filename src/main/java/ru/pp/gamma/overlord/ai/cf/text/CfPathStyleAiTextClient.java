package ru.pp.gamma.overlord.ai.cf.text;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import ru.pp.gamma.overlord.ai.account.CfAccountService;
import ru.pp.gamma.overlord.ai.account.dto.CfAccount;
import ru.pp.gamma.overlord.ai.cf.text.dto.path.CfTextMessageElement;
import ru.pp.gamma.overlord.ai.cf.text.dto.path.CfTextRequestDto;
import ru.pp.gamma.overlord.ai.cf.text.dto.path.CfTextResponseDto;
import ru.pp.gamma.overlord.ai.cf.text.dto.path.CfTextRole;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Component
public class CfPathStyleAiTextClient {

    private static final String URL_TEMPLATE = "https://api.cloudflare.com/client/v4/accounts/%s/ai/run/%s";
    private static final boolean IS_RAW = true;

    private final RestClient client;
    private final CfAccountService accountService;

    public CfPathStyleAiTextClient(
            @Qualifier("aiRestClient") RestClient client,
            CfAccountService accountService
    ) {
        this.client = client;
        this.accountService = accountService;
    }

    public String generate(String systemPrompt, String userPrompt, String modelId, int maxTokens) {
        CfAccount account = accountService.getAccount();

        CfTextResponseDto response = client.post()
                .uri(buildUrl(account.accountId(), modelId))
                .header("Authorization", "Bearer " + account.authToken())
                .contentType(APPLICATION_JSON)
                .body(buildBody(systemPrompt, userPrompt, maxTokens))
                .retrieve()
                .body(CfTextResponseDto.class);

        if (response == null) {
            throw new RuntimeException("CF Path API returned null response for model: " + modelId);
        }

        return response.result().response();
    }

    private String buildUrl(String accountId, String modelId) {
        return URL_TEMPLATE.formatted(accountId, modelId);
    }

    private CfTextRequestDto buildBody(String systemPrompt, String userPrompt, int maxTokens) {
        return new CfTextRequestDto(
                List.of(
                        new CfTextMessageElement(CfTextRole.system, systemPrompt),
                        new CfTextMessageElement(CfTextRole.user, userPrompt)
                ),
                maxTokens,
                IS_RAW
        );
    }
}