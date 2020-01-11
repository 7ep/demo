package com.coveros.training;

import com.coveros.training.domainobjects.Borrower;
import com.coveros.training.persistence.LibraryUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.stream.Collectors;

@MultipartConfig
@WebServlet(name = "LibraryBorrowerListSearch", urlPatterns = {"/borrower"}, loadOnStartup = 1)
public class LibraryBorrowerListSearchServlet extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(LibraryBorrowerListSearchServlet.class);
    public static final String RESULT = "result";
    static LibraryUtils libraryUtils = new LibraryUtils();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        final String idString = StringUtils.makeNotNullable(request.getParameter("id"));
        final String name = StringUtils.makeNotNullable(request.getParameter("name"));

        String result = "";
        if (idString.isEmpty() && name.isEmpty()) {
            result = listAllBorrowers();
        } else if (! idString.isEmpty() && name.isEmpty()) {
            result = searchById(idString);
        } else if (idString.isEmpty() ) {
            result = searchByName(name);
        } else  {  // both id and name have an input
            logger.info("Received request for borrowers, by name and id - id {} and name {}", idString, name);
            result = "Error: please search by either name or id, not both";
        }
        request.setAttribute(RESULT, result);

        ServletUtils.forwardToRestfulResult(request, response, logger);
    }

    private String searchByName(String name) {
        logger.info("Received request for borrowers, name requested - searching for borrower by name {}", name);
        final Borrower borrower = libraryUtils.searchForBorrowerByName(name);
        if (borrower.isEmpty()) {
            return "No borrowers found with a name of " + name;
        }
        return String.format("borrower result: Name: %s, Id: %s ", borrower.name, borrower.id);
    }

    private String searchById(String idString) {
        logger.info("Received request for borrowers, id requested - searching for borrower by id {}", idString);
        int id = 0;
        try {
            id = Integer.parseInt(idString);
        } catch (NumberFormatException ex) {
            return "Error: could not parse the borrower id as an integer";
        }
        final Borrower borrower = libraryUtils.searchForBorrowerById(id);
        if (borrower.isEmpty()) {
            return "No borrowers found with an id of " + idString;
        }
        return String.format("borrower result: Name: %s, Id: %s ", borrower.name, borrower.id);
    }

    private String listAllBorrowers() {
        logger.info("Received request for borrowers, no name or id requested - listing all borrowers");
        final List<Borrower> borrowers = libraryUtils.listAllBorrowers();
        final String allBorrowers = borrowers.stream().map(Borrower::toOutputString).collect(Collectors.joining(","));
        if (allBorrowers.isEmpty()) {
            return "No borrowers exist in the database";
        }
        return allBorrowers;
    }


}
