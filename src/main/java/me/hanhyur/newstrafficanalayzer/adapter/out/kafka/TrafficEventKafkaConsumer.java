package me.hanhyur.newstrafficanalayzer.adapter.out.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.hanhyur.newstrafficanalayzer.application.port.out.TrafficStatsPort;
import me.hanhyur.newstrafficanalayzer.domain.traffic.model.TrafficEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TrafficEventKafkaConsumer {

    private final TrafficStatsPort trafficStatsPort;

    @KafkaListener(topics = "${app.kafka.topic}", groupId = "${spring.kafka.consumer.group-id}")
    public void consumeTrafficEvents(@Payload TrafficEvent event) {
        log.info("Received event from Kafka: {}", event);

        if (event == null || event.getArticleId() == null || event.getEventType() == null) {
            log.warn("Received invalid event data (null or missing fields): {}", event);

            // TODO 잘못된 데이터 처리

            return;
        }

        try {
            switch (event.getEventType()) {
                case CLICK -> trafficStatsPort.incrementClickCount(event.getArticleId());
                case LIKE -> trafficStatsPort.incrementLikeCount(event.getArticleId());
                default -> log.warn("Unsupported event type received: {}", event.getEventType());
            }

            log.debug("Successfully processed event for article {}", event.getArticleId());
        } catch (Exception e) {
            log.error("Error processing event from Kafka for article {}: {}", event.getArticleId(), e.getMessage(), e);

            // TODO 에러 처리
        }
    }

}
