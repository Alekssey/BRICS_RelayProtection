package ru.mpei.relayprotection.model.protection.phaseHandling;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import ru.mpei.relayprotection.model.protection.signalHandling.StairActionManager;
import ru.mpei.relayprotection.model.protection.signalHandling.filters.fourier.internalClasses.dataObjects.Vector;

@Slf4j
public class DifferentialPhaseAnalyzer extends PhaseAnalyzer{
    @Setter
    private Vector firstSideInstMag;
    @Setter
    private Vector secondSideInstMag;

    public DifferentialPhaseAnalyzer(double setpoint, StairActionManager actionManager) {
        super(setpoint, actionManager);
    }

    @Override
    public void analyze() {
        if (Math.abs(this.firstSideInstMag.getMag() - this.secondSideInstMag.getMag()) < this.setpoint) return;

        Pair threadsStatus = this.isThreadsAlive();
        if (!threadsStatus.alive) {
            log.warn("The command to turn off the switch is blocked because {}", threadsStatus.msg);
            return;
        }

        this.needToAct = true;
        this.actionManager.act();
    }

    @Override
    public void act() {
        if (this.counter == 1) {
            this.counter = 0;
            synchronized (this.locker) {
                this.locker.notifyAll();
            }
        }
        else this.counter++;
    }

    private Pair isThreadsAlive() {
        long currentTime = System.currentTimeMillis();
        StringBuilder msg = new StringBuilder();

        if (!this.firstSideSvThread.isAlive()) msg.append("First side SV thread is dead. ");
        if (!this.secondSideSvThread.isAlive()) msg.append("Second side SV thread is dead. ");
        if (!msg.toString().isEmpty()) return new Pair(false, msg.toString());

        if (currentTime - this.firstSideSvThread.getLastSvStateUpdateTs() > 1) msg.append("First side SV thread is possible dead. ");
        if (currentTime - this.secondSideSvThread.getLastSvStateUpdateTs() > 1) msg.append("Second side SV thread is possible dead, ");
        return new Pair(msg.toString().isEmpty(), msg.toString());
    }

    private record Pair(boolean alive, String msg){}
}
