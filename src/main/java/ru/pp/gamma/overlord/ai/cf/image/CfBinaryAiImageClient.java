package ru.pp.gamma.overlord.ai.cf.image;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import ru.pp.gamma.overlord.ai.account.CfAccountService;
import ru.pp.gamma.overlord.ai.account.dto.CfAccount;
import ru.pp.gamma.overlord.ai.cf.image.dto.CfImageRequestDto;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Component
public class CfBinaryAiImageClient {

    private static final String URL_TEMPLATE =
            "https://api.cloudflare.com/client/v4/accounts/%s/ai/run/%s";

    private final RestClient client;
    private final CfAccountService accountService;

    public CfBinaryAiImageClient(
            @Qualifier("aiRestClient") RestClient client,
            CfAccountService accountService
    ) {
        this.client = client;
        this.accountService = accountService;
    }

    public byte[] generate(String prompt, int height, int width, String modelId) {
        CfAccount account = accountService.getAccount();

        byte[] imageBytes = client.post()
                .uri(buildUrl(account.accountId(), modelId))
                .header("Authorization", "Bearer " + account.authToken())
                .contentType(APPLICATION_JSON)
                .body(new CfImageRequestDto(prompt, height, width))
                .retrieve()
                .body(byte[].class);

        if (imageBytes == null || imageBytes.length == 0) {
            throw new RuntimeException("CF Binary image API returned empty response for model: " + modelId);
        }

        return imageBytes;
    }

    private String buildUrl(String accountId, String modelId) {
        return URL_TEMPLATE.formatted(accountId, modelId);
    }
}