package ru.mpei.relayprotection.model.protection.signalHandling.blockers;

import ru.mpei.relayprotection.model.enumerations.CrossingType;

public class OftenCrossingBlocker {
    private long prevCrossTime;
    private final long timeSetpoint;

    public OftenCrossingBlocker(double frequency) {
        this.prevCrossTime = System.currentTimeMillis();
        this.timeSetpoint = (long) (350 / frequency); // вычисляем 70 процентов от половины периода. Если пересечения будут чаще => надо болкироваться
    }

    public boolean checkBlocking(CrossingType crossType) {
        boolean status = System.currentTimeMillis() - this.prevCrossTime < this.timeSetpoint;
        this.prevCrossTime = System.currentTimeMillis();
        return status;
    }
}
