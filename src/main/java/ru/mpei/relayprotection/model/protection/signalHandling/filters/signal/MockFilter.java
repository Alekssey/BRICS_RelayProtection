package ru.mpei.relayprotection.model.protection.signalHandling.filters.signal;

public class MockFilter extends FrequencyFilter{
    @Override
    public double filter(double rawSignal) {
        return rawSignal;
    }
}
