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

public class MathServletTests {

    private final MathServlet mathServlet = Mockito.spy(new MathServlet());
    private final HttpServletRequest request = Mockito.mock(HttpServletRequest.class, RETURNS_DEEP_STUBS);
    private final HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
    private final Logger logger = Mockito.mock(Logger.class);

    /**
     * Testing a happy path, mocking the request, response, and forward.
     */
    @Test
    public void testPostService_HappyPath() {
        when(request.getParameter("item_a")).thenReturn("2");
        when(request.getParameter("item_b")).thenReturn("3");
        doNothing().when(mathServlet).forwardToResult(Mockito.any(), Mockito.any(), Mockito.any());

        mathServlet.doPost(request, response);

        verify(mathServlet).setResultToSum(request, 2, 3);
    }

    /**
     * Here we allow a call into the actual forwardToResult method.
     */
    @Test
    public void testPostService_Forward() {
        MathServlet.logger = logger;

        mathServlet.doPost(request, response);

        verify(request).getRequestDispatcher(ServletUtils.RESTFUL_RESULT_JSP);
        verify(MathServlet.logger, times(0)).error(Mockito.anyString());
    }

    /**
     * Here we allow a call into the actual forwardToResult method,
     * and we force an exception
     */
    @Test
    public void testPostService_realForward_withException() throws ServletException, IOException {
        MathServlet.logger = logger;
        final RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher(ServletUtils.RESTFUL_RESULT_JSP)).thenReturn(requestDispatcher);
        doThrow(new RuntimeException("hi there, exception here."))
                .when(requestDispatcher).forward(request, response);

        mathServlet.doPost(request, response);

        verify(request).getRequestDispatcher(ServletUtils.RESTFUL_RESULT_JSP);
        verify(MathServlet.logger).error(Mockito.anyString());
    }

}
