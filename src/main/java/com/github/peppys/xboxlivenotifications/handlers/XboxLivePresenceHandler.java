package com.github.peppys.xboxlivenotifications.handlers;

import com.github.peppys.xboxlivenotifications.dto.XboxLivePresencesResponse;
import com.github.peppys.xboxlivenotifications.services.xboxlive.XboxLivePresenceSyncService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/xbox-live-presences")
public class XboxLivePresenceHandler {
    private final XboxLivePresenceSyncService service;

    public XboxLivePresenceHandler(final XboxLivePresenceSyncService service) {
        this.service = service;
    }

    @GetMapping
    public Mono<ResponseEntity<XboxLivePresencesResponse>> list() {
        return service.findAll()
                .collectList()
                .map(presences -> ResponseEntity.ok()
                        .body(XboxLivePresencesResponse.builder()
                                .presences(presences)
                                .build()));
    }

    @PostMapping("/queue-sync")
    public Mono<ResponseEntity<Object>> queueSync() {
        return service.queueSync()
                .thenReturn(ResponseEntity.accepted().build());
    }

    @PostMapping("/sync")
    public Mono<ResponseEntity<XboxLivePresencesResponse>> sync() {
        return service.sync()
                .collectList()
                .map(presences -> ResponseEntity.ok()
                        .body(XboxLivePresencesResponse.builder()
                                .presences(presences)
                                .build()));
    }
}
