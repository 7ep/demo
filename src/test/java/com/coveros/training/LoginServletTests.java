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
    private static final String ALICE = "alice";
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
        when(request.getRequestDispatcher(ServletUtils.RESULT_JSP)).thenReturn(requestDispatcher);
        when(request.getParameter("username")).thenReturn("alice");
        when(request.getParameter("password")).thenReturn("abc123");
        when(LoginServlet.loginUtils.isUserRegistered("alice", "abc123")).thenReturn(true);

        loginServlet.doPost(request, response);

        Mockito.verify(request).setAttribute("result", "access granted");
    }

    /**
     * If a particular username and password are not registered, login will fail.  access denied.
     */
    @Test
    public void testShouldGetAccessDeniedIfUserNotRegistered() {
        when(request.getRequestDispatcher(ServletUtils.RESULT_JSP)).thenReturn(requestDispatcher);
        when(request.getParameter("username")).thenReturn("alice");
        when(request.getParameter("password")).thenReturn("abc123");
        when(LoginServlet.loginUtils.isUserRegistered("alice", "abc1234")).thenReturn(true);

        loginServlet.doPost(request, response);

        Mockito.verify(request).setAttribute("result", "access denied");
    }

    /**
     * If they pass in an empty string, it should return a message
     * indicating that.
     */
    @Test
    public void testEmptyString_Username() {
        when(request.getRequestDispatcher(ServletUtils.RESULT_JSP)).thenReturn(requestDispatcher);
        String emptyString = "";
        when(request.getParameter("username")).thenReturn(emptyString);

        // do the post
        loginServlet.doPost(request, response);

        // verify that the missing book title was handled
        Mockito.verify(request).setAttribute("result", "no username provided");
    }

    /**
     * If they pass in an empty string, it should return a message
     * indicating that.
     */
    @Test
    public void testEmptyString_Password() {
        when(request.getRequestDispatcher(ServletUtils.RESULT_JSP)).thenReturn(requestDispatcher);
        String emptyString = "";
        when(request.getParameter("password")).thenReturn(emptyString);
        when(request.getParameter("username")).thenReturn(ALICE);

        // do the post
        loginServlet.doPost(request, response);

        // verify that the missing book title was handled
        Mockito.verify(request).setAttribute("result", "no password provided");
    }
}
