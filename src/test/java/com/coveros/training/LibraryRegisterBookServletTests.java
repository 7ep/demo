package com.coveros.training;

import com.coveros.training.domainobjects.LibraryActionResults;
import com.coveros.training.persistence.LibraryUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.mockito.Mockito.*;

public class LibraryRegisterBookServletTests {

  private static final String RESULT_JSP = "result.jsp";
  private HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
  private HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
  private LibraryRegisterBookServlet libraryRegisterBookServlet;


  @Before
  public void before() {
    request = mock(HttpServletRequest.class);
    response = mock(HttpServletResponse.class);
    libraryRegisterBookServlet = spy(new LibraryRegisterBookServlet());
    LibraryRegisterBookServlet.libraryUtils = Mockito.mock(LibraryUtils.class);
  }

  @Test
  public void testHappyPathPost() {
    when(request.getParameter("book")).thenReturn("The DevOps Handbook");
    when(LibraryRegisterBookServlet.libraryUtils.registerBook(Mockito.anyString()))
        .thenReturn(LibraryActionResults.SUCCESS);

    // do the post
    libraryRegisterBookServlet.doPost(request, response);

    // verify that the correct redirect was chosen.
    verify(request).getRequestDispatcher(RESULT_JSP);
  }
}
