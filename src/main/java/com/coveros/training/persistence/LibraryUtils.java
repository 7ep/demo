package com.coveros.training.persistence;

import com.coveros.training.domainobjects.LibraryActionResults;
import com.coveros.training.domainobjects.Book;
import com.coveros.training.domainobjects.Borrower;
import com.coveros.training.domainobjects.Loan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Date;

public class LibraryUtils {

    private final PersistenceLayer persistence;
    private static final Logger logger = LoggerFactory.getLogger(LibraryUtils.class);

    public LibraryUtils(PersistenceLayer persistence) {
        this.persistence = persistence;
    }

    public LibraryUtils() {
        this(new PersistenceLayer());
    }

    public LibraryActionResults lendBook(String bookTitle, String borrowerName, Date borrowDate) {
        final Book book = searchForBookByTitle(bookTitle);
        final Borrower borrower = searchForBorrowerByName(borrowerName);
        return lendBook(book, borrower, borrowDate);
    }

    public LibraryActionResults lendBook(Book book, Borrower borrower, Date borrowDate) {
        if (book.isEmpty()) {
            logger.info("book was not registered");
            return LibraryActionResults.BOOK_NOT_REGISTERED;
        }

        if (borrower.isEmpty()) {
            logger.info("borrower was not registered");
            return LibraryActionResults.BORROWER_NOT_REGISTERED;
        }

        final Loan loan = searchForLoan(book);
        if (!loan.isEmpty()) {
            logger.info("book was already checked out");
            return LibraryActionResults.BOOK_CHECKED_OUT;
        }

        createLoan(book, borrower, borrowDate);
        return LibraryActionResults.SUCCESS;
    }

    /**
     * This is here so we can extract out the portion of code
     * that calls to the persistence layer, making it easier to test.
     */
    void createLoan(Book book, Borrower borrower, Date borrowDate) {
        logger.info("creating loan for book: {} by borrower: {} on date: {}", book.title, borrower.name, borrowDate.toString());
        persistence.createLoan(book, borrower, borrowDate);
    }

    public LibraryActionResults registerBorrower(String borrower) {
        final Borrower borrowerDetails = searchForBorrowerByName(borrower);
        final boolean borrowerWasFound = ! borrowerDetails.equals(Borrower.createEmpty());
        if (borrowerWasFound) {
            logger.info("borrower: {} was already registered", borrower);
            return LibraryActionResults.ALREADY_REGISTERED_BORROWER;
        }

        saveNewBorrower(borrower);
        return LibraryActionResults.SUCCESS;
    }

    /**
     * This is here so we can extract out the portion of code
     * that calls to the persistence layer, making it easier to test.
     */
    void saveNewBorrower(String borrower) {
        logger.info("saving new borrower: {}", borrower);
        persistence.saveNewBorrower(borrower);
    }

    public LibraryActionResults registerBook(String bookTitle) {
        final Book book = searchForBookByTitle(bookTitle);
        if (! book.isEmpty()) {
            logger.info("book was already registered");
            return LibraryActionResults.ALREADY_REGISTERED_BOOK;
        }

        saveNewBook(bookTitle);
        return LibraryActionResults.SUCCESS;
    }

    /**
     * This is here so we can extract out the portion of code
     * that calls to the persistence layer, making it easier to test.
     */
    void saveNewBook(String bookTitle) {
        logger.info("saving a new book: {}", bookTitle);
        persistence.saveNewBook(bookTitle);
    }

    public Loan searchForLoan(Book book) {
        logger.info("searching for loan by book with title: {}", book.title);
        return persistence.searchForLoan(book);
    }

    public Borrower searchForBorrowerByName(String borrowerName) {
        logger.info("searching for borrower by name: {}", borrowerName);
        return persistence.searchBorrowerDataByName(borrowerName);
    }

    public Book searchForBookByTitle(String title) {
        logger.info("search for book with title: {}", title);
        return persistence.searchBooksByTitle(title);
    }

    public static LibraryUtils createEmpty() {
        return new LibraryUtils(PersistenceLayer.createEmpty());
    }

    public boolean isEmpty() {
        return persistence.isEmpty();
    }
}
