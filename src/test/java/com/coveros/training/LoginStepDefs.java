package com.coveros.training;

import cucumber.api.PendingException;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;

public class LoginStepDefs {

    Boolean isRegisteredUser;

    @Given("^\"([^\"]*)\" is registered in the system with the password \"([^\"]*)\"$")
    public void isRegisteredInSystemWithPassword(String username, String password) {
        DatabaseUtils.destroyDatabase();
        RegistrationUtils.processRegistration(username, password);
    }

    @When("^when a user authenticates with (.*) and (.*)$")
    public void whenUserAuthenticatesWithUsernameAndPassword(String username, String password) {
        isRegisteredUser = LoginUtils.isUserRegistered(username, password);
    }

    @Then("^The system decides that they are authenticated.$")
    public void theSystemDecidesThatTheyAreAuthenticated() {
        Assert.assertTrue(isRegisteredUser);
    }

    @Then("^The system decides that they are not authenticated.$")
    public void theSystemDecidesThatTheyAreNotAuthenticated() {
        Assert.assertFalse(isRegisteredUser);
    }


}
