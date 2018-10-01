package com.coveros.training;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.mockito.Mockito.*;

public class RegisterServletTest {

    public static final RegistrationResult EMPTY_USERNAME = RegistrationResult.create(false, RegistrationStatusEnums.EMPTY_USERNAME.toString());
    public static final RegistrationResult SUCCESSFUL_REGISTRATION = RegistrationResult.create(true, RegistrationStatusEnums.SUCCESSFULLY_REGISTERED.toString());
    private HttpServletRequest request;
    private HttpServletResponse response;
    private RequestDispatcher requestDispatcher;
    private RegisterServlet registerServlet;

    @Before
    public void before() {
      request = mock(HttpServletRequest.class);
      response = mock(HttpServletResponse.class);
      requestDispatcher = mock(RequestDispatcher.class);
      registerServlet = spy(new RegisterServlet());
    } 

    /**
     * Makes sure that the request dispatcher sends the user to the right place.
     *
     * If the user enters no name when registering, they should be
     * sent to the error page "empty_username.html"
     */
    @Test
    public void doPostWithoutName() {
        // given a user entered an empty string for username
        mockRequestParam("username", "");
        mockRequestParam("password", "");
        mockRegisterUserToReturnSomeResponse(EMPTY_USERNAME);
        mockRequestDispatcherForExpectedRedirection("result.jsp");

        // do the post
        registerServlet.doPost(request, response);

        // verify that the correct redirect was chosen.
        verify(request).getRequestDispatcher("result.jsp");
    }

    /**
     * Makes sure that the request dispatcher sends the user to the right place.
     *
     * If the user enters their name when registering, they should be
     * sent to the successful registration page.
     */
    @Test
    public void doPostWithName()  {
        // given a user entered their username
        mockRequestParam("username", "Alice");
        mockRequestParam("password", "password123");
        mockRegisterUserToReturnSomeResponse(SUCCESSFUL_REGISTRATION);
        mockRequestDispatcherForExpectedRedirection("result.jsp");

        // do the post
        registerServlet.doPost(request, response);

        // verify that the correct redirect was chosen.
        verify(request).getRequestDispatcher("result.jsp");
    }


    private void mockRegisterUserToReturnSomeResponse(RegistrationResult expectedResult) {
        doReturn(expectedResult).when(registerServlet).processRegistration(Mockito.anyString(), Mockito.anyString());
    }

    private void mockRequestDispatcherForExpectedRedirection(String expectedPath) {
        when(request.getRequestDispatcher(expectedPath)).thenReturn(requestDispatcher);
    }

    private void mockRequestParam(String paramName, String name) {
        when(request.getParameter(paramName)).thenReturn(name);
    }


}
