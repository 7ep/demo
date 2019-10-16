package com.coveros.training;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class FibonacciTests {
    @Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                { 0, 0 }, { 1, 1 }, { 2, 1 }, { 3, 2 }, { 4, 3 }, { 5, 5 }, { 6, 8 }, {20, 6765}, {43, 433494437}
        });
    }

    private int fInput;

    private int fExpected;

    public FibonacciTests(int input, int expected) {
        this.fInput = input;
        this.fExpected = expected;
    }

    /**
     * Testing a very standard recursive version
     */
    @Test
    public void test() {
        assertEquals(String.format("for the %dth number, we expected %d", fInput, fExpected), fExpected, Fibonacci.calculate(fInput));
    }

    /**
     * Testing an iterative version found on Rosetta Code
     */
    @Test
    public void testIterative1() {
        assertEquals(String.format("for the %dth number, we expected %d", fInput, fExpected), fExpected, FibonacciIterative.fib(fInput));
    }

    /**
     * Testing a second iterative version found on Rosetta Code
     */
    @Test
    public void testIterative2() {
        assertEquals(String.format("for the %dth number, we expected %d", fInput, fExpected), fExpected, FibonacciIterative.itFibN(fInput));
    }
}