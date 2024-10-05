package ru.mpei.relayprotection.model.sv;

import lombok.Data;
import ru.mpei.relayprotection.model.protection.signalHandling.PhaseAnalyzer;
import ru.mpei.relayprotection.model.protection.signalHandling.SignalHandler;

import java.util.ArrayList;
import java.util.List;

public class ValueHolder {
    private double value;

    private final List<SignalHandler> handlers = new ArrayList<>();

    public double get() {
        return value;
    }

    public void set(double val) {
        this.value = val;
    }

    public void addHandler(SignalHandler handler) {
        this.handlers.add(handler);
    }
}
