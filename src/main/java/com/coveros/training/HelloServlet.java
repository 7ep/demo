package com.coveros.training;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "HelloServlet", urlPatterns = {"hello"}, loadOnStartup = 1) 
public class HelloServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        response.getWriter().print("Hello, World!");  
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException, NumberFormatException {

        String name = request.getParameter("name");
        if (name == null) name = "World";
        request.setAttribute("user", name);

        int item_a = Integer.parseInt(request.getParameter("item_a"));
        request.setAttribute("item_a", item_a);

        int item_b = Integer.parseInt(request.getParameter("item_b"));
        request.setAttribute("item_b", item_b);

        request.setAttribute("sum", doAdd(item_a, item_b));
        request.getRequestDispatcher("response.jsp").forward(request, response); 
    }

   private int doAdd(int a, int b) {
     return a + b;
   }
}
