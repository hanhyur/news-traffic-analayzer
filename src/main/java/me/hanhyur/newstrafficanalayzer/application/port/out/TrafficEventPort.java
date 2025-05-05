package me.hanhyur.newstrafficanalayzer.application.port.out;

import me.hanhyur.newstrafficanalayzer.domain.traffic.model.TrafficEvent;

public interface TrafficEventPort {

    void send(TrafficEvent event);

}
