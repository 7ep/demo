package com.coveros.training;

import com.coveros.training.domainobjects.Book;
import com.coveros.training.domainobjects.LibraryActionResults;
import com.coveros.training.persistence.LibraryUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.mockito.Mockito.*;

public class LibraryBookListSearchServletTests {

    public static final String A_BOOK = "a book";
    private HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
    private HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
    private LibraryBookListSearch libraryBookListSearch;
    private final RequestDispatcher requestDispatcher = Mockito.mock(RequestDispatcher.class);
    private LibraryUtils libraryUtils = Mockito.mock(LibraryUtils.class);


    @Before
    public void before() {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        libraryBookListSearch = spy(new LibraryBookListSearch());
        LibraryBookListSearch.libraryUtils = this.libraryUtils;
    }

    /**
     * If we don't pass a title or an id, we'll get a list of all books
     */
    @Test
    public void testListAllBooks() {
        when(request.getRequestDispatcher(ServletUtils.RESTFUL_RESULT_JSP)).thenReturn(requestDispatcher);

        // act
        libraryBookListSearch.doGet(request, response);

        // verify that the correct redirect was chosen.
        verify(libraryUtils).listAllBooks();
    }

    /**
     * If we pass an id, we'll get a particular book
     */
    @Test
    public void testSearchById() {
        when(request.getRequestDispatcher(ServletUtils.RESTFUL_RESULT_JSP)).thenReturn(requestDispatcher);
        when(request.getParameter("id")).thenReturn("1");
        // the following is just to avoid a null pointer exception when the test succeeds
        when(libraryUtils.searchForBookById(1)).thenReturn(Book.createEmpty());

        // act
        libraryBookListSearch.doGet(request, response);

        // verify that the correct redirect was chosen.
        verify(libraryUtils).searchForBookById(1);
    }

    /**
     * If we pass a title, we'll get a particular book
     */
    @Test
    public void testSearchByTitle() {
        when(request.getRequestDispatcher(ServletUtils.RESTFUL_RESULT_JSP)).thenReturn(requestDispatcher);
        when(request.getParameter("title")).thenReturn(A_BOOK);
        // the following is just to avoid a null pointer exception when the test succeeds
        when(libraryUtils.searchForBookByTitle(A_BOOK)).thenReturn(Book.createEmpty());

        // act
        libraryBookListSearch.doGet(request, response);

        // verify that the correct redirect was chosen.
        verify(libraryUtils).searchForBookByTitle(A_BOOK);
    }

}
