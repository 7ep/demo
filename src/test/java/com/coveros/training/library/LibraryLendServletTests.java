package com.coveros.training.library;

import com.coveros.training.library.domainobjects.LibraryActionResults;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Date;
import java.time.LocalDate;
import java.time.Month;

import static org.mockito.Mockito.*;

public class LibraryLendServletTests {

    private final static Date BORROW_DATE = Date.valueOf(LocalDate.of(2018, Month.JANUARY, 1));
    private static final String BOOK_TITLE = "The DevOps Handbook";
    private static final String ALICE = "alice";
    private final LibraryLendServlet libraryLendServlet = Mockito.spy(new LibraryLendServlet());
    private final LibraryUtils libraryUtils = Mockito.mock(LibraryUtils.class);
    private final HttpServletRequest request = Mockito.mock(HttpServletRequest.class, RETURNS_DEEP_STUBS);
    private final HttpServletResponse response = Mockito.mock(HttpServletResponse.class);

    @Test
    public void testHappyPathPost() {
        when(request.getParameter("book")).thenReturn(BOOK_TITLE);
        when(request.getParameter("borrower")).thenReturn(ALICE);
        doReturn(BORROW_DATE).when(libraryLendServlet).getDateNow();
        LibraryLendServlet.libraryUtils = libraryUtils;
        when(libraryUtils.lendBook(BOOK_TITLE, ALICE, BORROW_DATE)).thenReturn(LibraryActionResults.SUCCESS);

        libraryLendServlet.doPost(request, response);

        verify(request).setAttribute("result", "SUCCESS");
    }

    @Test
    public void testDateFunction() {
        final Date dateNow = libraryLendServlet.getDateNow();
        Assert.assertNotEquals(dateNow, Date.valueOf(LocalDate.MIN));
        Assert.assertNotEquals(dateNow, Date.valueOf(LocalDate.MAX));
    }

    /**
     * If they pass in an empty string for either field, it should return a message
     * indicating that.
     */
    @Test
    public void testEmptyString_Book() {
        String emptyString = "";
        when(request.getParameter("book")).thenReturn(emptyString);
        when(request.getParameter("borrower")).thenReturn(ALICE);

        // do the post
        libraryLendServlet.doPost(request, response);

        // verify that the missing book title was handled
        Mockito.verify(request).setAttribute("result", "NO_BOOK_TITLE_PROVIDED");
    }

    /**
     * If they pass in an empty string for either field, it should return a message
     * indicating that.
     */
    @Test
    public void testEmptyString_Borrower() {
        String emptyString = "";
        when(request.getParameter("borrower")).thenReturn(emptyString);
        when(request.getParameter("book")).thenReturn(BOOK_TITLE);

        // do the post
        libraryLendServlet.doPost(request, response);

        // verify that the missing borrower was handled
        Mockito.verify(request).setAttribute("result", "NO_BORROWER_PROVIDED");
    }

}
