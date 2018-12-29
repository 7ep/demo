package com.coveros.training;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;

@WebServlet(name = "LoginServlet", urlPatterns = {"/login"}, loadOnStartup = 1)
public class LoginServlet extends HttpServlet {

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

        final Connection connection = PersistenceLayer.createConnection();
        final PersistenceLayer persistenceLayer = new PersistenceLayer(connection);
        final LoginUtils loginUtils = new LoginUtils(persistenceLayer);
        final Boolean userRegistered = loginUtils.isUserRegistered(username, password);
        String responseText = userRegistered ? "access granted" : "access denied";
        request.setAttribute("result", responseText);
        request.getRequestDispatcher("result.jsp").forward(request, response);
    }





}

