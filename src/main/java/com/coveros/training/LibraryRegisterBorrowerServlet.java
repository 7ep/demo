package com.coveros.training;

import com.coveros.training.domainobjects.LibraryActionResults;
import com.coveros.training.persistence.LibraryUtils;
import com.coveros.training.persistence.RegistrationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "LibraryRegisterBorrowerServlet", urlPatterns = {"/registerborrower"}, loadOnStartup = 1)
public class LibraryRegisterBorrowerServlet extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(RegistrationUtils.class);
    static LibraryUtils libraryUtils = new LibraryUtils();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        final String borrower = request.getParameter("borrower");
        request.setAttribute("borrower", borrower);

        final LibraryActionResults libraryActionResults = libraryUtils.registerBorrower(borrower);

        request.setAttribute("result", libraryActionResults);
        ServletUtils.forwardToResult(request, response, logger);
    }

}
