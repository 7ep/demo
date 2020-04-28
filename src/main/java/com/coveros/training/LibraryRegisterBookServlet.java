package com.coveros.training;

import com.coveros.training.domainobjects.LibraryActionResults;
import com.coveros.training.persistence.LibraryUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "LibraryRegisterBookServlet", urlPatterns = {"/registerbook"}, loadOnStartup = 1)
public class LibraryRegisterBookServlet extends HttpServlet {

    private static final long serialVersionUID = -6971471412293552088L;
    private static final Logger logger = LoggerFactory.getLogger(LibraryRegisterBookServlet.class);
    static LibraryUtils libraryUtils = new LibraryUtils();

    @Override
    protected void doPost(final HttpServletRequest request, final HttpServletResponse response) {
        final String book = StringUtils.makeNotNullable(request.getParameter("book"));
        LibraryActionResults libraryActionResults;

        if (book.isEmpty()) {
            libraryActionResults = LibraryActionResults.NO_BOOK_TITLE_PROVIDED;
            logger.info("input for the book field was empty");
        } else {
            request.setAttribute("book", book);

            logger.info("received request to register a book, {}", book);

            libraryActionResults = libraryUtils.registerBook(book);
        }

        request.setAttribute("return_page", "library.html");
        request.setAttribute("result", libraryActionResults.toString());
        ServletUtils.forwardToResult(request, response, logger);
    }

}
