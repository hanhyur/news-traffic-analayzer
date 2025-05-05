package me.hanhyur.newstrafficanalayzer.adapter.out.redis;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.hanhyur.newstrafficanalayzer.application.port.out.TrafficStatsPort;
import me.hanhyur.newstrafficanalayzer.domain.traffic.model.TrafficStats;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class TrafficStatsRedisRepository implements TrafficStatsPort {

    private final StringRedisTemplate redisTemplate;

    private static final String KEY_PREFIX = "traffic_stats:";
    private static final String FIELD_CLICK_COUNT = "clickCount";
    private static final String FIELD_LIKE_COUNT = "likeCount";

    private String generateKey(String articleId) {
        return KEY_PREFIX + articleId;
    }

    @Override
    public void incrementClickCount(String articleId) {
        String key = generateKey(articleId);

        try {
            long newCount = redisTemplate.opsForHash().increment(KEY_PREFIX, FIELD_CLICK_COUNT, 1L);

            log.debug("Incremented click count for article {}: new count = {}", articleId, newCount);
        } catch (Exception e) {
            log.error("Failed to increment click count for article {} in Redis", articleId, e);

            // TODO Redis 장애 대응
        }
    }

    @Override
    public void incrementLikeCount(String articleId) {
        String key = generateKey(articleId);

        try {
            long newCount = redisTemplate.opsForHash().increment(key, FIELD_LIKE_COUNT, 1L);

            log.debug("Incremented like count for article {}: new count = {}", articleId, newCount);
        } catch (Exception e) {
            log.error("Failed to increment like count for article {} in Redis", articleId, e);

            // TODO Redis 장애 대응
        }
    }

    @Override
    public Optional<TrafficStats> findByArticleId(String articleId) {
        String key = generateKey(articleId);

        try {
            HashOperations<String, String, String> hashOps = redisTemplate.opsForHash();
            Map<String, String> entries = hashOps.entries(key);

            if (entries.isEmpty()) {
                log.debug("No stats found in Redis for article {}", articleId);

                return Optional.empty();
            }

            long clickCount = parseLongSafely(entries.get(FIELD_CLICK_COUNT));
            long likeCount = parseLongSafely(entries.get(FIELD_LIKE_COUNT));

            TrafficStats stats = TrafficStats.builder()
                    .articleId(articleId)
                    .clickCount(clickCount)
                    .likeCount(likeCount)
                    .build();

            log.debug("Found stats in Redis for article {}: {}", articleId, stats);

            return Optional.of(stats);
        } catch (Exception e) {
            log.error("Failed to find stats for article {} from Redis", articleId, e);

            return Optional.empty();
        }
    }

    private long parseLongSafely(String value) {
        if (value == null) {
            return 0L;
        }

        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            log.warn("Could not parse long value: '{}'. Defaulting to 0.", value);

            return 0L;
        }
    }

}
