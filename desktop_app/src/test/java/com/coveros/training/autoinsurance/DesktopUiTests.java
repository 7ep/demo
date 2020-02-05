package com.coveros.training.autoinsurance;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class DesktopUiTests {

    @Before
    public void startUI() {
        AutoInsuranceUI.main(new String[]{});
    }

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
        dt.quit();
        Assert.assertEquals("Premium increase: $100 Warning Ltr: LTR1 is canceled: false", label);
    }


}
