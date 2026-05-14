package ru.pp.gamma.overlord.ai.config;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.pp.gamma.overlord.ai.api.AiImageClient;
import ru.pp.gamma.overlord.ai.cf.image.CfBase64AiImageClient;
import ru.pp.gamma.overlord.ai.cf.image.CfBinaryAiImageClient;
import ru.pp.gamma.overlord.ai.cf.image.CfMultipartBinaryAiImageClient;
import ru.pp.gamma.overlord.ai.model.AiImageModel;

@Component
@RequiredArgsConstructor
public class AiImageClientDispatcher implements AiImageClient {

    private final CfBase64AiImageClient cfBase64Client;
    private final CfBinaryAiImageClient cfBinaryClient;
    private final CfMultipartBinaryAiImageClient cfMultipartBinaryClient;

    @Override
    public byte[] generate(String systemPrompt, String userPrompt, int height, int width, AiImageModel model) {
        String prompt = systemPrompt + "\n" + userPrompt;

        return switch (model.getApiStyle()) {
            case CF_BASE64 -> cfBase64Client.generate(prompt, height, width, model.getModelId());
            case CF_BINARY -> cfBinaryClient.generate(prompt, height, width, model.getModelId());
            case CF_MULTIPART_BINARY -> cfMultipartBinaryClient.generate(prompt, height, width, model.getModelId());
        };
    }
}