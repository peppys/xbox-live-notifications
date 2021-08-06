package com.github.peppys.xboxlivenotifications.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.peppys.xboxlivenotifications.xboxlive.entities.SocialFriendsResponse;
import com.google.cloud.firestore.annotation.DocumentId;
import com.google.cloud.firestore.annotation.PropertyName;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.cloud.gcp.data.firestore.Document;

import java.util.Date;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collectionName = "xbox-live-presences")
@Accessors(chain = true)
public class XboxLivePresence {
    @NonNull
    @DocumentId
    private String id;

    @NonNull
    private String gamertag;

    @NonNull
    @JsonProperty("full_name")
    @PropertyName("full_name")
    @Getter(onMethod_ = {@PropertyName("full_name")})
    @Setter(onMethod_ = {@PropertyName("full_name")})
    private String fullName;

    @NonNull
    private SocialFriendsResponse.Person.PresenceState state;

    @JsonProperty("created_at")
    @PropertyName("created_at")
    @Getter(onMethod_ = {@PropertyName("created_at")})
    @Setter(onMethod_ = {@PropertyName("created_at")})
    private Date createdAt;

    @JsonProperty("updated_at")
    @PropertyName("updated_at")
    @Getter(onMethod_ = {@PropertyName("updated_at")})
    @Setter(onMethod_ = {@PropertyName("updated_at")})
    private Date updatedAt;

    @JsonProperty("last_seen_at")
    @PropertyName("last_seen_at")
    @Getter(onMethod_ = {@PropertyName("last_seen_at")})
    @Setter(onMethod_ = {@PropertyName("last_seen_at")})
    private Date lastSeenAt;

    @JsonProperty("last_notified_at")
    @PropertyName("last_notified_at")
    @Getter(onMethod_ = {@PropertyName("last_notified_at")})
    @Setter(onMethod_ = {@PropertyName("last_notified_at")})
    private Date lastNotifiedAt;
}
