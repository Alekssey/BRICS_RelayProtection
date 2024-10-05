package ru.mpei.relayprotection.model.protection.signalHandling;

import lombok.Data;
import ru.mpei.relayprotection.model.sv.ValueHolder;

@Data
public abstract class SignalHandler {
    protected ValueHolder value;

    public SignalHandler(ValueHolder value) {
        this.value = value;
    }

    public abstract void handle();
}
