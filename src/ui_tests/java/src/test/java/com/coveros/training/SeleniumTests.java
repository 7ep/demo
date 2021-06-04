package com.coveros.training;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.*;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SeleniumTests {
    private static WebDriver driver;

    @BeforeClass
    public static void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
    }

    @AfterClass
    public static void tearDown() {
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

    /**
     * In this case, we're adding no books, so we shouldn't
     * be able to interact with this, it should throw ane exception
     *
     * more detail:
     * Under lending, books and borrowers inputs have three modes.
     * a) if no books/borrowers, lock the input
     * b) If 1 - 9, show a dropdown
     * c) If 10 and up, show an autocomplete
     */
    @Test(expected = org.openqa.selenium.ElementNotInteractableException.class)
    public void test_shouldShowLockedInput() {
        // clear the database...
        driver.get("http://localhost:8080/demo/flyway");

        driver.get("http://localhost:8080/demo/library.html");
        driver.findElement(By.id("lend_book")).sendKeys("some book");
    }

    /**
     * In this case, we have one book and one borrower,
     * so we should get a dropdown
     *
     * more detail:
     * Under lending, books and borrowers inputs have three modes.
     * a) if no books/borrowers, lock the input
     * b) If 1 - 9, show a dropdown
     * c) If 10 and up, show an autocomplete
     */
    @Test
    public void test_shouldShowDropdowns() {
        // clear the database...
        driver.get("http://localhost:8080/demo/flyway");
        ApiCalls.registerBook("some book");
        ApiCalls.registerBorrowers("some borrower");

        driver.get("http://localhost:8080/demo/library.html");

        // using the arrow keys to select an element is a very "dropdown" kind of behavior.
        driver.findElement(By.id("lend_book")).findElement(By.xpath("//option[contains(.,\'some book\')]")).click();
        driver.findElement(By.id("lend_borrower")).findElement(By.xpath("//option[contains(.,\'some borrower\')]")).click();
        driver.findElement(By.id("lend_book_submit")).click();
        final String result = driver.findElement(By.id("result")).getText();
        assertEquals("SUCCESS", result);
    }

    /**
     * In this case, we have 10 books and one borrower,
     * so we should get a autocomplete for books
     *
     * more detail:
     * Under lending, books and borrowers inputs have three modes.
     * a) if no books/borrowers, lock the input
     * b) If 1 - 9, show a dropdown
     * c) If 10 and up, show an autocomplete
     */
    @Test
    public void test_shouldShowAutocomplete() {
        // clear the database...
        driver.get("http://localhost:8080/demo/flyway");
        ApiCalls.registerBook("a");
        ApiCalls.registerBook("b");
        ApiCalls.registerBook("c");
        ApiCalls.registerBook("d");
        ApiCalls.registerBook("e");
        ApiCalls.registerBook("f");
        ApiCalls.registerBook("g");
        ApiCalls.registerBook("h");
        ApiCalls.registerBook("i");
        ApiCalls.registerBook("j");
        ApiCalls.registerBorrowers("some borrower");

        driver.get("http://localhost:8080/demo/library.html");

        // using the arrow keys to select an element is a very "dropdown" kind of behavior.
        driver.findElement(By.id("lend_book")).sendKeys("f");
        driver.findElement(By.xpath("//li[contains(.,\'f\')]")).click();
        driver.findElement(By.id("lend_borrower")).findElement(By.xpath("//option[contains(.,\'some borrower\')]")).click();
        driver.findElement(By.id("lend_book_submit")).click();
        final String result = driver.findElement(By.id("result")).getText();
        assertEquals("SUCCESS", result);
    }

    /**
     * If the value for a book or borrower has a quote in it,
     * single or double, it should continue to work.
     */
    @Test
    public void test_ShouldHandleQuotesInBookOrBorrowerValue() {
        // clear the database...
        driver.get("http://localhost:8080/demo/flyway");
        ApiCalls.registerBook("some \"book");
        ApiCalls.registerBorrowers("some \"borrower");

        driver.get("http://localhost:8080/demo/library.html");

        // using the arrow keys to select an element is a very "dropdown" kind of behavior.
        driver.findElement(By.id("lend_book")).findElement(By.xpath("//option[contains(.,\'some \"book\')]")).click();
        driver.findElement(By.id("lend_borrower")).findElement(By.xpath("//option[contains(.,\'some \"borrower\')]")).click();
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
