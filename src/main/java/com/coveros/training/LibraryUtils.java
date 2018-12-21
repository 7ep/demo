package com.coveros.training;

import java.time.OffsetDateTime;

class LibraryUtils {

    private final PersistenceLayer persistence;
    private final DatabaseUtils booksDb;
    private final DatabaseUtils lendingDb;

    LibraryUtils(PersistenceLayer persistence, DatabaseUtils booksDb, DatabaseUtils lendingDb) {
        this.persistence = persistence;
        this.booksDb = booksDb;
        this.lendingDb = lendingDb;
    }

    static LibraryUtils createEmpty() {
        return new LibraryUtils(new PersistenceLayer(new EmptyConnection()), DatabaseUtils.createEmpty(), DatabaseUtils.createEmpty());
    }

    LibraryActionResults lendBook(String book, String borrower, OffsetDateTime borrowTime) {
        if (booksDb.searchDatabaseForKey(book).isEmpty()) return LibraryActionResults.BOOK_NOT_REGISTERED;
        if (persistence.searchBorrowerDataByName(borrower).equals(BorrowerData.createEmpty())) return LibraryActionResults.BORROWER_NOT_REGISTERED;
        if (StringUtils.isNotEmpty(lendingDb.searchDatabaseForKey(book))) return LibraryActionResults.BOOK_CHECKED_OUT;
        lendingDb.saveTextToFile(book + " " + borrower + " " + borrowTime);
        return LibraryActionResults.SUCCESS;
    }

    LibraryActionResults registerBorrower(String borrower) {
        final BorrowerData borrowerDetails = persistence.searchBorrowerDataByName(borrower);
        final boolean borrowerWasFound = !borrowerDetails.equals(BorrowerData.createEmpty());
        if (borrowerWasFound) return LibraryActionResults.ALREADY_REGISTERED_BORROWER;

        persistence.saveNewBorrower(borrower);
        return LibraryActionResults.SUCCESS;
    }

    LibraryActionResults registerBook(String book) {
        final String bookDetails = booksDb.searchDatabaseForKey(book);
        if (! bookDetails.isEmpty()) return LibraryActionResults.ALREADY_REGISTERED_BOOK;

        booksDb.saveTextToFile(book);
        return LibraryActionResults.SUCCESS;
    }

}
