package ru.mpei.relayprotection.model.protection.phaseHandling;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import ru.mpei.relayprotection.model.protection.signalHandling.StairActionManager;
import ru.mpei.relayprotection.model.protection.signalHandling.filters.fourier.internalClasses.dataObjects.Vector;
import ru.mpei.relayprotection.model.sv.SvReceiver;

@Slf4j
public class DifferentialPhaseAnalyzer extends PhaseAnalyzer{
    @Setter
    private Vector firstSideInstMag;
    @Setter
    private Vector secondSideInstMag;

    public DifferentialPhaseAnalyzer(double setpoint, StairActionManager actionManager, SvReceiver svReceiver) {
        super(setpoint);
    }

    @Override
    public void process() {
//        if (Math.abs(this.firstSideInstMag.getMag() - this.secondSideInstMag.getMag()) < this.setpoint) return;
//
//        Pair threadsStatus = this.isThreadsAlive();
//        if (!threadsStatus.alive) {
//            log.warn("The command to turn off the switch is blocked because {}", threadsStatus.msg);
//            return;
//        }
//
//        this.needToAct = true;
//        this.actionManager.act();
    }

    @Override
    public void actualize() {

    }

//    @Override
//    public void act() {
//        if (this.counter == 1) {
//            this.counter = 0;
//            synchronized (this.locker) {
//                this.locker.notifyAll();
//            }
//        }
//        else this.counter++;
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
//
//    private record Pair(boolean alive, String msg){}
}
