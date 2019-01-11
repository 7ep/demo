package com.coveros.training;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "MathServlet", urlPatterns = {"/math"}, loadOnStartup = 1)
public class MathServlet extends HttpServlet {

  private static final Logger logger = LogManager.getLogger();

  private int putNumberInRequest(String itemName, HttpServletRequest request) {

    int item;
    try {
      item = Integer.parseInt(request.getParameter(itemName));
      request.setAttribute(itemName, item);
    } catch (NumberFormatException ex) {
      item = 0;
    }
    return item;
  }

  protected void doPost(HttpServletRequest request, HttpServletResponse response) {

    int item_a = putNumberInRequest("item_a", request);
    int item_b = putNumberInRequest("item_b", request);

    setResultToSum(request, item_a, item_b);
    try {
      request.getRequestDispatcher("result.jsp").forward(request, response);
    } catch (Exception ex) {
      logger.error("failed during forward: " + ex);
    }
  }

  void setResultToSum(HttpServletRequest request, int item_a, int item_b) {
    request.setAttribute("result", doAdd(item_a, item_b));
  }


  private int doAdd(int a, int b) {
    return a + b;
  }
}
