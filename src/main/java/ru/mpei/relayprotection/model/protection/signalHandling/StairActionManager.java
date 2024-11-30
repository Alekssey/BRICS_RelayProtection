package ru.mpei.relayprotection.model.protection.signalHandling;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import ru.mpei.relayprotection.model.LogicalNode;
import ru.mpei.relayprotection.model.protection.LineProtection;
import ru.mpei.relayprotection.model.sv.model.ValueHolder;
import ru.mpei.relayprotection.service.GateWayService;

@Slf4j
@Data
public class StairActionManager implements LogicalNode {
    private GateWayService gateway;
    private String tag;
    private final ValueHolder<Boolean> damagedPhaseA;
    private final ValueHolder<Boolean> damagedPhaseB;
    private final ValueHolder<Boolean> damagedPhaseC;
    private LineProtection parentProtection;
    private Thread sendingCommandTask;
    private boolean alreadyOff;

    public StairActionManager(LineProtection parentProtection, GateWayService gateway, String cmdName, ValueHolder<Boolean> phsA, ValueHolder<Boolean> phsB, ValueHolder<Boolean> phsC) {
        this.parentProtection = parentProtection;
        this.gateway = gateway;
        this.tag = cmdName;
        this.damagedPhaseA = phsA;
        this.damagedPhaseB = phsB;
        this.damagedPhaseC = phsC;
//        this.configureNotifyingTask();
    }

//    private void configureNotifyingTask() {
//        this.sendingCommandTask = new Thread(() -> {
//            boolean response = false;
//            int commandsCounter = 0;
//            while (!response) {
//                response = this.gateway.sendCommand(this.tag, 0);
//                if (!response) {
//                    log.warn("bad response from sending command");
//                    if (commandsCounter++ > 5) {
//                        try {
//                            Thread.sleep(500);
//                        } catch (InterruptedException e) {
//                            throw new RuntimeException(e);
//                        }
//                    }
//                }
//            }
//            this.configureNotifyingTask();
//        });
//    }

    @Override
    public synchronized void process() {
        StringBuilder sb = new StringBuilder();
        if (this.damagedPhaseA.get()) sb.append("Phase A damaged. ");
        if (this.damagedPhaseB.get()) sb.append("Phase B damaged. ");
        if (this.damagedPhaseC.get()) sb.append("Phase C damaged. ");
        if (!sb.isEmpty()) {
            log.warn(sb.toString());
            this.parentProtection.stop();
            this.alreadyOff = true;
            this.gateway.sendCommand(this.tag, 1);
//        if (!this.sendingCommandTask.isAlive()) this.sendingCommandTask.start();
        }
    }

    public void turnOffFromNeuronNetwork() {
        if (!this.alreadyOff
                && (!this.parentProtection.getSvReceiver().getReceiverSettings().isAnalyzeEnabled()
                || !this.parentProtection.getSvReceiver().getFirstThreadLifecycle().isThreadAlive()
                || !this.parentProtection.getSvReceiver().getSecondThreadLifecycle().isThreadAlive())) {
            log.warn("Turn off from neuron network");
            this.alreadyOff = true;
            this.gateway.sendCommand(this.tag, 1);
        } else {
            log.warn(String.format("Neuron Network signal ignored because line %s already turned off", this.parentProtection.getLineName()));
        }
    }

    @Override
    public void actualize() {}
}
