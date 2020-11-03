package com.github.peppys.xboxlivenotifications.services.sms;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
public class TwilioSMSService implements SMSService {
    @Override
    public Mono<Void> send(final String to, final String message) {
        log.info("Sending text to {}: {}", to, message);
        // TODO
        return Mono.empty();
    }
}
