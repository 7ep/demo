package com.coveros.training.helpers;

import org.junit.Test;

public class CheckUtilsTests {

    @Test(expected = IllegalArgumentException.class)
    public void testShouldThrowErrorFor0() {
        CheckUtils.IntParameterMustBePositive(0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testShouldThrowErrorForSubZero() {
        CheckUtils.IntParameterMustBePositive(-1);
    }

    @Test
    public void testShouldSucceedForPositiveValue() {
        CheckUtils.IntParameterMustBePositive(1);
    }

}
