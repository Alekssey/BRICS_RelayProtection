package ru.mpei.relayprotection.model.protection.phaseHandling;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import ru.mpei.relayprotection.model.protection.phaseHandling.PhaseAnalyzer;
import ru.mpei.relayprotection.model.protection.signalHandling.StairActionManager;
import ru.mpei.relayprotection.model.protection.signalHandling.chronometric.ChronometricHandler;
import ru.mpei.relayprotection.model.protection.signalHandling.chronometric.signalState.SignalStateHolderChronometric;
import ru.mpei.relayprotection.model.sv.SvReceiveRunner;

import java.util.*;

@Slf4j
@Setter
public class ChronometricPhaseAnalyzer extends PhaseAnalyzer {
    private SvReceiveRunner firstSideSvThread;
    private ChronometricHandler firstSideSignalHandler;
    private SvReceiveRunner secondSideSvThread;
    private ChronometricHandler secondSideSignalHandler;

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

        if (Math.abs(this.firstSideSignalHandler.getStateHolder().getCrossingTime() - this.secondSideSignalHandler.getStateHolder().getCrossingTime()) < this.setpoint) return;
        Pair threadsStatus = this.isThreadsAlive();
        if (!threadsStatus.alive) {
            log.warn("The command to turn off the switch is blocked because {}", threadsStatus.msg);
            return;
        }
        if (this.firstSideSignalHandler.getStateHolder().isBlocked() || this.secondSideSignalHandler.getStateHolder().isBlocked()) {
            log.warn("Turning off blocked because very often crossings detected");
            return;
        }

        this.needToAct = true;
        this.actionManager.act();

    }

    @Override
    public synchronized void act() {
        if (this.counter++ == 0) this.locker.notify();
        else counter = 0;
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
