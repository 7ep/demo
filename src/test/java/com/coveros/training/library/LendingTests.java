package com.coveros.training.library;

import com.coveros.training.library.domainobjects.Book;
import com.coveros.training.library.domainobjects.Borrower;
import com.coveros.training.library.domainobjects.LibraryActionResults;
import com.coveros.training.library.domainobjects.Loan;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.sql.Date;
import java.time.LocalDate;
import java.time.Month;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;

public class LendingTests {


    private final static Date BORROW_DATE = Date.valueOf(LocalDate.of(2018, Month.JANUARY, 1));
    private final static String BORROWER_A_NAME = "borrower_a";
    private final static String BORROWER_B_NAME = "borrower_b";
    private final static String TITLE = "Some book";
    private final static Book SAMPLE_BOOK = new Book(1, TITLE);
    private final static Borrower SAMPLE_BORROWER_A = new Borrower(1, BORROWER_A_NAME);
    private final static Borrower SAMPLE_BORROWER_B = new Borrower(1, BORROWER_B_NAME);
    private final LibraryUtils libraryUtils = Mockito.spy(LibraryUtils.class);

    /**
     * If a borrower and a book are registered, a user should be able to borrow it.
     */
    @Test
    public void shouldLendToUser() {
        mockSearchForLoan();
        mockThatLoanIsCreated(SAMPLE_BOOK, SAMPLE_BORROWER_A, BORROW_DATE);
        final LibraryActionResults libraryActionResults = libraryUtils.lendBook(SAMPLE_BOOK, SAMPLE_BORROWER_A, BORROW_DATE);
        Assert.assertEquals(LibraryActionResults.SUCCESS, libraryActionResults);
    }

    private void mockThatLoanIsCreated(Book book, Borrower borrower, Date borrowDate) {
        Mockito.doNothing().when(libraryUtils).createLoan(book, borrower, borrowDate);
    }

    private void mockSearchForLoan() {
        Mockito.doReturn(Loan.createEmpty()).when(libraryUtils).searchForLoanByBook(SAMPLE_BOOK);
    }

    /**
     * Just make sure that we get the expected response if we register a borrower
     */
    @Test
    public void shouldRegisterBorrower() {
        mockBorrowerNotRegistered(BORROWER_A_NAME);
        mockThatNewBorrowerGetsSaved(BORROWER_A_NAME);
        final LibraryActionResults result = libraryUtils.registerBorrower(BORROWER_A_NAME);
        Assert.assertEquals(LibraryActionResults.SUCCESS, result);
    }

    private void mockThatNewBorrowerGetsSaved(String borrowerAName) {
        doNothing().when(libraryUtils).saveNewBorrower(borrowerAName);
    }

    private void mockBorrowerNotRegistered(String borrower) {
        doReturn(Borrower.createEmpty()).when(libraryUtils).searchForBorrowerByName(borrower);
    }

    /**
     * Just make sure that we get the expected response if we register a book
     */
    @Test
    public void shouldRegisterBook() {
        mockThatBookNotRegistered(TITLE);
        mockThatNewBookGetsSaved(TITLE);
        final LibraryActionResults result = libraryUtils.registerBook(TITLE);
        Assert.assertEquals(LibraryActionResults.SUCCESS, result);
    }

    private void mockThatNewBookGetsSaved(String title) {
        doNothing().when(libraryUtils).saveNewBook(title);
    }

    private void mockThatBookNotRegistered(String title) {
        doReturn(Book.createEmpty()).when(libraryUtils).searchForBookByTitle(title);
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
        mockSearchForLoan();
        Mockito.when(libraryUtils.searchForLoanByBook(SAMPLE_BOOK)).thenReturn(new Loan(SAMPLE_BOOK, SAMPLE_BORROWER_B, 1, BORROW_DATE));
        final LibraryActionResults libraryActionResults_bob = libraryUtils.lendBook(SAMPLE_BOOK, SAMPLE_BORROWER_B, BORROW_DATE);
        Assert.assertEquals(LibraryActionResults.BOOK_CHECKED_OUT, libraryActionResults_bob);
    }

}
