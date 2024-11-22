package ru.mpei.relayprotection.model.protection.signalHandling;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import ru.mpei.relayprotection.model.protection.LineProtection;
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
    private LineProtection protection;
    private Thread sendingCommandTask;

    public StairActionManager() {
        this.configureNotifyingTask();
    }

    private void configureNotifyingTask() {
        this.sendingCommandTask = new Thread(() -> {
            boolean response = false;
            while (!response) {
                response = this.gateway.sendCommand(this.tag, 0);
                if (!response) {
                    try {
                        log.warn("bad response from sending command");
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            this.configureNotifyingTask();
        });
    }


    public synchronized void act() {
        StringBuilder sb = new StringBuilder();
        if (this.phaseA.isNeedToAct()) sb.append("phase A damaged. ");
        if (this.phaseB.isNeedToAct()) sb.append("phase B damaged. ");
        if (this.phaseC.isNeedToAct()) sb.append("phase C damaged. ");
        log.warn(sb.toString());
        this.protection.stop();
//        if (!this.sendingCommandTask.isAlive()) this.sendingCommandTask.start();
    }
}
