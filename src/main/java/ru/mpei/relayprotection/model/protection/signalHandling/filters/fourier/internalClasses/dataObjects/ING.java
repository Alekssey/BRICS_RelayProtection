package ru.mpei.relayprotection.model.protection.signalHandling.filters.fourier.internalClasses.dataObjects;

import lombok.Getter;
import lombok.Setter;
import ru.mpei.relayprotection.model.protection.signalHandling.filters.fourier.internalClasses.common.DataAttribute;

public class ING{
    @Getter @Setter
    private DataAttribute<Integer> setVal = new DataAttribute<>();
}
