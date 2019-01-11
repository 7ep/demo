package com.coveros.training;

import com.coveros.training.domainobjects.RegistrationResult;
import com.coveros.training.persistence.PersistenceLayer;
import com.coveros.training.persistence.RegistrationUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "RegisterServlet", urlPatterns = {"/register"}, loadOnStartup = 1)
public class RegisterServlet extends HttpServlet {

    private static final Logger logger = LogManager.getLogger();

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
            String username = putUsernameInRequest(request);
            String password = putPasswordInRequest(request);

            RegistrationResult registrationResult = processRegistration(username, password);

            request.setAttribute("result", registrationResult.toString());
        try {
            request.getRequestDispatcher("result.jsp").forward(request, response);
        } catch (Exception ex) {
            logger.error("failed during forward: " + ex);
        }
    }

    RegistrationResult processRegistration(String username, String password) {
        final PersistenceLayer persistenceLayer = new PersistenceLayer();
        final RegistrationUtils registrationUtils = new RegistrationUtils(persistenceLayer);
        return registrationUtils.processRegistration(username, password);
    }

}

