package ru.mpei.relayprotection.model.configuration;

import lombok.Getter;
import ru.mpei.relayprotection.model.enumerations.CurrentLevel;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@Getter
public class ProtectionStairCfg {
    @XmlElement(name = "currentLevel")
    private List<CurrentLevel> availableCurrentLevels;
    @XmlElement
    private double setpoint;

    @Override
    public String toString() {
        return "ProtectionStairCfg{" +
                "availableCurrentLevels=" + availableCurrentLevels +
                '}';
    }
}
