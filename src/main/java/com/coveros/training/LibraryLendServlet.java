package com.coveros.training;

import com.coveros.training.domainobjects.Book;
import com.coveros.training.domainobjects.Borrower;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.time.LocalDate;

@WebServlet(name = "LibraryLendServlet", urlPatterns = {"/lend"}, loadOnStartup = 1)
public class LibraryLendServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        final String book = request.getParameter("book");
        request.setAttribute("book", book);

        final String borrower = request.getParameter("borrower");
        request.setAttribute("borrower", borrower);

        final Date now = Date.valueOf(LocalDate.now());
        request.setAttribute("date", now.toString());

        final Connection connection = PersistenceLayer.createConnection();
        final PersistenceLayer persistenceLayer = new PersistenceLayer(connection);
        LibraryUtils libraryUtils = new LibraryUtils(persistenceLayer);

        final Book book1 = libraryUtils.searchForBookByTitle(book);
        final Borrower borrower1 = libraryUtils.searchForBorrowerByName(borrower);
        final LibraryActionResults libraryActionResults = libraryUtils.lendBook(book1, borrower1, now);

        request.setAttribute("result", libraryActionResults.toString());
        request.getRequestDispatcher("result.jsp").forward(request, response);
    }
}
