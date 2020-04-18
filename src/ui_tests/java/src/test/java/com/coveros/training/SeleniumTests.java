package com.coveros.training;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import static org.junit.Assert.assertEquals;

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
        driver.get("http://localhost:8080/demo/library.html");
        driver.findElement(By.cssSelector(".button-form:nth-child(4) > input")).click();
        driver.findElement(By.linkText("Return")).click();
        driver.findElement(By.id("register_book")).click();
        driver.findElement(By.id("register_book")).sendKeys("some book");
        driver.findElement(By.id("register_book_submit")).click();
        driver.findElement(By.linkText("Return")).click();
        driver.findElement(By.id("register_borrower")).click();
        driver.findElement(By.id("register_borrower")).sendKeys("some borrower");
        driver.findElement(By.id("register_borrower_submit")).click();
        driver.findElement(By.linkText("Return")).click();
        driver.findElement(By.id("lend_book")).click();
        driver.findElement(By.id("lend_book")).sendKeys("some book");
        driver.findElement(By.id("lend_borrower")).sendKeys("some borrower");
        driver.findElement(By.id("lend_book_submit")).click();
        final String result = driver.findElement(By.id("result")).getText();
        assertEquals("SUCCESS", result);
    }


}
