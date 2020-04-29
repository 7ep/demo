package com.coveros.training.math;

import com.coveros.training.mathematics.Ackermann;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;

import java.math.BigInteger;

public class AckermannStepDefs {

    private BigInteger result;

    @When("I calculate Ackermann's formula using {int} and {int}")
    public void i_calculate_ackermann_s_formula_using_and(int m, int n) {
        result = Ackermann.calculate(m, n);
    }

    @Then("the Ackermann result is {int}")
    public void i_get(int expected) {
        final BigInteger bigExpected = BigInteger.valueOf(expected);
        Assert.assertEquals(bigExpected, result);
    }

}
