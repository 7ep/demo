package com.coveros.training;

import com.coveros.training.domainobjects.Book;
import com.coveros.training.domainobjects.Borrower;
import com.coveros.training.domainobjects.LibraryActionResults;
import com.coveros.training.persistence.LibraryUtils;
import com.coveros.training.persistence.PersistenceLayer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Date;
import java.time.LocalDate;

@WebServlet(name = "LibraryLendServlet", urlPatterns = {"/lend"}, loadOnStartup = 1)
public class LibraryLendServlet extends HttpServlet {

  private static final Logger logger = LogManager.getLogger();

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response) {
    final String book = request.getParameter("book");
    request.setAttribute("book", book);

    final String borrower = request.getParameter("borrower");
    request.setAttribute("borrower", borrower);

    final Date now = Date.valueOf(LocalDate.now());
    request.setAttribute("date", now.toString());

    final PersistenceLayer persistenceLayer = new PersistenceLayer();
    LibraryUtils libraryUtils = new LibraryUtils(persistenceLayer);

    final Book book1 = libraryUtils.searchForBookByTitle(book);
    final Borrower borrower1 = libraryUtils.searchForBorrowerByName(borrower);
    final LibraryActionResults libraryActionResults = libraryUtils.lendBook(book1, borrower1, now);

    request.setAttribute("result", libraryActionResults.toString());
    forwardToResult(request, response, logger);
  }

  /**
   * Wrapping a static method call for testing.
   */
  void forwardToResult(HttpServletRequest request, HttpServletResponse response, Logger logger) {
    ServletUtils.forwardToResult(request, response, logger);
  }
}
