package ru.mpei.relayprotection.model.protection;

import lombok.Data;
import ru.mpei.relayprotection.model.LogicalNode;
import ru.mpei.relayprotection.model.protection.signalHandling.SignalHandler;

@Data
public class LineSide implements LogicalNode {
    private SignalHandler aPhaseHandler;
    private SignalHandler bPhaseHandler;
    private SignalHandler cPhaseHandler;

    @Override
    public void process() {
        this.aPhaseHandler.handle();
        this.bPhaseHandler.handle();
        this.cPhaseHandler.handle();
    }

    @Override
    public void actualize() {
        this.aPhaseHandler.actualize();
        this.bPhaseHandler.actualize();
        this.cPhaseHandler.actualize();
    }
}
