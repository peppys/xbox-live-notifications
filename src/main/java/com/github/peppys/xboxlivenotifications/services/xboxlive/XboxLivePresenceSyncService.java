package com.github.peppys.xboxlivenotifications.services.xboxlive;

import com.github.peppys.xboxlivenotifications.entities.PresenceChangedMessage;
import com.github.peppys.xboxlivenotifications.entities.XboxLivePresence;
import com.github.peppys.xboxlivenotifications.repositories.XboxLivePresenceRepository;
import com.github.peppys.xboxlivenotifications.xboxlive.XboxLiveAPIClient;
import com.github.peppys.xboxlivenotifications.xboxlive.entities.SocialFriendsResponse;
import com.github.peppys.xboxlivenotifications.xboxlive.requests.MySocialFriendsRequest;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.Date;
import java.util.function.Consumer;

@Slf4j
public class XboxLivePresenceSyncService {
    private final XboxLiveAPIClient xboxLive;
    private final XboxLivePresenceRepository repo;
    private final Runnable queuer;
    private final Consumer<PresenceChangedMessage> publisher;

    public XboxLivePresenceSyncService(final XboxLiveAPIClient xboxLive,
                                       final XboxLivePresenceRepository repo,
                                       final Runnable queuer,
                                       final Consumer<PresenceChangedMessage> publisher
    ) {
        this.xboxLive = xboxLive;
        this.repo = repo;
        this.queuer = queuer;
        this.publisher = publisher;
    }

    public Mono<Void> queueSync() {
        return Mono.fromRunnable(queuer);
    }

    public Flux<XboxLivePresence> sync() {
        return xboxLive.send(MySocialFriendsRequest.build(), SocialFriendsResponse.class)
            .flatMapMany(socialFriendsResponse -> Flux.fromIterable(socialFriendsResponse.getPeople()))
            // Find or create in DB
            .flatMap(person -> Flux.zip(Flux.just(person), repo.findOrCreate(person.getXuid(), XboxLivePresence.builder()
                .id(person.getXuid())
                .state(person.getPresenceState())
                .fullName(person.getRealName())
                .gamertag(person.getGamertag())
                .createdAt(Date.from(Instant.now()))
                .updatedAt(Date.from(Instant.now()))
                .build())))
            // Update presence state in DB
            .flatMap(tuple -> {
                var person = tuple.getT1();
                var presence = tuple.getT2();

                var stateChanged = person.getPresenceState() != presence.getState();

                presence
                    .setState(tuple.getT1().getPresenceState())
                    .setUpdatedAt(Date.from(Instant.now()));

                return Mono.when(
                    repo.save(presence),
                    // Publish on state changes
                    stateChanged ? publishStateChanged(presence) : Mono.just(false)
                )
                    .thenReturn(presence);
            });
    }

    private Mono<Void> publishStateChanged(final XboxLivePresence presence) {
        log.info("Discovered state changed for presence {}", presence);

        return Mono.fromRunnable(() -> publisher.accept(PresenceChangedMessage.builder()
            .presenceId(presence.getId())
            .newState(presence.getState())
            .build()
        ));
    }
}
