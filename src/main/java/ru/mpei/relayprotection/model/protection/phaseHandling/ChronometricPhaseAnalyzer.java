package ru.mpei.relayprotection.model.protection.phaseHandling;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import ru.mpei.relayprotection.model.enumerations.CrossingType;
import ru.mpei.relayprotection.model.protection.signalHandling.StairActionManager;
import ru.mpei.relayprotection.model.protection.signalHandling.chronometric.ChronometricSignalHandler;
import ru.mpei.relayprotection.model.sv.SvReceiveRunner;

@Slf4j
@Setter
public class ChronometricPhaseAnalyzer extends PhaseAnalyzer {
    private ChronometricSignalHandler firstSideSignalHandler;
    private ChronometricSignalHandler secondSideSignalHandler;

    public ChronometricPhaseAnalyzer(double setpoint, StairActionManager stairManager) {
        super(setpoint, stairManager);
    }

    @Override
    protected void analyze() {
//        try {
//            Thread.sleep((long) this.setpoint);
//        } catch (InterruptedException e) {
//            log.error("Analyzing thread was interrupted during waiting for notification");
//            throw new RuntimeException(e);
//        }
        long startTime = System.currentTimeMillis();
        while (System.currentTimeMillis() - startTime <  setpoint) {
            if ((this.firstSideSignalHandler.getStateHolder().getCrossing() == CrossingType.UP && this.secondSideSignalHandler.getStateHolder().getCrossing() == CrossingType.DOWN)
                    || (this.firstSideSignalHandler.getStateHolder().getCrossing() == CrossingType.DOWN && this.secondSideSignalHandler.getStateHolder().getCrossing() == CrossingType.UP)) return;
        }

//        if (Math.abs(this.firstSideSignalHandler.getStateHolder().getCrossingTime() - this.secondSideSignalHandler.getStateHolder().getCrossingTime()) < this.setpoint) return;
        log.error(String.valueOf(Math.abs(this.firstSideSignalHandler.getStateHolder().getCrossingTime() - this.secondSideSignalHandler.getStateHolder().getCrossingTime())));
        Pair threadsStatus = this.isThreadsAlive();
        if (!threadsStatus.alive) {
            log.warn("The command to turn off the switch is blocked because {}", threadsStatus.msg);
            return;
        }
        if (this.firstSideSignalHandler.getStateHolder().isBlocked() || this.secondSideSignalHandler.getStateHolder().isBlocked()) {
            log.warn("Turning off blocked because very often crossings detected");
            return;
        }

        log.warn("Find fault");
        this.needToAct = true;
        this.actionManager.act();

    }

    @Override
    public synchronized void act() {
        if (this.counter++ == 0) {
            synchronized (this.locker) {
                this.locker.notifyAll();
            }
        }
        else counter = 0;
    }

    private Pair isThreadsAlive() {
        long currentTime = System.currentTimeMillis();
        StringBuilder msg = new StringBuilder();

        if (!this.firstSideSvThread.isAlive()) msg.append("First side SV thread is dead. ");
        if (!this.secondSideSvThread.isAlive()) msg.append("Second side SV thread is dead. ");
//        System.err.println("1 is empty? = " + msg.toString().isEmpty() + "; msg = " + msg.toString() + "; msg capacity: " + msg.capacity());
//        if (!msg.toString().isEmpty()) return new Pair(false, msg.toString());

//        if (currentTime - this.firstSideSvThread.getLastSvStateUpdateTs() >= setpoint) msg.append("First side SV thread is possible dead. ");
//        if (currentTime - this.secondSideSvThread.getLastSvStateUpdateTs() >= setpoint) msg.append("Second side SV thread is possible dead, ");
//        System.err.println("2 is empty? = " + msg.toString().isEmpty() + "; msg = " + msg.toString());
        return new Pair(msg.toString().isEmpty(), msg.toString());
    }

    private record Pair(boolean alive, String msg){}
}
