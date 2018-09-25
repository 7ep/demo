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
        request.setAttribute("username", username);
        return username;
    }

    private String putPasswordInRequest(HttpServletRequest request) {
        String password = request.getParameter("password");
        if (password == null) request.setAttribute("password", "EMPTY_PASSWORD");
        request.setAttribute("password", password);
        return password;
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            String username = putUsernameInRequest(request);
            String password = putPasswordInRequest(request);

            final RegistrationResult registrationResult = processRegistration(username, password);

            request.setAttribute("result", registrationResult.toString());
            request.getRequestDispatcher("result.jsp").forward(request, response);

        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    protected RegistrationResult processRegistration(String username, String password) {
        final DatabaseUtils authDb = DatabaseUtils.obtainDatabaseAccess(DatabaseUtils.AUTH_DATABASE_NAME);
        final RegistrationUtils registrationUtils = new RegistrationUtils(authDb);
        return registrationUtils.processRegistration(username, password);
    }

}

