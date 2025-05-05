package me.hanhyur.newstrafficanalyzer.domain.traffic.model;

import lombok.*;

@Getter
@AllArgsConstructor
@Builder(toBuilder = true)
@ToString
@EqualsAndHashCode
public class TrafficStats {

    private final String articleId;

    @With
    private final long clickCount;

    @With
    private final long likeCount;

    public static class TrafficStatsBuilder {
        private long clickCount = 0L;
        private long likeCount = 0L;
    }

    public TrafficStats incrementClickCount() {
        return this.withClickCount(this.clickCount + 1);
    }

    public TrafficStats incrementLikeCount() {
        return this.withLikeCount(this.likeCount + 1);
    }
}
