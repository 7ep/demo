package com.coveros.training.authentication;

import com.coveros.training.helpers.ServletUtils;
import com.coveros.training.helpers.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Provides an API endpoint for login.
 * <p>
 * A user (librarian) enters their username and password to authenticate.
 * </p>
 */
@WebServlet(name = "LoginServlet", urlPatterns = {"/login"}, loadOnStartup = 1)
public class LoginServlet extends HttpServlet {

    private static final long serialVersionUID = 7302828964216520580L;
    private static final Logger logger = LoggerFactory.getLogger(LoginServlet.class);
    static LoginUtils loginUtils = new LoginUtils();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        String username = StringUtils.makeNotNullable(request.getParameter("username"));
        request.setAttribute("username", username);

        String password = StringUtils.makeNotNullable(request.getParameter("password"));
        request.setAttribute("password", password);

        String responseText;

        if (username.isEmpty()) {
            responseText = "no username provided";
        } else if (password.isEmpty()) {
            responseText = "no password provided";
        } else {
            logger.info("received request to authenticate a user, {}", username);

            final boolean userRegistered = loginUtils.isUserRegistered(username, password);
            responseText = userRegistered ? "access granted" : "access denied";
        }

        request.setAttribute("result", responseText);
        request.setAttribute("return_page", "library.html");
        ServletUtils.forwardToResult(request, response, logger);
    }

}

