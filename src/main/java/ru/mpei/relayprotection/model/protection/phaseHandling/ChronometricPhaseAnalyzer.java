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
    }

    @Override
    public void actualize() {
        this.needToAct.set(false);
    }

}
