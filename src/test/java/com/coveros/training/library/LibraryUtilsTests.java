package com.coveros.training.library;

import com.coveros.training.library.domainobjects.*;
import com.coveros.training.persistence.IPersistenceLayer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mockito;

import java.sql.Date;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.internal.verification.VerificationModeFactory.times;

public class LibraryUtilsTests {

    private IPersistenceLayer mockPersistenceLayer = Mockito.mock(IPersistenceLayer.class);
    private LibraryUtils libraryUtils = Mockito.spy(new LibraryUtils(mockPersistenceLayer));

    private final Book DEFAULT_BOOK = BookTests.createTestBook();
    private final Borrower DEFAULT_BORROWER = BorrowerTests.createTestBorrower();
    private final static Date BORROW_DATE = Date.valueOf(LocalDate.of(2018, Month.JANUARY, 1));


    @Before
    public void init() {
        mockPersistenceLayer = Mockito.mock(IPersistenceLayer.class);
        libraryUtils = Mockito.spy(new LibraryUtils(mockPersistenceLayer));
    }

    @Test
    public void testCanCreateEmpty() {
        final LibraryUtils libraryUtils = LibraryUtils.createEmpty();
        Assert.assertTrue(libraryUtils.isEmpty());
    }

    @Test
    public void testCanLendBook() {
        Mockito.doReturn(Loan.createEmpty()).when(libraryUtils).searchForLoanByBook(DEFAULT_BOOK);

        final LibraryActionResults libraryActionResults =
                libraryUtils.lendBook(DEFAULT_BOOK, DEFAULT_BORROWER, BORROW_DATE);

        Assert.assertEquals(LibraryActionResults.SUCCESS, libraryActionResults);
    }

    /**
     * Same as {@link #testCanLendBook()} but we're hitting its wrapper.
     */
    @Test
    public void testCanLendBook_wrapperMethod() {
        Mockito.doReturn(Loan.createEmpty()).when(libraryUtils).searchForLoanByBook(DEFAULT_BOOK);
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

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    /**
     * Makes no sense to allow registering a book with an empty string.
     * Throw an exception, since it's probably a dev error in that case - it
     * should never have been allowed to occur, by the developer.
     */
    @Test
    public void testCannotRegisterBookWithEmptyString() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("bookTitle was an empty string - disallowed when registering books");

        libraryUtils.registerBook("");
    }

    @Test
    public void testCanSearchForLoanByBook() {
        libraryUtils.searchForLoanByBook(DEFAULT_BOOK);
        Mockito.verify(mockPersistenceLayer, times(1)).searchForLoanByBook(DEFAULT_BOOK);
    }

    @Test
    public void testCanSearchForLoanByBorrower() {
        libraryUtils.searchForLoanByBorrower(DEFAULT_BORROWER);
        Mockito.verify(mockPersistenceLayer, times(1)).searchForLoanByBorrower(DEFAULT_BORROWER);
    }

    @Test
    public void testCanSearchForBorrowerByName() {
        libraryUtils.searchForBorrowerByName(DEFAULT_BORROWER.name);
        Mockito.verify(mockPersistenceLayer, times(1)).searchBorrowerDataByName(DEFAULT_BORROWER.name);
    }

    @Test
    public void testCanSearchForBooksByTitle() {
        Mockito.when(mockPersistenceLayer.searchBooksByTitle(DEFAULT_BOOK.title)).thenReturn(Optional.of(DEFAULT_BOOK));
        libraryUtils.searchForBookByTitle(DEFAULT_BOOK.title);
        Mockito.verify(mockPersistenceLayer, times(1)).searchBooksByTitle(DEFAULT_BOOK.title);
    }

    /**
     * We don't allow to search by empty string.
     */
    @Test
    public void testShouldThrowExceptionWhenSearchingWithEmptyStringAsBookTitle() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("when searching for a book, must include a non-empty string for title");

