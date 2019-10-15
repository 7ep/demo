package com.coveros.training;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class AckermannTests {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    /**
     * For an m of 4 and n of 1, it's stack overflow time!
     */
    @Test
    public void testShouldStackOverflow() {
        thrown.expect(StackOverflowError.class);
        long m = 4;
        long n = 1;
        Ackermann.calculate(m, n);
    }
}
