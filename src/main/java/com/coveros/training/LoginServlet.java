package com.coveros.training;

import com.coveros.training.persistence.LoginUtils;
import com.coveros.training.persistence.RegistrationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "LoginServlet", urlPatterns = {"/login"}, loadOnStartup = 1)
public class LoginServlet extends HttpServlet {

  private static final Logger logger = LoggerFactory.getLogger(RegistrationUtils.class);
  static LoginUtils loginUtils = new LoginUtils();

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

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response) {
    String username = putUsernameInRequest(request);
    String password = putPasswordInRequest(request);

    final Boolean userRegistered = loginUtils.isUserRegistered(username, password);
    String responseText = userRegistered ? "access granted" : "access denied";
    request.setAttribute("result", responseText);
    ServletUtils.forwardToResult(request, response, logger);
  }

}

