package com.coveros.training;

import java.sql.Date;
import java.time.OffsetDateTime;

class LibraryUtils {

    private final PersistenceLayer persistence;

    LibraryUtils(PersistenceLayer persistence) {
        this.persistence = persistence;
    }

    static LibraryUtils createEmpty() {
        return new LibraryUtils(new PersistenceLayer(new EmptyConnection()));
    }

    LibraryActionResults lendBook(Book book, Borrower borrower, Date borrowDate) {
        if (book.isEmpty()) return LibraryActionResults.BOOK_NOT_REGISTERED;

        if (borrower.isEmpty()) return LibraryActionResults.BORROWER_NOT_REGISTERED;

        final Loan loan = persistence.searchForLoan(book);
        if (!loan.isEmpty()) return LibraryActionResults.BOOK_CHECKED_OUT;

        persistence.createLoan(book, borrower, borrowDate);
        return LibraryActionResults.SUCCESS;
    }

    LibraryActionResults registerBorrower(String borrower) {
        final Borrower borrowerDetails = persistence.searchBorrowerDataByName(borrower);
        final boolean borrowerWasFound = !borrowerDetails.equals(Borrower.createEmpty());
        if (borrowerWasFound) return LibraryActionResults.ALREADY_REGISTERED_BORROWER;

        persistence.saveNewBorrower(borrower);
        return LibraryActionResults.SUCCESS;
    }

    LibraryActionResults registerBook(String bookTitle) {
        final Book book = persistence.searchBooksByTitle(bookTitle);
        if (! book.isEmpty()) return LibraryActionResults.ALREADY_REGISTERED_BOOK;

        persistence.saveNewBook(bookTitle);
        return LibraryActionResults.SUCCESS;
    }

    Loan searchForLoan(Book book) {
        return persistence.searchForLoan(book);
    }

    Borrower searchForBorrowerByName(String borrowerName) {
        return persistence.searchBorrowerDataByName(borrowerName);
    }

    public Book searchForBookByTitle(String title) {
        return persistence.searchBooksByTitle(title);
    }
}
