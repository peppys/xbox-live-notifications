package com.github.peppys.xboxlivenotifications.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.peppys.xboxlivenotifications.xboxlive.entities.SocialFriendsResponse;
import lombok.*;
import lombok.experimental.Accessors;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class PresenceChangedMessage {
    @NonNull
    @JsonProperty("presence_id")
    private String presenceId;

    @NonNull
    @JsonProperty("new_state")
    private SocialFriendsResponse.Person.PresenceState newState;

    public static PresenceChangedMessage fromBase64(String base64) {
        try {
            return new ObjectMapper().readValue(
                new String(Base64.getDecoder().decode(base64), StandardCharsets.UTF_8),
                PresenceChangedMessage.class
            );
        } catch (JsonProcessingException e) {
            throw new RuntimeException(String.format("Failed to build message from base64: %s", e.getMessage()));
        }
    }
}
