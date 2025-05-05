package me.hanhyur.newstrafficanalayzer.adapter.out.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.hanhyur.newstrafficanalayzer.application.port.out.TrafficEventPort;
import me.hanhyur.newstrafficanalayzer.domain.traffic.model.TrafficEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
@RequiredArgsConstructor
public class TrafficEventKafkaProducer implements TrafficEventPort {

    private final KafkaTemplate<String, TrafficEvent> kafkaTemplate;

    @Value("${app.kafka.topic}")
    private String topic;

    @Override
    public void send(TrafficEvent event) {
        String key = event.getArticleId();

        log.info("Attempting to send event to Kafka topic '{}' with key '{}' : {}", topic, key, event);

        CompletableFuture<SendResult<String, TrafficEvent>> future = kafkaTemplate.send(topic, key, event);

        future.whenComplete((result, ex) -> {
            if (ex == null) {
                log.info("Successfully send event for key '{}' to topic '{}', partition '{}', offset '{}'",
                        key,
                        result.getRecordMetadata().topic(),
                        result.getRecordMetadata().partition(),
                        result.getRecordMetadata().offset()
                );
            } else {
                log.error("Failed to send event for key '{}' to topic '{}' : {}", key, topic, ex.getMessage(), ex);

                // TODO: 전송 실패 시 재시도, Dead Letter Queue 전송 등 오류 처리 로직 추가 고려
            }
        });
    }
}
