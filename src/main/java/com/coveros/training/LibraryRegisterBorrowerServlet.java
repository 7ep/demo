package com.coveros.training;

import com.coveros.training.domainobjects.LibraryActionResults;
import com.coveros.training.persistence.LibraryUtils;
import com.coveros.training.persistence.PersistenceLayer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "LibraryRegisterBorrowerServlet", urlPatterns = {"/registerborrower"}, loadOnStartup = 1)
public class LibraryRegisterBorrowerServlet extends HttpServlet {

    private static final Logger logger = LogManager.getLogger();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) {

        final String borrower = request.getParameter("borrower");
        request.setAttribute("borrower", borrower);

        final PersistenceLayer persistenceLayer = new PersistenceLayer();
        LibraryUtils libraryUtils = new LibraryUtils(persistenceLayer);

        final LibraryActionResults libraryActionResults = libraryUtils.registerBorrower(borrower);

        request.setAttribute("result", libraryActionResults);
        try {
            request.getRequestDispatcher("result.jsp").forward(request, response);
        } catch (Exception ex) {
            logger.error("failed during forward: " + ex);
        }
    }
}
