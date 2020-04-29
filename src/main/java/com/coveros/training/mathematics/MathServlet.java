package com.coveros.training.mathematics;

import com.coveros.training.helpers.ServletUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// Multipart config is necessary so that we can properly receive data when
// sending via the "FormData" API.  Using FormData is part of the modern
// API and also allows sending files.
@MultipartConfig
@WebServlet(name = "MathServlet", urlPatterns = {"/math"}, loadOnStartup = 1)
public class MathServlet extends HttpServlet {

    private static final long serialVersionUID = 1766696864489619658L;
    static org.slf4j.Logger logger = LoggerFactory.getLogger(MathServlet.class);

    private int putNumberInRequest(String itemName, HttpServletRequest request) {
        int item = Integer.parseInt(request.getParameter(itemName));
        request.setAttribute(itemName, item);
        return item;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            int itemA = putNumberInRequest("item_a", request);
            int itemB = putNumberInRequest("item_b", request);

            logger.info("received request to add two numbers, {} and {}", itemA, itemB);

            setResultToSum(request, itemA, itemB);
        } catch (NumberFormatException ex) {
            request.setAttribute("result", "Error: only accepts integers");
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
    void setResultToSum(HttpServletRequest request, int itemA, int itemB) {
        final int result = Calculator.add(itemA, itemB);
        request.setAttribute("result", result);
    }

}
