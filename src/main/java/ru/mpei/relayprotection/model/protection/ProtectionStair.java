package ru.mpei.relayprotection.model.protection;

import lombok.Data;
import ru.mpei.relayprotection.model.enumerations.CurrentLevel;
import ru.mpei.relayprotection.model.protection.phaseHandling.PhaseAnalyzer;
import ru.mpei.relayprotection.model.protection.signalHandling.StairActionManager;
import ru.mpei.relayprotection.model.protection.phaseHandling.ChronometricPhaseAnalyzer;

import java.util.List;

@Data
public class ProtectionStair {
    private List<CurrentLevel> availableCurrentLevels;
    private LineSide firstSide = new LineSide();
    private LineSide SecondSide = new LineSide();
    private PhaseAnalyzer aPhaseAnalyzer;
    private PhaseAnalyzer bPhaseAnalyzer;
    private PhaseAnalyzer cPhaseAnalyzer;
    private StairActionManager actionManager = new StairActionManager();

    public ProtectionStair(List<CurrentLevel> availableCurrentLevels) {
        this.availableCurrentLevels = availableCurrentLevels;
    }

    @Override
    public String toString() {
        return "ProtectionStair{" +
                "availableCurrentLevels=" + availableCurrentLevels +
                ", firstSide=" + firstSide +
                ", SecondSide=" + SecondSide +
                '}';
    }
}
