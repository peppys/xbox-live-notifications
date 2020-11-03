package com.github.peppys.xboxlivenotifications.entities;

import com.github.peppys.xboxlivenotifications.xboxlive.entities.SocialFriendsResponse;
import com.google.cloud.firestore.annotation.DocumentId;
import com.google.cloud.firestore.annotation.ServerTimestamp;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
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
    private String fullName;

    @NonNull
    private SocialFriendsResponse.Person.PresenceState state;

    @ServerTimestamp
    private Date createdAt;

    @ServerTimestamp
    private Date updatedAt;

    private Date lastNotifiedAt;
}
