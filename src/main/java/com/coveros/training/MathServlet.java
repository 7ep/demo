package com.coveros.training;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "MathServlet", urlPatterns = {"/math"}, loadOnStartup = 1)
public class MathServlet extends HttpServlet {

  static Logger logger = LogManager.getLogger();

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
    ServletUtils.forwardToResult(request, response, logger);
  }

  /**
   * Wrapping a request set for easier testing and clarity.
   */
  void setResultToSum(HttpServletRequest request, int itemA, int itemB) {
    request.setAttribute("result", doAdd(itemA, itemB));
  }


  private int doAdd(int a, int b) {
    return a + b;
  }
}
