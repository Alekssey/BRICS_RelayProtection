package ru.mpei.relayprotection.model.protection.signalHandling.filters.fourier.internalClasses.dataObjects;

import lombok.Getter;
import lombok.Setter;
import ru.mpei.relayprotection.model.protection.signalHandling.filters.fourier.internalClasses.common.DataAttribute;

/**
 * With this class we save values of analogue signals (previously in float type)
 */
public class AnalogueValue {
    @Getter @Setter
    private DataAttribute<Integer> i = new DataAttribute<>();
    @Getter @Setter
    private DataAttribute<Double> f = new DataAttribute<>();
}
