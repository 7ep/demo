package com.coveros.training.persistence;

import com.coveros.training.domainobjects.LibraryActionResults;
import com.coveros.training.domainobjects.Book;
import com.coveros.training.domainobjects.Borrower;
import com.coveros.training.domainobjects.Loan;

import java.sql.Date;

public class LibraryUtils {

    private final PersistenceLayer persistence;

    public LibraryUtils(PersistenceLayer persistence) {
        this.persistence = persistence;
    }

    public LibraryUtils() {
        this(new PersistenceLayer());
    }

    public LibraryActionResults lendBook(Book book, Borrower borrower, Date borrowDate) {
        if (book.isEmpty()) return LibraryActionResults.BOOK_NOT_REGISTERED;

        if (borrower.isEmpty()) return LibraryActionResults.BORROWER_NOT_REGISTERED;

        final Loan loan = searchForLoan(book);
        if (!loan.isEmpty()) return LibraryActionResults.BOOK_CHECKED_OUT;

        createLoan(book, borrower, borrowDate);
        return LibraryActionResults.SUCCESS;
    }

    /**
     * This is here so we can extract out the portion of code
     * that calls to the persistence layer, making it easier to test.
     */
    void createLoan(Book book, Borrower borrower, Date borrowDate) {
        persistence.createLoan(book, borrower, borrowDate);
    }

    public LibraryActionResults registerBorrower(String borrower) {
        final Borrower borrowerDetails = searchForBorrowerByName(borrower);
        final boolean borrowerWasFound = ! borrowerDetails.equals(Borrower.createEmpty());
        if (borrowerWasFound) return LibraryActionResults.ALREADY_REGISTERED_BORROWER;

        saveNewBorrower(borrower);
        return LibraryActionResults.SUCCESS;
    }

    /**
     * This is here so we can extract out the portion of code
     * that calls to the persistence layer, making it easier to test.
     */
    void saveNewBorrower(String borrower) {
        persistence.saveNewBorrower(borrower);
    }

    public LibraryActionResults registerBook(String bookTitle) {
        final Book book = searchForBookByTitle(bookTitle);
        if (! book.isEmpty()) return LibraryActionResults.ALREADY_REGISTERED_BOOK;

        saveNewBook(bookTitle);
        return LibraryActionResults.SUCCESS;
    }

    /**
     * This is here so we can extract out the portion of code
     * that calls to the persistence layer, making it easier to test.
     */
    void saveNewBook(String bookTitle) {
        persistence.saveNewBook(bookTitle);
    }

    public Loan searchForLoan(Book book) {
        return persistence.searchForLoan(book);
    }

    public Borrower searchForBorrowerByName(String borrowerName) {
        return persistence.searchBorrowerDataByName(borrowerName);
    }

    public Book searchForBookByTitle(String title) {
        return persistence.searchBooksByTitle(title);
    }

    public static LibraryUtils createEmpty() {
        return new LibraryUtils(PersistenceLayer.createEmpty());
    }

    public boolean isEmpty() {
        return persistence.isEmpty();
    }
}
