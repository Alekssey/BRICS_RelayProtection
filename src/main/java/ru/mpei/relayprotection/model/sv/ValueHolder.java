package ru.mpei.relayprotection.model.sv;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;

public class ValueHolder {
    private double value;
    @Getter
    @JsonIgnore
    private final Object locker = new Object();

    public double get() {
        return value;
    }

    public void set(double val) {
        this.value = val;
        synchronized (this.locker) {
            this.locker.notifyAll();
        }
    }

}
