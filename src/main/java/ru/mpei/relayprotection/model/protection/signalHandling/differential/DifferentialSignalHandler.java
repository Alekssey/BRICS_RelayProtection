package ru.mpei.relayprotection.model.protection.signalHandling.differential;

import lombok.Getter;
import ru.mpei.relayprotection.model.protection.phaseHandling.PhaseAnalyzer;
import ru.mpei.relayprotection.model.protection.signalHandling.SignalHandler;
import ru.mpei.relayprotection.model.protection.signalHandling.filters.fourier.Fourier;
import ru.mpei.relayprotection.model.protection.signalHandling.filters.fourier.internalClasses.dataObjects.Vector;
import ru.mpei.relayprotection.model.sv.ValueHolder;

public class DifferentialSignalHandler extends SignalHandler {
    private final Fourier filter;
    @Getter
    private final Vector instMag = new Vector();

    public DifferentialSignalHandler(ValueHolder value, PhaseAnalyzer phaseAnalyzer, int bufferSize, double frequency) {
        super(value, phaseAnalyzer);
        this.filter = new Fourier(bufferSize, frequency);
    }

    @Override
    public void handle() {
        this.filter.process(this.value, this.instMag);
        this.phaseAnalyzer.act();
    }
}
