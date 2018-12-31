package com.coveros.training;

import com.coveros.training.domainobjects.LibraryActionResults;
import com.coveros.training.persistence.LibraryUtils;
import com.coveros.training.persistence.PersistenceLayer;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;

@WebServlet(name = "LibraryRegisterBookServlet", urlPatterns = {"/registerbook"}, loadOnStartup = 1)
public class LibraryRegisterBookServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        final String book = request.getParameter("book");
        request.setAttribute("book", book);

        final Connection connection = PersistenceLayer.createConnection();
        final PersistenceLayer persistenceLayer = new PersistenceLayer(connection);
        LibraryUtils libraryUtils = new LibraryUtils(persistenceLayer);

        final LibraryActionResults libraryActionResults = libraryUtils.registerBook(book);

        request.setAttribute("result", libraryActionResults.toString());
        request.getRequestDispatcher("result.jsp").forward(request, response);
    }

}
