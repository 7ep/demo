package com.coveros.training;

import com.coveros.training.domainobjects.Book;
import com.coveros.training.persistence.LibraryUtils;
import com.coveros.training.persistence.RegistrationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet(name = "LibraryBookListSearch", urlPatterns = {"/book"}, loadOnStartup = 1)
public class LibraryBookListSearch extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(RegistrationUtils.class);
    private static final String RESULT = "result";
    static LibraryUtils libraryUtils = new LibraryUtils();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        final String idString = StringUtils.makeNotNullable(request.getParameter("id"));
        final String title = StringUtils.makeNotNullable(request.getParameter("title"));

        if (idString.isEmpty() && title.isEmpty()) {
            logger.info("Received request for books, no title or id requested - listing all books");
            final List<Book> books = libraryUtils.listAllBooks();
            final String allBooks = books.stream().map(Book::toOutputString).collect(Collectors.joining(","));

            request.setAttribute(RESULT, allBooks);
        }

        if (! idString.isEmpty()) {
            logger.info("Received request for books, id requested - searching for book by id {}", idString);
            int id = 0;
            try {
                id = Integer.parseInt(idString);
            } catch (NumberFormatException ex) {
                request.setAttribute(RESULT, "Error: could not parse the book id as an integer");
            }
            final Book book = libraryUtils.searchForBookById(id);
            request.setAttribute(RESULT, String.format("book result: Title: %s, Id: %s ", book.title, book.id));
        }

        if (! title.isEmpty()) {
            logger.info("Received request for books, name requested - searching for book by title {}", title);
            final Book book = libraryUtils.searchForBookByTitle(title);
            request.setAttribute(RESULT, String.format("book result: Title: %s, Id: %s ", book.title, book.id));
        }

        ServletUtils.forwardToResult(request, response, logger);
    }

}
