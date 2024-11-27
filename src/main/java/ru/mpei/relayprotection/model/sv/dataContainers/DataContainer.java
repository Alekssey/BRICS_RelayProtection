package ru.mpei.relayprotection.model.sv.dataContainers;

import lombok.Getter;

@Getter
public class DataContainer {
    private final SvThreadInstDataContainer firstThreadDataContainer = new SvThreadInstDataContainer();
    private final SvThreadInstDataContainer secondThreadDataContainer = new SvThreadInstDataContainer();
    private final CommonBuffer buffer;

    public DataContainer(CommonBuffer buffer) {
        this.buffer = buffer;
    }
}
