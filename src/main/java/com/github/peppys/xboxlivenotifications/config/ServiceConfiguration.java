package com.github.peppys.xboxlivenotifications.config;

import com.github.peppys.xboxlivenotifications.entities.PresenceChangedMessage;
import com.github.peppys.xboxlivenotifications.repositories.XboxLivePresenceRepository;
import com.github.peppys.xboxlivenotifications.services.xboxlive.XboxLivePresenceSyncService;
import com.github.peppys.xboxlivenotifications.xboxlive.XboxLiveAPIClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gcp.pubsub.core.PubSubTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
@Slf4j
public class ServiceConfiguration {
    @Bean
    public XboxLivePresenceSyncService xboxLivePresenceSyncService(
        final XboxLiveAPIClient xboxLive,
        final XboxLivePresenceRepository repo,
        final PubSubTemplate pubsub,
        @Value("${xboxlivenotifications.presence-changed-topic}") final String topic
    ) {
        Runnable queuer = () -> log.info("TODO - QUEUE");
        Consumer<PresenceChangedMessage> publisher = (message) -> pubsub.publish(topic, message);

        return new XboxLivePresenceSyncService(
            xboxLive,
            repo,
            queuer,
            publisher
        );
    }
}
