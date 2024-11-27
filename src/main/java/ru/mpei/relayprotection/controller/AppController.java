package ru.mpei.relayprotection.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.mpei.relayprotection.model.sv.model.SvResponse;
import ru.mpei.relayprotection.service.ApplicationService;

import java.util.List;

@RestController
@RequestMapping("/protection")
public class AppController {
    @Autowired
    private ApplicationService service;

    @PostMapping("/start")
    public void startProtections(@RequestBody List<String> linesNames) {
        this.service.startProtectionsByLineNames(linesNames);
    }

    @PostMapping("/stop")
    public void stopProtections(@RequestBody List<String> linesNames) {
        this.service.stopProtectionsByLineNames(linesNames);
    }

    @GetMapping("/measurements")
    public List<SvResponse> getMeasurementsForLine(@RequestParam String lineName, @RequestParam int period) {
        return this.service.getMeasurementsForLine(lineName, period);
    }

    @GetMapping("/buffered_measurements")
    public List<SvResponse> getMeasurementsForLineFromBuffer(@RequestParam String lineName, @RequestParam int period) {
        return this.service.getMeasurementsForLineFromBuffer(lineName, period);
    }
}
