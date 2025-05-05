package me.hanhyur.newstrafficanalyzer.application.usecase;

import me.hanhyur.newstrafficanalyzer.domain.traffic.model.TrafficStats;

import java.util.Optional;

public interface GetTrafficStatsUseCase {

    Optional<TrafficStats> getStatsByArticleId(String articleId);

}
