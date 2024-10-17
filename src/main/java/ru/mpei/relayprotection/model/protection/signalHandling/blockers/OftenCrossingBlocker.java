package ru.mpei.relayprotection.model.protection.signalHandling.blockers;

import ru.mpei.relayprotection.model.enumerations.CrossingType;

public class OftenCrossingBlocker {
    private long prevCrossTime;
    private boolean status = false;
    private final long timeSetpoint;

    public OftenCrossingBlocker(double frequency) {
        this.prevCrossTime = System.currentTimeMillis();
        this.timeSetpoint = (long) (0.35 * 1000 / frequency);
    }

    public boolean checkBlocking(CrossingType crossType) {
        if (crossType != CrossingType.NO_CROSSING) {
            this.status = System.currentTimeMillis() - this.prevCrossTime < this.timeSetpoint;
            this.prevCrossTime = System.currentTimeMillis();
        }
        return this.status;
    }
}
