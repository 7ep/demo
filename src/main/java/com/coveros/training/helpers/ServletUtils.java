package com.coveros.training.helpers;


import org.slf4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ServletUtils {

    public static final String RESTFUL_RESULT_JSP = "restfulresult.jsp";
    public static final String RESULT_JSP = "result.jsp";

    private ServletUtils() {
        // using a private constructor to hide the implicit public one.
    }

    public static void forwardToResult(HttpServletRequest request, HttpServletResponse response, Logger logger) {
        try {
            request.getRequestDispatcher(RESULT_JSP).forward(request, response);
        } catch (Exception ex) {
            logger.error(String.format("failed during forward: %s", ex));
        }
    }

    public static void forwardToRestfulResult(HttpServletRequest request, HttpServletResponse response, Logger logger) {
        try {
            request.getRequestDispatcher(RESTFUL_RESULT_JSP).forward(request, response);
        } catch (Exception ex) {
            logger.error(String.format("failed during forward: %s", ex));
        }
    }
}
