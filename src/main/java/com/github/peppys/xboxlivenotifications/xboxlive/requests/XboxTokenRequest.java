package com.github.peppys.xboxlivenotifications.xboxlive.requests;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.github.peppys.xboxlivenotifications.xboxlive.XboxLiveAPIClient;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;

import java.util.List;

@Builder
@Data
@Accessors(chain = true)
@JsonNaming(PropertyNamingStrategy.UpperCamelCaseStrategy.class)
public class XboxTokenRequest implements APIRequest {
    private static final HttpHeaders headers;

    static {
        headers = new HttpHeaders();
        headers.add(XboxLiveAPIClient.HEADER_XBL_CONTRACT_VERSION, "1");
    }

    private final String relyingParty;

    private final String tokenType;

    private final Properties properties;

    @Override
    @JsonIgnore
    public Spec get() {
        return Spec.builder()
                .method(HttpMethod.POST)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .headers(headers)
                .uri("https://xsts.auth.xboxlive.com/xsts/authorize")
                .data(BodyInserters.fromValue(this))
                .build();
    }

    @Builder
    @Data
    @Accessors(chain = true)
    @JsonNaming(PropertyNamingStrategy.UpperCamelCaseStrategy.class)
    public static class Properties {
        private final List<String> userTokens;

        private final String sandboxId;
    }
}
