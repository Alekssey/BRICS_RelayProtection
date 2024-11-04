package ru.mpei.relayprotection.model.protection.signalHandling.filters.fourier.internalClasses.dataObjects;

import lombok.Getter;
import lombok.Setter;

/**
 * This class uses for describing complex measured value
 */
public class CMV {
    @Getter @Setter
    private Vector instCVal = new Vector();
}
