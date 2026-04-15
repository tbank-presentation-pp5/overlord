package ru.pp.gamma.overlord.ai.gigachat;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.UUID;

// добавил в целом потестить их модель
// в целом не плохая в поиске информации
// тоесть построение пред-презы (заметок) хорошо
@Slf4j
@Component
@RequiredArgsConstructor
public class GigaChatAiTextClient {

    private static final String BASE_URL = "https://giga.chat";
    private static final String USER_AGENT =
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 " +
                    "(KHTML, like Gecko) Chrome/140.0.0.0 Safari/537.36";

    private final ObjectMapper objectMapper;

    public String generate(String systemPrompt, String userPrompt, String modelId) {
        String deviceId = UUID.randomUUID().toString();

        HttpClient httpClient = HttpClient.newBuilder()
                .cookieHandler(new CookieManager(null, CookiePolicy.ACCEPT_ALL))
                .connectTimeout(Duration.ofSeconds(30))
                .build();

        try {
            initSession(httpClient, deviceId);
            return sendMessage(httpClient, deviceId, modelId, systemPrompt + "\n\n" + userPrompt);
        } catch (Exception e) {
            throw new RuntimeException("GigaChat request failed", e);
        }
    }

    // Init

    private void initSession(HttpClient client, String deviceId) throws Exception {
        get(client, deviceId, "/api/check");
        post(client, deviceId, "/api/smartproxy/api/public/gigachat/init", null);
        get(client, deviceId, "/api/giga-mw/api/v0/public/state");
        get(client, deviceId, "/config/version.json");
    }

    // Отправка сообщения со стримингом

    private String sendMessage(HttpClient client, String deviceId,
                               String modelId, String text) throws Exception {

        AiAgent aiAgent = new AiAgent(new String[0], false);
        String body = objectMapper.writeValueAsString(new GigaChatRequestPayload(
                text,
                new String[]{"GIGASEARCH"},
                modelId,
                100000,
                aiAgent
        ));

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/api/smartproxy/api/gc-public/api/v0/sessions/request"))
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .headers(
                        "User-Agent", USER_AGENT,
                        "Origin", BASE_URL,
                        "Referer", BASE_URL + "/",
                        "x-application-name", "gigachat-b2c-web",
                        "x-application-version", "0.91.0",
                        "x-device-id", deviceId,
                        "x-project-id", deviceId,
                        "x-sm-user-id", deviceId,
                        "x-request-id", UUID.randomUUID().toString(),
                        "Accept", "text/event-stream, application/json",
                        "Content-Type", "application/json",
                        "accept-language", "en-US,en;q=0.9",
                        "cache-control", "no-store, max-age=0",
                        "priority", "u=1, i",
                        "sec-ch-ua", "\"Chromium\";v=\"140\", \"Google Chrome\";v=\"140\"",
                        "sec-ch-ua-mobile", "?0",
                        "sec-ch-ua-platform", "\"Windows\"",
                        "sec-fetch-dest", "empty",
                        "sec-fetch-mode", "cors",
                        "sec-fetch-site", "same-origin"
                )
                .build();

        HttpResponse<java.io.InputStream> response =
                client.send(request, HttpResponse.BodyHandlers.ofInputStream());

        if (response.statusCode() != 200) {
            String error = new String(response.body().readAllBytes());
            throw new RuntimeException("GigaChat message request failed: " +
                    response.statusCode() + " – " + error);
        }

        return parseSseStream(response.body());
    }

    // SSE-парсер

    private String parseSseStream(java.io.InputStream stream) throws Exception {
        StringBuilder fullAnswer = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isEmpty()) continue;

                if (line.startsWith("data:")) {
                    String data = line.substring(5).strip();

                    if ("[DONE]".equals(data)) break;

                    try {
                        JsonNode obj = objectMapper.readTree(data);

                        // Финальное сообщение
                        if ("READY".equals(jsonText(obj, "status")) && obj.has("message")) {
                            JsonNode msg = obj.get("message");
                            if (msg.has("value")) {
                                fullAnswer.setLength(0);
                                fullAnswer.append(msg.get("value").asText());
                                break;
                            }
                        }

                        // промежуточный фрагмент
                        if (obj.has("delta")) {
                            fullAnswer.append(obj.get("delta").asText());
                        }
                    } catch (Exception parseEx) {
                        log.warn("GigaChat SSE: cannot parse JSON chunk, appending as text: {}", data);
                        fullAnswer.append(data);
                    }
                }
            }
        }

        return fullAnswer.toString();
    }

    // HTTP-хелперы

    private void get(HttpClient client, String deviceId, String path) throws Exception {
        HttpRequest req = baseRequestBuilder(deviceId, path).GET().build();
        client.send(req, HttpResponse.BodyHandlers.discarding());
    }

    private void post(HttpClient client, String deviceId,
                      String path, String jsonBody) throws Exception {
        var bodyPublisher = (jsonBody == null)
                ? HttpRequest.BodyPublishers.noBody()
                : HttpRequest.BodyPublishers.ofString(jsonBody);

        HttpRequest req = baseRequestBuilder(deviceId, path)
                .POST(bodyPublisher)
                .build();

        client.send(req, HttpResponse.BodyHandlers.discarding());
    }

    private HttpRequest.Builder baseRequestBuilder(String deviceId, String path) {
        return HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + path))
                .timeout(Duration.ofSeconds(30))
                .headers(
                        "User-Agent", USER_AGENT,
                        "Origin", BASE_URL,
                        "Referer", BASE_URL + "/",
                        "x-application-name", "gigachat-b2c-web",
                        "x-application-version", "0.91.0",
                        "x-device-id", deviceId,
                        "x-request-id", UUID.randomUUID().toString(),
                        "accept-language", "en-US,en;q=0.9"
                );
    }

    private String jsonText(JsonNode node, String field) {
        JsonNode f = node.get(field);
        return f != null ? f.asText() : null;
    }

    // Payload DTO

    record AiAgent(String[] queryDomains, boolean extendedResearch) {
    }

    private record GigaChatRequestPayload(
            String text,
            String[] featureFlags,
            String model,
            Integer max_tokens,
            AiAgent aiAgent
    ) {
    }
}