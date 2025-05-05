package me.hanhyur.newstrafficanalyzer.application.service;

import me.hanhyur.newstrafficanalyzer.application.port.out.TrafficStatsPort;
import me.hanhyur.newstrafficanalyzer.domain.traffic.model.TrafficStats;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class GetTrafficStatsServiceTest {

    @InjectMocks
    private GetTrafficStatsService getTrafficStatsService;

    @Mock
    private TrafficStatsPort trafficStatsPort;

    @Test
    @DisplayName("getStatsByArticleId 호출 시 Port가 통계 데이터를 반환하면, 해당 데이터가 포함된 Optional을 반환해야 한다")
    void getStatsByArticleId_whenStatsExist_shouldReturnOptionalWithStats() {
        String articleId = "article-found";
        TrafficStats expectedStats = TrafficStats.builder()
                .articleId(articleId)
                .clickCount(100)
                .likeCount(10)
                .build();

        given(trafficStatsPort.findByArticleId(articleId)).willReturn(Optional.of(expectedStats));

        Optional<TrafficStats> resultOptional = getTrafficStatsService.getStatsByArticleId(articleId);

        assertThat(resultOptional).isPresent();
        assertThat(resultOptional.get()).isEqualTo(expectedStats);
        then(trafficStatsPort).should(times(1)).findByArticleId(articleId);
    }

    @Test
    @DisplayName("getStatsByArticleId 호출 시 Port가 빈 Optional을 반환하면, 빈 Optional을 그대로 반환해야 한다")
    void getStatsByArticleId_whenStatsNotExist_shouldReturnEmptyOptional() {
        String articleId = "article-not-found";

        given(trafficStatsPort.findByArticleId(articleId)).willReturn(Optional.empty());

        Optional<TrafficStats> resultOptional = getTrafficStatsService.getStatsByArticleId(articleId);

        assertThat(resultOptional).isEmpty();
        then(trafficStatsPort).should(times(1)).findByArticleId(articleId);
    }

}