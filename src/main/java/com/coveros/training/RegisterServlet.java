package com.coveros.training;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "RegisterServlet", urlPatterns = {"register"}, loadOnStartup = 1)
public class RegisterServlet extends HttpServlet {

    private String putUsernameInRequest(HttpServletRequest request) {
        String username = request.getParameter("username");
        if (username == null) request.setAttribute("username", "EMPTY_USERNAME");
        return username;
    }

    private String putPasswordInRequest(HttpServletRequest request) {
        String password = request.getParameter("password");
        if (password == null) request.setAttribute("password", "EMPTY_PASSWORD");
        return password;
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            String username = putUsernameInRequest(request);
            String password = putPasswordInRequest(request);

            final RegistrationResult registrationResult = processRegistration(username, password);

            switch (registrationResult) {
                case ALREADY_REGISTERED:
                    request.getRequestDispatcher("already_registered.jsp").forward(request, response);
                    break;
                case SUCCESSFUL_REGISTRATION:
                    request.getRequestDispatcher("registration.jsp").forward(request, response);
                    break;
                case EMPTY_USERNAME:
                    request.getRequestDispatcher("empty_username.html").forward(request, response);
                    break;
                case PASSWORD_BAD:
                    request.getRequestDispatcher("bad_password.html").forward(request, response);
                    break;
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    protected RegistrationResult processRegistration(String username, String password) {
        return RegistrationUtils.processRegistration(username, password);
    }

}

