package com.coveros.training;

import org.junit.Assert;
import org.junit.Test;

public class DesktopUiTests {

    /**
     * An initial test to make sure the basics work
     */
    @Test
    public void testShouldGetCorrectCalculationHappyPath() {
        DesktopTester dt = new DesktopTester(new AutoInsuranceScriptClient());
        dt.setAge(22);
        dt.setClaims(1);
        dt.clickCalculate();
        final String label = dt.getLabel();
        Assert.assertEquals("Premium increase: $100 Warning Ltr: LTR1 is canceled: false", label);
    }
}
