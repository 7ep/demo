package com.coveros.training;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;

public class FibonacciStepDefs {

    long result;

    @When("I calculate the {int}")
    public void i_calculate_the(Integer nth_fibonacci_number) {
        result = Fibonacci.calculate(nth_fibonacci_number);
    }

    @Then("the Fibonacci result is {long}")
    public void iGetResult(long expected_result) {
        Assert.assertEquals(expected_result, result);
    }
}
