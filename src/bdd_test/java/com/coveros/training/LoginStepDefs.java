package com.coveros.training;

import com.coveros.training.persistence.LoginUtils;
import com.coveros.training.persistence.PersistenceLayer;
import com.coveros.training.persistence.RegistrationUtils;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;

import java.sql.Connection;

public class LoginStepDefs {

    private boolean isRegisteredUser;
    private RegistrationUtils registrationUtils = RegistrationUtils.createEmpty();
    private LoginUtils loginUtils = LoginUtils.createEmpty();
    private PersistenceLayer persistenceLayer;

    /**
     * create objects for registration and login, and clear the database.
     */
    private void initializeDatabaseAccess() {
        persistenceLayer = new PersistenceLayer();
        registrationUtils = new RegistrationUtils(persistenceLayer);
        loginUtils = new LoginUtils(persistenceLayer);
    }

    @Given("^\"([^\"]*)\" is registered in the system with the password \"([^\"]*)\"$")
    public void isRegisteredInSystemWithPassword(String username, String password) {
        initializeDatabaseAccess();

        registrationUtils.processRegistration(username, password);
    }

    @Then("^The system decides that they are authenticated.$")
    public void theSystemDecidesThatTheyAreAuthenticated() {
        Assert.assertTrue(isRegisteredUser);
        persistenceLayer.close();
    }

    @Then("^The system decides that they are not authenticated, because .*$")
    public void theSystemDecidesThatTheyAreNotAuthenticated() {
        Assert.assertFalse(isRegisteredUser);
        persistenceLayer.close();
    }


    @When("^when a user authenticates with \"([^\"]*)\" and \"([^\"]*)\"$")
    public void whenAUserAuthenticatesWithAnd(String username, String password){
        isRegisteredUser = loginUtils.isUserRegistered(username, password);
    }

}
