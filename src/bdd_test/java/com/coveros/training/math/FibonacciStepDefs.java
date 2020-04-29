package com.coveros.training.math;

import com.coveros.training.mathematics.Fibonacci;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;

public class FibonacciStepDefs {

    private long result;

    @When("I calculate the {int} Fibonacci number")
    public void i_calculate_the(Integer nth_fibonacci_number) {
        result = Fibonacci.calculate(nth_fibonacci_number);
    }

    @Then("the Fibonacci result is {long}")
    public void iGetResult(long expected_result) {
        Assert.assertEquals(expected_result, result);
    }
}
