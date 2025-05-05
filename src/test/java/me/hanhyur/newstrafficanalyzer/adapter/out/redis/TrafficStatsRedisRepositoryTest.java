package me.hanhyur.newstrafficanalyzer.adapter.out.redis;

import me.hanhyur.newstrafficanalyzer.AbstractIntegrationTest;
import me.hanhyur.newstrafficanalyzer.domain.traffic.model.TrafficStats;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class TrafficStatsRedisRepositoryTest extends AbstractIntegrationTest {

    @Autowired
    private TrafficStatsRedisRepository trafficStatsRedisRepository;

    @Autowired
    private StringRedisTemplate redisTemplate;

    private final String redisKeyPrefix = "traffic_stats:";
    private final String clickCountField = "clickCount";
    private final String likeCountField = "likeCount";

    @BeforeEach
    @AfterEach
    void cleanup() {
        redisTemplate.delete(redisTemplate.keys(redisKeyPrefix + "*"));
    }

    private String generateKey(String articleId) {
        return redisKeyPrefix + articleId;
    }

    @Test
    @DisplayName("incrementClickCount 호출 시 해당 기사의 clickCount가 1 증가해야 한다")
    void incrementClickCount_shouldIncrementClickCountByOne() {
        String articleId = "redis-test-article-1";
        String redisKey = generateKey(articleId);

        assertThat(redisTemplate.opsForHash().get(redisKey, clickCountField)).isNull();

        trafficStatsRedisRepository.incrementClickCount(articleId);
        assertThat(redisTemplate.opsForHash().get(redisKey, clickCountField)).isEqualTo("1");

        trafficStatsRedisRepository.incrementClickCount(articleId);
        assertThat(redisTemplate.opsForHash().get(redisKey, clickCountField)).isEqualTo("2");

        assertThat(redisTemplate.opsForHash().get(redisKey, likeCountField)).isNull();
    }

    @Test
    @DisplayName("incrementLikeCount 호출 시 해당 기사의 likeCount가 1 증가해야 한다")
    void incrementLikeCount_shouldIncrementLikeCountByOne() {
        String articleId = "redis-test-article-2";
        String redisKey = generateKey(articleId);

        assertThat(redisTemplate.opsForHash().get(redisKey, likeCountField)).isNull();

        trafficStatsRedisRepository.incrementLikeCount(articleId);
        assertThat(redisTemplate.opsForHash().get(redisKey, likeCountField)).isEqualTo("1");

        trafficStatsRedisRepository.incrementLikeCount(articleId);
        assertThat(redisTemplate.opsForHash().get(redisKey, likeCountField)).isEqualTo("2");

        assertThat(redisTemplate.opsForHash().get(redisKey, clickCountField)).isNull();
    }

    @Test
    @DisplayName("findByArticleId 호출 시 Redis에 저장된 통계 정보를 TrafficStats 객체로 반환해야 한다")
    void findByArticleId_whenStatsExist_shouldReturnTrafficStats() {
        String articleId = "redis-test-article-3";
        String redisKey = generateKey(articleId);

        redisTemplate.opsForHash().put(redisKey, clickCountField, "15");
        redisTemplate.opsForHash().put(redisKey, likeCountField, "7");

        Optional<TrafficStats> statsOptional = trafficStatsRedisRepository.findByArticleId(articleId);

        assertThat(statsOptional).isPresent();
        TrafficStats stats = statsOptional.get();
        assertThat(stats.getArticleId()).isEqualTo(articleId);
        assertThat(stats.getClickCount()).isEqualTo(15L);
        assertThat(stats.getLikeCount()).isEqualTo(7L);
    }

    @Test
    @DisplayName("findByArticleId 호출 시 Redis에 해당 기사 정보가 없으면 빈 Optional을 반환해야 한다")
    void findByArticleId_whenStatsNotExist_shouldReturnEmptyOptional() {
        String articleId = "redis-test-article-nonexistent";

        Optional<TrafficStats> statsOptional = trafficStatsRedisRepository.findByArticleId(articleId);

        assertThat(statsOptional).isEmpty();
    }

    @Test
    @DisplayName("findByArticleId 호출 시 clickCount 또는 likeCount 필드 중 하나만 있어도 조회되어야 한다")
    void findByArticleId_whenPartialStatsExist_shouldReturnTrafficStatsWithZeroForMissingField() {
        String articleId = "redis-test-article-partial";
        String redisKey = generateKey(articleId);

        redisTemplate.opsForHash().put(redisKey, clickCountField, "5");

        Optional<TrafficStats> statsOptional = trafficStatsRedisRepository.findByArticleId(articleId);

        assertThat(statsOptional).isPresent();
        TrafficStats stats = statsOptional.get();
        assertThat(stats.getArticleId()).isEqualTo(articleId);
        assertThat(stats.getClickCount()).isEqualTo(5L);
        assertThat(stats.getLikeCount()).isZero();

        redisTemplate.delete(redisKey);
        redisTemplate.opsForHash().put(redisKey, likeCountField, "3");

        statsOptional = trafficStatsRedisRepository.findByArticleId(articleId);

        assertThat(statsOptional).isPresent();
        stats = statsOptional.get();
        assertThat(stats.getArticleId()).isEqualTo(articleId);
        assertThat(stats.getClickCount()).isZero();
        assertThat(stats.getLikeCount()).isEqualTo(3L);
    }

    @Test
    @DisplayName("findByArticleId 호출 시 Redis 필드 값이 숫자가 아니면 0으로 파싱해야 한다 (parseLongSafely)")
    void findByArticleId_whenFieldValueIsNotNumber_shouldParseAsZero() {
        String articleId = "redis-test-article-invalid";
        String redisKey = generateKey(articleId);

        redisTemplate.opsForHash().put(redisKey, clickCountField, "not-a-number");
        redisTemplate.opsForHash().put(redisKey, likeCountField, "10");

        Optional<TrafficStats> statsOptional = trafficStatsRedisRepository.findByArticleId(articleId);

        assertThat(statsOptional).isPresent();
        TrafficStats stats = statsOptional.get();
        assertThat(stats.getArticleId()).isEqualTo(articleId);
        assertThat(stats.getClickCount()).isZero();
        assertThat(stats.getLikeCount()).isEqualTo(10L);
    }

}