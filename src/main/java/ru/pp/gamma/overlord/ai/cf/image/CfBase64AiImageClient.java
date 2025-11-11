package ru.pp.gamma.overlord.ai.cf.image;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import ru.pp.gamma.overlord.ai.api.AiImageClient;
import ru.pp.gamma.overlord.ai.cf.image.dto.CfImageRequestDto;
import ru.pp.gamma.overlord.ai.cf.image.dto.CfImageResultResponseDto;
import ru.pp.gamma.overlord.ai.cf.text.CfProps;

import java.util.Base64;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Component
public class CfBase64AiImageClient implements AiImageClient {

    private static final String URL_TEMPLATE = "https://api.cloudflare.com/client/v4/accounts/%s/ai/run/%s";

    private final RestClient client;
    private final CfProps cfProps;

    public CfBase64AiImageClient(
            @Qualifier("aiRestClient") RestClient client,
            CfProps cfProps
    ) {
        this.client = client;
        this.cfProps = cfProps;
    }

    @Override
    public byte[] generate(String systemPrompt, String userPrompt, int height, int width) {
        CfImageResultResponseDto response = client.post()
                .uri(getUrl())
                .header("Authorization", "Bearer " + cfProps.getAuthToken())
                .contentType(APPLICATION_JSON)
                .body(getBody(systemPrompt, userPrompt, height, width))
                .retrieve()
                .body(CfImageResultResponseDto.class);

        return Base64.getDecoder().decode(response.result().image());
    }

    private String getUrl() {
        return URL_TEMPLATE.formatted(
                cfProps.getIdAccount(),
                cfProps.getImageBase64Model()
        );
    }

    private CfImageRequestDto getBody(String systemPrompt, String userPrompt, int height, int width) {
        String prompt = systemPrompt + "\n" + userPrompt;

        return new CfImageRequestDto(
                prompt,
                height,
                width
        );
    }
}
