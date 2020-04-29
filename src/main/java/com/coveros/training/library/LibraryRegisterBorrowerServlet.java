package com.coveros.training.library;

import com.coveros.training.library.domainobjects.LibraryActionResults;
import com.coveros.training.helpers.ServletUtils;
import com.coveros.training.helpers.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Web API to register a new borrower with the library
 */
@WebServlet(name = "LibraryRegisterBorrowerServlet", urlPatterns = {"/registerborrower"}, loadOnStartup = 1)
public class LibraryRegisterBorrowerServlet extends HttpServlet {

    private static final long serialVersionUID = 3293380381170679010L;
    private static final Logger logger = LoggerFactory.getLogger(LibraryRegisterBorrowerServlet.class);
    static LibraryUtils libraryUtils = new LibraryUtils();

    @Override
    protected void doPost(final HttpServletRequest request, final HttpServletResponse response) {
        final String borrower = StringUtils.makeNotNullable(request.getParameter("borrower"));
        LibraryActionResults libraryActionResults;

        if (borrower.isEmpty()) {
            libraryActionResults = LibraryActionResults.NO_BORROWER_PROVIDED;
            logger.info("input for the borrower field was empty");
        } else {
            request.setAttribute("borrower", borrower);

            logger.info("received request to register a borrower, {}", borrower);

            libraryActionResults = libraryUtils.registerBorrower(borrower);
        }

        request.setAttribute("return_page", "library.html");
        request.setAttribute("result", libraryActionResults.toString());
        ServletUtils.forwardToResult(request, response, logger);
    }

}
