package com.coveros.training.mathematics;

import java.math.BigInteger;

/**
 * From https://rosettacode.org/wiki/Fibonacci_sequence#Iterative_35
 */
public class FibonacciIterative {

    private FibonacciIterative() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * O(log(n))
     */
    public static BigInteger fibAlgo1(long n) {
        if (n <= 0)
            return BigInteger.ZERO;

        BigInteger i =  (BigInteger.valueOf(n).subtract(BigInteger.ONE));
        BigInteger a = BigInteger.ONE;
        BigInteger b = BigInteger.ZERO;
        BigInteger c = BigInteger.ZERO;
        BigInteger d = BigInteger.ONE;
        BigInteger tmp1;
        BigInteger tmp2;

        while (i.compareTo(BigInteger.ZERO) > 0) {
            if (!i.mod(BigInteger.valueOf(2)).equals(BigInteger.ZERO)) {
                tmp1 = d.multiply(b).add(c.multiply(a));
                tmp2 = d.multiply (b.add(a)).add(c.multiply(b));
                a = tmp1;
                b = tmp2;
            }

            tmp1 = c.pow(2).add(d.pow(2));
            tmp2 = d.multiply(c.multiply(BigInteger.valueOf(2)).add(d));

            c = tmp1;
            d = tmp2;

            i = i.divide(BigInteger.valueOf(2));
        }
        return a.add(b);
    }

    public static BigInteger fibAlgo2(int n)
    {
        if (n < 2)
            return BigInteger.valueOf(n);
        BigInteger ans = BigInteger.ZERO;
        BigInteger n1 =  BigInteger.ZERO;
        BigInteger n2 =  BigInteger.ONE;
        for(n--; n > 0; n--)
        {
            ans = n1.add(n2);
            n1 = n2;
            n2 = ans;
        }
        return ans;
    }

}
