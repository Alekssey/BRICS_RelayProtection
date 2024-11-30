package ru.mpei.relayprotection.model.sv.dataContainers;

import lombok.Getter;
import ru.mpei.relayprotection.model.sv.model.ValueHolder;

@Getter
public class SvThreadInstDataContainer {
    private final ValueHolder<Double> instIa = new ValueHolder<>();
    private final ValueHolder<Double> instIb = new ValueHolder<>();
    private final ValueHolder<Double> instIc = new ValueHolder<>();
    private final Object locker = new Object();
    private long lastUpdateTime = System.currentTimeMillis();

    public void setData(double ia, double ib, double ic) {
        this.instIa.set(ia);
        this.instIb.set(ib);
        this.instIc.set(ic);
        this.lastUpdateTime = System.currentTimeMillis();
        synchronized (this.locker) {
            this.locker.notifyAll();
        }
    }
}
