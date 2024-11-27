package ru.mpei.relayprotection.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.mpei.relayprotection.model.protection.LineProtection;
import ru.mpei.relayprotection.model.protection.RelayProtectionComplex;
import ru.mpei.relayprotection.model.sv.model.SvResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ApplicationService {
    @Autowired
    private RelayProtectionComplex logicalDevice;

    public void startProtectionsByLineNames(List<String> linesNames) {
        this.logicalDevice.getProtections().stream()
                .filter(protection -> linesNames.contains(protection.getLineName()))
                .forEach(LineProtection::start);
    }

    public void stopProtectionsByLineNames(List<String> linesNames) {
        this.logicalDevice.getProtections().stream()
                .filter(protection -> linesNames.contains(protection.getLineName()))
                .forEach(LineProtection::stop);
    }

    public List<SvResponse> getMeasurementsForLine(String lineName, int period) {
        Optional<LineProtection> protection = this.logicalDevice.getProtections().stream()
                .filter(lineProtection -> lineProtection.getLineName().equals(lineName))
                .findAny();
        if (protection.isEmpty()) return new ArrayList<>();
        List<SvResponse> responses = protection.get().getBuffer().getMeasurementsForPeriod(period);
//        List<SvResponse> responses = new ArrayList<>();
//        responses.add(protection.get().getFirstSvThread().getMeasurementsForPeriod(period));
//        responses.add(protection.get().getSecondSvThread().getMeasurementsForPeriod(period));
        return responses;
    }

    public List<SvResponse> getMeasurementsForLineFromBuffer(String lineName, int period) {
        Optional<LineProtection> protection = this.logicalDevice.getProtections().stream()
                .filter(lineProtection -> lineProtection.getLineName().equals(lineName))
                .findAny();
        return protection.isPresent() ? protection.get().getBuffer().getMeasurementsForPeriod(period) : new ArrayList<>();
    }

}
