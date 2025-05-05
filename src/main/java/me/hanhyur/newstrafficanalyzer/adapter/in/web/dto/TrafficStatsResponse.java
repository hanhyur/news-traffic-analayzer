package me.hanhyur.newstrafficanalyzer.adapter.in.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.hanhyur.newstrafficanalyzer.domain.traffic.model.TrafficStats;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrafficStatsResponse {

    private String articleId;
    private long clickCount;
    private long likeCount;

    public static TrafficStatsResponse from(TrafficStats stats) {
        return TrafficStatsResponse.builder()
                .articleId(stats.getArticleId())
                .clickCount(stats.getClickCount())
                .likeCount(stats.getLikeCount())
                .build();
    }

}
