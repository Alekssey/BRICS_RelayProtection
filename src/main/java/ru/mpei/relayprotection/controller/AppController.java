package ru.mpei.relayprotection.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.mpei.relayprotection.service.ApplicationService;

import java.util.List;

@RestController
@RequestMapping("/protection")
public class AppController {
    @Autowired
    private ApplicationService service;

    @GetMapping("/configuration")
    public String getConfiguration() {
        return this.service.getTerminalConfiguration();
    }

    @PostMapping("/start")
    public void startProtections(@RequestBody List<String> linesNames) {
        this.service.startProtectionsByLineNames(linesNames);
    }

    @PostMapping("/stop")
    public void stopProtections(@RequestBody List<String> linesNames) {
        this.service.stopProtectionsByLineNames(linesNames);
    }

}
