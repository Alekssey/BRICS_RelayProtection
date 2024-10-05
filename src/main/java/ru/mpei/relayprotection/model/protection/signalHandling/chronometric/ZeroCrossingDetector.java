package ru.mpei.relayprotection.model.protection.signalHandling.chronometric;

import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.mpei.relayprotection.model.enumerations.CrossingType;


@NoArgsConstructor
public class ZeroCrossingDetector {
    private double prevValue;
    @Setter
    private double period = 0.25;

    public ZeroCrossingDetector(double period) {
        this.period = period;
    }

    public ZeroCrossDto checkCross(double val) {
        ZeroCrossDto ans;
        if (prevValue * val > 0) {
              return new ZeroCrossDto(CrossingType.NO_CROSSING, 0);
        }
        double timeAfterCross = period + prevValue * period / (val - prevValue);
        return prevValue > 0 ? new ZeroCrossDto(CrossingType.DOWN, timeAfterCross) : new ZeroCrossDto(CrossingType.UP, period - timeAfterCross);
    }

}
