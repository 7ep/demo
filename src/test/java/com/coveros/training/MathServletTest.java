package com.coveros.training;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;

public class MathServletTest {

    private int calculated_total;

    @Given("^my website is running and can do math$")
    public void myWebsiteIsRunningAndCanDoMath() {
        // just a comment.  No state to set up.
    }

    @When("^I add <num(\\d+)> to <num(\\d+)>$")
    public void iAddNumToNum(int num1, int num2) throws Throwable {
        calculated_total = num1 + num2;
    }

    @Then("^the result should be <num(\\d+)>$")
    public void theResultShouldBeTotal(int total) throws Throwable {
        Assert.assertEquals(total, calculated_total);
    }
}
