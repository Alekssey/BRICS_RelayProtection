package ru.mpei.relayprotection.model.protection.signalHandling.chronometric.crossingDetector;

import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.mpei.relayprotection.model.enumerations.CrossingType;
import ru.mpei.relayprotection.model.protection.signalHandling.chronometric.dto.ZeroCrossDto;


@NoArgsConstructor
public class SimpleZeroCrossingDetector{
    private double prevValue;

    public CrossingType checkCross(double val) {
        ZeroCrossDto ans;
        if (this.prevValue * val > 0) {
              return CrossingType.NO_CROSSING;
        }
        return this.prevValue > 0 ? CrossingType.DOWN : CrossingType.UP;
    }

}
