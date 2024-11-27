package ru.mpei.relayprotection.model.sv.model;

import lombok.Getter;
import lombok.Setter;

@Getter
public class SvThreadLifeCycle {
    @Setter
    private boolean isThreadAlive;
    private boolean hasNewPackets;
    private int packetsCounter;

    public void set() {
        this.hasNewPackets = true;
        this.packetsCounter++;
    }

    public void reset() {
        this.hasNewPackets = false;
        this.packetsCounter = 0;
    }
}
