package me.hanhyur.newstrafficanalyzer.application.service;

import lombok.RequiredArgsConstructor;
import me.hanhyur.newstrafficanalyzer.application.port.out.TrafficStatsPort;
import me.hanhyur.newstrafficanalyzer.application.usecase.GetTrafficStatsUseCase;
import me.hanhyur.newstrafficanalyzer.domain.traffic.model.TrafficStats;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GetTrafficStatsService implements GetTrafficStatsUseCase {

    private final TrafficStatsPort trafficStatsPort;

    @Override
    public Optional<TrafficStats> getStatsByArticleId(String articleId) {
        return trafficStatsPort.findByArticleId(articleId);
    }

}
