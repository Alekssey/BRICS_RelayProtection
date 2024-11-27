package ru.mpei.relayprotection.model.sv.model;

public class ValueHolder <T> {
    private T value;
    public T get() {
        return value;
    }

    public void set(T val) {
        this.value = val;
    }

}
