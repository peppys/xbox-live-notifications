package com.github.peppys.xboxlivenotifications.xboxlive.requests;

import com.github.peppys.xboxlivenotifications.xboxlive.XboxLiveAPIClient;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;

@Data
@Builder
public class MySocialFriendsRequest implements APIRequest {
    private static final HttpHeaders headers;

    static {
        headers = new HttpHeaders();
        headers.add(XboxLiveAPIClient.HEADER_XBL_CONTRACT_VERSION, "3");
        headers.add(HttpHeaders.ACCEPT_LANGUAGE, "en-US");
    }

    private final boolean xboxTokenRequired = true;

    @Override
    public Spec get() {
        return Spec.builder()
                .method(HttpMethod.GET)
                .headers(headers)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .uri(String.format("%s/users/me/people/social", XboxLiveAPIClient.PEOPLE_HUB_API_BASE_URI))
                .data(BodyInserters.empty())
                .build();
    }
}
