package com.coveros.training.helpers;

import com.coveros.training.helpers.CheckUtils;
import org.junit.Test;

public class CheckUtilsTests {

    @Test(expected = IllegalArgumentException.class)
    public void testShouldThrowErrorFor0() {
        CheckUtils.checkIntParamPositive(0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testShouldThrowErrorForSubZero() {
        CheckUtils.checkIntParamPositive(-1);
    }

    @Test
    public void testShouldSucceedForPositiveValue() {
        CheckUtils.checkIntParamPositive(1);
    }

}
