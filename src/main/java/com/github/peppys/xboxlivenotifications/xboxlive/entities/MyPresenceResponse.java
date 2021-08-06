package com.github.peppys.xboxlivenotifications.xboxlive.entities;

import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.Date;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)
@JsonNaming
public class MyPresenceResponse {
    private String xuid;

    private SocialFriendsResponse.Person.PresenceState state;

    private LastSeen lastSeen;

    @Data
    @Accessors(chain = true)
    @JsonNaming
    public static class LastSeen {
        private String deviceType;

        private String titleId;

        private String titleName;

        private Date timestamp;
    }
}
