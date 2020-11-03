package com.github.peppys.xboxlivenotifications.repositories;

import com.github.peppys.xboxlivenotifications.entities.XboxLivePresence;
import org.springframework.cloud.gcp.data.firestore.FirestoreTemplate;
import reactor.core.publisher.Mono;

public class XboxLivePresenceRepositoryCustomImpl implements XboxLivePresenceRepositoryCustom {
    private final FirestoreTemplate firestore;

    public XboxLivePresenceRepositoryCustomImpl(final FirestoreTemplate firestore) {
        this.firestore = firestore;
    }

    @Override
    public Mono<XboxLivePresence> findOrCreate(final String id, final XboxLivePresence presence) {
        return firestore
            .findById(Mono.just(id), XboxLivePresence.class)
            .switchIfEmpty(firestore.save(presence));
    }
}
