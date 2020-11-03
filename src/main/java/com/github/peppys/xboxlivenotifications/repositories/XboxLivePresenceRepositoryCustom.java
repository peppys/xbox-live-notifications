package com.github.peppys.xboxlivenotifications.repositories;

import com.github.peppys.xboxlivenotifications.entities.XboxLivePresence;
import reactor.core.publisher.Mono;

public interface XboxLivePresenceRepositoryCustom {
    Mono<XboxLivePresence> findOrCreate(final String id, final XboxLivePresence data);
}
