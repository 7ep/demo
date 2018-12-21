package com.coveros.training;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;

@WebServlet(name = "LibraryRegisterBookServlet", urlPatterns = {"registerbook"}, loadOnStartup = 1)
public class LibraryRegisterBookServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        final String book = request.getParameter("book");
        request.setAttribute("book", book);

        final DatabaseUtils booksDb = DatabaseUtils.obtainDatabaseAccess(DatabaseUtils.LIBRARY_BOOKS_DATABASE_NAME);
        final DatabaseUtils lendingDb = DatabaseUtils.obtainDatabaseAccess(DatabaseUtils.LIBRARY_LENDING_DATABASE);
        final PersistenceLayer persistenceLayer = new PersistenceLayer(new EmptyConnection());
        LibraryUtils libraryUtils = new LibraryUtils(persistenceLayer, booksDb, lendingDb);

        final LibraryActionResults libraryActionResults = libraryUtils.registerBook(book);

        request.setAttribute("result", libraryActionResults.toString());
        request.getRequestDispatcher("result.jsp").forward(request, response);
    }

}
