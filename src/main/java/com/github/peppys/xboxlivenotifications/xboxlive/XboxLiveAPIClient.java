package com.github.peppys.xboxlivenotifications.xboxlive;

import com.github.peppys.xboxlivenotifications.xboxlive.entities.RefreshTokenResponse;
import com.github.peppys.xboxlivenotifications.xboxlive.entities.UserTokenResponse;
import com.github.peppys.xboxlivenotifications.xboxlive.entities.XboxTokenResponse;
import com.github.peppys.xboxlivenotifications.xboxlive.requests.APIRequest;
import com.github.peppys.xboxlivenotifications.xboxlive.requests.RefreshTokenRequest;
import com.github.peppys.xboxlivenotifications.xboxlive.requests.UserTokenRequest;
import com.github.peppys.xboxlivenotifications.xboxlive.requests.XboxTokenRequest;
import lombok.extern.slf4j.Slf4j;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Collections;
import java.util.Optional;

@Slf4j
@Builder
@Data
public class XboxLiveAPIClient {
    public static final String HEADER_XBL_CONTRACT_VERSION = "x-xbl-contract-version";

    public static final String PEOPLE_HUB_API_BASE_URI = "https://peoplehub.xboxlive.com";

    public static final String USER_PRESENCE_API_BASE_URI = "https://userpresence.xboxlive.com";

    private final WebClient client;

    private final String clientId;

    private final String clientSecret;

    private final String refreshToken;

    private final String callerGamerTag;

    private final String callerFullName;

    private final Mono<String> authToken = Mono.fromSupplier(this::fetchAuthorizationToken)
            .flatMap(mono -> mono)
            .cache(
                    token -> Duration.ofHours(12),
                    throwable -> Duration.ZERO,
                    () -> Duration.ZERO
            );

    public <T> Mono<T> send(final APIRequest request, final Class<T> responseClass) {
        return Mono.just(request.isXboxTokenRequired())
                .filter(Boolean::booleanValue)
                .flatMap(ignore -> authToken
                        .map(authToken -> {
                            final var headers = new HttpHeaders();
                            headers.add(HttpHeaders.AUTHORIZATION, authToken);
                            return headers;
                        }))
                .switchIfEmpty(Mono.just(new HttpHeaders()))
                .flatMap(headers -> send(request, responseClass, headers));
    }

    private <T> Mono<T> send(final APIRequest request, final Class<T> responseClass, HttpHeaders headers) {
        final var spec = request.get();
        log.info("Sending request: {}", request.getClass().getSimpleName());

        return client
                .method(spec.getMethod())
                .uri(spec.getUri())
                .headers(h -> {
                    h.addAll(headers);
                    h.addAll(spec.getHeaders());
                })
                .contentType(spec.getContentType())
                .accept(spec.getAccept())
                .body(spec.getData())
                .retrieve()
                .bodyToMono(responseClass);
    }

    public Mono<String> fetchAuthorizationToken() {
        log.info("Fetching auth token...");
        return refreshToken()
                .map(RefreshTokenResponse::getAccessToken)
                .flatMap(this::fetchUserToken)
                .map(UserTokenResponse::getToken)
                .flatMap(this::fetchXboxToken)
                .map(r -> String.format(
                        "XBL3.0 x=%s;%s",
                        Optional.ofNullable(r.getDisplayClaims().get("xui"))
                                .map(displayClaims -> displayClaims.stream()
                                        .filter(displayClaim -> displayClaim.getUhs() != null)
                                        .findFirst()
                                        .orElseThrow(() -> new RuntimeException("No user hash found")))
                                .orElseThrow(() -> new RuntimeException("No display claims found"))
                                .getUhs(),
                        r.getToken())
                );
    }

    private Mono<XboxTokenResponse> fetchXboxToken(String userToken) {
        log.info("Fetching xbox token...");
        return send(
                XboxTokenRequest.builder()
                        .relyingParty("http://xboxlive.com")
                        .tokenType("JWT")
                        .properties(XboxTokenRequest.Properties.builder()
                                .userTokens(Collections.singletonList(userToken))
                                .sandboxId("RETAIL")
                                .build())
                        .build(),
                XboxTokenResponse.class
        );
    }

    private Mono<UserTokenResponse> fetchUserToken(String accessToken) {
        log.info("Fetching user token...");
        return send(
                UserTokenRequest.builder()
                        .relyingParty("http://auth.xboxlive.com")
                        .tokenType("JWT")
                        .properties(UserTokenRequest.Properties.builder()
                                .authMethod("RPS")
                                .siteName("user.auth.xboxlive.com")
                                .rpsTicket(String.format("d=%s", accessToken))
                                .build())
                        .build(),
                UserTokenResponse.class
        );
    }

    private Mono<RefreshTokenResponse> refreshToken() {
        log.info("Fetching oauth token...");
        return send(
                RefreshTokenRequest.builder()
                        .clientId(clientId)
                        .clientSecret(clientSecret)
                        .grantType("refresh_token")
                        .refreshToken(refreshToken)
                        .scope("Xboxlive.signin,Xboxlive.offline_access")
                        .build(),
                RefreshTokenResponse.class
        );
    }
}
