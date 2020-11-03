package com.github.peppys.xboxlivenotifications.handlers;

import com.github.peppys.xboxlivenotifications.dto.SyncXboxLivePresenceResponse;
import com.github.peppys.xboxlivenotifications.services.xboxlive.XboxLivePresenceSyncService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/tasks")
public class TaskHandler {
    private final XboxLivePresenceSyncService service;

    public TaskHandler(final XboxLivePresenceSyncService service) {
        this.service = service;
    }

    @PostMapping("/sync-xbox-live-presence-async")
    public Mono<ResponseEntity<Object>> queueSync() {
        return service.queueSync()
            .thenReturn(ResponseEntity.accepted().build());
    }

    @PostMapping("/sync-xbox-live-presence")
    public Mono<ResponseEntity<SyncXboxLivePresenceResponse>> sync() {
        return service.sync()
            .collectList()
            .map(response -> ResponseEntity.ok()
                .body(SyncXboxLivePresenceResponse.builder()
                    .presences(response)
                    .build()));
    }
}
