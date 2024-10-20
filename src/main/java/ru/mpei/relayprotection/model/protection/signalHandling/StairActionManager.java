package ru.mpei.relayprotection.model.protection.signalHandling;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import ru.mpei.relayprotection.model.protection.phaseHandling.PhaseAnalyzer;
import ru.mpei.relayprotection.service.GateWayService;

@Slf4j
@Data
public class StairActionManager {
    private GateWayService gateway;
    private PhaseAnalyzer phaseA;
    private PhaseAnalyzer phaseB;
    private PhaseAnalyzer phaseC;
    private String tag;

    public synchronized void act() {
        StringBuilder sb = new StringBuilder();
        if (this.phaseA.isNeedToAct()) sb.append("phase A damaged. ");
        if (this.phaseB.isNeedToAct()) sb.append("phase B damaged. ");
        if (this.phaseC.isNeedToAct()) sb.append("phase C damaged. ");
        log.warn(sb.toString());
        this.gateway.sendCommand(this.tag, 0);
    }
}
