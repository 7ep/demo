package com.coveros.training.library;

import com.coveros.training.library.domainobjects.LibraryActionResults;
import com.coveros.training.helpers.ServletUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.mockito.Mockito.*;

public class LibraryRegisterBorrowerServletTests {

    private HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
    private HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
    private LibraryRegisterBorrowerServlet libraryRegisterBorrowerServlet = spy(new LibraryRegisterBorrowerServlet());
    private final RequestDispatcher requestDispatcher = Mockito.mock(RequestDispatcher.class);

    @Before
    public void before() {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        libraryRegisterBorrowerServlet = spy(new LibraryRegisterBorrowerServlet());
        LibraryRegisterBorrowerServlet.libraryUtils = Mockito.mock(LibraryUtils.class);
    }

    @Test
    public void testHappyPathPost() {
        when(request.getRequestDispatcher(ServletUtils.RESULT_JSP)).thenReturn(requestDispatcher);
        when(request.getParameter("borrower")).thenReturn("Alice");
        when(LibraryRegisterBorrowerServlet.libraryUtils.registerBorrower(Mockito.anyString()))
                .thenReturn(LibraryActionResults.SUCCESS);

        // do the post
        libraryRegisterBorrowerServlet.doPost(request, response);

        // verify that the correct redirect was chosen.
        verify(request).getRequestDispatcher(ServletUtils.RESULT_JSP);
    }


    /**
     * If they pass in an empty string, it should return a message
     * indicating that.
     */
    @Test
    public void testEmptyString() {
        when(request.getRequestDispatcher(ServletUtils.RESULT_JSP)).thenReturn(requestDispatcher);
        String emptyString = "";
        when(request.getParameter("borrower")).thenReturn(emptyString);

        // do the post
        libraryRegisterBorrowerServlet.doPost(request, response);

        // verify that the missing borrower name was handled
        Mockito.verify(request).setAttribute("result", "NO_BORROWER_PROVIDED");
    }
}
