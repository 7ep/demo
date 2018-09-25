package com.coveros.training;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@WebServlet(name = "LibraryLendServlet", urlPatterns = {"lend"}, loadOnStartup = 1)
public class LibraryLendServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        final String book = request.getParameter("book");
        request.setAttribute("book", book);

        final String borrower = request.getParameter("borrower");
        request.setAttribute("borrower", borrower);

        final OffsetDateTime now = Instant.now().atOffset(ZoneOffset.UTC);
        request.setAttribute("date", now.toString());

        final DatabaseUtils booksDb = DatabaseUtils.obtainDatabaseAccess(DatabaseUtils.LIBRARY_BOOKS_DATABASE_NAME);
        final DatabaseUtils lendingDb = DatabaseUtils.obtainDatabaseAccess(DatabaseUtils.LIBRARY_LENDING_DATABASE);
        final DatabaseUtils borrowersDb = DatabaseUtils.obtainDatabaseAccess(DatabaseUtils.LIBRARY_BORROWER_DATABASE_NAME);
        LibraryUtils libraryUtils = new LibraryUtils(borrowersDb, booksDb, lendingDb);

        final LibraryActionResults libraryActionResults = libraryUtils.lendBook(book, borrower, now);

        request.setAttribute("result", libraryActionResults.toString());
        request.getRequestDispatcher("result.jsp").forward(request, response);
    }
}
