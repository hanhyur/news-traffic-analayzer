package me.hanhyur.newstrafficanalyzer.application.port.out;

import me.hanhyur.newstrafficanalyzer.domain.traffic.model.TrafficEvent;

public interface TrafficEventPort {

    void send(TrafficEvent event);

}
