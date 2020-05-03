package com.coveros.training.library;

import com.coveros.training.library.domainobjects.Book;
import com.coveros.training.helpers.ServletUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Arrays;

import static com.coveros.training.library.LibraryBookListSearchServlet.RESULT;
import static org.mockito.Mockito.*;

public class LibraryBookListSearchServletTests {

    public static final String A_BOOK = "a book";
    public static final Book DEFAULT_BOOK = new Book(1, A_BOOK);
    private HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
    private HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
    private LibraryBookListSearchServlet libraryBookListSearchServlet = spy(new LibraryBookListSearchServlet());
    private final RequestDispatcher requestDispatcher = Mockito.mock(RequestDispatcher.class);
    private final LibraryUtils libraryUtils = Mockito.mock(LibraryUtils.class);


    @Before
    public void before() {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        libraryBookListSearchServlet = spy(new LibraryBookListSearchServlet());
        LibraryBookListSearchServlet.libraryUtils = this.libraryUtils;
    }

    /**
     * If we don't pass a title or an id, we'll get a list of all books
     */
    @Test
    public void testListAllBooks() {
        when(request.getRequestDispatcher(ServletUtils.RESTFUL_RESULT_JSP)).thenReturn(requestDispatcher);
        when(libraryUtils.listAllBooks()).thenReturn(Arrays.asList(DEFAULT_BOOK));
        // act
        libraryBookListSearchServlet.doGet(request, response);

        // verify that the correct redirect was chosen.
        verify(request).setAttribute(RESULT, "[{\"Title\": \"a book\", \"Id\": \"1\"}]");
    }

    /**
     * If we don't pass a title or an id, we'll get a list of all books
     * this tests when the database is empty of books
     */
    @Test
    public void testListAllBooks_NoBooksInDatabase() {
        when(request.getRequestDispatcher(ServletUtils.RESTFUL_RESULT_JSP)).thenReturn(requestDispatcher);

        // act
        libraryBookListSearchServlet.doGet(request, response);

        // verify that the correct redirect was chosen.
        verify(request).setAttribute(RESULT, "No books exist in the database");
    }

    /**
     * If we pass an id, we'll get a particular book
     * testing for when there's no books found by that id
     */
    @Test
    public void testSearchById_noBookFound() {
        when(request.getRequestDispatcher(ServletUtils.RESTFUL_RESULT_JSP)).thenReturn(requestDispatcher);
        when(request.getParameter("id")).thenReturn("1");
        // the following is just to avoid a null pointer exception when the test succeeds
        when(libraryUtils.searchForBookById(1)).thenReturn(Book.createEmpty());

        // act
        libraryBookListSearchServlet.doGet(request, response);

        // verify that the correct redirect was chosen.
        verify(request).setAttribute(RESULT, "No books found with an id of 1");
    }

    /**
     * If we pass an id, we'll get a particular book
     */
    @Test
    public void testSearchById() {
        when(request.getRequestDispatcher(ServletUtils.RESTFUL_RESULT_JSP)).thenReturn(requestDispatcher);
        when(request.getParameter("id")).thenReturn("1");
        // the following is just to avoid a null pointer exception when the test succeeds
        when(libraryUtils.searchForBookById(1)).thenReturn(DEFAULT_BOOK);

        // act
        libraryBookListSearchServlet.doGet(request, response);

        // verify that the correct redirect was chosen.
        verify(request).setAttribute(RESULT, "[{\"Title\": \"a book\", \"Id\": \"1\"}]");
    }

    /**
     * If we pass a title, we'll get a particular book
     */
    @Test
    public void testSearchByTitle() {
        when(request.getRequestDispatcher(ServletUtils.RESTFUL_RESULT_JSP)).thenReturn(requestDispatcher);
        when(request.getParameter("title")).thenReturn(A_BOOK);
        // the following is just to avoid a null pointer exception when the test succeeds
        when(libraryUtils.searchForBookByTitle(A_BOOK)).thenReturn(DEFAULT_BOOK);

        // act
        libraryBookListSearchServlet.doGet(request, response);

        // verify that the correct redirect was chosen.
        verify(libraryUtils).searchForBookByTitle(A_BOOK);
        verify(request).setAttribute(RESULT, "[{\"Title\": \"a book\", \"Id\": \"1\"}]");
    }

    /**
     * If there aren't any books
     */
    @Test
    public void testSearchNoBooks() {
        when(request.getRequestDispatcher(ServletUtils.RESTFUL_RESULT_JSP)).thenReturn(requestDispatcher);
        when(request.getParameter("id")).thenReturn("");
        when(request.getParameter("title")).thenReturn("");

        // act
        libraryBookListSearchServlet.doGet(request, response);

        // verify that the correct redirect was chosen.
        verify(request).setAttribute(RESULT, "No books exist in the database");
    }

    /**
     * If nothing found by id
     */
    @Test
    public void testSearchNothingFoundById() {
        when(request.getRequestDispatcher(ServletUtils.RESTFUL_RESULT_JSP)).thenReturn(requestDispatcher);
        when(request.getParameter("id")).thenReturn("1");
        // the following is just to avoid a null pointer exception when the test succeeds
        when(libraryUtils.searchForBookById(1)).thenReturn(Book.createEmpty());

        // act
        libraryBookListSearchServlet.doGet(request, response);

        // verify that the correct redirect was chosen.
        verify(request).setAttribute(RESULT, "No books found with an id of 1");
    }

    /**
     * If no books found by title
     */
    @Test
    public void testSearchNothingFoundByTitle() {
        when(request.getRequestDispatcher(ServletUtils.RESTFUL_RESULT_JSP)).thenReturn(requestDispatcher);
        when(request.getParameter("title")).thenReturn(A_BOOK);
        // the following is just to avoid a null pointer exception when the test succeeds
        when(libraryUtils.searchForBookByTitle(A_BOOK)).thenReturn(Book.createEmpty());

        // act
        libraryBookListSearchServlet.doGet(request, response);

        // verify that the correct redirect was chosen.
        verify(libraryUtils).searchForBookByTitle(A_BOOK);
        verify(request).setAttribute(RESULT, "No books found with a title of " + A_BOOK);
    }

    /**
     * If we provide an ID and a title
     */
    @Test
    public void testSearchIdAndTitle() {
        when(request.getRequestDispatcher(ServletUtils.RESTFUL_RESULT_JSP)).thenReturn(requestDispatcher);
        when(request.getParameter("title")).thenReturn(A_BOOK);
        when(request.getParameter("id")).thenReturn("1");

        // act
        libraryBookListSearchServlet.doGet(request, response);

        // verify that the correct redirect was chosen.
        verify(request).setAttribute(RESULT, "Error: please search by either title or id, not both");
    }

}
