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
import java.sql.Date;
import java.time.LocalDate;

/**
 * Web API to lend a book to a borrower
 */
@WebServlet(name = "LibraryLendServlet", urlPatterns = {"/lend"}, loadOnStartup = 1)
public class LibraryLendServlet extends HttpServlet {

    private static final long serialVersionUID = -6507483398690297645L;
    private static final Logger logger = LoggerFactory.getLogger(LibraryLendServlet.class);
    static LibraryUtils libraryUtils = new LibraryUtils();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        LibraryActionResults libraryActionResults;

        final String book = StringUtils.makeNotNullable(request.getParameter("book"));
        request.setAttribute("book", book);

        final String borrower = StringUtils.makeNotNullable(request.getParameter("borrower"));
        request.setAttribute("borrower", borrower);

        if (book.isEmpty()) {
            libraryActionResults = LibraryActionResults.NO_BOOK_TITLE_PROVIDED;
        } else if (borrower.isEmpty()) {
            libraryActionResults = LibraryActionResults.NO_BORROWER_PROVIDED;
        } else {
            final Date now = getDateNow();
            request.setAttribute("date", now.toString());

            logger.info("received request to lend a book, {}, to {}", book, borrower);

            libraryActionResults = libraryUtils.lendBook(book, borrower, now);
        }
        request.setAttribute("result", libraryActionResults.toString());
        request.setAttribute("return_page", "library.html");
        ServletUtils.forwardToResult(request, response, logger);
    }

    /**
     * Wrapping the call to get a date for now,
     * so it's easier to stub for testing.
     */
    Date getDateNow() {
        return Date.valueOf(LocalDate.now());
    }

}
