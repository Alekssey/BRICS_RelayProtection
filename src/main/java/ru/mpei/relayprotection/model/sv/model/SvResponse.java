package ru.mpei.relayprotection.model.sv.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SvResponse {
    private String mac;
    private double[] ia;
    private double[] ib;
    private double[] ic;
}
