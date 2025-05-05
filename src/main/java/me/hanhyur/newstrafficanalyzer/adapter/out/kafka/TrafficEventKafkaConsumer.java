package me.hanhyur.newstrafficanalyzer.adapter.out.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.hanhyur.newstrafficanalyzer.application.port.out.TrafficStatsPort;
import me.hanhyur.newstrafficanalyzer.domain.traffic.model.TrafficEvent;
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
            log.debug("Processing event for articleId: {}, eventType: {}", event.getArticleId(), event.getEventType());

            switch (event.getEventType()) {
                case CLICK:
                    log.info("--> Calling incrementClickCount for articleId: {}", event.getArticleId());
                    trafficStatsPort.incrementClickCount(event.getArticleId());
                    log.info("<-- Finished incrementClickCount for articleId: {}", event.getArticleId());
                    break;
                case LIKE:
                    log.info("--> Calling incrementLikeCount for articleId: {}", event.getArticleId());
                    trafficStatsPort.incrementLikeCount(event.getArticleId());
                    log.info("<-- Finished incrementLikeCount for articleId: {}", event.getArticleId());
                    break;
                default:
                    log.warn("Unsupported event type received: {}", event.getEventType());
                    break;
            }
            log.debug("<<< Successfully processed event for article {}", event.getArticleId());

        } catch (Exception e) {
            log.error("!!! Error processing event from Kafka for article {}: {}", event.getArticleId(), e.getMessage(), e);
        }
    }

}
