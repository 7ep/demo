package com.coveros.training;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

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

    private void type (DomElement input, String text) {
        try {
            ((HtmlTextInput)input).type(text);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Tests the entire process of lending -
     *  registers a book, a borrower, and lends it.
     * See {{@link SeleniumTests#test_shouldLendBook} for a
     * full javascript version that runs on Chrome
     */
    @Test
    public void test_shouldLendBook() {
        long start = System.currentTimeMillis();

        for (int i = 0; i < 10; i++) {
            HtmlPage page = getPage("http://localhost:8080/demo/library.html");
            page = click(page.querySelector(".button-form:nth-child(4) > input"));
            page = click(page.getAnchorByText("Return"));
            page = click(page.getElementById("register_book"));
            type(page.getElementById("register_book"), "some book");
            page = click(page.getElementById("register_book_submit"));
            page = click(page.getAnchorByText("Return"));
            page = click(page.getElementById("register_borrower"));
            type(page.getElementById("register_borrower"), "some borrower");
            page = click(page.getElementById("register_borrower_submit"));
            page = click(page.getAnchorByText("Return"));
            page = click(page.getElementById("lend_book"));
            type(page.getElementById("lend_book"), "some book");
            type(page.getElementById("lend_borrower"), "some borrower");
            page = click(page.getElementById("lend_book_submit"));
            final DomElement result = page.getElementById("result");

            assertEquals("SUCCESS", result.getTextContent());
        }

        long finish = System.currentTimeMillis();
        long timeElapsed = finish - start;
        System.out.println("Time elapsed: " + timeElapsed + " millseconds");
    }




}
