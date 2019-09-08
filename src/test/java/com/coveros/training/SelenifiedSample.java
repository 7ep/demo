package com.coveros.training;

import com.coveros.selenified.Locator;
import com.coveros.selenified.Selenified;
import com.coveros.selenified.application.App;
import com.coveros.selenified.element.Element;
import org.testng.ITestContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class SelenifiedSample extends Selenified {

    static final String BASE_URL =  "http://localhost:8080/demo/";
    static final String RESET_DATABASE_URL = BASE_URL + "flyway";

    @BeforeClass(alwaysRun = true)
    public void beforeClass(ITestContext test) {
        // set the base URL for the tests here
        setAppURL(this, test, BASE_URL);
    }

    @Test(groups = {"sample"}, description = "Check that the title on the page is as expected")
    public void sampleTest() {
        // use this object to manipulate the app
        App app = this.apps.get();
        // verify the correct page title
        app.azzert().titleEquals("Web Demo");
        // verify no issues
        finish();
    }

    @Test(groups = {"sample"}, description = "Make sure we can successfully register a user")
    public void sampleTest2() {
        String username = "fakeuser";
        String password = "asdfpoiasefaslfaje";

        // use this object to manipulate the app
        App app = this.apps.get();

        app.goToURL(RESET_DATABASE_URL);
        app.goToURL(BASE_URL);

        // find the register user field and enter a username to register
        Element register_username = app.newElement(Locator.ID, "register_username");
        register_username.type(username);

        // find the register password field and enter a password
        Element register_password = app.newElement(Locator.ID, "register_password");
        register_password.type(password);

        // click to register the user
        Element register_submit = app.newElement(Locator.ID, "register_submit");
        register_submit.click();

        // assert we find the proper response in the result
        app.azzert().textPresent("successfully registered: true");

        // verify no issues
        finish();
    }
}

