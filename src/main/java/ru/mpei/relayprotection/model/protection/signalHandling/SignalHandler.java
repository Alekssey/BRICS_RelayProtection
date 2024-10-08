package ru.mpei.relayprotection.model.protection.signalHandling;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import ru.mpei.relayprotection.model.protection.phaseHandling.PhaseAnalyzer;
import ru.mpei.relayprotection.model.sv.ValueHolder;

@Data
@Slf4j
public abstract class SignalHandler {
    protected ValueHolder value;
    protected Thread task;
    protected PhaseAnalyzer phaseAnalyzer;

    public SignalHandler(ValueHolder value, PhaseAnalyzer phaseAnalyzer) {
        this.value = value;
        this.phaseAnalyzer = phaseAnalyzer;
        this.task = this.createHandlingTask();
    }

    protected Thread createHandlingTask() {
        return new Thread(() -> {
            while (true) {
                synchronized (this.value.getLocker()) {
                    try {
                        this.value.getLocker().wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        throw new RuntimeException(e);
                    }
                }
                handle();
            }
        });
    }

    public void startHandlingTask() {
        this.task.start();
    }

    public abstract void handle();
}
