package com.coveros.training;

import com.coveros.training.persistence.PersistenceLayer;
import com.coveros.training.persistence.RegistrationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "DbServlet", urlPatterns = {"/flyway"}, loadOnStartup = 1)
public class DbServlet extends HttpServlet {

    private static final String RESULT = "result";
    private static final Logger logger = LoggerFactory.getLogger(RegistrationUtils.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        final String action = StringUtils.makeNotNullable(request.getParameter("action"));
        final PersistenceLayer pl = new PersistenceLayer();
        switch (action) {
            case "clean":
                logger.info("received request to clean the database - i.e. remove all data and schema");
                pl.cleanDatabase();
                request.setAttribute(RESULT, "cleaned");
                break;
            case "migrate":
                logger.info("received request to migrate the database - i.e. add schema, but no data");
                pl.migrateDatabase();
                request.setAttribute(RESULT, "migrated");
                break;
            default:
                logger.info("received request to clean, then migrate the database - i.e. putting it a fresh state with no data");
                pl.cleanAndMigrateDatabase();
                request.setAttribute(RESULT, "cleaned and migrated");
        }
        request.setAttribute("return_page", "library.html");

        ServletUtils.forwardToResult(request, response, logger);
    }


}
