package com.coveros.training;

public class Ackermann {

    public static long calculate(long m, long n) {
        if (m == 0) return n + 1;
        if (n == 0) return calculate(m - 1, 1);
        return calculate(m - 1, calculate(m, n - 1));
    }
}
