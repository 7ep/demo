package com.coveros.training;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;

public class AckermannStepDefs {

    private long result;

    @When("I calculate Ackermann's formula using {int} and {int}")
    public void i_calculate_ackermann_s_formula_using_and(Integer int1, Integer int2) {
        long m = int1;
        long n = int2;
        result = Ackermann.calculate(m, n);
    }

    @Then("I get {int}")
    public void i_get(Integer expected) {
        Assert.assertEquals((long)expected, result);
    }

}
