package com.coveros.training.autoinsurance;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class AutoInsuranceProcessorTests {

    @Parameters
    public static Collection<Object[]> data() {

        // three-point bounds testing
        return Arrays.asList(new Object[][]{
        // claims,  age,  premiumIncrease,  warningLetter,  cancelPolicy, isError
                {0, 15, -1, WarningLetterEnum.NONE, false, true},
                {0, 16, 50, WarningLetterEnum.NONE, false, false},
                {0, 17, 50, WarningLetterEnum.NONE, false, false},

                {0, 25, 50, WarningLetterEnum.NONE, false, false},
                {0, 26, 25, WarningLetterEnum.NONE, false, false},
                {0, 27, 25, WarningLetterEnum.NONE, false, false},

                {0, 84, 25, WarningLetterEnum.NONE, false, false},
                {0, 85, 25, WarningLetterEnum.NONE, false, false},
                {0, 86, -1, WarningLetterEnum.NONE, false, true},

                {1, 15, -1, WarningLetterEnum.NONE, false, true},
                {1, 16, 100, WarningLetterEnum.LTR1, false, false},
                {1, 17, 100, WarningLetterEnum.LTR1, false, false},

                {1, 25, 100, WarningLetterEnum.LTR1, false, false},
                {1, 26, 50, WarningLetterEnum.NONE, false, false},
                {1, 27, 50, WarningLetterEnum.NONE, false, false},

                {1, 84, 50, WarningLetterEnum.NONE, false, false},
                {1, 85, 50, WarningLetterEnum.NONE, false, false},
                {1, 86, -1, WarningLetterEnum.NONE, false, true},

                {2, 15, -1, WarningLetterEnum.NONE, false, true},
                {2, 16, 400, WarningLetterEnum.LTR2, false, false},
                {2, 17, 400, WarningLetterEnum.LTR2, false, false},

                {2, 25, 400, WarningLetterEnum.LTR2, false, false},
                {2, 26, 200, WarningLetterEnum.LTR3, false, false},
                {2, 27, 200, WarningLetterEnum.LTR3, false, false},

                {2, 84, 200, WarningLetterEnum.LTR3, false, false},
                {2, 85, 200, WarningLetterEnum.LTR3, false, false},
                {2, 86, -1, WarningLetterEnum.NONE, false, true},

                {4, 15, -1, WarningLetterEnum.NONE, false, true},
                {4, 16, 400, WarningLetterEnum.LTR2, false, false},
                {4, 17, 400, WarningLetterEnum.LTR2, false, false},

                {4, 25, 400, WarningLetterEnum.LTR2, false, false},
                {4, 26, 200, WarningLetterEnum.LTR3, false, false},
                {4, 27, 200, WarningLetterEnum.LTR3, false, false},

                {4, 84, 200, WarningLetterEnum.LTR3, false, false},
                {4, 85, 200, WarningLetterEnum.LTR3, false, false},
                {4, 86, -1, WarningLetterEnum.NONE, false, true},

                {5, 26, 0, WarningLetterEnum.NONE, true, false},
                {6, 26, 0, WarningLetterEnum.NONE, true, false},
                {5, 80, 0, WarningLetterEnum.NONE, true, false},

                {-1, 80, -1, WarningLetterEnum.NONE, false, true},
        });
    }

    private int claims;
    private int age;
    private int premiumIncrease;
    private WarningLetterEnum warningLetter;
    private boolean cancelPolicy;
    private boolean isError;

    public AutoInsuranceProcessorTests(int claims, int age, int premiumIncrease, WarningLetterEnum warningLetter, boolean cancelPolicy, boolean isError) {
        this.claims = claims;
        this.age = age;
        this.premiumIncrease = premiumIncrease;
        this.warningLetter = warningLetter;
        this.cancelPolicy = cancelPolicy;
        this.isError = isError;
    }

    @Test
    public void testProcessor() {

        AutoInsuranceAction action = AutoInsuranceProcessor.process(claims, age);

        AutoInsuranceAction expectedAutoInsuranceAction = new AutoInsuranceAction(premiumIncrease, warningLetter, cancelPolicy, isError);

        Assert.assertEquals("claims: " + claims + " age: " + age, expectedAutoInsuranceAction, action);
    }
}
