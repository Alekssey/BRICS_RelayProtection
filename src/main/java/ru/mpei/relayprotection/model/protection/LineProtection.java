package ru.mpei.relayprotection.model.protection;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.mpei.relayprotection.model.enumerations.CurrentLevel;
import ru.mpei.relayprotection.model.sv.SvReceiveRunner;

import java.util.List;

@Data
@AllArgsConstructor
//@NoArgsConstructor
public class LineProtection {
    private String lineName;
    private ProtectionStair firstStair;
    private ProtectionStair secondStair;
    private SvReceiveRunner firstSvThread;
    private SvReceiveRunner secondSvThread;

    public LineProtection(String name, SvReceiveRunner firstSvThread, SvReceiveRunner secondSvThread) {
        this.lineName = name;
        this.firstSvThread = firstSvThread;
        this.secondSvThread = secondSvThread;
    }
}
