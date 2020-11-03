package com.github.peppys.xboxlivenotifications.services.sms;

import reactor.core.publisher.Mono;

public interface SMSService {
    Mono<Void> send(final String to, final String message);
}
