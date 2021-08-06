package com.github.peppys.xboxlivenotifications.xboxlive.requests;

import com.github.peppys.xboxlivenotifications.xboxlive.XboxLiveAPIClient;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;

@Data
@Builder
public class MyPresenceRequest implements APIRequest {
    private final boolean xboxTokenRequired = true;

    @Override
    public Spec get() {
        return Spec.builder()
                .method(HttpMethod.GET)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .uri(String.format("%s/users/me", XboxLiveAPIClient.USER_PRESENCE_API_BASE_URI))
                .data(BodyInserters.empty())
                .build();
    }
}
