package ru.mpei.relayprotection.model.protection.signalHandling.chronometric.signalState;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.mpei.relayprotection.model.enumerations.CrossingType;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignalStateHolderChronometric {
    private CrossingType crossing;
    private long crossingTime;
    private boolean blocked;

}
