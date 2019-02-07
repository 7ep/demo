package com.coveros.training;

import com.coveros.training.persistence.RegistrationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.coveros.training.persistence.PersistenceLayer.*;

@WebServlet(name = "DbServlet", urlPatterns = {"/flyway"}, loadOnStartup = 1)
public class DbServlet extends HttpServlet {

  private static final String RESULT = "result";
  private static final Logger logger = LoggerFactory.getLogger(RegistrationUtils.class);

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) {
    final String action = StringUtils.makeNotNullable(request.getParameter("action"));
    switch (action) {
      case "clean":
        cleanDatabase();
        request.setAttribute(RESULT, "cleaned");
        break;
      case "migrate":
        migrateDatabase();
        request.setAttribute(RESULT, "migrated");
        break;
      default:
        cleanAndMigrateDatabase();
        request.setAttribute(RESULT, "cleaned and migrated");
    }

    ServletUtils.forwardToResult(request, response, logger);
  }


}
