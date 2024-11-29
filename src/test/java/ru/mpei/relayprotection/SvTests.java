package ru.mpei.relayprotection;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import ru.mpei.relayprotection.model.sv.SvReceiver;
import ru.mpei.relayprotection.utils.JsonSerialization;

public class SvTests {

    @Test
    @SneakyThrows
    void myRealisationOfListener() {
        SvReceiver listener = new SvReceiver(
                "Realtek USB FE Family Controller #2",
                "01:0c:cd:04:00:01",
                "01:0c:cd:04:00:02",
                true,
                5000);
        listener.setAnalyzeActivityStatus(true);
        Thread.sleep(10_000);
//        System.out.println(JsonSerialization.writeAsJson(listener.getBuffer().getMeasurementsForPeriod(30)).get());
    }
}