        libraryUtils.searchForBookByTitle("");
    }

    @Test
    public void testCanSearchForBooksById() {
        Mockito.when(mockPersistenceLayer.searchBooksById(DEFAULT_BOOK.id)).thenReturn(Optional.of(DEFAULT_BOOK));
        libraryUtils.searchForBookById(DEFAULT_BOOK.id);
        Mockito.verify(mockPersistenceLayer, times(1)).searchBooksById(DEFAULT_BOOK.id);
    }

    /**
     * The id of the book must be 1 or greater.  0 isn't allowed, nor anything else below 1.
     */
    @Test
    public void testShouldThrowExceptionWhenSearchingWithLessThanOneAsBookId() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("when searching for a book, must include an id of one or greater");

        libraryUtils.searchForBookById(0);
    }

    /**
     * Basic happy path - delete a book that is registered.
     */
    @Test
    public void testCanDeleteBook() {
        Mockito.when(mockPersistenceLayer.searchBooksByTitle(DEFAULT_BOOK.title)).thenReturn(Optional.of(DEFAULT_BOOK));

        final LibraryActionResults result = libraryUtils.deleteBook(DEFAULT_BOOK);

        Mockito.verify(mockPersistenceLayer, times(1)).deleteBook(DEFAULT_BOOK.id);
        Assert.assertEquals(LibraryActionResults.SUCCESS, result);
    }

    /**
     * If a book isn't registered, then obviously we cannot delete it.
     */
    @Test
    public void testCannotDeleteNonRegisteredBook() {
        Mockito.when(mockPersistenceLayer.searchBooksByTitle(DEFAULT_BOOK.title)).thenReturn(Optional.empty());

        final LibraryActionResults result = libraryUtils.deleteBook(DEFAULT_BOOK);

        Mockito.verify(mockPersistenceLayer, times(0)).deleteBook(DEFAULT_BOOK.id);
        Assert.assertEquals(LibraryActionResults.NON_REGISTERED_BOOK_CANNOT_BE_DELETED, result);
    }

    /**
     * Basic happy path - delete a borrower who is registered.
     */
    @Test
    public void testCanDeleteBorrower() {
        Mockito.when(mockPersistenceLayer.searchBorrowerDataByName(DEFAULT_BORROWER.name)).thenReturn(Optional.of(DEFAULT_BORROWER));

        final LibraryActionResults result = libraryUtils.deleteBorrower(DEFAULT_BORROWER);

        Mockito.verify(mockPersistenceLayer, times(1)).deleteBorrower(DEFAULT_BORROWER.id);
        Assert.assertEquals(LibraryActionResults.SUCCESS, result);
    }

    /**
     * If a borrower isn't registered, then obviously we cannot delete them.
     */
    @Test
    public void testCannotDeleteNonRegisteredBorrower() {
        Mockito.when(mockPersistenceLayer.searchBorrowerDataByName(DEFAULT_BORROWER.name)).thenReturn(Optional.empty());

        final LibraryActionResults result = libraryUtils.deleteBorrower(DEFAULT_BORROWER);

        Mockito.verify(mockPersistenceLayer, times(0)).deleteBorrower(DEFAULT_BORROWER.id);
        Assert.assertEquals(LibraryActionResults.NON_REGISTERED_BORROWER_CANNOT_BE_DELETED, result);
    }

    /**
     * A test to cover the action of listing all the books in the database.
     * Perhaps unusual, given how large libraries get, but we're going MVP style - minimum
     * viable product.  At early stages we may develop features that later on become obsolete.
     */
    @Test
    public void testShouldBeAbleToListAllBooks() {
        final List<Book> books = generateListOfBooks(new String[]{"foo", "bar"});
        Mockito.when(mockPersistenceLayer.listAllBooks()).thenReturn(Optional.of(books));

        List<Book> bookList = libraryUtils.listAllBooks();

        Assert.assertEquals(books, bookList);
    }

    /**
     * A test to cover the action of listing all the borrowers in the database.
     * Perhaps unusual, given how large libraries get, but we're going MVP style - minimum
     * viable product.  At early stages we may develop features that later on become obsolete.
     */
    @Test
    public void testShouldBeAbleToListAllBorrowers() {
        final List<Borrower> borrowers = generateListOfBorrowers(new String[]{"foo", "bar"});
        Mockito.when(mockPersistenceLayer.listAllBorrowers()).thenReturn(Optional.of(borrowers));
        List<Borrower> borrowerList = libraryUtils.listAllBorrowers();
        Assert.assertEquals(borrowers, borrowerList);
    }

    @Test
    public void testShouldListAvailableBooks() {
        libraryUtils.listAvailableBooks();
        Mockito.verify(mockPersistenceLayer).listAvailableBooks();
    }

    /**
     * A helper function to generate a list of books, given a list of titles.
     */
    public static List<Book> generateListOfBooks(String[] bookTitles) {
        ArrayList<Book> bookList = new ArrayList<>();
        int id = 1;
        for(String s : bookTitles) {
            bookList.add(new Book(id, s));
            id++;
        }
        return bookList;
    }


    /**
     * A helper function to generate a list of borrowers, given a list of names.
     */
    public static List<Borrower> generateListOfBorrowers(String[] names) {
        ArrayList<Borrower> borrowerList = new ArrayList<>();
        int id = 1;
        for(String s : names) {
            borrowerList.add(new Borrower(id, s));
            id++;
        }
        return borrowerList;
    }


}
