package com.coveros.training;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;

import java.util.List;

public class RegistrationStepDefs {

    public static final RegistrationResult ALREADY_REGISTERED = RegistrationResult.create(false, RegistrationStatusEnums.ALREADY_REGISTERED.toString());
    String myUsername;
    RegistrationResult myRegistrationResult;
    List<RegistrationResult> resultsList;
    private RegistrationUtils registrationUtils;
    private LoginUtils loginUtils;

    /**
     * create objects for registration and login, and clear the database.
     */
    private void initializeDatabaseAccess() {
        final DatabaseUtils authDb = DatabaseUtils.obtainDatabaseAccess(DatabaseUtils.AUTH_DATABASE_NAME);
        authDb.clearDatabaseContents();
        registrationUtils = new RegistrationUtils(authDb);
        loginUtils = new LoginUtils(authDb);
    }

    // a password used that will suffice as a typical password
    private static String TYPICAL_PASSWORD = "LpcVWwRkWSNVH";

    @Given("^a user \"([^\"]*)\" is not currently registered in the system$")
    public void aUserIsNotCurrentlyRegisteredInTheSystem(String username) {
        initializeDatabaseAccess();
        Assert.assertFalse(userIsRegistered(username));
        myUsername = username;
    }

    @When("^they register with that username and use the password, \"([^\"]*)\"$")
    public void theyRegisterWithThatUsernameAndUseThePassword(String password) {
        registrationUtils.processRegistration(myUsername, password);
    }

    @Then("they become registered")
    public void they_become_registered() {
        Assert.assertTrue(userIsRegistered(myUsername));
    }

    private boolean userIsRegistered(String username) {
        return registrationUtils.isUserInDatabase(username);
    }

    @Given("^a username of \"([^\"]*)\" is registered$")
    public void aUsernameOfIsRegistered(String username) {
        initializeDatabaseAccess();
        registrationUtils.processRegistration(username, TYPICAL_PASSWORD);
        Assert.assertTrue(userIsRegistered(username));
        myUsername = username;
    }

    @When("a user tries to register with that same name")
    public void a_user_tries_to_register_with_that_same_name() {
        myRegistrationResult = registrationUtils.processRegistration(myUsername, TYPICAL_PASSWORD);
    }


    @Then("the system indicates a failure to register")
    public void the_system_indicates_a_failure_to_register() {
        Assert.assertEquals(ALREADY_REGISTERED, myRegistrationResult);
    }

    @When("^they enter their username and provide a poor (.*)$")
    public void theyEnterTheirUsernameAndProvideAPoorPassword(String password) {
        myRegistrationResult = registrationUtils.processRegistration(myUsername, password);
    }

    @Then("^they fail to register and the system indicates the (.*)$")
    public void theyFailToRegisterAndTheSystemIndicatesTheResponse(String response) {
        Assert.assertTrue(myRegistrationResult.toString().toLowerCase().contains(response));
    }
}
