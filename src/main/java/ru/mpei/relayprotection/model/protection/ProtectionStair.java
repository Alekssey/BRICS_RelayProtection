package ru.mpei.relayprotection.model.protection;

import lombok.Data;
import ru.mpei.relayprotection.model.enumerations.CurrentLevel;

import java.util.ArrayList;
import java.util.List;

@Data
public class ProtectionStair {
    private List<CurrentLevel> availableCurrentLevels;
    private LineSide firstSide = new LineSide();
    private LineSide SecondSide = new LineSide();

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
