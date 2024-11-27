package ru.mpei.relayprotection.model.protection.phaseHandling;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import ru.mpei.relayprotection.model.protection.signalHandling.chronometric.ChronometricSignalHandler;

@Slf4j
@Setter
public class ChronometricPhaseAnalyzer extends PhaseAnalyzer {
    private ChronometricSignalHandler firstSideSignalHandler;
    private ChronometricSignalHandler secondSideSignalHandler;

    public ChronometricPhaseAnalyzer(double setpoint) {
        super(setpoint);
        this.needToAct.set(false);
    }

    @Override
    public void process() {
        if (this.firstSideSignalHandler.getStateHolder().isHaveNeeCrossPoint()
                && !this.secondSideSignalHandler.getStateHolder().isHaveNeeCrossPoint()
                && System.currentTimeMillis() - this.firstSideSignalHandler.getStateHolder().getCrossingTime() >= this.setpoint) {
            if (!this.firstSideSignalHandler.getStateHolder().isBlocked() && !this.secondSideSignalHandler.getStateHolder().isBlocked()) {
                this.needToAct.set(true);
            } else {
                System.out.println("often crossings detected");
            }
        }
        else if (!this.firstSideSignalHandler.getStateHolder().isHaveNeeCrossPoint()
                && this.secondSideSignalHandler.getStateHolder().isHaveNeeCrossPoint()
                && System.currentTimeMillis() - this.secondSideSignalHandler.getStateHolder().getCrossingTime() >= this.setpoint) {
            if (!this.firstSideSignalHandler.getStateHolder().isBlocked() && !this.secondSideSignalHandler.getStateHolder().isBlocked()) {
                this.needToAct.set(true);
            } else {
                System.out.println("often crossings detected");
            }
        }
        else if (this.firstSideSignalHandler.getStateHolder().isHaveNeeCrossPoint()
                && this.secondSideSignalHandler.getStateHolder().isHaveNeeCrossPoint()) {
            if (this.firstSideSignalHandler.getStateHolder().getCrossing() == this.secondSideSignalHandler.getStateHolder().getCrossing()) {
                if (!this.firstSideSignalHandler.getStateHolder().isBlocked() && !this.secondSideSignalHandler.getStateHolder().isBlocked()) {
                    this.needToAct.set(true);
                } else {
                    System.out.println("often crossings detected");
                }
            } else {
                this.needToAct.set(false);
                this.firstSideSignalHandler.getStateHolder().deactivate();
                this.secondSideSignalHandler.getStateHolder().deactivate();
            }
        }


//        long startTime = System.currentTimeMillis();
//        while (System.currentTimeMillis() - startTime <  setpoint) {
//            if ((this.firstSideSignalHandler.getStateHolder().getCrossing() == CrossingType.UP && this.secondSideSignalHandler.getStateHolder().getCrossing() == CrossingType.DOWN)
//                    || (this.firstSideSignalHandler.getStateHolder().getCrossing() == CrossingType.DOWN && this.secondSideSignalHandler.getStateHolder().getCrossing() == CrossingType.UP)) {
//                System.err.println("go out before setpoint");
//                return;
//            }
//        }
//        System.err.println("go forward");
//        log.error(String.valueOf(Math.abs(this.firstSideSignalHandler.getStateHolder().getCrossingTime() - this.secondSideSignalHandler.getStateHolder().getCrossingTime())));
//        Pair threadsStatus = this.isThreadsAlive();
//        if (!threadsStatus.alive) {
//            log.warn("The command to turn off the switch is blocked because {}", threadsStatus.msg);
//            return;
//        }
//        if (this.firstSideSignalHandler.getStateHolder().isBlocked() || this.secondSideSignalHandler.getStateHolder().isBlocked()) {
//            log.warn("Turning off blocked because very often crossings detected");
//            return;
//        }
//
//        log.warn("Find fault");
//        this.needToAct = true;
//        this.actionManager.act();

    }

//    @Override
//    public synchronized void act() {
//        if (this.counter++ == 0) {
//            synchronized (this.locker) {
//                this.locker.notifyAll();
//            }
//        }
//        else counter = 0;
//    }

//    private Pair isThreadsAlive() {
//        StringBuilder msg = new StringBuilder();
//        if (!this.svReceiver.isFirstSvAlive()) msg.append("First SV is dead. ");
//        if (!this.svReceiver.isSecondSvAlive()) msg.append("Second SV is dead");
////        System.err.println("1 is empty? = " + msg.toString().isEmpty() + "; msg = " + msg.toString() + "; msg capacity: " + msg.capacity());
//        if (!msg.toString().isEmpty()) return new Pair(false, msg.toString());
//
//        long currentTime = System.currentTimeMillis();
//        if (currentTime - this.svReceiver.getFirstThreadDataContainer().getLastUpdateTime() >= setpoint) msg.append("First side SV thread is possible dead. ");
//        if (currentTime - this.svReceiver.getSecondThreadDataContainer().getLastUpdateTime() >= setpoint) msg.append("Second side SV thread is possible dead, ");
////        System.err.println("2 is empty? = " + msg.toString().isEmpty() + "; msg = " + msg.toString());
//        return new Pair(msg.toString().isEmpty(), msg.toString());
//    }

//    @Override
//    public void process() {

//    }

//    private record Pair(boolean alive, String msg){}
}
