package com.coveros.training;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "AckServlet", urlPatterns = {"/ackermann"}, loadOnStartup = 1)
public class AckServlet extends HttpServlet {

    static Logger logger = LoggerFactory.getLogger(AckServlet.class);

    private int putNumberInRequest(String itemName, HttpServletRequest request) {
        int item = Integer.parseInt(request.getParameter(itemName));
        request.setAttribute(itemName, item);
        return item;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            int ackParamM = putNumberInRequest("ack_param_m", request);
            int ackParamN = putNumberInRequest("ack_param_n", request);

            logger.info("received request to calculate Ackermann's with {} and {}", ackParamM, ackParamN);

            calculate(request, ackParamM, ackParamN);
        } catch (NumberFormatException ex) {
            request.setAttribute("result", "Error: only accepts integers");
        }
        forwardToResult(request, response, logger);
    }

    /**
     * Wrapping a static method call for testing.
     */
    void forwardToResult(HttpServletRequest request, HttpServletResponse response, Logger logger) {
        ServletUtils.forwardToResult(request, response, logger);
    }

    /**
     * Wrapping a request set for easier testing and clarity.
     */
    void calculate(HttpServletRequest request, int itemA, int itemB) {
        final long result = Ackermann.calculate(itemA, itemB);
        logger.info("Ackermann's result is {}", result);
        request.setAttribute("result", result);
    }

}
