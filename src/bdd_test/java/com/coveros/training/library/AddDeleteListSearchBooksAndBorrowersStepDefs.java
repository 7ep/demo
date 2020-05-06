package com.coveros.training.library;

import com.coveros.training.library.domainobjects.Book;
import com.coveros.training.library.domainobjects.Borrower;
import com.coveros.training.library.domainobjects.LibraryActionResults;
import com.coveros.training.library.domainobjects.Loan;
import com.coveros.training.persistence.IPersistenceLayer;
import com.coveros.training.persistence.PersistenceLayer;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;

import java.sql.Date;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import static com.coveros.training.library.LibraryUtilsTests.generateListOfBooks;
import static com.coveros.training.library.LibraryUtilsTests.generateListOfBorrowers;
import static org.junit.Assert.assertEquals;

public class AddDeleteListSearchBooksAndBorrowersStepDefs {

    public static final String ALICE = "alice";
    public static final String DEVOPS_HANDBOOK = "devops handbook";
    private Book myBook = Book.createEmpty();
    private String myBookTitle = "";
    private Borrower myBorrower = Borrower.createEmpty();
    private String myBorrowerName = "";
    private final Date JAN_1ST = Date.valueOf(LocalDate.of(2018, Month.JANUARY, 1));
    private LibraryUtils libraryUtils = LibraryUtils.createEmpty();
    private final Date JAN_2ND = Date.valueOf(LocalDate.of(2018, Month.JANUARY, 2));
    private LibraryActionResults libraryActionResults = LibraryActionResults.NULL;
    private final IPersistenceLayer pl = new PersistenceLayer();
    private List<Book> allBooks = new ArrayList<>();
    private List<Borrower> allBorrowers = new ArrayList<>();

    /**
     * Set up the databases, clear them, initialize the Library Utility with them.
     */
    private void initializeEmptyDatabaseAndUtility() {
        pl.cleanAndMigrateDatabase();
        libraryUtils = new LibraryUtils();
    }

    @Given("a book, {string}, is not currently registered in the system")
    public void a_book_is_not_currently_registered_in_the_system(String bookTitle) {
        myBookTitle = bookTitle;
        initializeEmptyDatabaseAndUtility();
    }

    @When("a librarian registers that book")
    public void a_librarian_registers_that_book() {
        libraryActionResults = libraryUtils.registerBook(myBookTitle);
        myBook = libraryUtils.searchForBookByTitle(myBookTitle);
    }

    @Then("the system has the book registered")
    public void the_system_has_the_book_registered() {
        assertEquals(LibraryActionResults.SUCCESS, libraryActionResults);
        Assert.assertTrue(myBook.id > 0);
    }

    @Given("a book, {string}, is currently registered in the system")
    public void a_book_is_currently_registered_in_the_system(String bookTitle) {
        initializeEmptyDatabaseAndUtility();
        myBookTitle = bookTitle;
        libraryUtils.registerBook(bookTitle);
        myBook = libraryUtils.searchForBookByTitle(bookTitle);
    }

    @When("a librarian deletes that book")
    public void a_librarian_deletes_that_book() {
        myBook = new Book(1, myBookTitle);
        libraryActionResults = libraryUtils.deleteBook(myBook);
    }

    @Then("the system does not have the book registered")
    public void the_system_does_not_have_the_book_registered() {
        myBook = libraryUtils.searchForBookByTitle(myBook.title);
        Assert.assertTrue(myBook.isEmpty());
    }

    @Then("the system reports an error indicating that the book is already registered")
    public void the_system_reports_an_error_indicating_that_TheBookIsAlreadyRegistered() {
        assertEquals(LibraryActionResults.ALREADY_REGISTERED_BOOK, libraryActionResults);
    }


    @Then("the system reports an error indicating that the book cannot be deleted because it was never registered")
    public void theSystemReportsAnErrorIndicatingThatTheBookCannotBeDeletedBecauseItWasNeverRegistered() {
        assertEquals(LibraryActionResults.NON_REGISTERED_BOOK_CANNOT_BE_DELETED, libraryActionResults);
    }

    @Given("a borrower, {string}, is not currently registered in the system")
    public void a_borrower_is_not_currently_registered_in_the_system(String borrower) {
        myBorrowerName = borrower;
        initializeEmptyDatabaseAndUtility();
    }

    @When("a librarian registers that borrower")
    public void a_librarian_registers_that_borrower() {
        libraryActionResults = libraryUtils.registerBorrower(myBorrowerName);
    }

    @Then("the system has the borrower registered")
    public void the_system_has_the_borrower_registered() {
        final Borrower borrower = libraryUtils.searchForBorrowerByName(myBorrowerName);
        assertEquals(borrower.name, myBorrowerName);
        Assert.assertTrue(borrower.id > 0);
        assertEquals(LibraryActionResults.SUCCESS, libraryActionResults);
    }

