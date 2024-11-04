package ru.mpei.relayprotection.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.mpei.relayprotection.model.protection.LineProtection;
import ru.mpei.relayprotection.model.protection.RelayProtectionComplex;

import java.util.List;

@Service
public class ApplicationService {
    @Autowired
    private RelayProtectionComplex logicalDevice;
    @Autowired
    private ObjectMapper mapper;

    public String getTerminalConfiguration() {
        try {
            return this.mapper.writeValueAsString(this.logicalDevice.getProtections());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }


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

}
