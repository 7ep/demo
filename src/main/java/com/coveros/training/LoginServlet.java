package com.coveros.training;

import com.coveros.training.persistence.LoginUtils;
import com.coveros.training.persistence.PersistenceLayer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "LoginServlet", urlPatterns = {"/login"}, loadOnStartup = 1)
public class LoginServlet extends HttpServlet {

  private static final Logger logger = LogManager.getLogger();
  static LoginUtils loginUtils = new LoginUtils();

  String putUsernameInRequest(HttpServletRequest request) {
    String username = request.getParameter("username");
    if (username == null) username = "EMPTY_USERNAME";
    request.setAttribute("username", username);
    return username;
  }

  String putPasswordInRequest(HttpServletRequest request) {
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
    forwardToResult(request, response, logger);
  }

  /**
   * Wrapping a static method call for testing.
   */
  void forwardToResult(HttpServletRequest request, HttpServletResponse response, Logger logger) {
    ServletUtils.forwardToResult(request, response, logger);
  }


}

