package com.coveros.training;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;

public class RegistrationStepDefs {

    String myUsername;
    RegistrationResult myRegistrationResult;

    // a password used that will suffice as a typical password
    private static String TYPICAL_PASSWORD = "typical_password_123";

    @Before
    public void beforeEachScenario() {
        DatabaseUtils.destroyDatabase();
    }

    @After
    public void afterEachScenario() {
        DatabaseUtils.destroyDatabase();
    }

    @Given("^a user \"([^\"]*)\" is not currently registered in the system$")
    public void aUserIsNotCurrentlyRegisteredInTheSystem(String username) {
        Assert.assertFalse(userIsRegistered(username));
        myUsername = username;
    }

    @When("^they register with that username and use the password, \"([^\"]*)\"$")
    public void theyRegisterWithThatUsernameAndUseThePassword(String password) {
        RegistrationUtils.processRegistration(myUsername, password);
    }

    @Then("they become registered")
    public void they_become_registered() {
        Assert.assertTrue(userIsRegistered(myUsername));
    }

    private boolean userIsRegistered(String username) {
        return RegistrationUtils.isUserInDatabase(username);
    }

    @Given("^a username of \"([^\"]*)\" is registered$")
    public void aUsernameOfIsRegistered(String username) {
        RegistrationUtils.processRegistration(username, TYPICAL_PASSWORD);
        Assert.assertTrue(userIsRegistered(username));
        myUsername = username;
    }

    @When("a user tries to register with that same name")
    public void a_user_tries_to_register_with_that_same_name() {
        myRegistrationResult = RegistrationUtils.processRegistration(myUsername, TYPICAL_PASSWORD);
    }


    @Then("the system indicates a failure to register")
    public void the_system_indicates_a_failure_to_register() {
        Assert.assertEquals(RegistrationResult.ALREADY_REGISTERED, myRegistrationResult);
    }


}
