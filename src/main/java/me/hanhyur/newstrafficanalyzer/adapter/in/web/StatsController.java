package me.hanhyur.newstrafficanalyzer.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import me.hanhyur.newstrafficanalyzer.adapter.in.web.dto.TrafficStatsResponse;
import me.hanhyur.newstrafficanalyzer.application.usecase.GetTrafficStatsUseCase;
import me.hanhyur.newstrafficanalyzer.domain.traffic.model.TrafficStats;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@Tag(name = "Stats API", description = "뉴스 통계 조회 API")
@RestController
@RequestMapping("/stats")
@RequiredArgsConstructor
public class StatsController {

    private final GetTrafficStatsUseCase getTrafficStatsUseCase;

    @Operation(summary = "기사별 통계 조회", description = "특정 기사 클릭 수, 좋아요 수 등 통계 조회")
    @ApiResponse(responseCode = "200", description = "통계 조회 성공",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = TrafficStatsResponse.class)))
    @ApiResponse(responseCode = "404", description = "해당 기사 통계 정보 없음")
    @GetMapping("/{articleId}")
    public ResponseEntity<TrafficStatsResponse> getTrafficStats(
            @Parameter(description = "조회할 기사 ID", required = true) @PathVariable String articleId) {
        Optional<TrafficStats> statsOptional = getTrafficStatsUseCase.getStatsByArticleId(articleId);

        return statsOptional
                .map(stats -> ResponseEntity.ok(TrafficStatsResponse.from(stats)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
