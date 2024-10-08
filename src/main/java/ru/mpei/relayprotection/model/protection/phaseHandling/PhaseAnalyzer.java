package ru.mpei.relayprotection.model.protection.phaseHandling;

import lombok.extern.slf4j.Slf4j;
import ru.mpei.relayprotection.model.protection.signalHandling.StairActionManager;

@Slf4j
public abstract class PhaseAnalyzer {
    protected final Object locker = new Object();
    protected double setpoint;
    protected Thread task;
    protected StairActionManager actionManager;
    protected int counter;


    public PhaseAnalyzer(double setpoint, StairActionManager actionManager) {
        this.setpoint = setpoint;
        this.task = this.createAnalyzingTask();
        this.actionManager = actionManager;
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
