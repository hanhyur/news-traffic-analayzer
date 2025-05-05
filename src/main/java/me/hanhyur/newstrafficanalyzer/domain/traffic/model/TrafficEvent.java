package me.hanhyur.newstrafficanalyzer.domain.traffic.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder(toBuilder = true)
@ToString
@EqualsAndHashCode
public class TrafficEvent {

    private final String articleId;
    private final EventType eventType;
    private final LocalDateTime timestamp;

    @JsonCreator
    public TrafficEvent(
            @JsonProperty("articleId") String articleId,
            @JsonProperty("eventType") EventType eventType,
            @JsonProperty("timestamp") LocalDateTime timestamp) {
        this.articleId = articleId;
        this.eventType = eventType;
        this.timestamp = timestamp;
    }

    public enum EventType {
        CLICK,
        LIKE,
        VIEW
    }

}
