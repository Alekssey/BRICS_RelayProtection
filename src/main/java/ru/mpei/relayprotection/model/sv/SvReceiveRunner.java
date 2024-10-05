package ru.mpei.relayprotection.model.sv;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import ru.mpei.model.SvAnalyzerData;
import ru.mpei.model.SvMsgParameters;
import ru.mpei.network.protocols.sv.receiving.SvReceiver;
import ru.mpei.relayprotection.model.protection.ProtectionStair;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Class represents SV receiver, which implements logic of receiving SV
 */
@Slf4j
@Getter
public class SvReceiveRunner {
    private final ScheduledExecutorService connectionChecker = Executors.newSingleThreadScheduledExecutor();
    private final SvAnalyzerData analyzerData;
    private final SvMsgParameters svMsgParameters;
    private double lastSvStateUpdateTs = 0;
    private boolean SvConnection = false;
//    private final List<ProtectionStair> subscribers = new ArrayList<>();

    private final ValueHolder ia = new ValueHolder();
    private final ValueHolder ib = new ValueHolder();
    private final ValueHolder ic = new ValueHolder();

//    public SvReceiveRunner configure(SvMsgParameters svMsgParameters, SvAnalyzerData cfg) {
//        this.svMsgParameters = svMsgParameters;
//        this.analyzerData = cfg;
//        return this;
//    }

    public SvReceiveRunner(SvMsgParameters svMsgParameters, SvAnalyzerData cfg) {
        this.svMsgParameters = svMsgParameters;
        this.analyzerData = cfg;
    }

    public void runSvReceive() {
        startConnectionChecking();
        startReceivingMeasurements();
    }

    private void startConnectionChecking() {
        connectionChecker.scheduleAtFixedRate(() -> {
            if (System.currentTimeMillis() - lastSvStateUpdateTs > analyzerData.getSvLostPeriod()) {
                log.error("Sv Data to {} is not actual. last packet was received  {} ms ago.", svMsgParameters.getMacDst(), System.currentTimeMillis() - lastSvStateUpdateTs);
                SvConnection = false;
            } else SvConnection = true;
        }, 0, analyzerData.getSvLostPeriod(), TimeUnit.MILLISECONDS);
    }

    private void startReceivingMeasurements() {

        AtomicInteger prevSmp = new AtomicInteger(-1);

        SvReceiver svReceiver = new SvReceiver();
        svReceiver.setIface(svMsgParameters.getIfaceId());
        svReceiver.setMac(svMsgParameters.getMacDst());

        svReceiver.setListener(packet -> {
            if (packet.getSmpCnt() == prevSmp.get()) return;
            prevSmp.set(packet.getSmpCnt());
            lastSvStateUpdateTs = System.currentTimeMillis();
            this.ia.set(packet.getIa().getInstMag().getValue() / 10_000);
            this.ib.set(packet.getIb().getInstMag().getValue() / 10_000);
            this.ic.set(packet.getIc().getInstMag().getValue() / 10_000);

//            this.notifyAllSubscribers();
        });
        try {
            svReceiver.start();
        } catch (Exception e) {
            throw new RuntimeException("Can not start process SV receiving");
        }
    }

//    public void subscribe(ProtectionStair subscriber) {
//        this.subscribers.add(subscriber);
//    }
//
//    private void notifyAllSubscribers() {
//        this.subscribers.forEach(ProtectionStair::action);
//    }
}
