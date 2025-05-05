package me.hanhyur.newstrafficanalyzer.application.service;

import me.hanhyur.newstrafficanalyzer.application.port.out.TrafficEventPort;
import me.hanhyur.newstrafficanalyzer.application.usecase.RecordTrafficCommand;
import me.hanhyur.newstrafficanalyzer.domain.traffic.model.TrafficEvent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RecordTrafficServiceTest {

    @InjectMocks
    private RecordTrafficService recordTrafficService;

    @Mock
    private TrafficEventPort trafficEventPort;

    @Captor
    private ArgumentCaptor<TrafficEvent> trafficEventCaptor;

    @Test
    @DisplayName("recordEvent 호출 시 TrafficEvent를 생성하고 TrafficEventPort.send를 호출해야 한다")
    void recordEvent_shouldCreateEventAndCallPort() {
        String articleId = "test-article-123";
        TrafficEvent.EventType eventType = TrafficEvent.EventType.CLICK;
        RecordTrafficCommand command = RecordTrafficCommand.from(articleId, eventType);

        recordTrafficService.recordEvent(command);

        verify(trafficEventPort, times(1)).send(trafficEventCaptor.capture());

        TrafficEvent capturedEvent = trafficEventCaptor.getValue();
        assertThat(capturedEvent).isNotNull();
        assertThat(capturedEvent.getArticleId()).isEqualTo(articleId);
        assertThat(capturedEvent.getEventType()).isEqualTo(eventType);
        assertThat(capturedEvent.getTimestamp()).isNotNull();
    }

}