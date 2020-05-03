package com.coveros.training.helpers;


import org.slf4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * These helper methods help to remove duplication in
 * the servlets.  See the methods for more detail on
 * what they do.
 */
public class ServletUtils {

    public static final String RESTFUL_RESULT_JSP = "restfulresult.jsp";
    public static final String RESULT_JSP = "result.jsp";

    private ServletUtils() {
        // using a private constructor to hide the implicit public one.
    }

    /**
     * A user posts information in the regular web application, and we react by forwarding them to another web page.
     * See result.jsp to get an idea.
     */
    public static void forwardToResult(HttpServletRequest request, HttpServletResponse response, Logger logger) {
        try {
            request.getRequestDispatcher(RESULT_JSP).forward(request, response);
        } catch (Exception ex) {
            logger.error(String.format("failed during forward: %s", ex));
        }
    }

    /**
     * A user has made a RESTful web API call, and we are responding with the minimal syntax.
     * See restfulresult.jsp to get an idea.
     */
    public static void forwardToRestfulResult(HttpServletRequest request, HttpServletResponse response, Logger logger) {
        try {
            request.getRequestDispatcher(RESTFUL_RESULT_JSP).forward(request, response);
        } catch (Exception ex) {
            logger.error(String.format("failed during forward: %s", ex));
        }
    }
}
