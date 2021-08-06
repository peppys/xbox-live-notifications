package com.github.peppys.xboxlivenotifications.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.peppys.xboxlivenotifications.xboxlive.entities.SocialFriendsResponse;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.util.Base64Utils;


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
            return new ObjectMapper()
                    .readValue(Base64Utils.decodeFromString(base64), PresenceChangedMessage.class);
        } catch (Exception e) {
            throw new RuntimeException(String.format("Failed to build message from base64: %s", e.getMessage()));
        }
    }
}
