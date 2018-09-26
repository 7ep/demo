package com.coveros.training;

import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

public class LendingTests {


    OffsetDateTime jan_1st = OffsetDateTime.of(LocalDateTime.of(2018, Month.JANUARY, 1, 0, 0), ZoneOffset.UTC);

    /**
     * If a borrower and a book are registered, a user should be able to borrow it.
     */
    @Test
    public void shouldLendToUser() {
        final LibraryUtils libraryUtils = initializeLibraryUtils();
        libraryUtils.registerBorrower("alice");
        libraryUtils.registerBook("catcher in the rye");
        final LibraryActionResults libraryActionResults = libraryUtils.lendBook("catcher in the rye", "alice", jan_1st);
        Assert.assertEquals(LibraryActionResults.SUCCESS, libraryActionResults);
    }

    /**
     * Bob wants to check out a book, but Alice has it.  Can he borrow it?
     */
    @Test
    public void shouldNotLendIfCurrentlyBorrowed() {
        final LibraryUtils libraryUtils = initializeLibraryUtils();
        libraryUtils.registerBorrower("alice");
        libraryUtils.registerBorrower("bob");
        libraryUtils.registerBook("catcher in the rye");

        // Alice can check it out...
        final LibraryActionResults libraryActionResults_alice = libraryUtils.lendBook("catcher in the rye", "alice", jan_1st);
        Assert.assertEquals(LibraryActionResults.SUCCESS, libraryActionResults_alice);

        // But Bob cannot...
        final LibraryActionResults libraryActionResults_bob = libraryUtils.lendBook("catcher in the rye", "bob", jan_1st);
        Assert.assertEquals(LibraryActionResults.BOOK_CHECKED_OUT, libraryActionResults_bob);
    }

    /**
     * Set up the databases, clear them, initialize the Library Utility with them.
     */
    private LibraryUtils initializeLibraryUtils() {
        DatabaseUtils borrowersDb = DatabaseUtils.obtainDatabaseAccess(DatabaseUtils.LIBRARY_BORROWER_DATABASE_NAME);
        DatabaseUtils lendingDb = DatabaseUtils.obtainDatabaseAccess(DatabaseUtils.LIBRARY_LENDING_DATABASE);
        DatabaseUtils booksDb = DatabaseUtils.obtainDatabaseAccess(DatabaseUtils.LIBRARY_BOOKS_DATABASE_NAME);

        borrowersDb.clearDatabaseContents();
        lendingDb.clearDatabaseContents();
        booksDb.clearDatabaseContents();

        return new LibraryUtils(borrowersDb, booksDb, lendingDb);
    }

}
