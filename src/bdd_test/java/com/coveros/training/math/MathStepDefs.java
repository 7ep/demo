package com.coveros.training.math;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;

public class MathStepDefs {

    private int calculated_total;

    @Given("^my website is running and can do math$")
    public void myWebsiteIsRunningAndCanDoMath() {
        // just a comment.  No state to set up.
    }

    @When("I add {int} to {int}")
    public void i_add_to(int num1, int num2) {
        calculated_total = num1 + num2;
    }

    @Then("the result should be {int}")
    public void the_result_should_be(int total) {
        Assert.assertEquals(total, calculated_total);
    }

}
