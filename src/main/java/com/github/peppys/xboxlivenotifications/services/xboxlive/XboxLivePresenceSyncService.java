package com.github.peppys.xboxlivenotifications.services.xboxlive;

import com.github.peppys.xboxlivenotifications.entities.PresenceChangedMessage;
import com.github.peppys.xboxlivenotifications.entities.XboxLivePresence;
import com.github.peppys.xboxlivenotifications.repositories.XboxLivePresenceRepository;
import com.github.peppys.xboxlivenotifications.xboxlive.XboxLiveAPIClient;
import com.github.peppys.xboxlivenotifications.xboxlive.entities.MyPresenceResponse;
import com.github.peppys.xboxlivenotifications.xboxlive.entities.SocialFriendsResponse;
import com.github.peppys.xboxlivenotifications.xboxlive.requests.MyPresenceRequest;
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

    public Flux<XboxLivePresence> findAll() {
        return repo.findAll();
    }

    public Mono<Void> queueSync() {
        return Mono.fromRunnable(queuer);
    }

    public Flux<XboxLivePresence> sync() {
        return findAllXboxProfiles()
                // Find or create in DB
                .flatMap(person -> repo.findOrCreate(person.getXuid(), XboxLivePresence.builder()
                        .id(person.getXuid())
                        .state(person.getPresenceState())
                        .fullName(person.getRealName())
                        .gamertag(person.getGamertag())
                        .lastSeenAt(person.getLastSeenDateTimeUtc())
                        .createdAt(Date.from(Instant.now()))
                        .updatedAt(Date.from(Instant.now()))
                        .build())
                        .zipWith(Mono.just(person))
                )
                // Update presence state in DB
                .flatMap(tuple -> {
                    final var presence = tuple.getT1();
                    final var person = tuple.getT2();
                    final var stateChanged = person.getPresenceState() != presence.getState();

                    presence
                            .setState(person.getPresenceState())
                            .setUpdatedAt(Date.from(Instant.now()))
                            .setLastSeenAt(person.getLastSeenDateTimeUtc());

                    return repo.save(presence)
                            .filter((p) -> stateChanged)
                            .flatMap(this::publishStateChanged)
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

    private Flux<SocialFriendsResponse.Person> findAllXboxProfiles() {
        return Mono.zip(
                xboxLive.send(MySocialFriendsRequest.builder().build(), SocialFriendsResponse.class),
                xboxLive.send(MyPresenceRequest.builder().build(), MyPresenceResponse.class)
                        .map(r -> SocialFriendsResponse.Person.builder()
                                .xuid(r.getXuid())
                                .gamertag(xboxLive.getCallerGamerTag())
                                .realName(xboxLive.getCallerFullName())
                                .presenceState(r.getState())
                                .lastSeenDateTimeUtc(r.getLastSeen().getTimestamp())
                                .build())
        )
                .onErrorMap(e -> new RuntimeException("Unexpected error while querying for xbox friends: " + e.getMessage(), e))
                // Combine caller with friends list
                .flatMapMany(tuple -> {
                    final var people = tuple.getT1().getPeople();
                    people.add(tuple.getT2());
                    return Flux.fromIterable(people);
                });
    }
}
