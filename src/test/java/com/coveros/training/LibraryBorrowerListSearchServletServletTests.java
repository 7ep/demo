package com.coveros.training;

import com.coveros.training.domainobjects.Book;
import com.coveros.training.domainobjects.Borrower;
import com.coveros.training.persistence.LibraryUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.mockito.Mockito.*;

public class LibraryBorrowerListSearchServletServletTests {

    private static final String A_BORROWER = "abe borrower";
    private static final Borrower DEFAULT_BORROWER = new Borrower(1, A_BORROWER);
    private HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
    private HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
    private LibraryBorrowerListSearchServlet libraryBorrowerListSearchServlet;
    private final RequestDispatcher requestDispatcher = Mockito.mock(RequestDispatcher.class);
    private LibraryUtils libraryUtils = Mockito.mock(LibraryUtils.class);


    @Before
    public void before() {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        libraryBorrowerListSearchServlet = spy(new LibraryBorrowerListSearchServlet());
        LibraryBorrowerListSearchServlet.libraryUtils = this.libraryUtils;
    }

    /**
     * If we don't pass a name or an id, we'll get a list of all borrowers
     */
    @Test
    public void testListAllBorrowers() {
        when(request.getRequestDispatcher(ServletUtils.RESTFUL_RESULT_JSP)).thenReturn(requestDispatcher);

        // act
        libraryBorrowerListSearchServlet.doGet(request, response);

        // verify that the correct redirect was chosen.
        verify(libraryUtils).listAllBorrowers();
    }

    /**
     * If we pass an id, we'll get a particular borrower
     */
    @Test
    public void testSearchById() {
        when(request.getRequestDispatcher(ServletUtils.RESTFUL_RESULT_JSP)).thenReturn(requestDispatcher);
        when(request.getParameter("id")).thenReturn("1");
        // the following is just to avoid a null pointer exception when the test succeeds
        when(libraryUtils.searchForBorrowerById(1)).thenReturn(Borrower.createEmpty());

        // act
        libraryBorrowerListSearchServlet.doGet(request, response);

        // verify that the correct redirect was chosen.
        verify(libraryUtils).searchForBorrowerById(1);
    }

    /**
     * If we pass a name, we'll get a particular borrower
     */
    @Test
    public void testSearchByName() {
        when(request.getRequestDispatcher(ServletUtils.RESTFUL_RESULT_JSP)).thenReturn(requestDispatcher);
        when(request.getParameter("name")).thenReturn(A_BORROWER);
        // the following is just to avoid a null pointer exception when the test succeeds
        when(libraryUtils.searchForBorrowerByName(A_BORROWER)).thenReturn(Borrower.createEmpty());

        // act
        libraryBorrowerListSearchServlet.doGet(request, response);

        // verify that the correct redirect was chosen.
        verify(libraryUtils).searchForBorrowerByName(A_BORROWER);
    }


    /**
     * If there aren't any borrowers
     */
    @Test
    public void testSearchNoBorrowers() {
        when(request.getRequestDispatcher(ServletUtils.RESTFUL_RESULT_JSP)).thenReturn(requestDispatcher);
        when(request.getParameter("id")).thenReturn("");
        when(request.getParameter("name")).thenReturn("");

        // act
        libraryBorrowerListSearchServlet.doGet(request, response);

        // verify that the correct redirect was chosen.
        verify(request).setAttribute(LibraryBorrowerListSearchServlet.RESULT, "No borrowers exist in the database");
    }

    /**
     * If nothing found by id
     */
    @Test
    public void testSearchNothingFoundById() {
        when(request.getRequestDispatcher(ServletUtils.RESTFUL_RESULT_JSP)).thenReturn(requestDispatcher);
        when(request.getParameter("id")).thenReturn("1");
        // the following is just to avoid a null pointer exception when the test succeeds
        when(libraryUtils.searchForBorrowerById(1)).thenReturn(Borrower.createEmpty());

        // act
        libraryBorrowerListSearchServlet.doGet(request, response);

        // verify that the correct redirect was chosen.
        verify(request).setAttribute(LibraryBorrowerListSearchServlet.RESULT, "No borrowers found with an id of 1");
    }

    /**
     * If no borrowers found by name
     */
    @Test
    public void testSearchNothingFoundByName() {
        when(request.getRequestDispatcher(ServletUtils.RESTFUL_RESULT_JSP)).thenReturn(requestDispatcher);
        when(request.getParameter("name")).thenReturn(A_BORROWER);
        // the following is just to avoid a null pointer exception when the test succeeds
        when(libraryUtils.searchForBorrowerByName(A_BORROWER)).thenReturn(Borrower.createEmpty());

        // act
        libraryBorrowerListSearchServlet.doGet(request, response);

        // verify that the correct redirect was chosen.
        verify(libraryUtils).searchForBorrowerByName(A_BORROWER);
        verify(request).setAttribute(LibraryBorrowerListSearchServlet.RESULT, "No borrowers found with a name of " + A_BORROWER);
    }

    /**
     * If we provide an ID and a name
     */
    @Test
    public void testSearchIdAndName() {
        when(request.getRequestDispatcher(ServletUtils.RESTFUL_RESULT_JSP)).thenReturn(requestDispatcher);
        when(request.getParameter("name")).thenReturn(A_BORROWER);
        when(request.getParameter("id")).thenReturn("1");

        // act
        libraryBorrowerListSearchServlet.doGet(request, response);

        // verify that the correct redirect was chosen.
        verify(request).setAttribute(LibraryBorrowerListSearchServlet.RESULT, "Error: please search by either name or id, not both");
    }

}
