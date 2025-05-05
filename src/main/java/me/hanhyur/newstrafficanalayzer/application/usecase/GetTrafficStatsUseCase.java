package me.hanhyur.newstrafficanalayzer.application.usecase;

import me.hanhyur.newstrafficanalayzer.domain.traffic.model.TrafficStats;

import java.util.Optional;

public interface GetTrafficStatsUseCase {

    Optional<TrafficStats> getStatsByArticleId(String articleId);

}
