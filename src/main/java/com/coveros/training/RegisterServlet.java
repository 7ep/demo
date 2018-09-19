package com.coveros.training;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;

import java.nio.charset.StandardCharsets;

@WebServlet(name = "RegisterServlet", urlPatterns = {"register"}, loadOnStartup = 1) 
public class RegisterServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        response.getWriter().print("Hello, World!");  
    }

    private String putUsernameInRequest(HttpServletRequest request) {
        String username = request.getParameter("username");
        if (username == null) username = "EMPTY_USERNAME";
        request.setAttribute("username", username);
        return username;
    }

    private String putPasswordInRequest(HttpServletRequest request) {
        String password = request.getParameter("password");
        if (password == null) password = "EMPTY_PASSWORD";
        request.setAttribute("password", password);
        return password;
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        String username = putUsernameInRequest(request);
        String password = putPasswordInRequest(request);

        saveToDatabase(username, password);
        request.getRequestDispatcher("registration.jsp").forward(request, response); 
    }

   protected void saveToDatabase(String username, String password) {
      try {
          final Path path = Paths.get("database.txt");
          StandardOpenOption openOption = Files.exists(path) ? 
                       StandardOpenOption.APPEND : 
                       StandardOpenOption.CREATE;
          Files.write(path, 
                      Arrays.asList(username + " " + password), 
                      StandardCharsets.UTF_8,
                      openOption );
      } catch (final IOException ioe) {
          throw new RuntimeException(ioe);
      }
   }
}
