package ru.mpei.relayprotection.model.protection.signalHandling.filters.fourier;

import ru.mpei.relayprotection.model.protection.signalHandling.filters.fourier.internalClasses.common.DataAttribute;
import ru.mpei.relayprotection.model.protection.signalHandling.filters.fourier.internalClasses.dataObjects.CMV;
import ru.mpei.relayprotection.model.protection.signalHandling.filters.fourier.internalClasses.dataObjects.ING;
import ru.mpei.relayprotection.model.protection.signalHandling.filters.fourier.internalClasses.dataObjects.MV;
import ru.mpei.relayprotection.model.protection.signalHandling.filters.fourier.internalClasses.dataObjects.Vector;
import ru.mpei.relayprotection.model.sv.ValueHolder;

/**
 * This class describe Fourier filter. With it help we get rated value of phase current, and it's angle
 */
public class Fourier {
    private ING bSize = new ING();
    private final MV[] buffer;
    public DataAttribute<Integer> bCount = new DataAttribute<>();
    public DataAttribute<Double> rVal = new DataAttribute<>();
    public
    DataAttribute<Double> imVal = new DataAttribute<>();
    public DataAttribute<Double> freq = new DataAttribute<>();
    public DataAttribute<Double> dT = new DataAttribute<>();
    private DataAttribute<Double> amplitude = new DataAttribute<>();
    private DataAttribute<Double> temporaryAng = new DataAttribute<>();

    public Fourier(int bufferSize, double frequency){
        bSize.getSetVal().setValue(bufferSize);
        bCount.setValue(0);
        rVal.setValue(0D);
        imVal.setValue(0D);
        freq.setValue(frequency);
        dT.setValue(0.02 / bufferSize);
        buffer = new MV[bufferSize];

        for (int i = 0; i < bufferSize; i++) {
            MV tempVal = new MV();
            tempVal.getInstMag().getF().setValue(0D);
            buffer[i] = tempVal;
        }
    }

    public void process(ValueHolder measuredValue, Vector result) {

        rVal.setValue(rVal.getValue()
                + (measuredValue.get()
                - buffer[bCount.getValue()].getInstMag().getF().getValue())
                * Math.sin(2 * Math.PI * freq.getValue() * bCount.getValue() * dT.getValue())
                * 2 / bSize.getSetVal().getValue()
        );
        imVal.setValue(imVal.getValue()
                + (measuredValue.get()
                - buffer[bCount.getValue()].getInstMag().getF().getValue())
                * Math.cos(2 * Math.PI * freq.getValue() * bCount.getValue() * dT.getValue())
                * 2 / bSize.getSetVal().getValue()
        );

        result.setMag(0.7071068 * Math.sqrt(Math.pow(rVal.getValue(), 2) + Math.pow(imVal.getValue(), 2)));
//        result.setAng(calculateAngle());

        buffer[bCount.getValue()].getInstMag().getF().setValue(measuredValue.get());
        bCount.setValue(bCount.getValue() + 1);
        if (bCount.getValue() >= bSize.getSetVal().getValue()){
            bCount.setValue(0);
        }
    }

    private double calculateAngle(){
        double angle = Math.acos(rVal.getValue() / (Math.sqrt(Math.pow(rVal.getValue(), 2) + Math.pow(imVal.getValue(), 2)))) * 180 / Math.PI;

        if (imVal.getValue() >= 0) {
            return angle;
        } else {
            return -1 * angle;
        }

    }
}
