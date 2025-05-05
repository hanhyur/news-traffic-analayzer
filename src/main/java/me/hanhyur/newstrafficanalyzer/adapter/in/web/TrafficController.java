package me.hanhyur.newstrafficanalyzer.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.hanhyur.newstrafficanalyzer.application.usecase.RecordTrafficCommand;
import me.hanhyur.newstrafficanalyzer.application.usecase.RecordTrafficUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Traffic API", description = "뉴스 트래픽 수집 API")
@RestController
@RequestMapping("/traffic")
@RequiredArgsConstructor
public class TrafficController {

    private final RecordTrafficUseCase recordTrafficUseCase;

    @Operation(summary = "트래픽 이벤트 수집", description = "기사 클릭, 좋아요 등 사용자 활동 이벤트 수신")
    @ApiResponse(responseCode = "202", description = "이벤트 처리 요청 성공 (비동기 처리)")
    @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터")
    @PostMapping
    public ResponseEntity<Void> recordTraffic(@Valid @RequestBody TrafficRequest request) {
        RecordTrafficCommand command = RecordTrafficCommand.from(
                request.getArticleId(),
                request.getEventType()
        );

        recordTrafficUseCase.recordEvent(command);

        return ResponseEntity.noContent().build();
    }

}
