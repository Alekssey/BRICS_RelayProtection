package ru.mpei.relayprotection.model.protection.signalHandling.chronometric;

import lombok.Getter;
import ru.mpei.relayprotection.model.enumerations.CrossingType;
import ru.mpei.relayprotection.model.protection.signalHandling.blockers.OftenCrossingBlocker;
import ru.mpei.relayprotection.model.protection.signalHandling.chronometric.crossingDetector.SimpleZeroCrossingDetector;
import ru.mpei.relayprotection.model.protection.signalHandling.chronometric.signalState.SignalStateHolderChronometric;
import ru.mpei.relayprotection.model.protection.signalHandling.filters.signal.FrequencyFilter;
import ru.mpei.relayprotection.model.protection.signalHandling.filters.signal.MockFilter;
import ru.mpei.relayprotection.model.protection.signalHandling.SignalHandler;
import ru.mpei.relayprotection.model.sv.model.ValueHolder;

public class ChronometricSignalHandler extends SignalHandler {
    private final FrequencyFilter filter;
    private final SimpleZeroCrossingDetector zeroCrossingDetector;
    private final OftenCrossingBlocker blocker;
    @Getter
    private final SignalStateHolderChronometric stateHolder;


    public ChronometricSignalHandler(ValueHolder<Double> value, double frequency) {
        super(value);
        this.filter = new MockFilter();
        this.zeroCrossingDetector = new SimpleZeroCrossingDetector();
        this.blocker = new OftenCrossingBlocker(frequency);
        this.stateHolder = new SignalStateHolderChronometric();
    }

    @Override
    public void handle() {
        double cleanValue = this.filter.filter(this.value.get());
        CrossingType crossing = this.zeroCrossingDetector.checkCross(cleanValue);
        if (crossing != CrossingType.NO_CROSSING) {
            this.stateHolder.activate(
                    crossing,
                    this.blocker.checkBlocking(crossing));
        }
    }

    @Override
    public void actualize() {
        this.stateHolder.deactivate();
        this.zeroCrossingDetector.setPrevValue(this.value.get() == null ? 0 : this.value.get());
    }
}
