package com.coveros.training;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SeleniumTests {
    private WebDriver driver;

    @Before
    public void setUp() {
        driver = new ChromeDriver();
    }

    @After
    public void tearDown() {
        driver.quit();
    }

    /**
     * Tests the entire process of lending -
     * registers a book, a borrower, and lends it.
     * See {{@link HtmlUnitTests#test_shouldLendBook} for a
     * non-javascript, headless version that runs on HtmlUnit
     */
    @Test
    public void test_shouldLendBook() {
        driver.get("http://localhost:8080/demo/flyway");
        driver.get("http://localhost:8080/demo/library.html");
        driver.findElement(By.id("register_book")).sendKeys("some book");
        driver.findElement(By.id("register_book_submit")).click();
        driver.findElement(By.linkText("Return")).click();
        driver.findElement(By.id("register_borrower")).sendKeys("some borrower");
        driver.findElement(By.id("register_borrower_submit")).click();
        driver.findElement(By.linkText("Return")).click();
        driver.findElement(By.id("lend_book")).sendKeys("some book");
        driver.findElement(By.id("lend_borrower")).sendKeys("some borrower");
        driver.findElement(By.id("lend_book_submit")).click();
        final String result = driver.findElement(By.id("result")).getText();
        assertEquals("SUCCESS", result);
    }

    @Test
    public void test_ShouldRegisterAndLoginUser() {
        driver.get("http://localhost:8080/demo/flyway");
        driver.get("http://localhost:8080/demo/library.html");
        driver.findElement(By.id("register_username")).sendKeys("some user");
        driver.findElement(By.id("register_password")).sendKeys("lasdfj;alsdkfjasdf");
        driver.findElement(By.id("register_submit")).click();

        final String registerResult = driver.findElement(By.id("result")).getText();
        assertTrue("result was " + registerResult,
                registerResult.contains("status: SUCCESSFULLY_REGISTERED"));

        driver.findElement(By.linkText("Return")).click();
        driver.findElement(By.id("login_username")).sendKeys("some user");
        driver.findElement(By.id("login_password")).sendKeys("lasdfj;alsdkfjasdf");
        driver.findElement(By.id("login_submit")).click();

        final String loginResult = driver.findElement(By.id("result")).getText();
        assertTrue("result was " + loginResult,
                loginResult.contains("access granted"));

    }


}
