package ru.mpei.relayprotection.model.protection.signalHandling.filters.fourier.internalClasses.dataObjects;

import lombok.Getter;
import lombok.Setter;

/**
 * This class uses for describing magnitude of analog value that we read from file
 */
public class MV {
    @Getter @Setter
    private AnalogueValue instMag = new AnalogueValue();
}
