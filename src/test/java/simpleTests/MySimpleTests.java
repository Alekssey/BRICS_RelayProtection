package simpleTests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

public class MySimpleTests {
    @Test
    public void testMillis() {
        long startTime = System.currentTimeMillis();
        while (System.currentTimeMillis() - startTime < 1000) {
            System.err.println(System.currentTimeMillis());
        }
    }

    @Test
    public void plus_plus_test() {
        int i = 0;
        System.out.println(i++);
        System.out.println(i);
    }

    @SneakyThrows
    @Test
    public void threadsTest() {
        Thread t = new Thread(() -> {
            int i = 0;
            while (i < 5) {
                System.out.println("msg");
                i++;
                try {
                    Thread.sleep(1_000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        System.out.println(t.isAlive());
        t.start();
        System.out.println(t.isAlive());
        Thread.sleep(7_000);
        System.out.println(t.isAlive());
    }


    @Test
    public void ttt() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        List<String> list = List.of("W1");
        System.out.println(mapper.writeValueAsString(list));

    }

    @Test
    public void returnFromWhile() {
        int i = 0;
        while ( i < 10) {
            System.out.println(i);
            if (i == 5) return;
            i++;
        }
    }
}
