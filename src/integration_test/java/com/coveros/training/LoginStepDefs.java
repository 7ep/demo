package com.coveros.training;

import com.coveros.training.DatabaseUtils;
import com.coveros.training.LoginUtils;
import com.coveros.training.RegistrationUtils;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;

public class LoginStepDefs {

    private boolean isRegisteredUser;
    private RegistrationUtils registrationUtils = RegistrationUtils.createEmpty();
    private LoginUtils loginUtils = LoginUtils.createEmpty();

    /**
     * create objects for registration and login, and clear the database.
     */
    private void initializeDatabaseAccess() {
        final DatabaseUtils authDb = DatabaseUtils.obtainDatabaseAccess(DatabaseUtils.AUTH_DATABASE_NAME);
        authDb.clearDatabaseContents();
        registrationUtils = new RegistrationUtils(authDb);
        loginUtils = new LoginUtils(authDb);
    }

    @Given("^\"([^\"]*)\" is registered in the system with the password \"([^\"]*)\"$")
    public void isRegisteredInSystemWithPassword(String username, String password) {
        initializeDatabaseAccess();

        registrationUtils.processRegistration(username, password);
    }

    @Then("^The system decides that they are authenticated.$")
    public void theSystemDecidesThatTheyAreAuthenticated() {
        Assert.assertTrue(isRegisteredUser);
    }

    @Then("^The system decides that they are not authenticated, because .*$")
    public void theSystemDecidesThatTheyAreNotAuthenticated() {
        Assert.assertFalse(isRegisteredUser);
    }


    @When("^when a user authenticates with \"([^\"]*)\" and \"([^\"]*)\"$")
    public void whenAUserAuthenticatesWithAnd(String username, String password){
        isRegisteredUser = loginUtils.isUserRegistered(username, password);
    }

}
