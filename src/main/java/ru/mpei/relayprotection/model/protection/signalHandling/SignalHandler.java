package ru.mpei.relayprotection.model.protection.signalHandling;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import ru.mpei.relayprotection.model.sv.model.ValueHolder;

@Data
@Slf4j
public abstract class SignalHandler {
    protected ValueHolder<Double> value;

    public SignalHandler(ValueHolder<Double> value) {
        this.value = value;
    }

    public abstract void handle();
    public abstract void actualize();
}
