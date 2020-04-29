package com.coveros.training.mathematics;

import com.coveros.training.helpers.ServletUtils;
import org.junit.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.mockito.Mockito.*;

public class FibServletTests {

    private final FibServlet fibServlet = Mockito.spy(new FibServlet());
    private final HttpServletRequest request = Mockito.mock(HttpServletRequest.class, RETURNS_DEEP_STUBS);
    private final HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
    private final Logger logger = Mockito.mock(Logger.class);

    /**
     * Testing a happy path, mocking the request, response, and forward.
     */
    @Test
    public void testPostService_HappyPath() {
        when(request.getParameter("fib_param_n")).thenReturn("2");
        when(request.getParameter("fib_algorithm_choice")).thenReturn("regular_recursive");
        doNothing().when(fibServlet).forwardToResult(Mockito.any(), Mockito.any(), Mockito.any());

        fibServlet.doPost(request, response);

        verify(fibServlet).defaultRecursiveCalculation(request, 2);
    }

    /**
     * Testing algorithm "tail_recursive 1"
     */
    @Test
    public void testPostService_tailRecursive1() {
        when(request.getParameter("fib_param_n")).thenReturn("2");
        when(request.getParameter("fib_algorithm_choice")).thenReturn("tail_recursive_1");
        doNothing().when(fibServlet).forwardToResult(Mockito.any(), Mockito.any(), Mockito.any());

        fibServlet.doPost(request, response);

        verify(fibServlet).tailRecursiveAlgo1Calc(request, 2);
    }

    /**
     * Testing algorithm "tail_recursive 2"
     */
    @Test
    public void testPostService_tailRecursive2() {
        when(request.getParameter("fib_param_n")).thenReturn("2");
        when(request.getParameter("fib_algorithm_choice")).thenReturn("tail_recursive_2");
        doNothing().when(fibServlet).forwardToResult(Mockito.any(), Mockito.any(), Mockito.any());

        fibServlet.doPost(request, response);

        verify(fibServlet).tailRecursiveAlgo2Calc(request, 2);
    }

    /**
     * Here we allow a call into the actual forwardToResult method.
     */
    @Test
    public void testPostService_Forward() {
        FibServlet.logger = logger;

        fibServlet.doPost(request, response);

        verify(request).getRequestDispatcher(ServletUtils.RESTFUL_RESULT_JSP);
        verify(FibServlet.logger, times(0)).error(Mockito.anyString());
    }

    /**
     * Here we allow a call into the actual forwardToResult method,
     * and we force an exception
     */
    @Test
    public void testPostService_realForward_withException() throws ServletException, IOException {
        FibServlet.logger = logger;
        final RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher(ServletUtils.RESTFUL_RESULT_JSP)).thenReturn(requestDispatcher);
        doThrow(new RuntimeException("hi there, exception here."))
                .when(requestDispatcher).forward(request, response);

        fibServlet.doPost(request, response);

        verify(request).getRequestDispatcher(ServletUtils.RESTFUL_RESULT_JSP);
        verify(FibServlet.logger).error(Mockito.anyString());
    }

}
