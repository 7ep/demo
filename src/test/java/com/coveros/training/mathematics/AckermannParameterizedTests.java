package com.coveros.training.mathematics;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class AckermannParameterizedTests {

    @Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
             //      m                 n                  expected
                {    0,                0,                    "1"     },

                {    1,                1,                    "3"     },
                {    1,                2,                    "4"     },
                {    1,                3,                    "5"     },

                {    2,                1,                    "5"     },
                {    2,                2,                    "7"     },
                {    2,                3,                    "9"     },
                {    2,                4,                    "11"    },

                {    3,                1,                    "13"    },
                {    3,                2,                    "29"    },
                {    3,                3,                    "61"    },

                {    4,                0,                    "13"    },
        });
    }

    private final long m;
    private final long n;
    private final BigInteger expected;

    public AckermannParameterizedTests(long m, long n, String expected) {
        this.m = m;
        this.n = n;
        this.expected = new BigInteger(expected);
    }

    @Test
    public void testShouldProperlyCalculate() {
        final BigInteger result = Ackermann.calculate((int)m, (int)n);
        Assert.assertEquals(String.format("for m of %d and n of %d we should have gotten %d", m, n, expected), expected, result);
    }

}
