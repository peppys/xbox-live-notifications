package com.github.peppys.xboxlivenotifications.config;

import com.github.peppys.xboxlivenotifications.xboxlive.XboxLiveAPIClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class APIClientConfiguration {
    @Bean
    public XboxLiveAPIClient apiClient(
            @Value("${xboxlivenotifications.xboxliveapi.oauth.client-id}") String clientId,
            @Value("${xboxlivenotifications.xboxliveapi.oauth.client-secret}") String clientSecret,
            @Value("${xboxlivenotifications.xboxliveapi.oauth.refresh-token}") String refreshToken
    ) {
        return XboxLiveAPIClient.builder()
                .client(WebClient.create())
                .clientId(clientId)
                .clientSecret(clientSecret)
                .refreshToken(refreshToken)
                .build();
    }
}
