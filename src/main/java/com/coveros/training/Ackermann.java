package com.coveros.training;

import java.math.BigInteger;


public class Ackermann {

    /**
     * Ackerman function.
     *
     * This version found at https://rosettacode.org/wiki/Ackermann_function#Java
     */
    public static BigInteger ack(BigInteger m, BigInteger n) {
        return m.equals(BigInteger.ZERO)
                ? n.add(BigInteger.ONE)
                : ack(m.subtract(BigInteger.ONE),
                n.equals(BigInteger.ZERO) ? BigInteger.ONE : ack(m, n.subtract(BigInteger.ONE)));
    }

    /**
     * A helper method to make it easy to call this with only integers
     * Calls to {@link #ack}
     */
    public static BigInteger calculate(int m, int n) {
        BigInteger bigM = BigInteger.valueOf(m);
        BigInteger bigN = BigInteger.valueOf(n);
        return ack(bigM, bigN);
    }
}
