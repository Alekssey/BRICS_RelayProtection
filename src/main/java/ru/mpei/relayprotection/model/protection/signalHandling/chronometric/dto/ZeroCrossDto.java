package ru.mpei.relayprotection.model.protection.signalHandling.chronometric.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.mpei.relayprotection.model.enumerations.CrossingType;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ZeroCrossDto {
    private CrossingType type;
    private double time;
}
