package ru.mpei.relayprotection.model.configuration;

import lombok.Getter;
import ru.mpei.model.SvAnalyzerData;
import ru.mpei.model.SvMsgParameters;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
@Getter
public class SvThreadCfg {
    @XmlElement
    private SvAnalyzerData metadata;
    @XmlElement
    private SvMsgParameters cfgData;

    @Override
    public String toString() {
        return "SvThreadCfg{" +
                "metadata=" + metadata +
                ", cfgData=" + cfgData +
                '}';
    }
}
