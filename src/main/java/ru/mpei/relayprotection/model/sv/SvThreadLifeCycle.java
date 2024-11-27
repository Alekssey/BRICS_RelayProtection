package ru.mpei.relayprotection.model.sv;

import lombok.Getter;

@Getter
public class SvThreadLifeCycle {
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
