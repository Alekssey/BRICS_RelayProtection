package ru.mpei.relayprotection.model.protection.signalHandling.chronometric.signalState;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.mpei.relayprotection.model.enumerations.CrossingType;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class SignalStateHolderChronometric {
    private CrossingType crossing;
    private long crossingTime;
    private boolean blocked;
    private boolean haveNeeCrossPoint;

    public void activate(CrossingType cross, boolean block) {
        this.crossing = cross;
        this.crossingTime = System.currentTimeMillis();
        this.blocked = block;
        this.haveNeeCrossPoint = true;
    }

    public void deactivate() {
        this.haveNeeCrossPoint = false;
    }

}
