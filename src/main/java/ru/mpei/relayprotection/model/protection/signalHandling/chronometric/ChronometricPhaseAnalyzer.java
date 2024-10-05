package ru.mpei.relayprotection.model.protection.signalHandling.chronometric;

import ru.mpei.relayprotection.model.enumerations.ActionType;
import ru.mpei.relayprotection.model.protection.signalHandling.PhaseAnalyzer;
import ru.mpei.relayprotection.model.protection.signalHandling.blockers.BlockingStatus;

public class ChronometricPhaseAnalyzer extends PhaseAnalyzer {

    @Override
    public ActionType analyze(ZeroCrossDto zcDto, BlockingStatus blockingStatus) {
        return null;
    }
}