    @Given("a borrower, {string}, is currently registered in the system")
    public void a_borrower_is_currently_registered_in_the_system(String borrowerName) {
        initializeEmptyDatabaseAndUtility();
        myBorrowerName = borrowerName;
        libraryActionResults = libraryUtils.registerBorrower(borrowerName);
    }

    @When("a librarian deletes that borrower")
    public void a_librarian_deletes_that_borrower() {
        final Borrower borrower = new Borrower(1, myBorrowerName);
        libraryActionResults = libraryUtils.deleteBorrower(borrower);
    }

    @Then("the system does not have the borrower registered")
    public void the_system_does_not_have_the_borrower_registered() {
        final Borrower borrower = libraryUtils.searchForBorrowerByName(myBorrowerName);
        Assert.assertTrue(borrower.isEmpty());
    }

    @Given("a library with the following books registered: a, b, c")
    public void a_library_with_the_following_books_registered() {
        initializeEmptyDatabaseAndUtility();
        libraryUtils.registerBook("a");
        libraryUtils.registerBook("b");
        libraryUtils.registerBook("c");
    }

    @When("a librarian lists all the registered books")
    public void a_librarian_lists_all_the_registered_books() {
        allBooks = libraryUtils.listAllBooks();
    }

    @Then("the whole list of books is returned")
    public void the_whole_list_of_books_is_returned() {
        final List<Book> expectedBooks = generateListOfBooks(new String[]{"a", "b", "c"});
        assertEquals(expectedBooks, allBooks);
    }

    @Then("the whole list of borrowers is returned")
    public void the_whole_list_of_borrowers_is_returned() {
        final List<Borrower> expectedBorrowers = generateListOfBorrowers(new String[]{"a", "b", "c"});
        assertEquals(expectedBorrowers, allBorrowers);
    }

    @When("a librarian searches by that title")
    public void a_librarian_searches_by_that_title() {
        myBook = libraryUtils.searchForBookByTitle(myBookTitle);
    }

    @Then("the system returns the book's full data")
    public void the_system_returns_the_books_full_data() {
        assertEquals(myBookTitle, myBook.title);
        Assert.assertTrue(myBook.id > 0);
    }

    @Then("the system returns the borrower's full data")
    public void the_system_returns_the_borrowers_full_data() {
        assertEquals(myBorrowerName, myBorrower.name);
        Assert.assertTrue(myBorrower.id > 0);
    }

    @When("a librarian searches by its id")
    public void a_librarian_searches_by_its_id() {
        myBook = libraryUtils.searchForBookById(myBook.id);
    }

    @Given("no books are registered in the system")
    public void no_books_are_registered_in_the_system() {
        initializeEmptyDatabaseAndUtility();
    }

    @When("a librarian searches for a book by id {int}")
    public void a_librarian_searches_for_a_book_by_id(Integer bookId) {
        myBook = libraryUtils.searchForBookById(bookId);
    }

    @Then("the system returns an empty result for the book")
    public void the_system_returns_an_empty_result_for_the_book() {
        Assert.assertTrue(myBook.isEmpty());
    }

    @Then("the system returns an empty result for the borrower")
    public void the_system_returns_an_empty_result_for_the_borrower() {
        Assert.assertTrue(myBorrower.isEmpty());
    }

    @When("a librarian searches for a book by title of {string}")
    public void a_librarian_searches_for_a_book_by_title_of(String bookTitle) {
        myBook = libraryUtils.searchForBookByTitle(bookTitle);
    }

    @Then("the system reports that there are no books in the system")
    public void the_system_reports_that_there_are_no_books_in_the_system() {
        Assert.assertTrue(allBooks.isEmpty());
    }

    @Given("a library with the following borrowers registered: a, b, c")
    public void a_library_with_the_following_borrowers_registered() {
        initializeEmptyDatabaseAndUtility();
        libraryUtils.registerBorrower("a");
        libraryUtils.registerBorrower("b");
        libraryUtils.registerBorrower("c");
    }

    @When("a librarian lists all the registered borrowers")
    public void a_librarian_lists_all_the_registered_borrowers() {
        allBorrowers = libraryUtils.listAllBorrowers();
    }

    @When("a librarian searches by that name")
    public void a_librarian_searches_by_that_name() {
        myBorrower = libraryUtils.searchForBorrowerByName(myBorrowerName);
    }

    @Given("no borrowers are registered in the system")
    public void no_borrowers_are_registered_in_the_system() {
        initializeEmptyDatabaseAndUtility();
    }

    @When("a librarian searches for a borrower by id {int}")
    public void a_librarian_searches_for_a_borrower_by_id(Integer id) {
        myBorrower = libraryUtils.searchForBorrowerById(id);
    }

