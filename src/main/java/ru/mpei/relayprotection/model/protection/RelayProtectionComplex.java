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
import ru.mpei.relayprotection.model.protection.phaseHandling.ChronometricPhaseAnalyzer;
import ru.mpei.relayprotection.model.protection.phaseHandling.DifferentialPhaseAnalyzer;
import ru.mpei.relayprotection.model.protection.phaseHandling.PhaseAnalyzer;
import ru.mpei.relayprotection.model.protection.signalHandling.SignalHandler;
import ru.mpei.relayprotection.model.protection.signalHandling.chronometric.ChronometricHandler;
import ru.mpei.relayprotection.model.protection.signalHandling.differential.DifferentialSignalHandler;
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

        // ToDo: добавить проверку наличия ступени в конфигурации и только тогда создавать элементы для нее
        cfg.getLinesProtections().forEach(cfgEl -> {
            SvReceiveRunner thread1 = new SvReceiveRunner(cfgEl.getFirstSvThread().getCfgData(), cfgEl.getFirstSvThread().getMetadata());
            SvReceiveRunner thread2 = new SvReceiveRunner(cfgEl.getSecondSvThread().getCfgData(), cfgEl.getSecondSvThread().getMetadata());

            LineProtection protection = new LineProtection(
                    cfgEl.getLineName(),
                    new ProtectionStair(cfgEl.getFirstStair().getAvailableCurrentLevels()),
                    new ProtectionStair(cfgEl.getSecondStair().getAvailableCurrentLevels()),
                    thread1,
                    thread2);

            PhaseAnalyzer stair1APhaseAnalyzer = new ChronometricPhaseAnalyzer(cfgEl.getFirstStair().getSetpoint(), protection.getFirstStair().getActionManager());
            PhaseAnalyzer stair1BPhaseAnalyzer = new ChronometricPhaseAnalyzer(cfgEl.getFirstStair().getSetpoint(), protection.getFirstStair().getActionManager());
            PhaseAnalyzer stair1CPhaseAnalyzer = new ChronometricPhaseAnalyzer(cfgEl.getFirstStair().getSetpoint(), protection.getFirstStair().getActionManager());

            PhaseAnalyzer stair2APhaseAnalyzer = new DifferentialPhaseAnalyzer(cfgEl.getSecondStair().getSetpoint(), protection.getSecondStair().getActionManager());
            PhaseAnalyzer stair2BPhaseAnalyzer = new DifferentialPhaseAnalyzer(cfgEl.getSecondStair().getSetpoint(), protection.getSecondStair().getActionManager());
            PhaseAnalyzer stair2CPhaseAnalyzer = new DifferentialPhaseAnalyzer(cfgEl.getSecondStair().getSetpoint(), protection.getSecondStair().getActionManager());

            SignalHandler stair1side1phaseA = new ChronometricHandler(thread1.getIa(), stair1APhaseAnalyzer, cfg.getFrequency());
            SignalHandler stair1side1phaseB = new ChronometricHandler(thread1.getIb(), stair1BPhaseAnalyzer, cfg.getFrequency());
            SignalHandler stair1side1phaseC = new ChronometricHandler(thread1.getIc(), stair1CPhaseAnalyzer, cfg.getFrequency());

            SignalHandler stair1side2phaseA = new ChronometricHandler(thread2.getIa(), stair1APhaseAnalyzer, cfg.getFrequency());
            SignalHandler stair1side2phaseB = new ChronometricHandler(thread2.getIb(), stair1BPhaseAnalyzer, cfg.getFrequency());
            SignalHandler stair1side2phaseC = new ChronometricHandler(thread2.getIc(), stair1CPhaseAnalyzer, cfg.getFrequency());

            SignalHandler stair2side1phaseA = new DifferentialSignalHandler(thread1.getIa(), stair2APhaseAnalyzer);
            SignalHandler stair2side1phaseB = new DifferentialSignalHandler(thread1.getIb(), stair2BPhaseAnalyzer);
            SignalHandler stair2side1phaseC = new DifferentialSignalHandler(thread1.getIc(), stair2CPhaseAnalyzer);

            SignalHandler stair2side2phaseA = new DifferentialSignalHandler(thread2.getIa(), stair2APhaseAnalyzer);
            SignalHandler stair2side2phaseB = new DifferentialSignalHandler(thread2.getIb(), stair2BPhaseAnalyzer);
            SignalHandler stair2side2phaseC = new DifferentialSignalHandler(thread2.getIc(), stair2CPhaseAnalyzer);

            protection.getFirstStair().getFirstSide().setAPhaseHandler(stair1side1phaseA);
            protection.getFirstStair().getFirstSide().setBPhaseHandler(stair1side1phaseB);
            protection.getFirstStair().getFirstSide().setCPhaseHandler(stair1side1phaseC);

            protection.getFirstStair().getSecondSide().setAPhaseHandler(stair1side2phaseA);
            protection.getFirstStair().getSecondSide().setBPhaseHandler(stair1side2phaseB);
            protection.getFirstStair().getSecondSide().setCPhaseHandler(stair1side2phaseC);

            protection.getSecondStair().getFirstSide().setAPhaseHandler(stair2side1phaseA);
            protection.getSecondStair().getFirstSide().setBPhaseHandler(stair2side1phaseB);
            protection.getSecondStair().getFirstSide().setCPhaseHandler(stair2side1phaseC);

            protection.getSecondStair().getSecondSide().setAPhaseHandler(stair2side2phaseA);
            protection.getSecondStair().getSecondSide().setBPhaseHandler(stair2side2phaseB);
            protection.getSecondStair().getSecondSide().setCPhaseHandler(stair2side2phaseC);

            protection.getFirstStair().setAPhaseAnalyzer(stair1APhaseAnalyzer);
            protection.getFirstStair().setBPhaseAnalyzer(stair1BPhaseAnalyzer);
            protection.getFirstStair().setCPhaseAnalyzer(stair1CPhaseAnalyzer);

            protection.getSecondStair().setAPhaseAnalyzer(stair2APhaseAnalyzer);
            protection.getSecondStair().setBPhaseAnalyzer(stair2BPhaseAnalyzer);
            protection.getSecondStair().setCPhaseAnalyzer(stair2CPhaseAnalyzer);

            this.protections.add(protection);
        });
    }

}
