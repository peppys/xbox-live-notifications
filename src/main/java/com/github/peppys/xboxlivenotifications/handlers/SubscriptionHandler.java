package com.github.peppys.xboxlivenotifications.handlers;

import com.github.peppys.xboxlivenotifications.dto.PubSubSubscriptionEvent;
import com.github.peppys.xboxlivenotifications.entities.PresenceChangedMessage;
import com.github.peppys.xboxlivenotifications.services.xboxlive.XboxLivePresenceObserver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/subscriptions")
public class SubscriptionHandler {
    private final XboxLivePresenceObserver observer;

    public SubscriptionHandler(final XboxLivePresenceObserver observer) {
        this.observer = observer;
    }

    @PostMapping("/xbox-live-status-changed")
    public Mono<ResponseEntity<Object>> statusChanged(@RequestBody PubSubSubscriptionEvent body) {
        final var message = PresenceChangedMessage.fromBase64(body.getMessage().getData());

        return observer.onPresenceChanged(message.getPresenceId(), message.getNewState())
                .thenReturn(ResponseEntity.ok().build());
    }
}
