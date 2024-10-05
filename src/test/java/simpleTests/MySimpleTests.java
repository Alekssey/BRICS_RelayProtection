package simpleTests;

import org.junit.Test;

public class MySimpleTests {
    @Test
    public void testMillis() {
        long startTime = System.currentTimeMillis();
        while (System.currentTimeMillis() - startTime < 1000) {
            System.err.println(System.currentTimeMillis());
        }
    }
}
