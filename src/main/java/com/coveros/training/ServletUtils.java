package com.coveros.training;


import org.slf4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

class ServletUtils {

  private ServletUtils() {
    // using a private constructor to hide the implicit public one.
  }

  static void forwardToResult(HttpServletRequest request, HttpServletResponse response, Logger logger) {
    try {
      request.getRequestDispatcher("result.jsp").forward(request, response);
    } catch (Exception ex) {
      logger.error("failed during forward: " + ex);
    }
  }
}
