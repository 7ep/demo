package com.coveros.training;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import cucumber.api.java.en.When;

public class HelloServletTest {
    @Mock private HttpServletRequest request;
    @Mock private HttpServletResponse response;
    @Mock private RequestDispatcher requestDispatcher;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void test_hello_shouldReturnStaticContent() throws Exception {
        StringWriter stringWriter = mockTheResponseBody(response);

        new HelloServlet().doGet(request, response);

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
        when(request.getRequestDispatcher("response.jsp"))
            .thenReturn(requestDispatcher);

        new HelloServlet().doPost(request, response);

        verify(request).setAttribute("user", "World");
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
        when(request.getParameter("name")).thenReturn("Dolly");
        when(request.getRequestDispatcher("response.jsp"))
            .thenReturn(requestDispatcher);

        new HelloServlet().doPost(request, response);

        verify(request).setAttribute("user", "Dolly");
        verify(requestDispatcher).forward(request,response);
    }


    @When("I run a failing step")
    public void i_run_a_failing_step() {
        // Write code here that turns the phrase above into concrete actions
        throw new RuntimeException();
    }


}
