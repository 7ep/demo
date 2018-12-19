package com.coveros.training;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "MathServlet", urlPatterns = {"math"}, loadOnStartup = 1) 
public class MathServlet extends HttpServlet {

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

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

        int item_a = putNumberInRequest("item_a", request);
        int item_b = putNumberInRequest("item_b", request);

        request.setAttribute("result", doAdd(item_a, item_b));
        request.getRequestDispatcher("result.jsp").forward(request, response);
    }


   private int doAdd(int a, int b) {
     return a + b;
   }
}
