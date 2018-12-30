package com.coveros.training;

import com.coveros.training.domainobjects.Book;
import com.coveros.training.domainobjects.Borrower;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.sql.Date;
import java.time.*;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LendingTests {


    private final static Date BORROW_DATE = Date.valueOf(LocalDate.of(2018, Month.JANUARY, 1));
    private final static String BORROWER_A_NAME = "borrower_a";
    private final static String BORROWER_B_NAME = "borrower_b";
    private final static String TITLE = "Some book";
    private final static Book SAMPLE_BOOK = new Book(TITLE, 1);
    private final static Borrower SAMPLE_BORROWER_A = new Borrower(1, BORROWER_A_NAME);
    private final static Borrower SAMPLE_BORROWER_B = new Borrower(1, BORROWER_B_NAME);
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
        Mockito.when(mockPersistenceLayer.searchForLoan(SAMPLE_BOOK)).thenReturn(Loan.createEmpty());
        final LibraryActionResults libraryActionResults = libraryUtils.lendBook(SAMPLE_BOOK, SAMPLE_BORROWER_A, BORROW_DATE);
        Assert.assertEquals(LibraryActionResults.SUCCESS, libraryActionResults);
    }

    /**
     * Just make sure that we get the expected response if we register a borrower
     */
    @Test
    public void shouldRegisterBorrower() {
        mockBorrowerNotRegistered(mockPersistenceLayer);
        final LibraryActionResults result = libraryUtils.registerBorrower(BORROWER_A_NAME);
        Assert.assertEquals(LibraryActionResults.SUCCESS, result);
    }

    private static void mockBorrowerNotRegistered(PersistenceLayer mockPersistenceLayer) {
        when(mockPersistenceLayer.searchBorrowerDataByName(BORROWER_A_NAME)).thenReturn(Borrower.createEmpty());
    }

    /**
     * Just make sure that we get the expected response if we register a book
     */
    @Test
    public void shouldRegisterBook() {
        mockThatBookNotRegistered();
        final LibraryActionResults result = libraryUtils.registerBook(TITLE);
        Assert.assertEquals(LibraryActionResults.SUCCESS, result);
    }

    private void mockThatBookNotRegistered() {
        Mockito.when(mockPersistenceLayer.searchBooksByTitle(TITLE)).thenReturn(Book.createEmpty());
    }

    /**
     * We shouldn't be able to lend if we try to lend
     * to a borrower who isn't registered
     */
    @Test
    public void testShouldNotLendIfBorrowerNotRegistered() {
        final LibraryActionResults result = libraryUtils.lendBook(SAMPLE_BOOK, Borrower.createEmpty(), BORROW_DATE);
        Assert.assertEquals(LibraryActionResults.BORROWER_NOT_REGISTERED, result);
    }

    /**
     * We shouldn't be able to lend if we try to lend
     * out a book that isn't registered
     */
    @Test
    public void testShouldNotLendIfBookNotRegistered() {
        final LibraryActionResults result = libraryUtils.lendBook(Book.createEmpty(), SAMPLE_BORROWER_A, BORROW_DATE);
        Assert.assertEquals(LibraryActionResults.BOOK_NOT_REGISTERED, result);
    }

    /**
     * b wants to check out a book, but a has it.  Can they borrow it? (no)
     */
    @Test
    public void shouldNotLendIfCurrentlyBorrowed() {
        Mockito.when(mockPersistenceLayer.searchForLoan(SAMPLE_BOOK)).thenReturn( new Loan(SAMPLE_BOOK, SAMPLE_BORROWER_B, 1, BORROW_DATE));
        final LibraryActionResults libraryActionResults_bob = libraryUtils.lendBook(SAMPLE_BOOK, SAMPLE_BORROWER_B, BORROW_DATE);
        Assert.assertEquals(LibraryActionResults.BOOK_CHECKED_OUT, libraryActionResults_bob);
    }

    /**
     * Set up the databases, clear them, initialize the Library Utility with them.
     */
    private LibraryUtils initializeLibraryUtils(PersistenceLayer mockPersistenceLayer) {
        return new LibraryUtils(mockPersistenceLayer);
    }

}
