package com.coveros.training;

import com.coveros.training.persistence.PersistenceLayer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
    if (choice == "dropallschemas") {
      persistenceLayer.dropAllSchemas();
      request.setAttribute("result", "schemas dropped");
    }
    else if (choice.equals("restore")) {
      persistenceLayer.runRestore("db_sample_files/v2_empty_schema.sql");
      request.setAttribute("result", "database restored");
    }

    ServletUtils.forwardToResult(request, response, logger);
  }

}
