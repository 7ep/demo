package com.coveros.training.autoinsurance;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Assert;
import org.junit.Test;

import static com.coveros.training.autoinsurance.WarningLetterEnum.LTR1;

public class AutoInsuranceActionTests {

    @Test
    public void testShouldHaveEqualsAndHashcodeImplementedCorrectly() {
        EqualsVerifier.forClass(AutoInsuranceAction.class).verify();
    }

    @Test
    public void testShouldOutputGoodString() {
        final AutoInsuranceAction action = createTestAutoInsuranceAction();
        Assert.assertTrue("toString was: " + action.toString(), action.toString().contains("premiumIncreaseDollars=50,warningLetterEnum=LTR1,isPolicyCanceled=false"));
    }

    public static AutoInsuranceAction createTestAutoInsuranceAction() {
        return new AutoInsuranceAction(50, LTR1, false, false);
    }

    @Test
    public void testCanCreateEmpty() {
        final AutoInsuranceAction autoInsuranceAction = AutoInsuranceAction.createEmpty();
        Assert.assertTrue(autoInsuranceAction.isEmpty());
    }
}
