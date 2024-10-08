package ru.mpei.relayprotection.model.protection.signalHandling.differential;

import ru.mpei.relayprotection.model.protection.phaseHandling.PhaseAnalyzer;
import ru.mpei.relayprotection.model.protection.signalHandling.SignalHandler;
import ru.mpei.relayprotection.model.sv.ValueHolder;

public class DifferentialSignalHandler extends SignalHandler {
    public DifferentialSignalHandler(ValueHolder value, PhaseAnalyzer phaseAnalyzer) {
        super(value, phaseAnalyzer);
    }

    @Override
    public void handle() {

    }
}
