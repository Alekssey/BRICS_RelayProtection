package ru.mpei.relayprotection.model.protection.signalHandling.blockers;

import ru.mpei.relayprotection.model.enumerations.CrossingType;

public class OftenCrossingBlocker {
    private long prevCrossTime;

    public OftenCrossingBlocker() {
        this.prevCrossTime = System.currentTimeMillis();
    }

    public BlockingStatus checkBlocking(CrossingType crossType) {
        if (crossType != CrossingType.NO_CROSSING) {
            this.prevCrossTime = System.currentTimeMillis();
            return System.currentTimeMillis() - this.prevCrossTime < 7 ? BlockingStatus.BLOCK : BlockingStatus.UNBLOCKING;
        }
        return BlockingStatus.UNBLOCKING;
    }
}
