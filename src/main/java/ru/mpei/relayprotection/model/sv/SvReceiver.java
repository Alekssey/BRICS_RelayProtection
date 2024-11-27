package ru.mpei.relayprotection.model.sv;

import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.pcap4j.core.*;
import ru.mpei.relayprotection.model.sv.dataContainers.CommonBuffer;
import ru.mpei.relayprotection.model.protection.ProtectionStair;
import ru.mpei.relayprotection.model.sv.dataContainers.DataContainer;
import ru.mpei.relayprotection.model.sv.model.SvThreadLifeCycle;
import ru.mpei.relayprotection.model.sv.settings.NetworkSettings;
import ru.mpei.relayprotection.model.sv.settings.SvReceiverSettings;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@Slf4j
@Getter
public class SvReceiver {
    private final NetworkSettings netCfg;
    private final SvReceiverSettings receiverSettings;

    private final DataContainer dataContainer;

    private PcapHandle handle;
    private final PacketListener packetListener;

    private final ScheduledFuture<?> selfDiagnosis;

    private final SvThreadLifeCycle firstThreadLifecycle = new SvThreadLifeCycle();
    private final SvThreadLifeCycle secondThreadLifecycle = new SvThreadLifeCycle();

    private final List<ProtectionStair> stairs = new ArrayList<>();

    public SvReceiver(String iFaceDesc, String mac1, String mac2, boolean isDebugOn, long svLostPeriod) {
        this.dataContainer = new DataContainer(new CommonBuffer(mac1, mac2));
        this.netCfg = new NetworkSettings(iFaceDesc, mac1, mac2);
        this.receiverSettings = new SvReceiverSettings(false, isDebugOn, svLostPeriod);
        this.packetListener = this.createPacketListener();
        this.selfDiagnosis = this.configureSelfDiagnosisTask();
        this.start();
    }

    public void subscribeOnPackets(ProtectionStair newSubscriber) {
        this.stairs.add(newSubscriber);
    }

    public void setAnalyzeActivityStatus(boolean newStatus) {
        this.receiverSettings.setAnalyzeEnabled(newStatus);
        if (newStatus) this.stairs.forEach(ProtectionStair::actualize);
    }

    @SneakyThrows
    public void start() {
        if (this.handle == null) {
            Optional<PcapNetworkInterface> nic = getNetworkInterface();
            if (nic.isEmpty()) return;
            this.handle = nic.get().openLive(65536, PcapNetworkInterface.PromiscuousMode.PROMISCUOUS, 10);
            this.handle.setFilter(this.netCfg.getFilterExpression(), BpfProgram.BpfCompileMode.OPTIMIZE);
            Thread captureThread = new Thread(() -> {
                try {
                    log.info("Starting packet capture");
                    this.handle.loop(0, packetListener);
                } catch (PcapNativeException | InterruptedException | NotOpenException e) {
                    throw new RuntimeException("Can not start SV receiving loop");
                }
                log.info("Packet capturing end");
            });
            captureThread.start();
        } else {
            log.error("SvReceiver already started");
        }
    }

    @SneakyThrows
    private Optional<PcapNetworkInterface> getNetworkInterface() {
        Optional<PcapNetworkInterface> nic = Pcaps.findAllDevs().stream().filter(card -> this.netCfg.getIFace().equals(card.getDescription())).findFirst();
        if(nic.isPresent()) {
            return nic;
        } else {
            log.error("Network interface {} not found. Available interfaces:", this.netCfg.getIFace());
            Pcaps.findAllDevs().forEach(i -> log.error(i.toString()));
            return Optional.empty();
        }
    }
    private PacketListener createPacketListener() {
        return packet -> {
            if (!this.receiverSettings.isAnalyzeEnabled()) return;
            byte[] rawData = packet.getRawData();
            String macDst = this.extractMac(rawData, 0);
            int ia = this.extractValue(rawData, 63);
            int ib = this.extractValue(rawData, 71);
            int ic = this.extractValue(rawData, 79);
//            System.out.println(macDst + "; " + ia + "; " + ib + "; " + ic);

            if (macDst.equals(this.netCfg.getMac1())) {
                this.dataContainer.getFirstThreadDataContainer().setData(ia/10.0, ib/10.0, ic/10.0);
                firstThreadLifecycle.set();
            } else {
                this.dataContainer.getSecondThreadDataContainer().setData(ia/10.0, ib/10.0, ic/10.0);
                secondThreadLifecycle.set();
            }
            if (this.receiverSettings.isDebugEnabled()) this.dataContainer.getBuffer().set(macDst, ia, ib, ic);
            if (firstThreadLifecycle.isHasNewPackets() && secondThreadLifecycle.isHasNewPackets()) {
                if (this.firstThreadLifecycle.getPacketsCounter() == 1 && this.secondThreadLifecycle.getPacketsCounter() == 1) {
//                    System.out.println("work");
                    this.stairs.forEach(ProtectionStair::process);
                } else {
//                    System.out.println("actualize");
                    this.stairs.forEach(ProtectionStair::actualize);
                }
                this.firstThreadLifecycle.reset();
                this.secondThreadLifecycle.reset();
            }
        };
    }

    private ScheduledFuture<?> configureSelfDiagnosisTask() {
        ScheduledExecutorService ses = Executors.newSingleThreadScheduledExecutor();
        return ses.scheduleWithFixedDelay(() -> {
                if (!this.receiverSettings.isAnalyzeEnabled()) return;
                long now = System.currentTimeMillis();
                if (now - this.dataContainer.getFirstThreadDataContainer().getLastUpdateTime() > this.receiverSettings.getSvLostPeriod()) {
                    log.warn("SV data for mac {} is not actual", this.netCfg.getMac1());
                    this.firstThreadLifecycle.setThreadAlive(false);
                } else {
                    this.firstThreadLifecycle.setThreadAlive(true);
                }
                if (now - this.dataContainer.getSecondThreadDataContainer().getLastUpdateTime() > this.receiverSettings.getSvLostPeriod()) {
                    log.warn("SV data for mac {} is not actual", this.netCfg.getMac2());
                    this.secondThreadLifecycle.setThreadAlive(false);
                } else {
                    this.secondThreadLifecycle.setThreadAlive(true);
                }
            }, 0, 1000, TimeUnit.MILLISECONDS);
    }

    private String extractMac(byte[] buffer, int offset) {
        return String.format("%02x:%02x:%02x:%02x:%02x:%02x",
                buffer[offset],
                buffer[offset + 1],
                buffer[offset + 2],
                buffer[offset + 3],
                buffer[offset + 4],
                buffer[offset + 5]);
    }

    private int extractValue (byte[] buffer, int offset) {
        return buffer[offset + 3] & 0xFF | (buffer[offset + 2] & 0xFF) << 8 | (buffer[offset + 1] & 0xFF) << 16 | (buffer[offset] & 0xFF) << 24;
    }

}
