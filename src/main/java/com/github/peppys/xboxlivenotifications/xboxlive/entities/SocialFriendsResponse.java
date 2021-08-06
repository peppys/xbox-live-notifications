package com.github.peppys.xboxlivenotifications.xboxlive.entities;

import lombok.*;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@Accessors(chain = true)
public class SocialFriendsResponse {
    private List<Person> people;

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @ToString
    @Accessors(chain = true)
    public static class Person {
        public enum PresenceState {
            Offline,
            Online
        }

        private String xuid;

        private boolean isFavorite;

        private String gamertag;

        private String realName;

        private PresenceState presenceState;

        private Date lastSeenDateTimeUtc;
    }
}
