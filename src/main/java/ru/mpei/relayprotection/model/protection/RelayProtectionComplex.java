package ru.mpei.relayprotection.model.protection;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.mpei.relayprotection.model.configuration.CfgRoot;
import ru.mpei.relayprotection.model.protection.signalHandling.SignalHandler;
import ru.mpei.relayprotection.model.protection.signalHandling.chronometric.ChronometricHandler;
import ru.mpei.relayprotection.model.protection.signalHandling.differential.DifferentialSignalHandler;
import ru.mpei.relayprotection.model.sv.SvReceiveRunner;
import ru.mpei.relayprotection.utils.WorkWithCfg;

import java.util.ArrayList;
import java.util.List;

@Component
public class RelayProtectionComplex {
    @Value("${protections.cfg-file.path}")
    private String cfgPath;
    private List<LineProtection> protections = new ArrayList<>();

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
                    new ProtectionStair(cfgEl.getFirstStair().getAvailableCurrentLevels()),
                    new ProtectionStair(cfgEl.getSecondStair().getAvailableCurrentLevels()),
                    thread1,
                    thread2);

            SignalHandler stair1side1phaseA = new ChronometricHandler(thread1.getIa());
            SignalHandler stair1side1phaseB = new ChronometricHandler(thread1.getIb());
            SignalHandler stair1side1phaseC = new ChronometricHandler(thread1.getIc());

            SignalHandler stair1side2phaseA = new ChronometricHandler(thread2.getIa());
            SignalHandler stair1side2phaseB = new ChronometricHandler(thread2.getIb());
            SignalHandler stair1side2phaseC = new ChronometricHandler(thread2.getIc());

            SignalHandler stair2side1phaseA = new DifferentialSignalHandler(thread1.getIa());
            SignalHandler stair2side1phaseB = new DifferentialSignalHandler(thread1.getIb());
            SignalHandler stair2side1phaseC = new DifferentialSignalHandler(thread1.getIc());

            SignalHandler stair2side2phaseA = new DifferentialSignalHandler(thread2.getIa());
            SignalHandler stair2side2phaseB = new DifferentialSignalHandler(thread2.getIb());
            SignalHandler stair2side2phaseC = new DifferentialSignalHandler(thread2.getIc());

            thread1.getIa().addHandler(stair1side1phaseA);
            thread1.getIb().addHandler(stair1side1phaseB);
            thread1.getIc().addHandler(stair1side1phaseC);
            thread1.getIa().addHandler(stair2side1phaseA);
            thread1.getIb().addHandler(stair2side1phaseB);
            thread1.getIc().addHandler(stair2side1phaseC);

            thread2.getIa().addHandler(stair1side2phaseA);
            thread2.getIb().addHandler(stair1side2phaseB);
            thread2.getIc().addHandler(stair1side2phaseC);
            thread2.getIa().addHandler(stair2side2phaseA);
            thread2.getIb().addHandler(stair2side2phaseB);
            thread2.getIc().addHandler(stair2side2phaseC);

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

//            thread1.subscribe(protection.getFirstStair());
//            thread1.subscribe(protection.getSecondStair());
//            thread2.subscribe(protection.getFirstStair());
//            thread2.subscribe(protection.getSecondStair());

//            System.err.println(protection.getLineName());
//            System.err.println(protection.getFirstStair());
//            System.err.println(protection.getSecondStair());
//            System.err.println(protection.getFirstSvThread().getSvMsgParameters().getMacDst());
//            System.err.println(protection.getSecondSvThread().getSvMsgParameters().getMacDst());
        });
    }

}
