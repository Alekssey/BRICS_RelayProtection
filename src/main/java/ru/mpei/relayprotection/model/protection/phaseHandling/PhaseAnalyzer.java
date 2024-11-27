package ru.mpei.relayprotection.model.protection.phaseHandling;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import ru.mpei.relayprotection.model.LogicalNode;
import ru.mpei.relayprotection.model.sv.model.ValueHolder;

@Slf4j
public abstract class PhaseAnalyzer implements LogicalNode {
    protected double setpoint;
    @Getter
    protected ValueHolder<Boolean> needToAct = new ValueHolder<>();

    public PhaseAnalyzer(double setpoint) {
        this.setpoint = setpoint;
    }

}
