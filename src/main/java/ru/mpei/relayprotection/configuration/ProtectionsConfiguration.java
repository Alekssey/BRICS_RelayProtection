package ru.mpei.relayprotection.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import ru.mpei.relayprotection.model.protection.LineProtection;
import ru.mpei.relayprotection.model.configuration.CfgRoot;
import ru.mpei.relayprotection.model.sv.SvReceiveRunner;
import ru.mpei.relayprotection.utils.WorkWithCfg;

@Configuration
@Slf4j
// TODO: remove this class and package
public class ProtectionsConfiguration {
    @Value("${protections.cfg-file.path}")
    private String cfgPath;
    private CfgRoot cfg;

//    @PostConstruct
    private void readCfg() {
        this.cfg = WorkWithCfg.unMarshalAny(CfgRoot.class, this.cfgPath);
        if (this.cfg == null) {
            throw new RuntimeException("Bad configuration");
        }
        /*this.cfg.getLinesProtections().forEach(cfgEl -> {
            System.err.println(cfgEl.toString());
            LineProtection protection = this.lineProtection(
                    this.svThreadReceiver().configure(cfgEl.getFirstSvThread().getCfgData(), cfgEl.getFirstSvThread().getMetadata()),
                    this.svThreadReceiver().configure(cfgEl.getSecondSvThread().getCfgData(), cfgEl.getSecondSvThread().getMetadata())
            );
            protection.setLineName(cfgEl.getLineName());
            protection.getFirstStair().setAvailableCurrentLevels(cfgEl.getFirstStair().getAvailableCurrentLevels());
            protection.getSecondStair().setAvailableCurrentLevels(cfgEl.getSecondStair().getAvailableCurrentLevels());
//            protection.getFirstSvThread().configure(cfgEl.getFirstSvThread().getCfgData(), cfgEl.getFirstSvThread().getMetadata());
//            protection.getSecondSvThread().configure(cfgEl.getSecondSvThread().getCfgData(), cfgEl.getSecondSvThread().getMetadata());
//            protection.setFirstSvThread(this.svThreadReceiver().configure(cfgEl.getFirstSvThread().getCfgData(), cfgEl.getFirstSvThread().getMetadata()));
//            protection.setSecondSvThread(this.svThreadReceiver().configure(cfgEl.getSecondSvThread().getCfgData(), cfgEl.getSecondSvThread().getMetadata()));

        });*/


//        this.cfg.getLinesProtections().forEach(cfgEl -> {
//            SvReceiveRunner thread1 = this.svThreadReceiver().configure(cfgEl.getFirstSvThread().getCfgData(), cfgEl.getFirstSvThread().getMetadata());
//            SvReceiveRunner thread2 = this.svThreadReceiver().configure(cfgEl.getSecondSvThread().getCfgData(), cfgEl.getSecondSvThread().getMetadata());
//
//            System.err.println(thread1.getSvMsgParameters().getMacDst());
//            System.err.println(thread2.getSvMsgParameters().getMacDst());
//
//            LineProtection protection = this.lineProtection(thread1, thread2);
//            protection.setLineName(cfgEl.getLineName());
//            protection.getFirstStair().setAvailableCurrentLevels(cfgEl.getFirstStair().getAvailableCurrentLevels());
//            protection.getSecondStair().setAvailableCurrentLevels(cfgEl.getSecondStair().getAvailableCurrentLevels());
//
//            System.err.println(protection.getLineName());
//            System.err.println(protection.getFirstStair());
//            System.err.println(protection.getSecondStair());
//            System.err.println(protection.getFirstSvThread().getSvMsgParameters().getMacDst());
//            System.err.println(protection.getSecondSvThread().getSvMsgParameters().getMacDst());
//
//        });
    }

//    @Bean
//    @Scope("prototype")
//    public LineProtection lineProtection(SvReceiveRunner thread1, SvReceiveRunner thread2) {
//        System.err.println("creating new LineProtection");
//        return new LineProtection(thread1, thread2);
//    }

//    @Bean
//    @Scope("prototype")
//    public SvReceiveRunner svThreadReceiver() {
//        System.err.println("creating new thread");
//        return new SvReceiveRunner();
//    }

}
