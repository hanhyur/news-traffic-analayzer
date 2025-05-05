package me.hanhyur.newstrafficanalyzer.application.port.out;

import me.hanhyur.newstrafficanalyzer.domain.traffic.model.TrafficStats;

import java.util.Optional;

public interface TrafficStatsPort {

    void incrementClickCount(String articleId);

    void incrementLikeCount(String articleId);

    Optional<TrafficStats> findByArticleId(String articleId);

}
