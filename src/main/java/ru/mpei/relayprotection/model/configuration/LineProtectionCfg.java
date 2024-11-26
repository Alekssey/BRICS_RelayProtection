package ru.mpei.relayprotection.model.configuration;

import lombok.Getter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
@Getter
public class LineProtectionCfg {
    @XmlElement
    private String lineName;
    @XmlElement
    private double frequency;
    @XmlElement
    private String cmdName;
    @XmlElement
    private ProtectionStairCfg firstStair;
    @XmlElement
    private ProtectionStairCfg secondStair;
    @XmlElement(name = "SvData")
    private SvCfg svData;


//    @Override
//    public String toString() {
//        return "LineProtectionCfg{" +
//                "lineName='" + lineName + '\'' +
//                ", firstStair=" + firstStair +
//                ", secondStair=" + secondStair +
//                ", firstSvThread=" + firstSvThread +
//                ", secondSvThread=" + secondSvThread +
//                '}';
//    }
}
