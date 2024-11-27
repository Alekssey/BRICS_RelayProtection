package ru.mpei.relayprotection.model.protection.signalHandling.chronometric;

import lombok.Getter;
import ru.mpei.relayprotection.model.enumerations.CrossingType;
import ru.mpei.relayprotection.model.protection.signalHandling.blockers.OftenCrossingBlocker;
import ru.mpei.relayprotection.model.protection.signalHandling.chronometric.crossingDetector.SimpleZeroCrossingDetector;
import ru.mpei.relayprotection.model.protection.signalHandling.chronometric.signalState.SignalStateHolderChronometric;
import ru.mpei.relayprotection.model.protection.signalHandling.filters.signal.FrequencyFilter;
import ru.mpei.relayprotection.model.protection.signalHandling.filters.signal.MockFilter;
import ru.mpei.relayprotection.model.protection.signalHandling.SignalHandler;
import ru.mpei.relayprotection.model.sv.ValueHolder;

public class ChronometricSignalHandler extends SignalHandler {
    private final FrequencyFilter filter;// = new MockFilter();
    private final SimpleZeroCrossingDetector zeroCrossingDetector;// = new SimpleZeroCrossingDetector();
    private final OftenCrossingBlocker blocker;
    @Getter
    private final SignalStateHolderChronometric stateHolder;// = new SignalStateHolderChronometric();


    public ChronometricSignalHandler(ValueHolder<Double> value, double frequency) {
        super(value);
        this.filter = new MockFilter();
        this.zeroCrossingDetector = new SimpleZeroCrossingDetector();
        this.blocker = new OftenCrossingBlocker(frequency);
        this.stateHolder = new SignalStateHolderChronometric();
    }

    @Override
    public synchronized void handle() {
        double cleanValue = this.filter.filter(this.value.get());
        CrossingType crossing = this.zeroCrossingDetector.checkCross(cleanValue);
        if (crossing != CrossingType.NO_CROSSING) {
//            System.out.println(crossing);
            this.stateHolder.activate(
                    crossing,
                    this.blocker.checkBlocking(crossing));
        }
    }
}
