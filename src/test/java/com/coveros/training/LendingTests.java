package com.coveros.training;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static org.mockito.Mockito.when;

public class LendingTests {


    private final static OffsetDateTime BORROW_DATE = OffsetDateTime.of(LocalDateTime.of(2018, Month.JANUARY, 1, 0, 0), ZoneOffset.UTC);
    private final static String BORROWER_A = "borrower_a";
    private final static String BORROWER_B = "borrower_b";
    private final static String BOOK = "Some book";
    private final static BorrowerData SAMPLE_BORROWER = new BorrowerData(1, "sample_borrower");
    private PersistenceLayer mockPersistenceLayer = Mockito.mock(PersistenceLayer.class);
    private LibraryUtils libraryUtils = LibraryUtils.createEmpty();

    /**
     * Take care of some stuff before we can get into the interesting work.
     *
     * We'll create a mock PersistenceLayer object that we can modify as need
     * be for our tests.  We'll also initialize our other "databases"
     */
    @Before
    public void init() {
        mockPersistenceLayer = Mockito.mock(PersistenceLayer.class);
        libraryUtils = initializeLibraryUtils(mockPersistenceLayer);
    }

    /**
     * If a borrower and a book are registered, a user should be able to borrow it.
     */
    @Test
    public void shouldLendToUser() {
        mockThatBorrowerExists();
        libraryUtils.registerBook(BOOK);
        final LibraryActionResults libraryActionResults = libraryUtils.lendBook(BOOK, BORROWER_A, BORROW_DATE);
        Assert.assertEquals(LibraryActionResults.SUCCESS, libraryActionResults);
    }

    /**
     * Just make sure that we get the expected response if we register a borrower
     */
    @Test
    public void shouldRegisterBorrower() {
        mockBorrowerNotRegistered(mockPersistenceLayer);
        final LibraryActionResults result = libraryUtils.registerBorrower(BORROWER_A);
        Assert.assertEquals(LibraryActionResults.SUCCESS, result);
    }

    private static void mockBorrowerNotRegistered(PersistenceLayer mockPersistenceLayer) {
        when(mockPersistenceLayer.searchBorrowerDataByName(BORROWER_A)).thenReturn(BorrowerData.createEmpty());
    }

    /**
     * Just make sure that we get the expected response if we register a book
     */
    @Test
    public void shouldRegisterBook() {
        final LibraryActionResults result = libraryUtils.registerBook(BOOK);
        Assert.assertEquals(LibraryActionResults.SUCCESS, result);
    }

    /**
     * We shouldn't be able to lend if we try to lend
     * to a borrower who isn't registered
     */
    @Test
    public void testShouldNotLendIfBorrowerNotRegistered() {
        libraryUtils.registerBook(BOOK);
        mockBorrowerNotRegistered(mockPersistenceLayer);
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
        mockThatBorrowerExists();
        // note that we aren't registering the book here
        final LibraryActionResults result = libraryUtils.lendBook(BOOK, BORROWER_A, BORROW_DATE);
        Assert.assertEquals(LibraryActionResults.BOOK_NOT_REGISTERED, result);
    }

    /**
     * b wants to check out a book, but a has it.  Can they borrow it? (no)
     */
    @Test
    public void shouldNotLendIfCurrentlyBorrowed() {
        mockThatBorrowerExists();
        libraryUtils.registerBook(BOOK);

        // Alice can check it out...
        final LibraryActionResults libraryActionResults_alice = libraryUtils.lendBook(BOOK, BORROWER_A, BORROW_DATE);
        Assert.assertEquals(LibraryActionResults.SUCCESS, libraryActionResults_alice);

        // But Bob cannot...
        final LibraryActionResults libraryActionResults_bob = libraryUtils.lendBook(BOOK, BORROWER_B, BORROW_DATE);
        Assert.assertEquals(LibraryActionResults.BOOK_CHECKED_OUT, libraryActionResults_bob);
    }

    /**
     * Mock out the call to search for a borrower - cause the system
     * to determine that we did indeed find a borrower.
     */
    private void mockThatBorrowerExists() {
        when(mockPersistenceLayer.searchBorrowerDataByName(Mockito.any()))
                .thenReturn(LendingTests.SAMPLE_BORROWER);
    }

    /**
     * Set up the databases, clear them, initialize the Library Utility with them.
     */
    private LibraryUtils initializeLibraryUtils(PersistenceLayer mockPersistenceLayer) {
        DatabaseUtils lendingDb = DatabaseUtils.obtainDatabaseAccess(DatabaseUtils.LIBRARY_LENDING_DATABASE);
        DatabaseUtils booksDb = DatabaseUtils.obtainDatabaseAccess(DatabaseUtils.LIBRARY_BOOKS_DATABASE_NAME);

        lendingDb.clearDatabaseContents();
        booksDb.clearDatabaseContents();

        return new LibraryUtils(mockPersistenceLayer, booksDb, lendingDb);
    }

}