    @Then("the system reports that there are no borrowers with that id")
    public void the_system_reports_that_there_are_no_borrowers_with_that_id() {
        Assert.assertTrue(myBorrower.isEmpty());
    }

    @When("a librarian searches for a borrower by name of {string}")
    public void a_librarian_searches_for_a_borrower_by_name_of(String name) {
        myBorrower = libraryUtils.searchForBorrowerByName(name);
    }

    @Then("the system reports that there are no borrowers found with that name")
    public void the_system_reports_that_there_are_no_borrowers_found_with_that_name() {
        Assert.assertTrue(myBorrower.isEmpty());
    }

    @When("a librarian lists all the borrowers")
    public void a_librarian_lists_all_the_borrowers() {
        allBorrowers = libraryUtils.listAllBorrowers();
    }

    @Then("the system returns an empty list of borrowers")
    public void the_system_returns_an_empty_list_of_borrowers() {
        Assert.assertTrue(allBorrowers.isEmpty());
    }

    @Then("the system reports an error indicating that the borrower is already registered")
    public void the_system_reports_an_error_indicating_that_the_borrower_is_already_registered() {
        assertEquals(LibraryActionResults.ALREADY_REGISTERED_BORROWER, libraryActionResults);
    }

    @Then("the system reports an error indicating that the borrower cannot be deleted because he or she was never registered")
    public void the_system_reports_an_error_indicating_that_the_borrower_cannot_be_deleted_because_he_or_she_was_never_registered() {
        assertEquals(LibraryActionResults.NON_REGISTERED_BORROWER_CANNOT_BE_DELETED, libraryActionResults);
    }

    @When("a librarian searches by that id")
    public void a_librarian_searches_by_that_id() {
        final Borrower tempBorrower = libraryUtils.searchForBorrowerByName(myBorrowerName);
        myBorrower = libraryUtils.searchForBorrowerById(tempBorrower.id);
    }

    @Given("a book, {string}, is currently loaned out")
    public void aBookIsCurrentlyLoanedOut(String bookTitle) {
        initializeEmptyDatabaseAndUtility();
        libraryUtils.registerBorrower(ALICE);
        libraryUtils.registerBook(bookTitle);
        myBookTitle = bookTitle;
        libraryUtils.lendBook(bookTitle, ALICE, JAN_1ST);
    }

    @Given("a book is currently loaned out to {string}")
    public void aBookIsCurrentlyLoanedOutTo(String borrowerName) {
        initializeEmptyDatabaseAndUtility();
        libraryUtils.registerBorrower(borrowerName);
        myBorrowerName = borrowerName;
        libraryUtils.registerBook(DEVOPS_HANDBOOK);
        libraryUtils.lendBook(DEVOPS_HANDBOOK, borrowerName, JAN_1ST);
    }

    @Given("a book is loaned to {string}")
    public void aBookIsLoanedTo(String borrowerName) {
        myBorrowerName = borrowerName;
        initializeEmptyDatabaseAndUtility();
        libraryUtils.registerBook(DEVOPS_HANDBOOK);
        libraryUtils.registerBorrower(borrowerName);
        libraryUtils.lendBook(DEVOPS_HANDBOOK, borrowerName, JAN_2ND);
        myBook = libraryUtils.searchForBookByTitle(DEVOPS_HANDBOOK);
    }

    @Then("the loan is deleted as well")
    public void theLoanIsDeletedAsWell() {
        final Loan loan = libraryUtils.searchForLoanByBook(myBook);
        Assert.assertTrue(loan.isEmpty());
    }

    @Given("a book, {string}, is loaned out")
    public void aBookIsLoanedOut(String bookTitle) {
        initializeEmptyDatabaseAndUtility();
        myBookTitle = bookTitle;
        libraryUtils.registerBook(myBookTitle);
        libraryUtils.registerBorrower(ALICE);
        libraryUtils.lendBook(bookTitle, ALICE, JAN_1ST);
    }

    @Given("some books are checked out")
    public void some_books_are_checked_out() {
        initializeEmptyDatabaseAndUtility();
        // register a, b, and c
        libraryUtils.registerBook("a");
        libraryUtils.registerBook("b");
        libraryUtils.registerBook("c");
        libraryUtils.registerBorrower("someone");

        // loan out b
        libraryUtils.lendBook("b", "someone", JAN_1ST);
    }

    @When("a librarian lists the available books")
    public void a_librarian_lists_the_available_books() {
        allBooks = libraryUtils.listAvailableBooks();
    }

    @Then("the system responds with only the available books")
    public void the_system_responds_with_only_the_available_books() {
        List<Book> expectedBooks = new ArrayList<>();
        expectedBooks.add(new Book(1, "a"));
        expectedBooks.add(new Book(3, "c"));
        assertEquals(expectedBooks, allBooks);
    }
}
