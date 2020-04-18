package com.coveros.training;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class HtmlUnitTests {

    private WebClient driver;

    @Before
    public void setUp() {
        driver = new WebClient();
        // prevent javascript from running.  We want these tests to really zip.
        driver.getOptions().setJavaScriptEnabled(false);
    }

    @After
    public void tearDown() {
        driver.close();
    }


    private HtmlPage getPage(String url) {
        try {
            return driver.getPage(url);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private HtmlPage click(DomElement button) {
        try {
            return button.click();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private void type(DomElement input, String text) {
        try {
            ((HtmlTextInput) input).type(text);
        } catch (Exception ex) {
            if (ex.getMessage().contains("HtmlPasswordInput cannot be cast to class com.gargoylesoftware.htmlunit.html.HtmlTextInput")) {
                try {
                    ((HtmlPasswordInput) input).type(text);
                } catch (Exception ex1) {
                    throw new RuntimeException(ex1);
                }
            }
        }
    }

    /**
     * Tests the entire process of lending -
     * registers a book, a borrower, and lends it.
     * See {{@link SeleniumTests#test_shouldLendBook} for a
     * full javascript version that runs on Chrome
     */
    @Test
    public void test_shouldLendBook() {
        getPage("http://localhost:8080/demo/flyway");
        HtmlPage page = getPage("http://localhost:8080/demo/library.html");
        type(page.getElementById("register_book"), "some book");
        page = click(page.getElementById("register_book_submit"));
        page = click(page.getAnchorByText("Return"));
        type(page.getElementById("register_borrower"), "some borrower");
        page = click(page.getElementById("register_borrower_submit"));
        page = click(page.getAnchorByText("Return"));
        type(page.getElementById("lend_book"), "some book");
        type(page.getElementById("lend_borrower"), "some borrower");
        page = click(page.getElementById("lend_book_submit"));
        final DomElement result = page.getElementById("result");

        assertEquals("SUCCESS", result.getTextContent());
    }

    /**
     * Testing that the UI for registering and logging in a user (a librarian) works without javascript.
     */
    @Test
    public void test_shouldRegisterAndLoginUser() {
        getPage("http://localhost:8080/demo/flyway");
        HtmlPage page = getPage("http://localhost:8080/demo/library.html");
        type(page.getElementById("register_username"), "some user");
        type(page.getElementById("register_password"), "lksdjfoapsijfasdf");
        page = click(page.getElementById("register_submit"));
        final DomElement registerResult = page.getElementById("result");

        assertTrue("result was " + registerResult.getTextContent(),
                registerResult.getTextContent().contains("status: SUCCESSFULLY_REGISTERED"));

        page = click(page.getAnchorByText("Return"));
        type(page.getElementById("login_username"), "some user");
        type(page.getElementById("login_password"), "lksdjfoapsijfasdf");
        page = click(page.getElementById("login_submit"));
        final DomElement loginResult = page.getElementById("result");

        assertTrue("result was " + loginResult.getTextContent(),
                loginResult.getTextContent().contains("access granted"));
    }

}
