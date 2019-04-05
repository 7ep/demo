package com.coveros.training;

import com.coveros.training.domainobjects.RegistrationResult;
import com.coveros.training.persistence.RegistrationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "RegisterServlet", urlPatterns = {"/register"}, loadOnStartup = 1)
public class RegisterServlet extends HttpServlet {

    private static final String PASSWORD_PARAM = "password";
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(RegistrationUtils.class);
    private static final String USERNAME_PARAM = "username";
    private static final String EMPTY_USERNAME = "EMPTY_USERNAME";
    private static final String EMPTY_PASSWORD = "EMPTY_PASSWORD";
    static RegistrationUtils registrationUtils = new RegistrationUtils();

    private String putUsernameInRequest(HttpServletRequest request) {
        String username = request.getParameter(USERNAME_PARAM);
        if (username == null) request.setAttribute(USERNAME_PARAM, EMPTY_USERNAME);
        request.setAttribute(USERNAME_PARAM, username);
        return username;
    }

    private String putPasswordInRequest(HttpServletRequest request) {
        String password = request.getParameter(PASSWORD_PARAM);
        if (password == null) request.setAttribute(PASSWORD_PARAM, EMPTY_PASSWORD);
        request.setAttribute(PASSWORD_PARAM, password);
        return password;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        String username = putUsernameInRequest(request);
        String password = putPasswordInRequest(request);

        // Added because SonarQube was recommending (suggested null could be passed).  Not exactly sure why.
        username = StringUtils.makeNotNullable(username);
        password = StringUtils.makeNotNullable(password);

        logger.info("received request to register a user, {}", username);

        RegistrationResult registrationResult = registrationUtils.processRegistration(username, password);

        request.setAttribute("result", registrationResult.toString());
        forwardToResult(request, response, logger);
    }

    /**
     * Wrapping a static method call for testing.
     */
    private void forwardToResult(HttpServletRequest request, HttpServletResponse response, Logger logger) {
        ServletUtils.forwardToResult(request, response, logger);
    }

}

