package ru.mpei.relayprotection.model.protection;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import ru.mpei.relayprotection.model.buffer.CommonBuffer;
import ru.mpei.relayprotection.model.sv.SvReceiver;

@Data
@Slf4j
public class LineProtection {
    private String lineName;
    private ProtectionStair firstStair;
    private ProtectionStair secondStair;
    private SvReceiver svReceiver;
    private final CommonBuffer buffer;

    public LineProtection(String name, SvReceiver svReceiver, CommonBuffer buffer) {
        this.lineName = name;
        this.svReceiver = svReceiver;
        this.buffer = buffer;
    }

    public void start() {
        log.info("Start protection on {}", this.getLineName());
        this.svReceiver.setAnalyzeActivityStatus(true);
    }

    public void stop() {
        log.info("Stop protection on {}", this.getLineName());
        this.svReceiver.setAnalyzeActivityStatus(false);
    }
}
