package com.coveros.training;

import com.coveros.training.persistence.LoginUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.mockito.Mockito.*;

public class LoginServletTests {
  private static final String RESULT_JSP = "result.jsp";
  private HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
  private HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
  private RequestDispatcher requestDispatcher = Mockito.mock(RequestDispatcher.class);
  private LoginServlet loginServlet;

  @Before
  public void before() {
    request = mock(HttpServletRequest.class);
    response = mock(HttpServletResponse.class);
    requestDispatcher = mock(RequestDispatcher.class);
    loginServlet = spy(new LoginServlet());
    LoginServlet.loginUtils = Mockito.mock(LoginUtils.class);
  }

  @Test
  public void testHappyPathPost() {
    when(request.getRequestDispatcher(RESULT_JSP)).thenReturn(requestDispatcher);
    doReturn("alice").when(loginServlet).putUsernameInRequest(request);
    doReturn("abc123").when(loginServlet).putPasswordInRequest(request);
    when(LoginServlet.loginUtils.isUserRegistered("alice", "abc123")).thenReturn(true);

    loginServlet.doPost(request, response);

    Mockito.verify(request).setAttribute("result", "access granted");
  }
}
