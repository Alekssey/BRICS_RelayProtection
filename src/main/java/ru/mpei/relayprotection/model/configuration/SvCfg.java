package ru.mpei.relayprotection.model.configuration;

import lombok.Getter;
import ru.mpei.model.SvAnalyzerData;
import ru.mpei.model.SvMsgParameters;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
@Getter
public class SvCfg {
    @XmlElement
    private String ifaceDescription;
    @XmlElement
    private String firstSideMacDst;
    @XmlElement
    private String secondSideMacDst;
    @XmlElement
    private long lostPeriod;
    @XmlElement
    private boolean enableDebug;

    @Override
    public String toString() {
        return "SvCfg{" +
                "ifaceDescription='" + ifaceDescription + '\'' +
                ", firstSideMacDst='" + firstSideMacDst + '\'' +
                ", secondSideMacDst='" + secondSideMacDst + '\'' +
                ", lostPeriod=" + lostPeriod +
                '}';
    }
}
