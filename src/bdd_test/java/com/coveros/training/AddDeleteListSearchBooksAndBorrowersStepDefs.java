package com.coveros.training;

import com.coveros.training.domainobjects.Book;
import com.coveros.training.domainobjects.Borrower;
import com.coveros.training.domainobjects.LibraryActionResults;
import com.coveros.training.domainobjects.Loan;
import com.coveros.training.persistence.LibraryUtils;
import com.coveros.training.persistence.PersistenceLayer;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;

import java.sql.Date;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.coveros.training.persistence.LibraryUtilsTests.generateListOfBooks;

public class AddDeleteListSearchBooksAndBorrowersStepDefs {

    private Book myBook = Book.createEmpty();
    private String myBookTitle = "";
    private Borrower myBorrower = Borrower.createEmpty();
    private String myBorrowerName = "";
    private final Date jan_1st = Date.valueOf(LocalDate.of(2018, Month.JANUARY, 1));
    private LibraryUtils libraryUtils = LibraryUtils.createEmpty();
    private final Date Jan_2nd = Date.valueOf(LocalDate.of(2018, Month.JANUARY, 2));
    private LibraryActionResults libraryActionResults = LibraryActionResults.NULL;
    private PersistenceLayer pl = new PersistenceLayer();
    private List<Book> allBooks = new ArrayList<>();

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
        Assert.assertEquals(LibraryActionResults.SUCCESS, libraryActionResults);
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
        final Book book = new Book(1, myBookTitle);
        libraryActionResults = libraryUtils.deleteBook(book);
    }

    @Then("the system does not have the book registered")
    public void the_system_does_not_have_the_book_registered() {
        myBook = libraryUtils.searchForBookByTitle(myBook.title);
        Assert.assertTrue(myBook.isEmpty());
    }

    @Then("the system reports an error indicating that the book is already registered")
    public void the_system_reports_an_error_indicating_that_TheBookIsAlreadyRegistered() {
        Assert.assertEquals(LibraryActionResults.ALREADY_REGISTERED_BOOK, libraryActionResults);
    }


    @Then("the system reports an error indicating that the book cannot be deleted because it was never registered")
    public void theSystemReportsAnErrorIndicatingThatTheBookCannotBeDeletedBecauseItWasNeverRegistered() {
        Assert.assertEquals(LibraryActionResults.NON_REGISTERED_BOOK_CANNOT_BE_DELETED, libraryActionResults);
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
        Assert.assertEquals(borrower.name, myBorrowerName);
        Assert.assertTrue(borrower.id > 0);
        Assert.assertEquals(LibraryActionResults.SUCCESS, libraryActionResults);
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

    @Then("that list is returned")
    public void that_list_is_returned() {
        final List<Book> expectedBooks = generateListOfBooks(new String[]{"a", "b", "c"});
        Assert.assertEquals(expectedBooks, allBooks);
    }

    @When("a librarian searches by that title")
    public void a_librarian_searches_by_that_title() {
        myBook = libraryUtils.searchForBookByTitle(myBookTitle);
    }

    @Then("the system returns its full data")
    public void the_system_returns_its_full_data() {
        Assert.assertEquals(myBookTitle, myBook.title);
        Assert.assertTrue(myBook.id > 0);
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

    @Then("the system returns an empty result")
    public void the_system_returns_an_empty_result() {
        Assert.assertTrue(myBook.isEmpty());
    }

    @When("a librarian searches for a book by title of {string}")
    public void a_librarian_searches_for_a_book_by_title_of(String bookTitle) {
        myBook = libraryUtils.searchForBookByTitle(bookTitle);
    }

    @Then("the system reports that there are no books in the system")
    public void the_system_reports_that_there_are_no_books_in_the_system() {
        Assert.assertTrue(allBooks.isEmpty());
    }

    @Given("a library with the following borrowers registered: {string}, {string}, {string}")
    public void a_library_with_the_following_borrowers_registered(String string, String string2, String string3) {
        // Write code here that turns the phrase above into concrete actions
        throw new cucumber.api.PendingException();
    }

    @When("a librarian lists all the registered borrowers")
    public void a_librarian_lists_all_the_registered_borrowers() {
        // Write code here that turns the phrase above into concrete actions
        throw new cucumber.api.PendingException();
    }

    @When("a librarian searches by that name")
    public void a_librarian_searches_by_that_name() {
        // Write code here that turns the phrase above into concrete actions
        throw new cucumber.api.PendingException();
    }

    @Given("a borrower, {string}, with id of {int}, is currently registered in the system")
    public void a_borrower_with_id_of_is_currently_registered_in_the_system(String string, Integer int1) {
        // Write code here that turns the phrase above into concrete actions
        throw new cucumber.api.PendingException();
    }

    @Given("no borrowers are registered in the system")
    public void no_borrowers_are_registered_in_the_system() {
        // Write code here that turns the phrase above into concrete actions
        throw new cucumber.api.PendingException();
    }

    @When("a librarian searches for a borrower by id {int}")
    public void a_librarian_searches_for_a_borrower_by_id(Integer int1) {
        // Write code here that turns the phrase above into concrete actions
        throw new cucumber.api.PendingException();
    }

    @Then("the system reports that there are no borrowers with that id")
    public void the_system_reports_that_there_are_no_borrowers_with_that_id() {
        // Write code here that turns the phrase above into concrete actions
        throw new cucumber.api.PendingException();
    }

    @When("a librarian searches for a borrower by name of {string}")
    public void a_librarian_searches_for_a_borrower_by_name_of(String string) {
        // Write code here that turns the phrase above into concrete actions
        throw new cucumber.api.PendingException();
    }

    @Then("the system reports that there are no borrowers found with that name")
    public void the_system_reports_that_there_are_no_borrowers_found_with_that_name() {
        // Write code here that turns the phrase above into concrete actions
        throw new cucumber.api.PendingException();
    }

    @When("a librarian lists all the borrowers")
    public void a_librarian_lists_all_the_borrowers() {
        // Write code here that turns the phrase above into concrete actions
        throw new cucumber.api.PendingException();
    }

    @Then("the system reports that there are no borrowers in the system")
    public void the_system_reports_that_there_are_no_borrowers_in_the_system() {
        // Write code here that turns the phrase above into concrete actions
        throw new cucumber.api.PendingException();
    }

    @Then("the system reports an error indicating that the borrower is already registered")
    public void the_system_reports_an_error_indicating_that_the_borrower_is_already_registered() {
        Assert.assertEquals(LibraryActionResults.ALREADY_REGISTERED_BORROWER, libraryActionResults);
    }

    @Then("the system reports an error indicating that the borrower cannot be deleted because he or she was never registered")
    public void the_system_reports_an_error_indicating_that_the_borrower_cannot_be_deleted_because_he_or_she_was_never_registered() {
        Assert.assertEquals(LibraryActionResults.NON_REGISTERED_BORROWER_CANNOT_BE_DELETED, libraryActionResults);
    }

    @When("a librarian searches by that id")
    public void a_librarian_searches_by_that_id() {
        // Write code here that turns the phrase above into concrete actions
        throw new cucumber.api.PendingException();
    }

}
