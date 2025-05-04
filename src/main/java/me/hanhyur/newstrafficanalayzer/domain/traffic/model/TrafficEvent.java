package me.hanhyur.newstrafficanalayzer.domain.traffic.model;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder(toBuilder = true)
@ToString
@EqualsAndHashCode
public class TrafficEvent {

    private final String articleId;
    private final EventType eventType;
    private final LocalDateTime timestamp;

    public enum EventType {
        CLICK,
        LIKE,
        VIEW
    }

}
