package ru.mpei.relayprotection.model.protection.phaseHandling;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import ru.mpei.relayprotection.model.protection.signalHandling.StairActionManager;
import ru.mpei.relayprotection.model.protection.signalHandling.chronometric.ChronometricSignalHandler;
import ru.mpei.relayprotection.model.sv.SvReceiveRunner;
import ru.mpei.relayprotection.model.sv.SvReceiver;
import ru.mpei.relayprotection.model.sv.SvThreadInstDataContainer;

@Slf4j
public abstract class PhaseAnalyzer {
    protected final Object locker = new Object();
    protected double setpoint;
    protected Thread task;
    protected StairActionManager actionManager;
    protected int counter;
    @Getter
    protected boolean needToAct = false;
    @Setter
    protected SvReceiver svReceiver;


    public PhaseAnalyzer(double setpoint, StairActionManager actionManager, SvReceiver svReceiver) {
        this.setpoint = setpoint;
        this.task = this.createAnalyzingTask();
        this.actionManager = actionManager;
        this.svReceiver = svReceiver;
        this.startAnalyzingTask();
    }

    protected Thread createAnalyzingTask() {
        return new Thread(() -> {
            while(true) {
                synchronized (this.locker) {
                    try {
                        this.locker.wait();
                    } catch (InterruptedException e) {
                        log.error("Analyzing thread was interrupted during waiting for notification");
                        throw new RuntimeException(e);
                    }
                    this.analyze();
                }
            }
        });
    }

    public void startAnalyzingTask() {
        this.task.start();
    }

    protected abstract void analyze();

    public abstract void act();

}
