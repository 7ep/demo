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

import static com.coveros.training.library.LibraryBookListAvailableServlet.RESULT;
import static org.mockito.Mockito.*;

public class LibraryBookListAvailableServletTests {

    public static final String A_BOOK = "a book";
    public static final Book DEFAULT_BOOK = new Book(1, A_BOOK);
    private HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
    private HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
    private LibraryBookListAvailableServlet libraryBookListAvailableServlet = spy(new LibraryBookListAvailableServlet());
    private final RequestDispatcher requestDispatcher = Mockito.mock(RequestDispatcher.class);
    private final LibraryUtils libraryUtils = Mockito.mock(LibraryUtils.class);


    @Before
    public void before() {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        libraryBookListAvailableServlet = spy(new LibraryBookListAvailableServlet());
        LibraryBookListAvailableServlet.libraryUtils = this.libraryUtils;
    }

    /**
     * If we don't pass a title or an id, we'll get a list of all books
     */
    @Test
    public void testListAvailableBooks_OneBook() {
        when(request.getRequestDispatcher(ServletUtils.RESTFUL_RESULT_JSP)).thenReturn(requestDispatcher);
        when(libraryUtils.listAvailableBooks()).thenReturn(Arrays.asList(DEFAULT_BOOK));

        // act
        libraryBookListAvailableServlet.doGet(request, response);

        // verify that the correct redirect was chosen.
        verify(request).setAttribute(RESULT, "[{\"Title\": \"a book\", \"Id\": \"1\"}]");
    }

    /**
     * If we don't pass a title or an id, we'll get a list of all books
     */
    @Test
    public void testListAvailableBooks_MultipleBook() {
        when(request.getRequestDispatcher(ServletUtils.RESTFUL_RESULT_JSP)).thenReturn(requestDispatcher);
        when(libraryUtils.listAvailableBooks()).thenReturn(Arrays.asList(DEFAULT_BOOK, DEFAULT_BOOK, DEFAULT_BOOK));

        // act
        libraryBookListAvailableServlet.doGet(request, response);

        // verify that the correct redirect was chosen.
        verify(request).setAttribute(RESULT, "[{\"Title\": \"a book\", \"Id\": \"1\"},{\"Title\": \"a book\", \"Id\": \"1\"},{\"Title\": \"a book\", \"Id\": \"1\"}]");
    }


    /**
     * If we don't pass a title or an id, we'll get a list of all books
     * This tests what happens if there's no books in the database
     */
    @Test
    public void testListAvailableBooks_EmptyList() {
        when(request.getRequestDispatcher(ServletUtils.RESTFUL_RESULT_JSP)).thenReturn(requestDispatcher);

        // act
        libraryBookListAvailableServlet.doGet(request, response);

        // verify that the correct redirect was chosen.
        verify(request).setAttribute(RESULT, "No books exist in the database");
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
        libraryBookListAvailableServlet.doGet(request, response);

        // verify that the correct redirect was chosen.
        verify(request).setAttribute(RESULT, "No books exist in the database");
    }


}
