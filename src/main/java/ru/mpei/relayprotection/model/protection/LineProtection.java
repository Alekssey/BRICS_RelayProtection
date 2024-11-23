package ru.mpei.relayprotection.model.protection;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.mpei.relayprotection.model.buffer.MyBuffer;
import ru.mpei.relayprotection.model.enumerations.CurrentLevel;
import ru.mpei.relayprotection.model.sv.SvReceiveRunner;

import java.util.List;

@Data
//@AllArgsConstructor
@Slf4j
public class LineProtection {
    private String lineName;
//    private ProtectionStair firstStair;
//    private ProtectionStair secondStair;
    private SvReceiveRunner firstSvThread;
    private SvReceiveRunner secondSvThread;
    private final MyBuffer buffer;

    public LineProtection(String name, SvReceiveRunner firstSvThread, SvReceiveRunner secondSvThread, MyBuffer buffer) {
        this.lineName = name;
        this.firstSvThread = firstSvThread;
        this.secondSvThread = secondSvThread;
        this.buffer = buffer;
    }

    public void start() {
        log.info("Start protection on {}", this.getLineName());
        this.firstSvThread.setInWork(true);
        this.secondSvThread.setInWork(true);
    }

    public void stop() {
        log.info("Stop protection on {}", this.getLineName());
        this.firstSvThread.setInWork(false);
        this.secondSvThread.setInWork(false);
    }
}
