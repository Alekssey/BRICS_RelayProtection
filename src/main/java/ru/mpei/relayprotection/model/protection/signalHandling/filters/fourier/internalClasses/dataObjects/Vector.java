package ru.mpei.relayprotection.model.protection.signalHandling.filters.fourier.internalClasses.dataObjects;

import lombok.Getter;
import lombok.Setter;

/**
 * This class uses for describing vector values: rated signal magnitude and phase
 */
public class Vector {
    @Getter @Setter
    private double mag;
    @Getter @Setter
    private double ang;
}
