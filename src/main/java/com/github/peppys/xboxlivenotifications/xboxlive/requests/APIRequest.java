package com.github.peppys.xboxlivenotifications.xboxlive.requests;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ClientHttpRequest;
import org.springframework.web.reactive.function.BodyInserter;

import java.util.function.Supplier;

public interface APIRequest extends Supplier<APIRequest.Spec> {
    default boolean isXboxTokenRequired() {
        return false;
    }

    @Data
    @Builder
    class Spec {
        private HttpMethod method;

        @Builder.Default
        private HttpHeaders headers = new HttpHeaders();

        private MediaType contentType;

        private MediaType accept;

        private String uri;

        private BodyInserter<?, ? super ClientHttpRequest> data;

    }
}
