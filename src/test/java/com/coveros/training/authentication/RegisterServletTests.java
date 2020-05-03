package com.coveros.training.authentication;

import com.coveros.training.authentication.domainobjects.RegistrationResult;
import com.coveros.training.authentication.domainobjects.RegistrationStatusEnums;
import com.coveros.training.helpers.ServletUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.mockito.Mockito.*;

public class RegisterServletTests {

    private static final String ALICE = "alice";
    private static final RegistrationResult EMPTY_USERNAME = new RegistrationResult(false, RegistrationStatusEnums.EMPTY_USERNAME);
    private static final RegistrationResult SUCCESSFUL_REGISTRATION = new RegistrationResult(true, RegistrationStatusEnums.SUCCESSFULLY_REGISTERED);
    private HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
    private HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
    private RequestDispatcher requestDispatcher = Mockito.mock(RequestDispatcher.class);
    private RegisterServlet registerServlet = spy(new RegisterServlet());

    @Before
    public void before() {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        requestDispatcher = mock(RequestDispatcher.class);
        registerServlet = spy(new RegisterServlet());
        RegisterServlet.registrationUtils = Mockito.mock(RegistrationUtils.class);
    }

    /**
     * Makes sure that the request dispatcher sends the user to the right place.
     * <p>
     * If the user enters no name when registering, they should be
     * sent to the error page "empty_username.html"
     */
    @Test
    public void doPostWithoutName() {
        // given a user entered an empty string for username
        mockRequestParam("username", "");
        mockRequestParam("password", "");
        mockRegisterUserToReturnSomeResponse(EMPTY_USERNAME);
        mockRequestDispatcherForExpectedRedirection(ServletUtils.RESULT_JSP);

        // do the post
        registerServlet.doPost(request, response);

        // verify that the correct redirect was chosen.
        verify(request).getRequestDispatcher(ServletUtils.RESULT_JSP);
    }

    /**
     * Makes sure that the request dispatcher sends the user to the right place.
     * <p>
     * If the user enters their name when registering, they should be
     * sent to the successful registration page.
     */
    @Test
    public void doPostWithName() {
        // given a user entered their username
        mockRequestParam("username", "Alice");
        mockRequestParam("password", "password123");
        mockRegisterUserToReturnSomeResponse(SUCCESSFUL_REGISTRATION);
        mockRequestDispatcherForExpectedRedirection(ServletUtils.RESULT_JSP);

        // do the post
        registerServlet.doPost(request, response);

        // verify that the correct redirect was chosen.
        verify(request).getRequestDispatcher(ServletUtils.RESULT_JSP);
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
        when(request.getParameter("password")).thenReturn("abc123");

        // do the post
        registerServlet.doPost(request, response);

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
        registerServlet.doPost(request, response);

        // verify that the missing book title was handled
        Mockito.verify(request).setAttribute("result", "no password provided");
    }


    private void mockRegisterUserToReturnSomeResponse(RegistrationResult expectedResult) {
        when(RegisterServlet.registrationUtils.processRegistration(Mockito.anyString(), Mockito.anyString())).thenReturn(expectedResult);
    }

    private void mockRequestDispatcherForExpectedRedirection(String expectedPath) {
        when(request.getRequestDispatcher(expectedPath)).thenReturn(requestDispatcher);
    }

    private void mockRequestParam(String paramName, String name) {
        when(request.getParameter(paramName)).thenReturn(name);
    }


}
