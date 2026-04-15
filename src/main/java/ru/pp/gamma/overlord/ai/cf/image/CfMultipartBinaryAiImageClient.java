package ru.pp.gamma.overlord.ai.cf.image;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import ru.pp.gamma.overlord.ai.account.CfAccountService;
import ru.pp.gamma.overlord.ai.account.dto.CfAccount;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA;

@Component
public class CfMultipartBinaryAiImageClient {

    private static final String URL_TEMPLATE =
            "https://api.cloudflare.com/client/v4/accounts/%s/ai/run/%s";

    private final RestClient client;
    private final CfAccountService accountService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public CfMultipartBinaryAiImageClient(
            @Qualifier("aiRestClient") RestClient client,
            CfAccountService accountService
    ) {
        this.client = client;
        this.accountService = accountService;
    }

    public byte[] generate(String prompt, int height, int width, String modelId) {
        CfAccount account = accountService.getAccount();

        String jsonResponse = client.post()
                .uri(buildUrl(account.accountId(), modelId))
                .header("Authorization", "Bearer " + account.authToken())
                .contentType(MULTIPART_FORM_DATA)
                .body(buildMultipart(prompt, height, width))
                .retrieve()
                .body(String.class);

        if (jsonResponse == null || jsonResponse.isEmpty()) {
            throw new RuntimeException("CF Multipart Binary image API returned empty response");
        }

        try {
            // парсим JSON и извлекаем Base64-строку
            JsonNode root = objectMapper.readTree(jsonResponse);
            JsonNode resultNode = root.path("result");
            if (resultNode.isMissingNode()) {
                throw new RuntimeException("No 'result' field in API response: " + jsonResponse);
            }
            String base64Image = resultNode.path("image").asText();
            if (base64Image == null || base64Image.isEmpty()) {
                throw new RuntimeException("No 'image' field in API response: " + jsonResponse);
            }

            // декодируем Base64 в байты
            byte[] imageBytes = Base64.getDecoder().decode(base64Image);

            // конвертируем JPEG в PNG
            // без конвертации не работало
            // ещё пока на тестировании, если все ворк уберу
            return convertJpegToPng(imageBytes);

        } catch (IOException e) {
            throw new RuntimeException("Failed to parse API response: " + e.getMessage(), e);
        }
    }

    private byte[] convertJpegToPng(byte[] jpegData) {
        try (ByteArrayInputStream bais = new ByteArrayInputStream(jpegData);
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            BufferedImage image = ImageIO.read(bais);
            if (image == null) {
                throw new IOException("Не удалось прочитать изображение, возможно повреждённые данные");
            }

            ImageIO.write(image, "PNG", baos);
            return baos.toByteArray();

        } catch (IOException e) {
            throw new RuntimeException("Ошибка конвертации JPEG в PNG: " + e.getMessage(), e);
        }
    }

    private String buildUrl(String accountId, String modelId) {
        return URL_TEMPLATE.formatted(accountId, modelId);
    }

    private MultiValueMap<String, HttpEntity<?>> buildMultipart(String prompt, int height, int width) {
        MultiValueMap<String, HttpEntity<?>> parts = new LinkedMultiValueMap<>();
        parts.add("prompt", textPart(prompt));
        parts.add("height", textPart(String.valueOf(height)));
        parts.add("width", textPart(String.valueOf(width)));
        return parts;
    }

    private HttpEntity<String> textPart(String value) {
        return new HttpEntity<>(value, new HttpHeaders());
    }
}