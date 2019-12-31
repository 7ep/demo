package com.coveros.training;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

public class DateUtilsTests {

    /**
     * This contrived example is here to provide an opportunity to
     * improve a flaky test.  Our tests should always return the same
     * result, we want total control over our laboratory.
     */
    @Ignore("used for classes")
    @Test
    public void testShouldReturnEvenTime() {
        Assert.assertTrue(DateUtils.isTimeEven());
    }

}
