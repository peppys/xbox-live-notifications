package com.github.peppys.xboxlivenotifications.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Builder
@Data
public class PubSubSubscriptionEvent {
    private Message message;
    private String subscription;

    @Builder
    @Data
    public static class Message {
        private String data;
        private String messageId;
        private Date publishTime;
    }
}
