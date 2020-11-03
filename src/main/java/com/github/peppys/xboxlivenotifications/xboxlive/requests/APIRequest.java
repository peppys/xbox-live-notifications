package com.github.peppys.xboxlivenotifications.xboxlive.requests;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;

import java.util.Map;

public interface APIRequest {
    Spec buildSpec();

    @Data
    @Builder
    class Spec {
        private HttpMethod method;
        private HttpHeaders headers;
        private MediaType accept;
        private String uri;
        private Map<String, Object> data;
    }
}
