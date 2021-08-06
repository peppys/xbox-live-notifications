package com.github.peppys.xboxlivenotifications.xboxlive.requests;

import lombok.Builder;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;

@Builder
public class RefreshTokenRequest implements APIRequest {
    private final String clientId;

    private final String clientSecret;

    private final String grantType;

    private final String refreshToken;

    private final String scope;

    @Override
    public Spec get() {
        return Spec.builder()
                .method(HttpMethod.POST)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .accept(MediaType.APPLICATION_JSON)
                .uri("https://login.live.com/oauth20_token.srf")
                .data(BodyInserters.fromFormData("client_id", clientId)
                        .with("client_secret", clientSecret)
                        .with("grant_type", grantType)
                        .with("refresh_token", refreshToken)
                        .with("scope", scope))
                .build();
    }
}
