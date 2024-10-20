package ru.mpei.relayprotection.model.configuration;

import lombok.Getter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@Getter
@XmlRootElement(name = "protections")
@XmlAccessorType(XmlAccessType.FIELD)
public class CfgRoot {
    @XmlElement(name = "lineProtection")
    private List<LineProtectionCfg> linesProtections = new ArrayList<>();
}
