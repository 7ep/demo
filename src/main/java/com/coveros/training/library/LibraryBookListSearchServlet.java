package com.coveros.training.library;

import com.coveros.training.library.domainobjects.Book;
import com.coveros.training.helpers.ServletUtils;
import com.coveros.training.helpers.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Web API to list all books or search books by id / title
 */
@MultipartConfig
@WebServlet(name = "LibraryBookListSearch", urlPatterns = {"/book"}, loadOnStartup = 1)
public class LibraryBookListSearchServlet extends HttpServlet {

    private static final long serialVersionUID = -56598540573518235L;
    private static final Logger logger = LoggerFactory.getLogger(LibraryBookListSearchServlet.class);
    public static final String RESULT = "result";
    static LibraryUtils libraryUtils = new LibraryUtils();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        final String idString = StringUtils.makeNotNullable(request.getParameter("id"));
        final String title = StringUtils.makeNotNullable(request.getParameter("title"));

        String result;
        if (idString.isEmpty() && title.isEmpty()) {
            result = listAllBooks();
        } else if (! idString.isEmpty() && title.isEmpty()) {
            result = searchById(idString);
        } else if (idString.isEmpty() ) {
            result = searchByTitle(title);
        } else  {  // both id and title have an input
            logger.info("Received request for books, by title and id - id {} and title {}", idString, title);
            result = "Error: please search by either title or id, not both";
        }
        request.setAttribute(RESULT, result);

        ServletUtils.forwardToRestfulResult(request, response, logger);
    }

    private String searchByTitle(String title) {
        logger.info("Received request for books, name requested - searching for book by title {}", title);
        final Book book = libraryUtils.searchForBookByTitle(title);
        if (book.isEmpty()) {
            return "No books found with a title of " + title;
        }
        return "[" + book.toOutputString() + "]";
    }

    private String searchById(String idString) {
        logger.info("Received request for books, id requested - searching for book by id {}", idString);
        int id;
        try {
            id = Integer.parseInt(idString);
        } catch (NumberFormatException ex) {
            return "Error: could not parse the book id as an integer";
        }
        final Book book = libraryUtils.searchForBookById(id);
        if (book.isEmpty()) {
            return "No books found with an id of " + idString;
        }
        return "[" + book.toOutputString() + "]";
    }

    private String listAllBooks() {
        logger.info("Received request for books, no title or id requested - listing all books");
        final List<Book> books = libraryUtils.listAllBooks();
        final String allBooks = books.stream().map(Book::toOutputString).collect(Collectors.joining(","));
        if (allBooks.isEmpty()) {
            return "No books exist in the database";
        }
        return "[" + allBooks + "]";
    }

}
