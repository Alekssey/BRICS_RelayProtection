package ru.mpei.relayprotection.model.sv;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SvReceiverSettings {
    private boolean analyzeEnabled;
    private boolean debugEnabled;
    private long svLostPeriod;
}
