package ru.pp.gamma.overlord.externalapi.util;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@RequiredArgsConstructor
@Component
public class FileDownloader {

    private final RestClient proxyRestClient;

    public byte[] download(String url) {
        return proxyRestClient.get()
                .uri(url)
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36")
                .retrieve()
                .body(byte[].class);
    }

}
