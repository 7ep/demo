package com.coveros.training;

import com.coveros.training.domainobjects.LibraryActionResults;
import com.coveros.training.persistence.LibraryUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.mockito.Mockito.*;

public class LibraryRegisterBorrowerServletTests {

  private static final String RESULT_JSP = "result.jsp";
  private HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
  private HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
  private LibraryRegisterBorrowerServlet libraryRegisterBorrowerServlet;
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
    when(request.getRequestDispatcher("result.jsp")).thenReturn(requestDispatcher);
    when(request.getParameter("borrower")).thenReturn("Alice");
    when(LibraryRegisterBorrowerServlet.libraryUtils.registerBorrower(Mockito.anyString()))
        .thenReturn(LibraryActionResults.SUCCESS);

    // do the post
    libraryRegisterBorrowerServlet.doPost(request, response);

    // verify that the correct redirect was chosen.
    verify(request).getRequestDispatcher(RESULT_JSP);
  }
}
