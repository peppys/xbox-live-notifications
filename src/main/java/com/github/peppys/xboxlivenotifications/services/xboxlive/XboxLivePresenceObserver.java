package com.github.peppys.xboxlivenotifications.services.xboxlive;

import com.github.peppys.xboxlivenotifications.repositories.XboxLivePresenceRepository;
import com.github.peppys.xboxlivenotifications.services.sms.SMSService;
import com.github.peppys.xboxlivenotifications.xboxlive.entities.SocialFriendsResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.Date;

@Component
@Slf4j
public class XboxLivePresenceObserver {
    private final SMSService sms;
    private final XboxLivePresenceRepository repo;
    private final String phone;

    public XboxLivePresenceObserver(
            final SMSService sms,
            final XboxLivePresenceRepository repo,
            @Value("${xboxlivenotifications.subscribing-phone-number}") final String phone) {
        this.sms = sms;
        this.repo = repo;
        this.phone = phone;
    }

    public Mono<Void> onPresenceChanged(final String id, final SocialFriendsResponse.Person.PresenceState state) {
        return Mono.just(state)
                .filter(s -> s == SocialFriendsResponse.Person.PresenceState.Online)
                .flatMap(s -> repo.findById(id)
                        .switchIfEmpty(Mono.error(new IllegalArgumentException(String.format("%s does not exist", id))))
                )
                .doOnNext(presence -> log.info("Notifying that {} has just become {}", presence.getGamertag(), state))
                .flatMap(presence -> sms.send(phone, String.format("%s has just logged on to Xbox Live!", presence.getGamertag()))
                        .thenReturn(presence))
                .flatMap(presence -> repo.save(
                        presence
                                .setLastNotifiedAt(Date.from(Instant.now()))
                                .setUpdatedAt(Date.from(Instant.now()))
                        )
                )
                .then();
    }
}
