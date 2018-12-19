package com.coveros.training;

import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

public class LendingTests {


    private final static OffsetDateTime BORROW_DATE = OffsetDateTime.of(LocalDateTime.of(2018, Month.JANUARY, 1, 0, 0), ZoneOffset.UTC);
    private final static String BORROWER_A = "borrower_a";
    private final static String BORROWER_B = "borrower_b";
    private final static String BOOK = "Some book";


    /**
     * If a borrower and a book are registered, a user should be able to borrow it.
     */
    @Test
    public void shouldLendToUser() {
        final LibraryUtils libraryUtils = initializeLibraryUtils();
        libraryUtils.registerBorrower(BORROWER_A);
        libraryUtils.registerBook(BOOK);
        final LibraryActionResults libraryActionResults = libraryUtils.lendBook(BOOK, BORROWER_A, BORROW_DATE);
        Assert.assertEquals(LibraryActionResults.SUCCESS, libraryActionResults);
    }

    /**
     * Just make sure that we get the expected response if we register a borrower
     */
    @Test
    public void shouldRegisterBorrower() {
        final LibraryUtils libraryUtils = initializeLibraryUtils();
        final LibraryActionResults result = libraryUtils.registerBorrower(BORROWER_A);
        Assert.assertEquals(LibraryActionResults.SUCCESS, result);
    }

    /**
     * Just make sure that we get the expected response if we register a book
     */
    @Test
    public void shouldRegisterBook() {
        final LibraryUtils libraryUtils = initializeLibraryUtils();
        final LibraryActionResults result = libraryUtils.registerBook(BOOK);
        Assert.assertEquals(LibraryActionResults.SUCCESS, result);
    }

    /**
     * We shouldn't be able to lend if we try to lend
     * to a borrower who isn't registered
     */
    @Test
    public void testShouldNotLendIfBorrowerNotRegistered() {
        final LibraryUtils libraryUtils = initializeLibraryUtils();
        libraryUtils.registerBook(BOOK);
        // note that we aren't registering the borrower here
        final LibraryActionResults result = libraryUtils.lendBook(BOOK, BORROWER_A, BORROW_DATE);
        Assert.assertEquals(LibraryActionResults.BORROWER_NOT_REGISTERED, result);
    }


    /**
     * We shouldn't be able to lend if we try to lend
     * out a book that isn't registered
     */
    @Test
    public void testShouldNotLendIfBookNotRegistered() {
        final LibraryUtils libraryUtils = initializeLibraryUtils();
        libraryUtils.registerBorrower(BORROWER_A);
        // note that we aren't registering the book here
        final LibraryActionResults result = libraryUtils.lendBook(BOOK, BORROWER_A, BORROW_DATE);
        Assert.assertEquals(LibraryActionResults.BOOK_NOT_REGISTERED, result);
    }

    /**
     * b wants to check out a book, but a has it.  Can they borrow it? (no)
     */
    @Test
    public void shouldNotLendIfCurrentlyBorrowed() {
        final LibraryUtils libraryUtils = initializeLibraryUtils();
        libraryUtils.registerBorrower(BORROWER_A);
        libraryUtils.registerBorrower(BORROWER_B);
        libraryUtils.registerBook(BOOK);

        // Alice can check it out...
        final LibraryActionResults libraryActionResults_alice = libraryUtils.lendBook(BOOK, BORROWER_A, BORROW_DATE);
        Assert.assertEquals(LibraryActionResults.SUCCESS, libraryActionResults_alice);

        // But Bob cannot...
        final LibraryActionResults libraryActionResults_bob = libraryUtils.lendBook(BOOK, BORROWER_B, BORROW_DATE);
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
