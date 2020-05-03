package com.coveros.training.library;

import com.coveros.training.library.domainobjects.Book;
import com.coveros.training.library.domainobjects.Borrower;
import com.coveros.training.library.domainobjects.LibraryActionResults;
import com.coveros.training.library.domainobjects.Loan;
import com.coveros.training.persistence.IPersistenceLayer;
import com.coveros.training.persistence.PersistenceLayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * Business logic for handling library needs.  For example, {@link #lendBook}
 */
public class LibraryUtils {

    private final IPersistenceLayer persistence;
    private static final Logger logger = LoggerFactory.getLogger(LibraryUtils.class);

    public LibraryUtils(IPersistenceLayer persistence) {
        this.persistence = persistence;
    }

    public LibraryUtils() {
        this(new PersistenceLayer());
    }

    /**
     * Lend a book to a borrower.
     * @param bookTitle The title of a registered book, e.g. see {@link #registerBook(String)}
     * @param borrowerName the name of a registered borrower, e.g. see {@link #registerBorrower(String)}
     * @param borrowDate the date the book is being lent out.
     * @return an enum {@link LibraryActionResults} indicating the resultant status
     */
    public LibraryActionResults lendBook(String bookTitle, String borrowerName, Date borrowDate) {
        logger.info("starting process to lend a book: {} to borrower: {}", bookTitle, borrowerName);
        final Book book = searchForBookByTitle(bookTitle);
        final Book foundBook = new Book(book.id, bookTitle);
        final Borrower borrower = searchForBorrowerByName(borrowerName);
        final Borrower foundBorrower = new Borrower(borrower.id, borrowerName);
        return lendBook(foundBook, foundBorrower, borrowDate);
    }

    public LibraryActionResults lendBook(Book book, Borrower borrower, Date borrowDate) {
        if (book.id == 0) {
            logger.info("book: {} was not registered.  Lending failed", book.title);
            return LibraryActionResults.BOOK_NOT_REGISTERED;
        }

        if (borrower.id == 0) {
            logger.info("borrower: {} was not registered.  Lending failed", borrower.name);
            return LibraryActionResults.BORROWER_NOT_REGISTERED;
        }

        final Loan loan = searchForLoanByBook(book);
        if (!loan.isEmpty()) {
            logger.info("book: {} was already checked out on {}.  Lending failed", book.title, loan.checkoutDate);
            return LibraryActionResults.BOOK_CHECKED_OUT;
        }

        logger.info("book: {} is available for borrowing by valid borrower: {}", book.title, borrower.name);
        createLoan(book, borrower, borrowDate);
        return LibraryActionResults.SUCCESS;
    }

    /**
     * This is here so we can extract out the portion of code
     * that calls to the persistence layer, making it easier to test.
     */
    void createLoan(Book book, Borrower borrower, Date borrowDate) {
        logger.info("creating loan for book: {} by borrower: {}", book.title, borrower.name);
        persistence.createLoan(book, borrower, borrowDate);
    }

    /**
     * Register a borrower with the library
     * @param borrower the name of a borrower
     * @return an enum, {@link LibraryActionResults} indicating the resultant status
     */
    public LibraryActionResults registerBorrower(String borrower) {
        logger.info("trying to register a borrower with name: {}", borrower);
        final Borrower borrowerDetails = searchForBorrowerByName(borrower);
        final boolean borrowerWasFound = !borrowerDetails.equals(Borrower.createEmpty());
        if (borrowerWasFound) {
            logger.info("borrower: {} was already registered", borrower);
            return LibraryActionResults.ALREADY_REGISTERED_BORROWER;
        }
        logger.info("borrower: {} was not found.  Registering new borrower...", borrower);
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

    /**
     * Register a new book with the library
     * @param bookTitle the title of a book
     * @return an enum {@link LibraryActionResults} indicating the resultant status
     */
    public LibraryActionResults registerBook(String bookTitle) {
        if (bookTitle.isEmpty()) {
            throw new IllegalArgumentException("bookTitle was an empty string - disallowed when registering books");
        }
        logger.info("trying to register a book with title: {}", bookTitle);
        final Book book = searchForBookByTitle(bookTitle);
        if (!book.isEmpty()) {
            logger.info("book: {} was already registered", bookTitle);
            return LibraryActionResults.ALREADY_REGISTERED_BOOK;
        }
        logger.info("book: {} was not found.  Registering new book...", bookTitle);
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

    public Loan searchForLoanByBook(Book book) {
        logger.info("searching for loan by book with title: {}", book.title);
        return persistence.searchForLoanByBook(book).orElse(Loan.createEmpty());
    }


    public List<Loan> searchForLoanByBorrower(Borrower borrower) {
        logger.info("searching for loan by borrower with name: {}", borrower.name);
        return persistence.searchForLoanByBorrower(borrower).orElse(new ArrayList<>());
    }

    public Borrower searchForBorrowerByName(String borrowerName) {
        logger.info("searching for borrower by name: {}", borrowerName);
        return persistence.searchBorrowerDataByName(borrowerName).orElse(Borrower.createEmpty());
    }

    public Book searchForBookByTitle(String title) {
        if (title.isEmpty()) {
            throw new IllegalArgumentException("when searching for a book, must include a non-empty string for title");
        }
        logger.info("search for book with title: {}", title);
        final Book book = persistence.searchBooksByTitle(title).orElse(Book.createEmpty());
        if (book.isEmpty()) {
            logger.info("No book found with title of {}", title);
        } else {
            logger.info("book found with title of {}", title);
        }
        return book;
    }

    /**
     * The id has to be positive.  Exception will be thrown otherwise.
     */
    public Book searchForBookById(long id) {
        if (id < 1) {
            throw new IllegalArgumentException("when searching for a book, must include an id of one or greater");
        }
        logger.info("search for book with id: {}", id);
        final Book book = persistence.searchBooksById(id).orElse(Book.createEmpty());
        if (book.isEmpty()) {
            logger.info("No book found with id of {}", id);
        } else {
            logger.info("Book found with id of {}", id);
        }
        return book;
    }

    /**
     * The id has to be positive.  Exception will be thrown otherwise.
     */
    public Borrower searchForBorrowerById(long id) {
        if (id < 1) {
            throw new IllegalArgumentException("when searching for a borrower, must include an id of one or greater");
        }
        logger.info("search for borrower with id: {}", id);
        final Borrower borrower = persistence.searchBorrowersById(id).orElse(Borrower.createEmpty());
        if (borrower.isEmpty()) {
            logger.info("No borrower found with id of {}", id);
        } else {
            logger.info("borrower found with id of {}", id);
        }
        return borrower;
    }

    public static LibraryUtils createEmpty() {
        return new LibraryUtils(PersistenceLayer.createEmpty());
    }

    public boolean isEmpty() {
        return persistence.isEmpty();
    }

    public LibraryActionResults deleteBook(Book book) {
        logger.info("deleting a book.  id: {}, title: {}", book.id, book.title);
        final Book bookInDatabase = searchForBookByTitle(book.title);
        if (bookInDatabase.isEmpty()) {
            logger.info("book not found in database.  Therefore, obviously, cannot be deleted");
            return LibraryActionResults.NON_REGISTERED_BOOK_CANNOT_BE_DELETED;
        }
        persistence.deleteBook(book.id);
        logger.info("book with title: {} and id: {} was deleted", bookInDatabase.title, bookInDatabase.id);
        return LibraryActionResults.SUCCESS;
    }

    public LibraryActionResults deleteBorrower(Borrower borrower) {
        logger.info("deleting a borrower.  id: {}, name: {}", borrower.id, borrower.name);
        final Borrower borrowerInDatabase = searchForBorrowerByName(borrower.name);
        if (borrowerInDatabase.isEmpty()) {
            logger.info("borrower not found in database.  Therefore, obviously, cannot be deleted");
            return LibraryActionResults.NON_REGISTERED_BORROWER_CANNOT_BE_DELETED;
        }
        persistence.deleteBorrower(borrower.id);
        logger.info("borrower with name: {} and id: {} was deleted", borrowerInDatabase.name, borrowerInDatabase.id);
        return LibraryActionResults.SUCCESS;
    }

    public List<Book> listAllBooks() {
        logger.info("received request to list all books");
        return persistence.listAllBooks().orElse(new ArrayList<>());
    }


    public List<Borrower> listAllBorrowers() {
        logger.info("received request to list all borrowers");
        return persistence.listAllBorrowers().orElse(new ArrayList<>());
    }


    public List<Book> listAvailableBooks() {
        logger.info("received request to list available books");
        return persistence.listAvailableBooks().orElse(new ArrayList<>());
    }
}
