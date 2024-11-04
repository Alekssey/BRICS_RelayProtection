package ru.mpei.relayprotection.model.protection.signalHandling.filters.fourier.internalClasses.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Class contains variable of any type
 * @param <T>
 */
@NoArgsConstructor
@AllArgsConstructor
public class DataAttribute<T> {
    @Getter @Setter
    private T value;
}
