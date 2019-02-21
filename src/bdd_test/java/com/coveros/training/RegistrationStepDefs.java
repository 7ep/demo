package com.coveros.training;

import com.coveros.training.domainobjects.PasswordResult;
import com.coveros.training.domainobjects.RegistrationResult;
import com.coveros.training.domainobjects.RegistrationStatusEnums;
import com.coveros.training.persistence.PersistenceLayer;
import com.coveros.training.persistence.RegistrationUtils;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;

public class RegistrationStepDefs {

    private static final RegistrationResult ALREADY_REGISTERED = new RegistrationResult(false, RegistrationStatusEnums.ALREADY_REGISTERED);
    private String myUsername = "";
    private RegistrationResult myRegistrationResult = RegistrationResult.createEmpty();
    private RegistrationUtils registrationUtils = RegistrationUtils.createEmpty();
    private PasswordResult passwordResult = PasswordResult.createEmpty();
    private PersistenceLayer pl = new PersistenceLayer();

    /**
     * create objects for registration and login, and clear the database.
     */
    private void initializeDatabaseAccess() {
        pl.cleanAndMigrateDatabase();
        registrationUtils = new RegistrationUtils();
    }

    // a password used that will suffice as a typical password
    private final static String TYPICAL_PASSWORD = "LpcVWwRkWSNVH";

    @Given("^a user \"([^\"]*)\" is not currently registered in the system$")
    public void aUserIsNotCurrentlyRegisteredInTheSystem(String username) {
        initializeDatabaseAccess();
        Assert.assertFalse(userIsRegistered(username));
        myUsername = username;
    }

    @When("^they register with that username and use the password \"([^\"]*)\"$")
    public void theyRegisterWithThatUsernameAndUseThePassword(String pw) {
        registrationUtils.processRegistration(myUsername, pw);
    }

    @Then("they become registered")
    public void they_become_registered() {
        registrationUtils.isUserInDatabase(myUsername);
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

    @When("^they enter their username and provide a poor password of (.*)$")
    public void theyEnterTheirUsernameAndProvideAPoorPassword(String password) {
        myRegistrationResult = registrationUtils.processRegistration(myUsername, password);
    }

    @Then("^they fail to register and the system indicates a response: (.*)$")
    public void theyFailToRegisterAndTheSystemIndicatesAResponse(String response) {
        Assert.assertTrue(myRegistrationResult.toString()
                .toLowerCase()
                .replace("_", " ")
                .contains(response));
    }

    @Given("^a user is in the midst of registering for an account$")
    public void aUserIsInTheMidstOfRegisteringForAnAccount() {
        // just a comment.  No state needs to be set up.
    }

    @When("^they try registering with the password (.*)$")
    public void theyTryRegisteringWithThePasswordPassword(String password) {
        passwordResult = RegistrationUtils.isPasswordGood(password);
    }

    @Then("^the system returns that the password has insufficient entropy, taking this long to crack: (.*)$")
    public void theSystemReturnsThatThePasswordHasInsufficientEntropyTakingThisLongToCrackTime_to_crack(String timeToCrack) {
        Assert.assertEquals(timeToCrack, passwordResult.timeToCrackOffline);
    }
}
