package ru.pp.gamma.overlord.ai.account;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.net.http.HttpClient;
import java.time.Duration;

@Slf4j
@Component
public class AccountProviderClient {
    private final RestClient restClient;
    private final AccounterProps props;

    public AccountProviderClient(RestClient.Builder restClientBuilder, AccounterProps props) {
        this.props = props;

        HttpClient httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .connectTimeout(Duration.ofSeconds(10))
                .build();

        JdkClientHttpRequestFactory requestFactory = new JdkClientHttpRequestFactory(httpClient);

        this.restClient = restClientBuilder
                .requestFactory(requestFactory)
                .build();

        log.info("AccountProviderClient initialized. Accounter URL: {}, enabled: {}",
                props.getUrl(), props.isEnabled());
    }

    public AccountResponse getAccount() {
        if (!props.isEnabled()) {
            log.warn("Accounter integration is disabled");
            return null;
        }

        String url = props.getUrl() + "/get_acc";
        log.info("Requesting account from Accounter: {}", url);

        try {
            AccountResponse response = restClient.get()
                    .uri(url)
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, (_, res) -> {
                        log.error("Accounter error: {} {}", res.getStatusCode(), res.getStatusText());
                        throw new RuntimeException("Accounter error: " + res.getStatusCode());
                    })
                    .body(AccountResponse.class);

            log.info("Received response from Accounter: status={}, accountId={}, neurons={}",
                    response.getStatus(), response.getAccountId(), response.getNeuronsCount());

            return response;

        } catch (Exception e) {
            log.error("Failed to get account from Accounter: {}", e.getMessage(), e);
            return null;
        }
    }
}