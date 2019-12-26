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

    @When("I add (.*) to (.*)")
    public void i_add_to(String num1, String num2) {
        // Write code here that turns the phrase above into concrete actions
        calculated_total = Integer.parseInt(num1) + Integer.parseInt(num2);
    }

    @Then("the result should be (.*)")
    public void the_result_should_be(String total) {
        Assert.assertEquals(Integer.parseInt(total), calculated_total);
    }

}
