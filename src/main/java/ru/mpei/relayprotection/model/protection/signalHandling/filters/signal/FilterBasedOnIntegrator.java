package ru.mpei.relayprotection.model.protection.signalHandling.filters.signal;

public class FilterBasedOnIntegrator extends FrequencyFilter{
    private double filteringCoefficient;
    private double clearSignal = 0;

    public FilterBasedOnIntegrator(double filteringCoefficient) {
        this.filteringCoefficient = filteringCoefficient;
    }

    @Override
    public double filter(double rawSignal) {
        this.clearSignal = (1 - this.filteringCoefficient) * this.clearSignal + this.filteringCoefficient * rawSignal;
        return this.clearSignal;
    }
}
