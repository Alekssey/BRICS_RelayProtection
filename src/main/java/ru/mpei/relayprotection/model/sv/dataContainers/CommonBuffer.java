package ru.mpei.relayprotection.model.sv.dataContainers;

import ru.mpei.relayprotection.model.sv.model.SvResponse;

import java.util.List;

public class CommonBuffer {
    private final String mac1;
    private final String mac2;
    private final int maxSize = 12_000;
    private final double[] side1_mas_ia = new double[maxSize];
    private final double[] side1_mas_ib = new double[maxSize];
    private final double[] side1_mas_ic = new double[maxSize];
    private final double[] side2_mas_ia = new double[maxSize];
    private final double[] side2_mas_ib = new double[maxSize];
    private final double[] side2_mas_ic = new double[maxSize];
    private int index = 0;

    public CommonBuffer(String mac1, String mac2) {
        this.mac1 = mac1;
        this.mac2 = mac2;
    }

    public synchronized void set(String mac, double v1, double v2, double v3) {
        if (mac.equals(mac1)) {
            side1_mas_ia[index] = v1;
            side1_mas_ib[index] = v2;
            side1_mas_ic[index] = v3;
            side2_mas_ia[index] = side2_mas_ia[index == 0 ? maxSize - 1 : index - 1];
            side2_mas_ib[index] = side2_mas_ib[index == 0 ? maxSize - 1 : index - 1];
            side2_mas_ic[index] = side2_mas_ic[index == 0 ? maxSize - 1 : index - 1];
        } else {
            side2_mas_ia[index] = v1;
            side2_mas_ib[index] = v2;
            side2_mas_ic[index] = v3;
            side1_mas_ia[index] = side1_mas_ia[index == 0 ? maxSize - 1 : index - 1];
            side1_mas_ib[index] = side1_mas_ib[index == 0 ? maxSize - 1 : index - 1];
            side1_mas_ic[index] = side1_mas_ic[index == 0 ? maxSize - 1 : index - 1];
        }
        index = index == maxSize - 1 ? 0 : index + 1;
    }

    public List<SvResponse> getMeasurementsForPeriod(int periodMillis) {
        int necessaryNumberOfPoint = 80 * periodMillis / 20;
        int localIndex = index;
        return List.of(
                new SvResponse(
                        mac1,
                    takeNecessaryPartOfArray(necessaryNumberOfPoint, localIndex, side1_mas_ia),
                    takeNecessaryPartOfArray(necessaryNumberOfPoint, localIndex, side1_mas_ib),
                    takeNecessaryPartOfArray(necessaryNumberOfPoint, localIndex, side1_mas_ic)),
                new SvResponse(
                        mac2,
                        takeNecessaryPartOfArray(necessaryNumberOfPoint, localIndex, side2_mas_ia),
                        takeNecessaryPartOfArray(necessaryNumberOfPoint, localIndex, side2_mas_ib),
                        takeNecessaryPartOfArray(necessaryNumberOfPoint, localIndex, side2_mas_ic)));
    }

    private double[] takeNecessaryPartOfArray(int necessarySize, int indexToStart, double[] sourceMas) {
        double[] outBuffer = new double[necessarySize];
        if (necessarySize <= indexToStart) {
            System.arraycopy(sourceMas, indexToStart - necessarySize, outBuffer, 0, necessarySize);
            return outBuffer;
        }
        System.arraycopy(sourceMas, 0, outBuffer, necessarySize - indexToStart, indexToStart);
        System.arraycopy(sourceMas, maxSize - necessarySize + indexToStart, outBuffer, 0, necessarySize - indexToStart);
        return outBuffer;
    }
}
