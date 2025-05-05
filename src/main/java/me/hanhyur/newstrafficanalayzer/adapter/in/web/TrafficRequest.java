package me.hanhyur.newstrafficanalayzer.adapter.in.web;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.hanhyur.newstrafficanalayzer.domain.traffic.model.TrafficEvent;

@Getter
@Setter
@NoArgsConstructor
public class TrafficRequest {

    @NotBlank(message = "Article ID는 필수")
    private String articleId;

    @NotBlank(message = "Event Type은 필수")
    private TrafficEvent.EventType eventType;

}
