package ru.mpei.relayprotection.model.protection;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.mpei.relayprotection.model.buffer.MyBuffer;
import ru.mpei.relayprotection.model.configuration.CfgRoot;
import ru.mpei.relayprotection.model.sv.SvReceiveRunner;
import ru.mpei.relayprotection.utils.WorkWithCfg;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class RelayProtectionComplex {
    @Value("${protections.cfg-file.path}")
    private String cfgPath;
    @Getter
    private final List<LineProtection> protections = new ArrayList<>();

    @PostConstruct
    private void configure() {
        CfgRoot cfg = WorkWithCfg.unMarshalAny(CfgRoot.class, this.cfgPath);
        if (cfg == null) {
            throw new RuntimeException("Bad configuration");
        }

        cfg.getLinesProtections().forEach(cfgEl -> {
            MyBuffer buffer = new MyBuffer(cfgEl.getFirstSvThread().getCfgData().getMacDst(), cfgEl.getSecondSvThread().getCfgData().getMacDst());
            SvReceiveRunner thread1 = new SvReceiveRunner(cfgEl.getFirstSvThread().getCfgData(), cfgEl.getFirstSvThread().getMetadata(), buffer);
            SvReceiveRunner thread2 = new SvReceiveRunner(cfgEl.getSecondSvThread().getCfgData(), cfgEl.getSecondSvThread().getMetadata(), buffer);
            LineProtection protection = new LineProtection(
                    cfgEl.getLineName(),
                    thread1,
                    thread2,
                    buffer);

            this.protections.add(protection);
        });

        log.error("look");
    }



}
