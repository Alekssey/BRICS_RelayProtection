package ru.mpei.relayprotection.model.protection.signalHandling.chronometric;

import ru.mpei.relayprotection.model.enumerations.CrossingType;
import ru.mpei.relayprotection.model.protection.phaseHandling.PhaseAnalyzer;
import ru.mpei.relayprotection.model.protection.signalHandling.blockers.OftenCrossingBlocker;
import ru.mpei.relayprotection.model.protection.signalHandling.chronometric.crossingDetector.SimpleZeroCrossingDetector;
import ru.mpei.relayprotection.model.protection.signalHandling.chronometric.signalState.SignalStateHolderChronometric;
import ru.mpei.relayprotection.model.protection.signalHandling.filters.signal.FrequencyFilter;
import ru.mpei.relayprotection.model.protection.signalHandling.filters.signal.MockFilter;
import ru.mpei.relayprotection.model.protection.signalHandling.SignalHandler;
import ru.mpei.relayprotection.model.sv.ValueHolder;

public class ChronometricHandler extends SignalHandler {
    private final SimpleZeroCrossingDetector zeroCrossingDetector = new SimpleZeroCrossingDetector();
    private final FrequencyFilter filter = new MockFilter();
    private final OftenCrossingBlocker blocker;
    private final SignalStateHolderChronometric stateHolder = new SignalStateHolderChronometric();


    public ChronometricHandler(ValueHolder value, PhaseAnalyzer phaseAnalyzer, double frequency) {
        super(value, phaseAnalyzer);
        this.blocker = new OftenCrossingBlocker(frequency);
    }

    @Override
    public void handle() {
        double cleanValue = this.filter.filter(this.value.get());
        CrossingType crossing = this.zeroCrossingDetector.checkCross(cleanValue);
        this.stateHolder.setCrossing(crossing);
        this.stateHolder.setBlocked(this.blocker.checkBlocking(crossing));
        this.phaseAnalyzer.act();
    }
}
