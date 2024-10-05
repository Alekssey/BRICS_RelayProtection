package ru.mpei.relayprotection.model.protection;

import lombok.Data;
import ru.mpei.relayprotection.model.protection.signalHandling.SignalHandler;

@Data
public class LineSide {
    private SignalHandler aPhaseHandler;
    private SignalHandler bPhaseHandler;
    private SignalHandler cPhaseHandler;
}
