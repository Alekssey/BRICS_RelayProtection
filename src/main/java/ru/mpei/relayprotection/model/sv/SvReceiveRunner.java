package ru.mpei.relayprotection.model.sv;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import ru.mpei.model.SvAnalyzerData;
import ru.mpei.model.SvMsgParameters;
import ru.mpei.network.protocols.sv.receiving.SvReceiver;
import ru.mpei.relayprotection.model.buffer.MyBuffer;

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
    private long lastSvStateUpdateTs = System.currentTimeMillis();
    private boolean alive = false;

    private final ValueHolder ia = new ValueHolder();
    private final ValueHolder ib = new ValueHolder();
    private final ValueHolder ic = new ValueHolder();

    private final MyBuffer buffer;

    private final int maxSize = 12_000;
    private int index = 0;
    private final double[] mas_ia = new double[maxSize];
    private final double[] mas_ib = new double[maxSize];
    private final double[] mas_ic = new double[maxSize];

    @Setter
    private boolean isInWork = false;

    public SvReceiveRunner(SvMsgParameters svMsgParameters, SvAnalyzerData cfg, MyBuffer buffer) {
        this.buffer = buffer;
        this.svMsgParameters = svMsgParameters;
        this.analyzerData = cfg;
        this.runSvReceive();
    }

    public void runSvReceive() {
        startConnectionChecking();
        startReceivingMeasurements();
    }

    private void startConnectionChecking() {
        connectionChecker.scheduleAtFixedRate(() -> {
            if (System.currentTimeMillis() - lastSvStateUpdateTs > analyzerData.getSvLostPeriod()) {
                log.error("Sv Data to {} is not actual. last packet was received  {} ms ago.", svMsgParameters.getMacDst(), System.currentTimeMillis() - lastSvStateUpdateTs);
                alive = false;
            } else alive = true;
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
//            System.err.println(this.svMsgParameters.getMacDst() + " receive measurements:"
//                    + " ia: " + packet.getIa().getInstMag().getValue() / 10_000
//                    + "; ib: " + packet.getIb().getInstMag().getValue() / 10_000
//                    + "; ic: " + packet.getIc().getInstMag().getValue() / 10_000);
//            System.err.println(this.svMsgParameters.getMacDst() + " : " + packet);

            if (!this.isInWork) return;

            double ia = packet.getIa().getInstMag().getValue() / 10_000;
            double ib = packet.getIb().getInstMag().getValue() / 10_000;
            double ic = packet.getIc().getInstMag().getValue() / 10_000;

            this.ia.set(ia);
            this.ib.set(ib);
            this.ic.set(ic);

//            mas_ia[index] = ia;
//            mas_ib[index] = ib;
//            mas_ic[index] = ic;
//            index = index == maxSize - 1 ? 0 : index + 1;
            this.buffer.set(this.svMsgParameters.getMacDst(), ia, ib, ic);


        });
        try {
            svReceiver.start();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Can not start process SV receiving");
        }
    }

    public SvResponse getMeasurementsForPeriod(int periodMillis) {
        int necessaryNumberOfPoint = 80 * periodMillis / 20;
        int localIndex = index;
        return new SvResponse(
                svMsgParameters.getMacDst(),
                takeNecessaryPartOfArray(necessaryNumberOfPoint, localIndex, mas_ia),
                takeNecessaryPartOfArray(necessaryNumberOfPoint, localIndex, mas_ib),
                takeNecessaryPartOfArray(necessaryNumberOfPoint, localIndex, mas_ic)
        );
    }

    private double[] takeNecessaryPartOfArray(int necessarySize, int indexToStart, double[] sourceMas) {
        double[] outBuffer = new double[necessarySize];
        if (necessarySize <= indexToStart) {
            System.arraycopy(sourceMas, indexToStart - necessarySize, outBuffer, 0, necessarySize);
            return outBuffer;
        }
        System.arraycopy(sourceMas, 0, outBuffer, necessarySize - indexToStart, indexToStart);
        System.arraycopy(sourceMas, maxSize - necessarySize + indexToStart, outBuffer, 0, necessarySize - indexToStart);
        return outBuffer;
    }

}
