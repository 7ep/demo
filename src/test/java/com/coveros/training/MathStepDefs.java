package com.coveros.training;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
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
        calculated_total = Integer.valueOf(num1) + Integer.valueOf(num2);
    }

    @Then("the result should be (.*)")
    public void the_result_should_be(String total) {
        Assert.assertEquals((int)Integer.valueOf(total), calculated_total);
    }

}
