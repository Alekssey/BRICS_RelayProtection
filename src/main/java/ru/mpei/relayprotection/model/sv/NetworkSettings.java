package ru.mpei.relayprotection.model.sv;

import lombok.Data;

@Data
public class NetworkSettings {
    private final String iFace;
    private final String filterExpression;
    private final String mac1;
    private final String mac2;

    public NetworkSettings(String iFace, String mac1, String mac2) {
        this.iFace = iFace;
        this.mac1 = mac1;
        this.mac2 = mac2;
        this.filterExpression = "ether proto 0x88ba  && (ether dst " + mac1 + " || ether dst " + mac2 + ")";
    }
}
