package com.github.peppys.xboxlivenotifications.xboxlive;

import com.github.peppys.xboxlivenotifications.xboxlive.requests.APIRequest;
import com.google.common.net.HttpHeaders;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Builder
@Slf4j
public class XboxLiveAPIClient {
    private final WebClient client;
    private final String clientId;
    private final String clientSecret;
    private final String redirectUri;

    private final String temporaryToken;

    public static String HEADER_XBL_CONTRACT_VERSION = "x-xbl-contract-version";
    public static String PEOPLE_HUB_API_BASE_URI = "https://peoplehub.xboxlive.com";

    public <T> Mono<T> send(final APIRequest request, final Class<T> responseClass) {
        var spec = request.buildSpec();

        return getAuthorizationToken()
            .flatMap(token -> client
                .method(spec.getMethod())
                .uri(spec.getUri())
                .headers(httpHeaders -> {
                    httpHeaders.set(HttpHeaders.AUTHORIZATION, token);
                    httpHeaders.addAll(spec.getHeaders());
                })
                .accept(spec.getAccept())
                .bodyValue(spec.getData())
                .retrieve()
                .bodyToMono(responseClass));
    }

    private Mono<String> getAuthorizationToken() {
        log.info("Using client with temporary token {}", temporaryToken);

        // TODO - Fetch via API
        return Mono.just(temporaryToken);
    }
}
