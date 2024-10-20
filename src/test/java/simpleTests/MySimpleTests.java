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

    @Test
    public void plus_plus_test() {
        int i = 0;
        System.out.println(i++);
        System.out.println(i);
    }
}
