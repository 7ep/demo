package com.coveros.training;

import java.time.OffsetDateTime;

public class LibraryUtils {

    private final PersistenceLayer persistence;
    private final DatabaseUtils booksDb;
    private final DatabaseUtils lendingDb;

    LibraryUtils(PersistenceLayer persistence, DatabaseUtils booksDb, DatabaseUtils lendingDb) {
        this.persistence = persistence;
        this.booksDb = booksDb;
        this.lendingDb = lendingDb;
    }

    LibraryActionResults lendBook(String book, String borrower, OffsetDateTime borrowTime) {
        if (booksDb.searchDatabaseForKey(book) == null) return LibraryActionResults.BOOK_NOT_REGISTERED;
        if (persistence.searchBorrowerDataByName(borrower) == null) return LibraryActionResults.BORROWER_NOT_REGISTERED;
        if (lendingDb.searchDatabaseForKey(book) != null) return LibraryActionResults.BOOK_CHECKED_OUT;
        lendingDb.saveTextToFile(book + " " + borrower + " " + borrowTime);
        return LibraryActionResults.SUCCESS;
    }

    LibraryActionResults registerBorrower(String borrower) {
        final BorrowerData borrowerDetails = persistence.searchBorrowerDataByName(borrower);
        if (borrowerDetails != null) return LibraryActionResults.ALREADY_REGISTERED_BORROWER;

        persistence.saveNewBorrower(borrower);
        return LibraryActionResults.SUCCESS;
    }

    LibraryActionResults registerBook(String book) {
        final String bookDetails = booksDb.searchDatabaseForKey(book);
        if (bookDetails != null) return LibraryActionResults.ALREADY_REGISTERED_BOOK;

        booksDb.saveTextToFile(book);
        return LibraryActionResults.SUCCESS;
    }

    public String queryBookInfo(String myBook) {
        return lendingDb.searchDatabaseForKey(myBook);
    }
}
