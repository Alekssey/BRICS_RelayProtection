package ru.mpei.relayprotection.service;

import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.pcap4j.core.*;

import java.util.Optional;

@Slf4j
@Data
public class SvReceiver {
    private String iFace;
    private String mac1 = "01:0C:CD:04:00:01";
    private String mac2 = "01:0C:CD:04:00:02";

    private PcapHandle handle;
    private final PacketListener defailPacketListener = packet -> {

        byte[] rawData = packet.getRawData();
        String macDst = this.extractMac(rawData, 0);
        int ia = this.extractValue(rawData, 63);
        int ib = this.extractValue(rawData, 71);
        int ic = this.extractValue(rawData, 79);
        System.out.println(macDst + "; " + ia + "; " + ib + "; " + ic);
    };

    public SvReceiver(String iFace) {
        this.iFace = iFace;
    }

    static {
        try {
            Pcaps.findAllDevs().stream().forEach(nic -> log.info(nic.toString()));
        } catch (PcapNativeException e) {
            throw new RuntimeException(e);
        }
    }


    @SneakyThrows
    public void start() {
        if (handle == null) {
            initializeNetworkInterface();
            String filter = "ether proto 0x88ba  && (ether dst " + mac1 + " || ether dst " + mac2 + ")";
            handle.setFilter(filter, BpfProgram.BpfCompileMode.OPTIMIZE);
            Thread captureThread = new Thread(() -> {
                try {
                    log.info("Starting packet capture");
                    handle.loop(0, defailPacketListener);
                } catch (PcapNativeException e) {
                    throw new RuntimeException(e);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } catch (NotOpenException e) {
                    throw new RuntimeException(e);
                }
                log.info("Packet capturing end");
            });
            captureThread.start();
        }
    }

    @SneakyThrows
    private void initializeNetworkInterface() {
        Optional<PcapNetworkInterface> nic = Pcaps.findAllDevs().stream().filter(card -> iFace.equals(card.getDescription())).findFirst();
        if(nic.isPresent()) {
            this.handle = nic.get().openLive(65536, PcapNetworkInterface.PromiscuousMode.PROMISCUOUS, 10);
            log.info("Network handler created: {}", nic.get());
        } else {
            log.error("Network interface not found");
        }
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
