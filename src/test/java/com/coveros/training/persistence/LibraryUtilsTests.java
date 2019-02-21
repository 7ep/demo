package com.coveros.training.persistence;

import com.coveros.training.domainobjects.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.sql.Date;
import java.time.LocalDate;
import java.time.Month;

import static org.mockito.internal.verification.VerificationModeFactory.times;

public class LibraryUtilsTests {

    private LibraryUtils libraryUtils;
    private PersistenceLayer mockPersistenceLayer;
    private final Book DEFAULT_BOOK = BookTests.createTestBook();
    private final Borrower DEFAULT_BORROWER = BorrowerTests.createTestBorrower();
    private final static Date BORROW_DATE = Date.valueOf(LocalDate.of(2018, Month.JANUARY, 1));


    @Before
    public void init() {
        mockPersistenceLayer = Mockito.mock(PersistenceLayer.class);
        libraryUtils = Mockito.spy(new LibraryUtils(mockPersistenceLayer));
    }

    @Test
    public void testCanCreateEmpty() {
        final LibraryUtils libraryUtils = LibraryUtils.createEmpty();
        Assert.assertTrue(libraryUtils.isEmpty());
    }

    @Test
    public void testCanLendBook() {
        Mockito.doReturn(Loan.createEmpty()).when(libraryUtils).searchForLoan(DEFAULT_BOOK);

        final LibraryActionResults libraryActionResults =
                libraryUtils.lendBook(DEFAULT_BOOK, DEFAULT_BORROWER, BORROW_DATE);

        Assert.assertEquals(LibraryActionResults.SUCCESS, libraryActionResults);
    }

    /**
     * Same as {@link #testCanLendBook()} but we're hitting its wrapper.
     */
    @Test
    public void testCanLendBook_wrapperMethod() {
        Mockito.doReturn(Loan.createEmpty()).when(libraryUtils).searchForLoan(DEFAULT_BOOK);
        Mockito.doReturn(DEFAULT_BOOK).when(libraryUtils).searchForBookByTitle(DEFAULT_BOOK.title);
        Mockito.doReturn(DEFAULT_BORROWER).when(libraryUtils).searchForBorrowerByName(DEFAULT_BORROWER.name);

        final LibraryActionResults libraryActionResults =
                libraryUtils.lendBook(DEFAULT_BOOK.title, DEFAULT_BORROWER.name, BORROW_DATE);

        Assert.assertEquals(LibraryActionResults.SUCCESS, libraryActionResults);
    }

    @Test
    public void testCanRegisterBorrower() {
        Mockito.doReturn(Borrower.createEmpty()).when(libraryUtils).searchForBorrowerByName(DEFAULT_BORROWER.name);

        final LibraryActionResults libraryActionResults
                = libraryUtils.registerBorrower(DEFAULT_BORROWER.name);

        Assert.assertEquals(LibraryActionResults.SUCCESS, libraryActionResults);
    }

    @Test
    public void testCanRegisterBook() {
        Mockito.doReturn(Book.createEmpty()).when(libraryUtils).searchForBookByTitle(DEFAULT_BOOK.title);

        final LibraryActionResults libraryActionResults = libraryUtils.registerBook(DEFAULT_BOOK.title);

        Assert.assertEquals(LibraryActionResults.SUCCESS, libraryActionResults);
    }

    @Test
    public void testCanSearchForLoan() {
        libraryUtils.searchForLoan(DEFAULT_BOOK);
        Mockito.verify(mockPersistenceLayer, times(1)).searchForLoan(DEFAULT_BOOK);
    }

    @Test
    public void testCanSearchForBorrowerByName() {
        libraryUtils.searchForBorrowerByName(DEFAULT_BORROWER.name);
        Mockito.verify(mockPersistenceLayer, times(1)).searchBorrowerDataByName(DEFAULT_BORROWER.name);
    }

    @Test
    public void testCanSearchForBooksByTitle() {
        libraryUtils.searchForBookByTitle(DEFAULT_BOOK.title);
        Mockito.verify(mockPersistenceLayer, times(1)).searchBooksByTitle(DEFAULT_BOOK.title);
    }


}
