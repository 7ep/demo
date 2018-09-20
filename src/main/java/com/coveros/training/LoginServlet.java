package com.coveros.training;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;

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
import java.util.StringTokenizer;

import java.nio.charset.StandardCharsets;

@WebServlet(name = "LoginServlet", urlPatterns = {"login"}, loadOnStartup = 1) 
public class LoginServlet extends HttpServlet {

  private static String DATABASE_NAME = "database.txt";

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

        if (isUserRegistered(username, password)) {
          request.getRequestDispatcher("welcome.jsp").forward(request, response); 
        } else {
          request.getRequestDispatcher("not_registered.jsp").forward(request, response); 
        }
    }


   protected boolean isUserRegistered(String username, String password) {
     try {
      File database = new File(DATABASE_NAME);
      try (BufferedReader br = new BufferedReader(new FileReader(database))) {
        String line;
        while ((line = br.readLine()) != null) {
          StringTokenizer st = new StringTokenizer(line);
          while (st.hasMoreTokens()) {
            if (st.nextToken().equals(username) && st.nextToken().equals(password)) {
              return true;
            }
          }
        }
        // if we get to this point, we never found that username
        return false;
      }
     } catch (Exception ex) {
       throw new RuntimeException(ex);
     }
   }


}

