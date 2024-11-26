package ru.mpei.relayprotection.model.sv;

import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.pcap4j.core.*;
import ru.mpei.relayprotection.model.buffer.CommonBuffer;

import java.util.Optional;

@Slf4j
@Getter
public class SvReceiver {
    private final NetworkSettings netCfg;
    private final SvReceiverSettings receiverSettings;
    private final SvThreadInstDataContainer firstThreadDataContainer = new SvThreadInstDataContainer();
    private final SvThreadInstDataContainer secondThreadDataContainer = new SvThreadInstDataContainer();
    private final CommonBuffer buffer;
    private PcapHandle handle;
    private final PacketListener packetListener;
    private boolean isFirstSvAlive = true; // ToDo: add daemon scheduled task witch will monitor is threads alive
    private boolean isSecondSvAlive = true; // ToDo: add daemon scheduled task witch will monitor is threads alive

    // ToDo: decrease number of input parameters by placing ready internal objects
    public SvReceiver(String iFaceDesc, String mac1, String mac2, boolean isDebugOn, long svLostPeriod) {
        this.netCfg = new NetworkSettings(iFaceDesc, mac1, mac2);
        this.receiverSettings = new SvReceiverSettings(false, isDebugOn, svLostPeriod);
        this.buffer = new CommonBuffer(mac1, mac2);
        this.packetListener = this.createPacketListener();
        this.start();
    }

    public void setAnalyzeActivityStatus(boolean newStatus) {
        this.receiverSettings.setAnalyzeEnabled(newStatus);
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
                System.out.println(macDst + "; " + ia + "; " + ib + "; " + ic);
                this.firstThreadDataContainer.setData(ia, ib, ic);
            } else {
                System.err.println(macDst + "; " + ia + "; " + ib + "; " + ic);
                this.secondThreadDataContainer.setData(ia, ib, ic);
            }
            if (this.receiverSettings.isDebugEnabled()) this.buffer.set(macDst, ia, ib, ic);
        };
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
