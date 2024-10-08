package ru.mpei.relayprotection.model.protection.phaseHandling;

import lombok.extern.slf4j.Slf4j;
import ru.mpei.relayprotection.model.protection.phaseHandling.PhaseAnalyzer;
import ru.mpei.relayprotection.model.protection.signalHandling.StairActionManager;

import java.util.*;

@Slf4j
public class ChronometricPhaseAnalyzer extends PhaseAnalyzer {
    private final Map<Thread, Object[]> sidesData = new HashMap<>();


    public ChronometricPhaseAnalyzer(double setpoint, StairActionManager stairManager) {
        super(setpoint, stairManager);
    }


    @Override
    protected void analyze() {
        try {
            Thread.sleep((long) this.setpoint);
        } catch (InterruptedException e) {
            log.error("Analyzing thread was interrupted during waiting for notification");
            throw new RuntimeException(e);
        }

        if (this.sidesData.entrySet().stream().anyMatch(Objects::isNull)) {
            // ToDo: notify actionManager
        }
    }

    @Override
    public synchronized void act() {
        if (this.counter == 0) {
            this.locker.notify();
        } else {
            counter = 0;
        }
        this.counter ++;
    }
}
