package ru.mpei.relayprotection.model.protection.signalHandling.chronometric;

import ru.mpei.relayprotection.model.protection.signalHandling.blockers.BlockingStatus;
import ru.mpei.relayprotection.model.protection.signalHandling.blockers.OftenCrossingBlocker;
import ru.mpei.relayprotection.model.protection.signalHandling.filters.signal.FrequencyFilter;
import ru.mpei.relayprotection.model.protection.signalHandling.filters.signal.MockFilter;
import ru.mpei.relayprotection.model.protection.signalHandling.SignalHandler;
import ru.mpei.relayprotection.model.sv.ValueHolder;

public class ChronometricHandler extends SignalHandler {
    private final ZeroCrossingDetector zeroCrossingDetector = new ZeroCrossingDetector();
    private final FrequencyFilter filter = new MockFilter();
    private final OftenCrossingBlocker blocker = new OftenCrossingBlocker();
    private ChronometricPhaseAnalyzer phaseAnalyzer;

    public ChronometricHandler(ValueHolder value) {
        super(value);
    }

//    public ChronometricHandler(ValueHolder valHolder) {
//        this.value = valHolder;
//    }


    @Override
    public void handle() {
        double cleanValue = this.filter.filter(this.value.get());
        ZeroCrossDto dto = this.zeroCrossingDetector.checkCross(cleanValue);
        BlockingStatus blockStatus = this.blocker.checkBlocking(dto.getType());
//        this.phaseAnalyzer.
    }
}
