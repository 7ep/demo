package com.coveros.training;

import java.time.OffsetDateTime;

public class LibraryUtils {

    private final DatabaseUtils borrowerDb;
    private final DatabaseUtils booksDb;
    private final DatabaseUtils lendingDb;

    public LibraryUtils(DatabaseUtils borrowerDb, DatabaseUtils booksDb, DatabaseUtils lendingDb) {
        this.borrowerDb = borrowerDb;
        this.booksDb = booksDb;
        this.lendingDb = lendingDb;
    }

    public LibraryActionResults lendBook(String book, String borrower, OffsetDateTime borrowTime) {
        if (booksDb.searchDatabaseForKey(book) == null) return LibraryActionResults.BOOK_NOT_REGISTERED;
        if (borrowerDb.searchDatabaseForKey(borrower) == null) return LibraryActionResults.BORROWER_NOT_REGISTERED;
        if (lendingDb.searchDatabaseForKey(book) != null) return LibraryActionResults.BOOK_CHECKED_OUT;
        lendingDb.saveTextToFile(book + " " + borrower + " " + borrowTime);
        return LibraryActionResults.SUCCESS;
    }

    public LibraryActionResults registerBorrower(String borrower) {
        final String borrowerDetails = borrowerDb.searchDatabaseForKey(borrower);
        if (borrowerDetails != null) return LibraryActionResults.ALREADY_REGISTERED_BORROWER;

        borrowerDb.saveTextToFile(borrower);
        return LibraryActionResults.SUCCESS;
    }

    public LibraryActionResults registerBook(String book) {
        final String bookDetails = booksDb.searchDatabaseForKey(book);
        if (bookDetails != null) return LibraryActionResults.ALREADY_REGISTERED_BOOK;

        booksDb.saveTextToFile(book);
        return LibraryActionResults.SUCCESS;
    }

    public String queryBookInfo(String myBook) {
        return lendingDb.searchDatabaseForKey(myBook);
    }
}
