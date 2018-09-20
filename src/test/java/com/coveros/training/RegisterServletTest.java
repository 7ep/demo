package com.coveros.training;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.api.PendingException;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;


import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import cucumber.api.java.en.When;

public class RegisterServletTest {
    private HttpServletRequest request;
    private HttpServletResponse response;
    private RequestDispatcher requestDispatcher;
    private RegisterServlet registerServlet;

    @Before
    public void before() {
      request = mock(HttpServletRequest.class);
      response = mock(HttpServletResponse.class);
      requestDispatcher = mock(RequestDispatcher.class);
      registerServlet = spy(RegisterServlet.class);
    } 

    @Test
    public void test_hello_shouldReturnStaticContent() throws Exception {
        StringWriter stringWriter = mockTheResponseBody(response);

        new RegisterServlet().doGet(request, response);

        assertEquals("Hello, World!", stringWriter.toString());
    }

    /**
      * helps make the test language a bit cleaner.  Mocks the
      * response body
      */  
    private StringWriter mockTheResponseBody(HttpServletResponse response) 
        throws Exception {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);

        when(response.getWriter()).thenReturn(printWriter);
        return stringWriter;
    }

    /**
     * Makes sure that the request dispatcher sends
     * what we expect to the user
     *
     * This test checks what happens if the user entered
     * no text for the name.
     */
    @Test
    public void doPostWithoutName() throws Exception {
        when(request.getRequestDispatcher("registration.jsp"))
            .thenReturn(requestDispatcher);
        when(registerServlet.isUserInDatabase("EMPTY_USERNAME")).thenReturn(false);
        doNothing().when(registerServlet).saveToDatabase("EMPTY_USERNAME", "");

        new RegisterServlet().doPost(request, response);

        verify(request).setAttribute("username", "EMPTY_USERNAME");
        verify(requestDispatcher).forward(request,response);
    }

    /**
     * Makes sure that the request dispatcher sends
     * what we expect to the user
     *
     * This test checks what happens if the user entered
     * the text "Dolly" for the name.
     */
    @Test
    public void doPostWithName() throws Exception {
        when(request.getParameter("username")).thenReturn("Dolly");
        when(request.getRequestDispatcher("registration.jsp"))
            .thenReturn(requestDispatcher);
        when(registerServlet.isUserInDatabase("Dolly")).thenReturn(false);
        doNothing().when(registerServlet).saveToDatabase("Dolly", "");

        registerServlet.doPost(request, response);

        verify(request).setAttribute("username", "Dolly");
        verify(requestDispatcher).forward(request,response);
    }


    @Given("that I am not currently registered in the system")
    public void that_I_am_not_currently_registered_in_the_system() {
        // Write code here that turns the phrase above into concrete actions
    }

    @When("I register with these names and passwords:")
    public void i_register_with_these_names_and_passwords(List<List<String>> dataTable) {
        // Write code here that turns the phrase above into concrete actions
        // For automatic transformation, change DataTable to one of
        // List<E>, List<List<E>>, List<Map<K,V>>, Map<K,V> or
        // Map<K, List<V>>. E,K,V must be a String, Integer, Float,
        // Double, Byte Short, Long, BigInteger or BigDecimal.
        //
        // For other transformations you can register a DataTableType.
        throw new PendingException();
    }

    @Then("a new account is created for me")
    public void a_new_account_is_created_for_me() {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }




}
