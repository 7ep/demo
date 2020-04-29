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

public class AckServletTests {

    private final AckServlet ackServlet = Mockito.spy(new AckServlet());
    private final HttpServletRequest request = Mockito.mock(HttpServletRequest.class, RETURNS_DEEP_STUBS);
    private final HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
    private final Logger logger = Mockito.mock(Logger.class);

    /**
     * Testing a happy path, mocking the request, response, and forward.
     */
    @Test
    public void testPostService_HappyPath() {
        when(request.getParameter("ack_param_m")).thenReturn("2");
        when(request.getParameter("ack_param_n")).thenReturn("3");
        when(request.getParameter("ack_algorithm_choice")).thenReturn("regular_recursive");
        doNothing().when(ackServlet).forwardToResult(Mockito.any(), Mockito.any(), Mockito.any());

        ackServlet.doPost(request, response);

        verify(ackServlet).regularRecursive(request, 2, 3);
    }

    /**
     * Testing the tail recursive algorithm
     */
    @Test
    public void testPostService_TailRecursive() {
        when(request.getParameter("ack_param_m")).thenReturn("2");
        when(request.getParameter("ack_param_n")).thenReturn("3");
        when(request.getParameter("ack_algorithm_choice")).thenReturn("tail_recursive");
        doNothing().when(ackServlet).forwardToResult(Mockito.any(), Mockito.any(), Mockito.any());

        ackServlet.doPost(request, response);

        verify(ackServlet).tailRecursive(request, 2, 3);
    }

    /**
     * Here we allow a call into the actual forwardToResult method.
     */
    @Test
    public void testPostService_Forward() {
        AckServlet.logger = logger;

        ackServlet.doPost(request, response);

        verify(request).getRequestDispatcher(ServletUtils.RESTFUL_RESULT_JSP);
        verify(AckServlet.logger, times(0)).error(Mockito.anyString());
    }

    /**
     * Here we allow a call into the actual forwardToResult method,
     * and we force an exception
     */
    @Test
    public void testPostService_realForward_withException() throws ServletException, IOException {
        AckServlet.logger = logger;
        final RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher(ServletUtils.RESTFUL_RESULT_JSP)).thenReturn(requestDispatcher);
        doThrow(new RuntimeException("hi there, exception here."))
                .when(requestDispatcher).forward(request, response);

        ackServlet.doPost(request, response);

        verify(request).getRequestDispatcher(ServletUtils.RESTFUL_RESULT_JSP);
        verify(AckServlet.logger).error(Mockito.anyString());
    }

}
