package com.coveros.training;

import com.coveros.training.domainobjects.Borrower;
import com.coveros.training.persistence.LibraryUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet(name = "LibraryBorrowerListSearch", urlPatterns = {"/borrower"}, loadOnStartup = 1)
public class LibraryBorrowerListSearchServlet extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(LibraryBorrowerListSearchServlet.class);
    private static final String RESULT = "result";
    static LibraryUtils libraryUtils = new LibraryUtils();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        final String idString = StringUtils.makeNotNullable(request.getParameter("id"));
        final String name = StringUtils.makeNotNullable(request.getParameter("name"));

        if (idString.isEmpty() && name.isEmpty()) {
            logger.info("Received request for borrowers, no name or id requested - listing all borrowers");
            final List<Borrower> borrowers = libraryUtils.listAllBorrowers();
            final String allBorrowers = borrowers.stream().map(Borrower::toOutputString).collect(Collectors.joining(","));

            request.setAttribute(RESULT, allBorrowers);
        }

        if (! idString.isEmpty()) {
            logger.info("Received request for borrowers, id requested - searching for borrower by id {}", idString);
            int id = 0;
            try {
                id = Integer.parseInt(idString);
            } catch (NumberFormatException ex) {
                request.setAttribute(RESULT, "Error: could not parse the borrower id as an integer");
            }
            final Borrower borrower = libraryUtils.searchForBorrowerById(id);
            request.setAttribute(RESULT, String.format("borrower result: Name: %s, Id: %s ", borrower.name, borrower.id));
        }

        if (! name.isEmpty()) {
            logger.info("Received request for borrower, name requested - searching for borrower by name {}", name);
            final Borrower borrower = libraryUtils.searchForBorrowerByName(name);
            request.setAttribute(RESULT, String.format("borrower result: Name: %s, Id: %s ", borrower.name, borrower.id));
        }

        ServletUtils.forwardToRestfulResult(request, response, logger);
    }

}
