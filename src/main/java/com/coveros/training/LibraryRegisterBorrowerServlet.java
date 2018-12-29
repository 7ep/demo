package com.coveros.training;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;

@WebServlet(name = "LibraryRegisterBorrowerServlet", urlPatterns = {"/registerborrower"}, loadOnStartup = 1)
public class LibraryRegisterBorrowerServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

        final String borrower = request.getParameter("borrower");
        request.setAttribute("borrower", borrower);

        final Connection connection = PersistenceLayer.createConnection();
        final PersistenceLayer persistenceLayer = new PersistenceLayer(connection);
        LibraryUtils libraryUtils = new LibraryUtils(persistenceLayer);

        final LibraryActionResults libraryActionResults = libraryUtils.registerBorrower(borrower);

        request.setAttribute("result", libraryActionResults);
        request.getRequestDispatcher("result.jsp").forward(request, response);
    }
}
