package com.coveros.training;

import com.coveros.training.persistence.PersistenceLayer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.flywaydb.core.Flyway;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "DbServlet", urlPatterns = {"/dbutils"}, loadOnStartup = 1)
public class DbServlet extends HttpServlet {

  static Logger logger = LogManager.getLogger();

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) {
    final String choice = request.getParameter("choice");
    final PersistenceLayer persistenceLayer = new PersistenceLayer();
    if (choice.equals("clean")) {
      Flyway flyway = Flyway.configure()
          .schemas("ADMINISTRATIVE", "LIBRARY", "AUTH")
          .dataSource("jdbc:h2:~/training", "sa", "sa")
          .load();
      flyway.clean();
      request.setAttribute("result", "schemas dropped");
    }
    else if (choice.equals("restore")) {
      persistenceLayer.runRestore("db_sample_files/v2_empty_schema.sql");
      request.setAttribute("result", "database restored");
    }

    ServletUtils.forwardToResult(request, response, logger);
  }

}
