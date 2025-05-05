package me.hanhyur.newstrafficanalyzer.domain.traffic.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class TrafficStatsTest {

    @Test
    @DisplayName("incrementClickCount는 클릭 수가 1 증가된 새 TrafficStats 객체를 반환해야 한다")
    void incrementClickCount_shouldReturnNewInstanceWithIncrementedCount() {
        TrafficStats initialStats = TrafficStats.builder()
                .articleId("article-1")
                .clickCount(5)
                .likeCount(2)
                .build();

        TrafficStats incrementedStats = initialStats.incrementClickCount();

        assertThat(incrementedStats).isNotSameAs(initialStats);

        assertThat(incrementedStats.getClickCount()).isEqualTo(initialStats.getClickCount() + 1);

        assertThat(incrementedStats.getArticleId()).isEqualTo(initialStats.getArticleId());
        assertThat(incrementedStats.getLikeCount()).isEqualTo(initialStats.getLikeCount());

        assertThat(initialStats.getClickCount()).isEqualTo(5);
        assertThat(initialStats.getLikeCount()).isEqualTo(2);
    }

    @Test
    @DisplayName("incrementLikeCount는 좋아요 수가 1 증가된 새 TrafficStats 객체를 반환해야 한다")
    void incrementLikeCount_shouldReturnNewInstanceWithIncrementedCount() {
        TrafficStats initialStats = TrafficStats.builder()
                .articleId("article-2")
                .clickCount(10)
                .likeCount(3)
                .build();

        TrafficStats incrementedStats = initialStats.incrementLikeCount();

        assertThat(incrementedStats).isNotSameAs(initialStats);
        assertThat(incrementedStats.getLikeCount()).isEqualTo(initialStats.getLikeCount() + 1);
        assertThat(incrementedStats.getArticleId()).isEqualTo(initialStats.getArticleId());
        assertThat(incrementedStats.getClickCount()).isEqualTo(initialStats.getClickCount());
        assertThat(initialStats.getLikeCount()).isEqualTo(3);
    }

    @Test
    @DisplayName("Builder 기본값으로 카운트가 0으로 설정되어야 한다")
    void builder_shouldInitializeCountsToZero() {
        TrafficStats stats = TrafficStats.builder()
                .articleId("article-3")
                .build();

        assertThat(stats.getArticleId()).isEqualTo("article-3");
        assertThat(stats.getClickCount()).isZero();
        assertThat(stats.getLikeCount()).isZero();
    }
}