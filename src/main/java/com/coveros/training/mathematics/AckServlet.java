package com.coveros.training.mathematics;

import com.coveros.training.helpers.ServletUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigInteger;

@MultipartConfig
@WebServlet(name = "AckServlet", urlPatterns = {"/ackermann"}, loadOnStartup = 1)
public class AckServlet extends HttpServlet {

    private static final long serialVersionUID = 5669410483481180165L;
    public static final String RESULT = "result";
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
            String algorithm = request.getParameter("ack_algorithm_choice");

            logger.info("received request to calculate Ackermann's with {} and {} and the {} algorithm", ackParamM, ackParamN, algorithm);

            if (algorithm.equals("tail_recursive")) {
                tailRecursive(request, ackParamM, ackParamN);
            } else {
                regularRecursive(request, ackParamM, ackParamN);
            }

        } catch (NumberFormatException ex) {
            request.setAttribute(RESULT, "Error: only accepts integers");
        }
        forwardToResult(request, response, logger);
    }

    /**
     * Wrapping a static method call for testing.
     */
    void forwardToResult(HttpServletRequest request, HttpServletResponse response, Logger logger) {
        ServletUtils.forwardToRestfulResult(request, response, logger);
    }

    /**
     * Wrapping a request set for easier testing and clarity.
     */
    void regularRecursive(HttpServletRequest request, int itemA, int itemB) {
        final BigInteger result = Ackermann.calculate(itemA, itemB);
        logger.info("Ackermann's result is {}", result);
        request.setAttribute(RESULT, result);
    }

    /**
     * Wrapping a request set for easier testing and clarity.
     */
    void tailRecursive(HttpServletRequest request, int itemA, int itemB) {
        final BigInteger result = AckermannIterative.calculate(itemA, itemB);
        logger.info("Ackermann's result is {}", result);
        request.setAttribute(RESULT, result);
    }

}
