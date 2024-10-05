package ru.mpei.relayprotection.service;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.apache.catalina.core.ApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;
import ru.mpei.relayprotection.model.protection.LineProtection;
import ru.mpei.relayprotection.model.sv.SvReceiveRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class ApplicationService {
//    @Autowired
//    private CommunicationWithContext springContext;

//    @PostConstruct
//    private void parseCfg() {
//        lookOnBeans();
//    }
//    public void lookOnBeans() {
//        Map<String, SvReceiveRunner> list = CommunicationWithContext.getContext().getBeansOfType(SvReceiveRunner.class);
//        list.entrySet().forEach(s -> {
//            System.out.print(s.getKey() + " : " + s.getValue());
//        });
//    }
}
