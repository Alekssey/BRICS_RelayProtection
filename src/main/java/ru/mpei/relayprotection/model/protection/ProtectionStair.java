package ru.mpei.relayprotection.model.protection;

import lombok.Data;
import ru.mpei.relayprotection.model.LogicalNode;
import ru.mpei.relayprotection.model.enumerations.CurrentLevel;
import ru.mpei.relayprotection.model.protection.phaseHandling.PhaseAnalyzer;
import ru.mpei.relayprotection.model.protection.signalHandling.StairActionManager;

import java.util.List;

@Data
public class ProtectionStair implements LogicalNode {
    private List<CurrentLevel> availableCurrentLevels;
    private LineSide firstSide = new LineSide();
    private LineSide secondSide = new LineSide();
    private PhaseAnalyzer aPhaseAnalyzer;
    private PhaseAnalyzer bPhaseAnalyzer;
    private PhaseAnalyzer cPhaseAnalyzer;
    private StairActionManager actionManager;

    public ProtectionStair(List<CurrentLevel> availableCurrentLevels) {
        this.availableCurrentLevels = availableCurrentLevels;
    }

    @Override
    public void process() {
        this.firstSide.process();
        this.secondSide.process();
        this.aPhaseAnalyzer.process();
        this.bPhaseAnalyzer.process();
        this.cPhaseAnalyzer.process();
        this.actionManager.process();
    }

    @Override
    public void actualize() {
        this.firstSide.actualize();
        this.secondSide.actualize();
        this.aPhaseAnalyzer.actualize();
        this.bPhaseAnalyzer.actualize();
        this.cPhaseAnalyzer.actualize();

        this.actionManager.process();
    }

    @Override
    public String toString() {
        return "ProtectionStair{" +
                "availableCurrentLevels=" + availableCurrentLevels +
                ", firstSide=" + firstSide +
                ", SecondSide=" + secondSide +
                '}';
    }
}
