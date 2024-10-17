package ru.mpei.relayprotection.model.protection.phaseHandling;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import ru.mpei.relayprotection.model.protection.phaseHandling.PhaseAnalyzer;
import ru.mpei.relayprotection.model.protection.signalHandling.StairActionManager;
import ru.mpei.relayprotection.model.protection.signalHandling.chronometric.signalState.SignalStateHolderChronometric;
import ru.mpei.relayprotection.model.sv.SvReceiveRunner;

import java.util.*;

@Slf4j
@Setter
public class ChronometricPhaseAnalyzer extends PhaseAnalyzer {
    private SvReceiveRunner firstSideSvThread;
    private SignalStateHolderChronometric firstSideStateHolder;
    private SvReceiveRunner secondSideSvThread;
    private SignalStateHolderChronometric secondSideStateHolder;


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

        if (Math.abs(this.firstSideStateHolder.getCrossingTime() - this.secondSideStateHolder.getCrossingTime()) > this.setpoint) {
            Pair threadsStatus = this.isThreadsAlive();
            if (threadsStatus.alive) {
                // ToDo: notify actionManager
            } else {
                log.error("The command to turn off the switch is blocked because {}", threadsStatus.msg);
            }
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

    private Pair isThreadsAlive() {
        long currentTime = System.currentTimeMillis();
        StringBuilder msg = new StringBuilder();

        if (!this.firstSideSvThread.isAlive()) msg.append("First side SV thread is dead. ");
        if (!this.secondSideSvThread.isAlive()) msg.append("Second side SV thread is dead. ");
        if (msg.capacity() != 0) return new Pair(false, msg.toString());

        if (currentTime - this.firstSideSvThread.getLastSvStateUpdateTs() > 1) msg.append("First side SV thread is possible dead. ");
        if (currentTime - this.secondSideSvThread.getLastSvStateUpdateTs() > 1) msg.append("Second side SV thread is possible dead, ");
        return new Pair(msg.toString().isEmpty(), msg.toString());
    }

    private record Pair(boolean alive, String msg){}
}
