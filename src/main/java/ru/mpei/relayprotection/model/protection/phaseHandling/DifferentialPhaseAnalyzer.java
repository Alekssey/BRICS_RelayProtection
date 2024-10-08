package ru.mpei.relayprotection.model.protection.phaseHandling;

import ru.mpei.relayprotection.model.protection.signalHandling.StairActionManager;

public class DifferentialPhaseAnalyzer extends PhaseAnalyzer{
    public DifferentialPhaseAnalyzer(double setpoint, StairActionManager actionManager) {
        super(setpoint, actionManager);
    }

    @Override
    public void analyze() {

    }

    @Override
    public void act() {

    }
}
