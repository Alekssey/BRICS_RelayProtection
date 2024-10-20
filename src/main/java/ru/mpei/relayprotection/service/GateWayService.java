package ru.mpei.relayprotection.service;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GateWayService {
    @Value("${104.server.ip}")
    private String ip;
    @Value("${104.server.port}")
    private String port;
    private String sendUrl;
    private final RestTemplate rt = new RestTemplate();

    @PostConstruct
    private void createUrl() {
        this.sendUrl = "http://" + ip + ":" + port + "/iec104/send/command";
    }

    public boolean sendCommand(String tag, double value){
        ResponseEntity<Void> ent = rt.postForEntity(sendUrl, new CommandTO(tag, value + ""), Void.class);
        return ent.getStatusCode().is2xxSuccessful();
    }

    record CommandTO(String name, String value) {}

}
