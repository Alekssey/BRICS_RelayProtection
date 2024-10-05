package ru.mpei.relayprotection.model.protection.signalHandling;

import ru.mpei.relayprotection.model.enumerations.ActionType;
import ru.mpei.relayprotection.model.protection.signalHandling.blockers.BlockingStatus;
import ru.mpei.relayprotection.model.protection.signalHandling.chronometric.ZeroCrossDto;

public abstract class PhaseAnalyzer {
    public abstract ActionType analyze(ZeroCrossDto zcDto, BlockingStatus blockingStatus);

}
