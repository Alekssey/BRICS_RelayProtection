package ru.mpei.relayprotection.model.sv;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;

import java.util.concurrent.atomic.AtomicInteger;

public class ValueHolder {
    private double value;
    @Getter
    @JsonIgnore
    private final AtomicInteger locker = new AtomicInteger();

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
