package com.coveros.training;

import org.junit.Assert;
import org.junit.Test;

import java.math.BigInteger;

public class FibonacciTests {

    private static final String FIB_FOR_43 = "433494437";
    private static final String FIB_FOR_200 = "280571172992510140037611932413038677189525";
    private static final String FIB_FOR_2000 = "4224696333392304878706725602341482782579852840250681098010280137314308584370130707224123599639141511088446087538909603607640194711643596029271983312598737326253555802606991585915229492453904998722256795316982874482472992263901833716778060607011615497886719879858311468870876264597369086722884023654422295243347964480139515349562972087652656069529806499841977448720155612802665404554171717881930324025204312082516817125";

    @Test
    public void testSmallValuesFibAlgo1() {
        final BigInteger fib = FibonacciIterative.fib_algo1(43);
        Assert.assertEquals(new BigInteger(FIB_FOR_43), fib);
    }

    @Test
    public void testSmallValuesFibAlgo2() {
        final BigInteger fib = FibonacciIterative.fib_algo2(43);
        Assert.assertEquals(new BigInteger(FIB_FOR_43), fib);
    }

    @Test
    public void testLargeValuesFibAlgo1() {
        final BigInteger fib = FibonacciIterative.fib_algo1(200);
        Assert.assertEquals(new BigInteger(FIB_FOR_200), fib);
    }

    @Test
    public void testLargeValuesFibAlgo2() {
        final BigInteger fib = FibonacciIterative.fib_algo2(200);
        Assert.assertEquals(new BigInteger(FIB_FOR_200), fib);
    }

    @Test
    public void testLargerValuesFibAlgo1() {
        final BigInteger fib = FibonacciIterative.fib_algo1(2000);
        Assert.assertEquals(new BigInteger(FIB_FOR_2000), fib);
    }

    @Test
    public void testLargerValuesFibAlgo2() {
        final BigInteger fib = FibonacciIterative.fib_algo2(2000);
        Assert.assertEquals(new BigInteger(FIB_FOR_2000), fib);
    }
}
