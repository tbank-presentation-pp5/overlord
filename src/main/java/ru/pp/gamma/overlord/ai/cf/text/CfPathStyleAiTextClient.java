package ru.pp.gamma.overlord.ai.cf.text;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import ru.pp.gamma.overlord.ai.api.AiTextClient;
import ru.pp.gamma.overlord.ai.cf.text.dto.path.CfTextMessageElement;
import ru.pp.gamma.overlord.ai.cf.text.dto.path.CfTextRequestDto;
import ru.pp.gamma.overlord.ai.cf.text.dto.path.CfTextResponseDto;
import ru.pp.gamma.overlord.ai.cf.text.dto.path.CfTextRole;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Component
@Qualifier("pathStyleAiTextClient")
public class CfPathStyleAiTextClient implements AiTextClient {

    private static final String URL_TEMPLATE = "https://api.cloudflare.com/client/v4/accounts/%s/ai/run/%s";
    private static final int MAX_TOKENS = 128_000;
    private static final boolean IS_RAW = true;

    private final RestClient client;
    private final CfProps cfProps;

    public CfPathStyleAiTextClient(
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

        return response.result().response();
    }

    private String getUrl() {
        return URL_TEMPLATE.formatted(
                cfProps.getIdAccount(),
                cfProps.getPathStyleModel()
        );
    }

    private CfTextRequestDto getBody(String systemPrompt, String userPrompt) {
        CfTextMessageElement systemMessage = new CfTextMessageElement(CfTextRole.system, systemPrompt);
        CfTextMessageElement userMessage = new CfTextMessageElement(CfTextRole.user, userPrompt);

        return new CfTextRequestDto(
                List.of(systemMessage, userMessage),
                MAX_TOKENS,
                IS_RAW
        );
    }
}
