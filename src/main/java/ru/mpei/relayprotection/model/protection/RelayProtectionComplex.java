package ru.mpei.relayprotection.model.protection;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.mpei.relayprotection.model.configuration.CfgRoot;
import ru.mpei.relayprotection.model.configuration.LineProtectionCfg;
import ru.mpei.relayprotection.model.protection.phaseHandling.ChronometricPhaseAnalyzer;
import ru.mpei.relayprotection.model.protection.signalHandling.chronometric.ChronometricHandler;
import ru.mpei.relayprotection.model.sv.SvReceiveRunner;
import ru.mpei.relayprotection.service.GateWayService;
import ru.mpei.relayprotection.utils.WorkWithCfg;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class RelayProtectionComplex {
    @Value("${protections.cfg-file.path}")
    private String cfgPath;
    @Autowired
    private GateWayService gateway;
    @Autowired
    private ObjectMapper mapper;
    @Getter
    private final List<LineProtection> protections = new ArrayList<>();

    @PostConstruct
    private void configure() {
        CfgRoot cfg = WorkWithCfg.unMarshalAny(CfgRoot.class, this.cfgPath);
        if (cfg == null) {
            throw new RuntimeException("Bad configuration");
        }

        cfg.getLinesProtections().forEach(cfgEl -> {
            SvReceiveRunner thread1 = new SvReceiveRunner(cfgEl.getFirstSvThread().getCfgData(), cfgEl.getFirstSvThread().getMetadata());
            SvReceiveRunner thread2 = new SvReceiveRunner(cfgEl.getSecondSvThread().getCfgData(), cfgEl.getSecondSvThread().getMetadata());

            LineProtection protection = new LineProtection(
                    cfgEl.getLineName(),
                    thread1,
                    thread2);

            if (cfgEl.getFirstStair() != null) this.configureFirstStair(protection, thread1, thread2, cfgEl);
            if (cfgEl.getSecondStair() != null) this.configureSecondStair(protection, thread1, thread2, cfgEl);

            this.protections.add(protection);
        });

        log.error("look");
    }

    private void configureFirstStair(LineProtection protection, SvReceiveRunner thread1, SvReceiveRunner thread2, LineProtectionCfg cfg) {
        ProtectionStair stair = new ProtectionStair(cfg.getFirstStair().getAvailableCurrentLevels());

        stair.setAPhaseAnalyzer(new ChronometricPhaseAnalyzer(cfg.getFirstStair().getSetpoint(), stair.getActionManager()));
        stair.setBPhaseAnalyzer(new ChronometricPhaseAnalyzer(cfg.getFirstStair().getSetpoint(), stair.getActionManager()));
        stair.setCPhaseAnalyzer(new ChronometricPhaseAnalyzer(cfg.getFirstStair().getSetpoint(), stair.getActionManager()));

        stair.getFirstSide().setAPhaseHandler(new ChronometricHandler(thread1.getIa(), stair.getAPhaseAnalyzer(), cfg.getFrequency()));
        stair.getFirstSide().setBPhaseHandler(new ChronometricHandler(thread1.getIa(), stair.getBPhaseAnalyzer(), cfg.getFrequency()));
        stair.getFirstSide().setCPhaseHandler(new ChronometricHandler(thread1.getIc(), stair.getCPhaseAnalyzer(), cfg.getFrequency()));

        stair.getSecondSide().setAPhaseHandler(new ChronometricHandler(thread2.getIa(), stair.getAPhaseAnalyzer(), cfg.getFrequency()));
        stair.getSecondSide().setBPhaseHandler(new ChronometricHandler(thread2.getIa(), stair.getBPhaseAnalyzer(), cfg.getFrequency()));
        stair.getSecondSide().setCPhaseHandler(new ChronometricHandler(thread2.getIc(), stair.getCPhaseAnalyzer(), cfg.getFrequency()));

        ((ChronometricPhaseAnalyzer) stair.getAPhaseAnalyzer()).setFirstSideSvThread(thread1);
        ((ChronometricPhaseAnalyzer) stair.getAPhaseAnalyzer()).setSecondSideSvThread(thread2);
        ((ChronometricPhaseAnalyzer) stair.getAPhaseAnalyzer()).setFirstSideSignalHandler((ChronometricHandler) stair.getFirstSide().getAPhaseHandler());
        ((ChronometricPhaseAnalyzer) stair.getAPhaseAnalyzer()).setSecondSideSignalHandler((ChronometricHandler) stair.getSecondSide().getAPhaseHandler());

        ((ChronometricPhaseAnalyzer) stair.getBPhaseAnalyzer()).setFirstSideSvThread(thread1);
        ((ChronometricPhaseAnalyzer) stair.getBPhaseAnalyzer()).setSecondSideSvThread(thread2);
        ((ChronometricPhaseAnalyzer) stair.getBPhaseAnalyzer()).setFirstSideSignalHandler((ChronometricHandler) stair.getFirstSide().getBPhaseHandler());
        ((ChronometricPhaseAnalyzer) stair.getBPhaseAnalyzer()).setSecondSideSignalHandler((ChronometricHandler) stair.getSecondSide().getBPhaseHandler());

        ((ChronometricPhaseAnalyzer) stair.getCPhaseAnalyzer()).setFirstSideSvThread(thread1);
        ((ChronometricPhaseAnalyzer) stair.getCPhaseAnalyzer()).setSecondSideSvThread(thread2);
        ((ChronometricPhaseAnalyzer) stair.getCPhaseAnalyzer()).setFirstSideSignalHandler((ChronometricHandler) stair.getFirstSide().getCPhaseHandler());
        ((ChronometricPhaseAnalyzer) stair.getCPhaseAnalyzer()).setSecondSideSignalHandler((ChronometricHandler) stair.getSecondSide().getCPhaseHandler());

        stair.getActionManager().setTag(cfg.getCmdName());
        stair.getActionManager().setGateway(this.gateway);
        stair.getActionManager().setPhaseA(stair.getAPhaseAnalyzer());
        stair.getActionManager().setPhaseB(stair.getBPhaseAnalyzer());
        stair.getActionManager().setPhaseC(stair.getCPhaseAnalyzer());

        protection.setFirstStair(stair);
    }
    private void configureSecondStair(LineProtection protection, SvReceiveRunner thread1, SvReceiveRunner thread2, LineProtectionCfg cfg) {
//        ProtectionStair stair = new ProtectionStair(cfg.getFirstStair().getAvailableCurrentLevels());
//        ToDo: add configuration
//        protection.setFirstStair(stair);
    }

}
