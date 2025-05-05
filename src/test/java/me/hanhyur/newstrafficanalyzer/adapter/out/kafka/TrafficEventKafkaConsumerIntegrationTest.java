package me.hanhyur.newstrafficanalyzer.adapter.out.kafka;

import me.hanhyur.newstrafficanalyzer.AbstractIntegrationTest;
import me.hanhyur.newstrafficanalyzer.domain.traffic.model.TrafficEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

class TrafficEventKafkaConsumerIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private KafkaTemplate<String, TrafficEvent> kafkaTemplate;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Value("${app.kafka.topic}")
    private String topic;

    private final String redisKeyPrefix = "traffic_stats:";
    private final String clickCountField = "clickCount";
    private final String likeCountField = "likeCount";

    @BeforeEach
    void setUp() {
        redisTemplate.delete(redisTemplate.keys(redisKeyPrefix + "*"));
    }

    @Test
    @DisplayName("CLICK 이벤트 수신 시 Redis의 clickCount가 1 증가해야 한다")
    void shouldConsumeClickEventAndIncrementRedisClickCount() {
        String articleId = "article-integ-test-1";
        TrafficEvent clickEvent = TrafficEvent.builder()
                .articleId(articleId)
                .eventType(TrafficEvent.EventType.CLICK)
                .timestamp(LocalDateTime.now())
                .build();
        String redisKey = redisKeyPrefix + articleId;

        kafkaTemplate.send(topic, clickEvent.getArticleId(), clickEvent);

        await().atMost(10, TimeUnit.SECONDS).untilAsserted(() -> {
            String clickCount = (String) redisTemplate.opsForHash().get(redisKey, clickCountField);
            assertThat(clickCount).isNotNull();
            assertThat(Long.parseLong(clickCount)).isEqualTo(1L);
        });

        String likeCount = (String) redisTemplate.opsForHash().get(redisKey, likeCountField);
        assertThat(likeCount).isNull();
    }

    @Test
    @DisplayName("LIKE 이벤트 수신 시 Redis의 likeCount가 1 증가해야 한다")
    void shouldConsumeLikeEventAndIncrementRedisLikeCount() {
        String articleId = "article-integ-test-2";
        TrafficEvent likeEvent = TrafficEvent.builder()
                .articleId(articleId)
                .eventType(TrafficEvent.EventType.LIKE)
                .timestamp(LocalDateTime.now())
                .build();
        String redisKey = redisKeyPrefix + articleId;

        kafkaTemplate.send(topic, likeEvent.getArticleId(), likeEvent);

        await().atMost(10, TimeUnit.SECONDS).untilAsserted(() -> {
            String likeCount = (String) redisTemplate.opsForHash().get(redisKey, likeCountField);
            assertThat(likeCount).isNotNull().isEqualTo("1");
        });

        String clickCount = (String) redisTemplate.opsForHash().get(redisKey, clickCountField);
        assertThat(clickCount).isNull();
    }

    @Test
    @DisplayName("동일 기사에 대해 여러 CLICK 이벤트 수신 시 clickCount가 누적되어야 한다")
    void shouldAccumulateCountsForMultipleEvents() {
        String articleId = "article-integ-test-3";
        TrafficEvent clickEvent1 = TrafficEvent.builder().articleId(articleId).eventType(TrafficEvent.EventType.CLICK).timestamp(LocalDateTime.now()).build();
        TrafficEvent clickEvent2 = TrafficEvent.builder().articleId(articleId).eventType(TrafficEvent.EventType.CLICK).timestamp(LocalDateTime.now()).build();
        String redisKey = redisKeyPrefix + articleId;

        kafkaTemplate.send(topic, clickEvent1.getArticleId(), clickEvent1);
        kafkaTemplate.send(topic, clickEvent2.getArticleId(), clickEvent2);

        await().atMost(10, TimeUnit.SECONDS).untilAsserted(() -> {
            String clickCount = (String) redisTemplate.opsForHash().get(redisKey, clickCountField);
            assertThat(clickCount).isNotNull().isEqualTo("2");
        });
    }

}