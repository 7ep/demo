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

  public static final String PASSWORD_PARAM = "password";
  private static final Logger logger = LogManager.getLogger();
  public static final String USERNAME_PARAM = "username";
  public static final String EMPTY_USERNAME = "EMPTY_USERNAME";
  public static final String EMPTY_PASSWORD = "EMPTY_PASSWORD";

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

    RegistrationResult registrationResult = processRegistration(username, password);

    request.setAttribute("result", registrationResult.toString());
    forwardToResult(request, response, logger);
  }

  /**
   * Wrapping a static method call for testing.
   */
  void forwardToResult(HttpServletRequest request, HttpServletResponse response, Logger logger) {
    ServletUtils.forwardToResult(request, response, logger);
  }

  RegistrationResult processRegistration(String username, String password) {
    final PersistenceLayer persistenceLayer = new PersistenceLayer();
    final RegistrationUtils registrationUtils = new RegistrationUtils(persistenceLayer);
    return registrationUtils.processRegistration(username, password);
  }

}

