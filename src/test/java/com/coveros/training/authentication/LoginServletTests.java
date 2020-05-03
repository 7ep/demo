package com.coveros.training.authentication;

import com.coveros.training.helpers.ServletUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.mockito.Mockito.*;

/**
 * These tests cover the testing of the endpoint itself, not so much
 * anything it calls.  That's why we mock out everything outside
 * this class.  It makes our tests run very quickly, and enables us to
 * focus with laser-like precision on the functionality this class
 * provides.  Other tests will provide integration testing - but not
 * this one.
 */
public class LoginServletTests {
    public static final String DEFAULT_USERNAME = "alice";
    public static final String DEFAULT_PASSWORD = "abc123";
    private HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
    private HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
    private RequestDispatcher requestDispatcher = Mockito.mock(RequestDispatcher.class);
    private LoginServlet loginServlet = spy(new LoginServlet());

    /**
     * We'll be mocking requests and responses and so on throughout these
     * tests, so just initialize them here for later use.  Notice
     * that we use spy for the LoginServlet.  This specifically allows us to mock
     * out its loginUtils class, but otherwise, all LoginServlet calls
     * are to the real code.  Why mock LoginUtils? So that we can prevent
     * real calls into that class - it's assumed we have already tested
     * that class on its own, we just want to test code specific to this class.
     *
     * LoginServlet uses a static method, {@link ServletUtils#forwardToResult}
     * that has a call to {@link HttpServletRequest#getRequestDispatcher} that
     * we need to mock out.
     */
    @Before
    public void before() {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        requestDispatcher = mock(RequestDispatcher.class);
        loginServlet = spy(new LoginServlet());
        LoginServlet.loginUtils = Mockito.mock(LoginUtils.class);

        // this is always called in a static method, it's boilerplate for these tests.
        when(request.getRequestDispatcher(ServletUtils.RESULT_JSP)).thenReturn(requestDispatcher);
    }

    /**
     * The basic happy path - a username and password are entered
     */
    @Test
    public void testHappyPathPost() {
        // not the greatest password in the world, but we're not testing that
        // at this level - rather, just that the endpoint got a password at all
        setMock_UsernameAndPassword(DEFAULT_USERNAME, DEFAULT_PASSWORD);
        setMock_LoginUtilsUserRegistered(DEFAULT_USERNAME, DEFAULT_PASSWORD, true);

        loginServlet.doPost(request, response);

        verifyExpectedResult("access granted");
    }

    /**
     * If a particular username and password are not registered, login will fail.  access denied.
     */
    @Test
    public void testShouldGetAccessDeniedIfUserNotRegistered() {
        setMock_UsernameAndPassword(DEFAULT_USERNAME, DEFAULT_PASSWORD);
        setMock_LoginUtilsUserRegistered(DEFAULT_USERNAME, DEFAULT_PASSWORD, false);

        loginServlet.doPost(request, response);

        verifyExpectedResult("access denied");
    }

    /**
     * If they pass in an empty string for username, it should return a message
     * indicating that.
     */
    @Test
    public void testEmptyString_Username() {
        // Empty string for the username.
        setMock_UsernameAndPassword("", DEFAULT_PASSWORD);

        loginServlet.doPost(request, response);

        verifyExpectedResult("no username provided");
    }

    /**
     * If they pass in an empty string for password, it should return a message
     * indicating that.
     */
    @Test
    public void testEmptyString_Password() {
        // Empty string for the password.
        setMock_UsernameAndPassword(DEFAULT_USERNAME, "");

        loginServlet.doPost(request, response);

        verifyExpectedResult("no password provided");
    }

    /**
     * Mock out the username and password values on the request, the
     * user's input to this endpoint
     */
    private void setMock_UsernameAndPassword(String username, String password) {
        when(request.getParameter("username")).thenReturn(username);
        when(request.getParameter("password")).thenReturn(password);
    }


    /**
     * Mock out the call and response for LoginUtils, so we can
     * better control how our endpoint will behave in different situations
     * @param username check this if already registered
     * @param password check this if already registered
     * @param expectedReturn what we want the call to return for the purposes of our test
     */
    private void setMock_LoginUtilsUserRegistered(String username, String password, boolean expectedReturn) {
        when(LoginServlet.loginUtils.isUserRegistered(username, password)).thenReturn(expectedReturn);
    }


    /**
     * A wrapper around the mock just to better document intention
     * @param expectedResult what we expect as the bottom-line result,
     *                       which will end up sent to the user as a message
     */
    private void verifyExpectedResult(String expectedResult) {
        Mockito.verify(request).setAttribute("result", expectedResult);
    }
}
