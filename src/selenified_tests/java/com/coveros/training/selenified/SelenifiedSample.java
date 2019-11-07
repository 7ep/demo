package com.coveros.training.selenified;

import com.coveros.selenified.Locator;
import com.coveros.selenified.Selenified;
import com.coveros.selenified.application.App;
import com.coveros.selenified.element.Element;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.ITestContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class SelenifiedSample extends Selenified {

    static final String BASE_URL =  "http://localhost:8080/demo/";
    static final String LIBRARY_URL = BASE_URL + "library.html";
    static final String RESET_DATABASE_URL = BASE_URL + "flyway";

    @BeforeClass(alwaysRun = true)
    public void beforeClass(ITestContext test) {
        // set the base URL for the tests here
        setAppURL(this, test, LIBRARY_URL);
    }

    @Test(groups = {"sample"}, description = "Check that the title on the page is as expected")
    public void sampleTest() {
        // use this object to manipulate the app
        App app = this.apps.get();
        // verify the correct page title
        app.azzert().titleEquals("Library");
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
        app.goToURL(LIBRARY_URL);

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

    /**
     *  This is a test to do whatever blah blah blah
     *
     */
    @Test
    public void sampleTest3() {
        // arrange
        App app = this.apps.get();

        final WebDriver driver = app.getDriver();
        String username = "someuser";
        String password = "passworsdosjfasldf";

        app.goToURL(LIBRARY_URL);

        final WebElement login_username = driver.findElement(By.id("login_username"));
        login_username.sendKeys(username);

        final WebElement login_password = driver.findElement(By.id("login_password"));
        login_password.sendKeys(password);

        // act
        final WebElement login_submit = driver.findElement(By.id("login_submit"));
        login_submit.click();

        // assert
        final WebElement result = driver.findElement(By.id("result"));
        Assert.assertEquals("access denied", result.getText());
    }

    /**
     * Testing out logging in with invalid credentials
     *
     * This uses basic Selenium, not Selenified.
     */
    @Test
    public void sampleTest4() {
        App app = this.apps.get();
        final WebDriver driver = app.getDriver();
        String username = "someuser";
        String password = "passworsdosjfasldf";

        app.goToURL(LIBRARY_URL);

        final WebElement register_username = driver.findElement(By.id("register_username"));
        register_username.sendKeys(username);

        final WebElement register_password = driver.findElement(By.id("register_password"));
        register_password.sendKeys(password);

        final WebElement register_submit = driver.findElement(By.id("register_submit"));
        register_submit.click();

        driver.get(LIBRARY_URL);

        final WebElement login_username = driver.findElement(By.id("login_username"));
        login_username.sendKeys(username);

        final WebElement login_password = driver.findElement(By.id("login_password"));
        login_password.sendKeys(password);

        final WebElement login_submit = driver.findElement(By.id("login_submit"));
        login_submit.click();

        final WebElement result = driver.findElement(By.id("result"));
        Assert.assertEquals("access granted", result.getText());

        finish();
    }

}







  