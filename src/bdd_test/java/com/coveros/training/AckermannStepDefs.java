package com.coveros.training;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;

public class AckermannStepDefs {

    private long result;

    @When("I calculate Ackermann's formula using {long} and {long}")
    public void i_calculate_ackermann_s_formula_using_and(long int1, long int2) {
        long m = int1;
        long n = int2;
        result = Ackermann.calculate(m, n);
    }

    @Then("the Ackermann result is {long}")
    public void i_get(long expected) {
        Assert.assertEquals(expected, result);
    }

}
