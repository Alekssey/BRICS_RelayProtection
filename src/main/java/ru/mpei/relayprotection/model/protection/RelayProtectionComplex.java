package ru.mpei.relayprotection.model.protection;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.mpei.relayprotection.model.buffer.CommonBuffer;
import ru.mpei.relayprotection.model.configuration.CfgRoot;
import ru.mpei.relayprotection.model.configuration.LineProtectionCfg;
import ru.mpei.relayprotection.model.protection.phaseHandling.ChronometricPhaseAnalyzer;
import ru.mpei.relayprotection.model.protection.phaseHandling.DifferentialPhaseAnalyzer;
import ru.mpei.relayprotection.model.protection.signalHandling.StairActionManager;
import ru.mpei.relayprotection.model.protection.signalHandling.chronometric.ChronometricSignalHandler;
import ru.mpei.relayprotection.model.protection.signalHandling.differential.DifferentialSignalHandler;
import ru.mpei.relayprotection.model.sv.SvReceiveRunner;
import ru.mpei.relayprotection.model.sv.SvReceiver;
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
    @Getter
    private final List<LineProtection> protections = new ArrayList<>();

    @PostConstruct
    private void configure() {
        CfgRoot cfg = WorkWithCfg.unMarshalAny(CfgRoot.class, this.cfgPath);
        if (cfg == null) {
            throw new RuntimeException("Bad configuration");
        }

        cfg.getLinesProtections().forEach(cfgEl -> {

            SvReceiver svReceiver = new SvReceiver(
                    cfgEl.getSvData().getIfaceDescription(),
                    cfgEl.getSvData().getFirstSideMacDst(),
                    cfgEl.getSvData().getSecondSideMacDst(),
                    cfgEl.getSvData().isEnableDebug(),
                    cfgEl.getSvData().getLostPeriod());
            LineProtection protection = new LineProtection(cfgEl.getLineName(), svReceiver, svReceiver.getBuffer());

            if (cfgEl.getFirstStair() != null) this.configureFirstStair(protection, svReceiver, cfgEl);
            if (cfgEl.getSecondStair() != null) this.configureSecondStair(protection, svReceiver, cfgEl);

            this.protections.add(protection);
        });

        log.error("look");
    }

    private void configureFirstStair(LineProtection protection, SvReceiver svReceiver, LineProtectionCfg cfg) {
        ProtectionStair stair = new ProtectionStair(cfg.getFirstStair().getAvailableCurrentLevels(), new StairActionManager(gateway, cfg.getCmdName()));

        stair.setAPhaseAnalyzer(new ChronometricPhaseAnalyzer(cfg.getFirstStair().getSetpoint(), stair.getActionManager(), svReceiver));
        stair.setBPhaseAnalyzer(new ChronometricPhaseAnalyzer(cfg.getFirstStair().getSetpoint(), stair.getActionManager(), svReceiver));
        stair.setCPhaseAnalyzer(new ChronometricPhaseAnalyzer(cfg.getFirstStair().getSetpoint(), stair.getActionManager(), svReceiver));

        stair.getFirstSide().setAPhaseHandler(new ChronometricSignalHandler(svReceiver.getFirstThreadDataContainer().getInstIa(), stair.getAPhaseAnalyzer(), svReceiver.getFirstThreadDataContainer().getLocker(), cfg.getFrequency()));
        stair.getFirstSide().setBPhaseHandler(new ChronometricSignalHandler(svReceiver.getFirstThreadDataContainer().getInstIb(), stair.getBPhaseAnalyzer(), svReceiver.getFirstThreadDataContainer().getLocker(), cfg.getFrequency()));
        stair.getFirstSide().setCPhaseHandler(new ChronometricSignalHandler(svReceiver.getFirstThreadDataContainer().getInstIb(), stair.getCPhaseAnalyzer(), svReceiver.getFirstThreadDataContainer().getLocker(), cfg.getFrequency()));

        stair.getSecondSide().setAPhaseHandler(new ChronometricSignalHandler(svReceiver.getSecondThreadDataContainer().getInstIa(), stair.getAPhaseAnalyzer(), svReceiver.getSecondThreadDataContainer().getLocker(), cfg.getFrequency()));
        stair.getSecondSide().setBPhaseHandler(new ChronometricSignalHandler(svReceiver.getSecondThreadDataContainer().getInstIb(), stair.getBPhaseAnalyzer(), svReceiver.getSecondThreadDataContainer().getLocker(), cfg.getFrequency()));
        stair.getSecondSide().setCPhaseHandler(new ChronometricSignalHandler(svReceiver.getSecondThreadDataContainer().getInstIc(), stair.getCPhaseAnalyzer(), svReceiver.getSecondThreadDataContainer().getLocker(), cfg.getFrequency()));

        ((ChronometricPhaseAnalyzer) stair.getAPhaseAnalyzer()).setFirstSideSignalHandler((ChronometricSignalHandler) stair.getFirstSide().getAPhaseHandler());
        ((ChronometricPhaseAnalyzer) stair.getAPhaseAnalyzer()).setSecondSideSignalHandler((ChronometricSignalHandler) stair.getSecondSide().getAPhaseHandler());

        ((ChronometricPhaseAnalyzer) stair.getBPhaseAnalyzer()).setFirstSideSignalHandler((ChronometricSignalHandler) stair.getFirstSide().getBPhaseHandler());
        ((ChronometricPhaseAnalyzer) stair.getBPhaseAnalyzer()).setSecondSideSignalHandler((ChronometricSignalHandler) stair.getSecondSide().getBPhaseHandler());

        ((ChronometricPhaseAnalyzer) stair.getCPhaseAnalyzer()).setFirstSideSignalHandler((ChronometricSignalHandler) stair.getFirstSide().getCPhaseHandler());
        ((ChronometricPhaseAnalyzer) stair.getCPhaseAnalyzer()).setSecondSideSignalHandler((ChronometricSignalHandler) stair.getSecondSide().getCPhaseHandler());

        stair.getActionManager().setPhaseA(stair.getAPhaseAnalyzer());
        stair.getActionManager().setPhaseB(stair.getBPhaseAnalyzer());
        stair.getActionManager().setPhaseC(stair.getCPhaseAnalyzer());
        stair.getActionManager().setProtection(protection);

        protection.setFirstStair(stair);
    }

    private void configureSecondStair(LineProtection protection, SvReceiver svReceiver, LineProtectionCfg cfg) {
        ProtectionStair stair = new ProtectionStair(cfg.getSecondStair().getAvailableCurrentLevels(), new StairActionManager(gateway, cfg.getCmdName()));

//        stair.setAPhaseAnalyzer(new DifferentialPhaseAnalyzer(cfg.getSecondStair().getSetpoint(), stair.getActionManager()));
//        stair.setBPhaseAnalyzer(new DifferentialPhaseAnalyzer(cfg.getSecondStair().getSetpoint(), stair.getActionManager()));
//        stair.setCPhaseAnalyzer(new DifferentialPhaseAnalyzer(cfg.getSecondStair().getSetpoint(), stair.getActionManager()));

//        stair.getFirstSide().setAPhaseHandler(new DifferentialSignalHandler(thread1.getIa(), stair.getAPhaseAnalyzer(), 80, cfg.getFrequency()));
//        stair.getFirstSide().setBPhaseHandler(new DifferentialSignalHandler(thread1.getIa(), stair.getBPhaseAnalyzer(), 80, cfg.getFrequency()));
//        stair.getFirstSide().setCPhaseHandler(new DifferentialSignalHandler(thread1.getIc(), stair.getCPhaseAnalyzer(), 80, cfg.getFrequency()));
//
//        stair.getSecondSide().setAPhaseHandler(new DifferentialSignalHandler(thread2.getIa(), stair.getAPhaseAnalyzer(), 80, cfg.getFrequency()));
//        stair.getSecondSide().setBPhaseHandler(new DifferentialSignalHandler(thread2.getIa(), stair.getBPhaseAnalyzer(), 80, cfg.getFrequency()));
//        stair.getSecondSide().setCPhaseHandler(new DifferentialSignalHandler(thread2.getIc(), stair.getCPhaseAnalyzer(), 80, cfg.getFrequency()));
//
//        stair.getAPhaseAnalyzer().setFirstSideSvThread(thread1);
//        stair.getAPhaseAnalyzer().setSecondSideSvThread(thread2);
//        ((DifferentialPhaseAnalyzer) stair.getAPhaseAnalyzer()).setFirstSideInstMag(((DifferentialSignalHandler) stair.getFirstSide().getAPhaseHandler()).getInstMag());
//        ((DifferentialPhaseAnalyzer) stair.getAPhaseAnalyzer()).setSecondSideInstMag(((DifferentialSignalHandler) stair.getSecondSide().getAPhaseHandler()).getInstMag());
//
//        stair.getBPhaseAnalyzer().setFirstSideSvThread(thread1);
//        stair.getBPhaseAnalyzer().setSecondSideSvThread(thread2);
//        ((DifferentialPhaseAnalyzer) stair.getBPhaseAnalyzer()).setFirstSideInstMag(((DifferentialSignalHandler) stair.getFirstSide().getBPhaseHandler()).getInstMag());
//        ((DifferentialPhaseAnalyzer) stair.getBPhaseAnalyzer()).setSecondSideInstMag(((DifferentialSignalHandler) stair.getSecondSide().getBPhaseHandler()).getInstMag());
//
//        stair.getCPhaseAnalyzer().setFirstSideSvThread(thread1);
//        stair.getCPhaseAnalyzer().setSecondSideSvThread(thread2);
//        ((DifferentialPhaseAnalyzer) stair.getCPhaseAnalyzer()).setFirstSideInstMag(((DifferentialSignalHandler) stair.getFirstSide().getCPhaseHandler()).getInstMag());
//        ((DifferentialPhaseAnalyzer) stair.getCPhaseAnalyzer()).setSecondSideInstMag(((DifferentialSignalHandler) stair.getSecondSide().getCPhaseHandler()).getInstMag());
//
//        stair.getActionManager().setTag(cfg.getCmdName());
//        stair.getActionManager().setGateway(this.gateway);
//        stair.getActionManager().setPhaseA(stair.getAPhaseAnalyzer());
//        stair.getActionManager().setPhaseB(stair.getBPhaseAnalyzer());
//        stair.getActionManager().setPhaseC(stair.getCPhaseAnalyzer());
//
//        stair.getActionManager().setProtection(protection);
//
//        protection.setSecondStair(stair);
    }

}
