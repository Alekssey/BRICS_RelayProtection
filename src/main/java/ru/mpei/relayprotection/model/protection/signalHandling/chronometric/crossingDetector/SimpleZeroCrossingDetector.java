package ru.mpei.relayprotection.model.protection.signalHandling.chronometric.crossingDetector;

import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.mpei.relayprotection.model.enumerations.CrossingType;
import ru.mpei.relayprotection.model.protection.signalHandling.chronometric.dto.ZeroCrossDto;


@NoArgsConstructor
public class SimpleZeroCrossingDetector{
    @Setter
    private double prevValue;

    public CrossingType checkCross(double val) {
//        System.out.println("pv: " + prevValue + "; nv: " + val);
        if (prevValue == 0.0 || this.prevValue * val > 0) {
            this.prevValue = val;
            return CrossingType.NO_CROSSING;
        }
        CrossingType response = this.prevValue > 0 ? CrossingType.DOWN : CrossingType.UP;
        this.prevValue = val;
        return response;
    }

}
