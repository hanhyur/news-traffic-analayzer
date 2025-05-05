package me.hanhyur.newstrafficanalyzer.application.service;

import lombok.RequiredArgsConstructor;
import me.hanhyur.newstrafficanalyzer.application.port.out.TrafficEventPort;
import me.hanhyur.newstrafficanalyzer.application.usecase.RecordTrafficCommand;
import me.hanhyur.newstrafficanalyzer.application.usecase.RecordTrafficUseCase;
import me.hanhyur.newstrafficanalyzer.domain.traffic.model.TrafficEvent;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RecordTrafficService implements RecordTrafficUseCase {

    private final TrafficEventPort trafficEventPort;

    @Override
    public void recordEvent(RecordTrafficCommand command) {
        TrafficEvent event = TrafficEvent.builder()
                .articleId(command.getArticleId())
                .eventType(command.getEventType())
                .timestamp(LocalDateTime.now())
                .build();

        trafficEventPort.send(event);
    }
}
