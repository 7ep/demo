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

public class AddDeleteListSearchBooksAndBorrowersStepDefs {

    private Book myBook = Book.createEmpty();
    private Borrower myBorrower = Borrower.createEmpty();
    private final Date jan_1st = Date.valueOf(LocalDate.of(2018, Month.JANUARY, 1));
    private LibraryUtils libraryUtils = LibraryUtils.createEmpty();
    private final Date Jan_2nd = Date.valueOf(LocalDate.of(2018, Month.JANUARY, 2));
    private LibraryActionResults libraryActionResults = LibraryActionResults.NULL;
    private PersistenceLayer pl = new PersistenceLayer();

    /**
     * Set up the databases, clear them, initialize the Library Utility with them.
     */
    private void initializeEmptyDatabaseAndUtility() {
        pl.cleanAndMigrateDatabase();
        libraryUtils = new LibraryUtils();
    }

    @Given("a book, {string}, is not currently registered in the system")
    public void a_book_is_not_currently_registered_in_the_system(String string) {
        // Write code here that turns the phrase above into concrete actions
        throw new cucumber.api.PendingException();
    }

    @When("a librarian registers that book")
    public void a_librarian_registers_that_book() {
        // Write code here that turns the phrase above into concrete actions
        throw new cucumber.api.PendingException();
    }

    @Then("the system has the book registered")
    public void the_system_has_the_book_registered() {
        // Write code here that turns the phrase above into concrete actions
        throw new cucumber.api.PendingException();
    }

    @Given("a book, {string}, is currently registered in the system")
    public void a_book_is_currently_registered_in_the_system(String string) {
        // Write code here that turns the phrase above into concrete actions
        throw new cucumber.api.PendingException();
    }

    @When("a librarian deletes that book")
    public void a_librarian_deletes_that_book() {
        // Write code here that turns the phrase above into concrete actions
        throw new cucumber.api.PendingException();
    }

    @Then("the system does not have the book registered")
    public void the_system_does_not_have_the_book_registered() {
        // Write code here that turns the phrase above into concrete actions
        throw new cucumber.api.PendingException();
    }

    @Then("the system reports an error indicating that {string}")
    public void the_system_reports_an_error_indicating_that(String string) {
        // Write code here that turns the phrase above into concrete actions
        throw new cucumber.api.PendingException();
    }

    @Given("a borrower, {string}, is not currently registered in the system")
    public void a_borrower_is_not_currently_registered_in_the_system(String string) {
        // Write code here that turns the phrase above into concrete actions
        throw new cucumber.api.PendingException();
    }

    @When("a librarian registers that borrower")
    public void a_librarian_registers_that_borrower() {
        // Write code here that turns the phrase above into concrete actions
        throw new cucumber.api.PendingException();
    }

    @Then("the system has the borrower registered")
    public void the_system_has_the_borrower_registered() {
        // Write code here that turns the phrase above into concrete actions
        throw new cucumber.api.PendingException();
    }

    @Given("a borrower, {string}, is currently registered in the system")
    public void a_borrower_is_currently_registered_in_the_system(String string) {
        // Write code here that turns the phrase above into concrete actions
        throw new cucumber.api.PendingException();
    }

    @When("a librarian deletes that borrower")
    public void a_librarian_deletes_that_borrower() {
        // Write code here that turns the phrase above into concrete actions
        throw new cucumber.api.PendingException();
    }

    @Then("the system does not have the borrower registered")
    public void the_system_does_not_have_the_borrower_registered() {
        // Write code here that turns the phrase above into concrete actions
        throw new cucumber.api.PendingException();
    }

    @Given("a library with the following books registered: {string}, {string}, {string}")
    public void a_library_with_the_following_books_registered(String string, String string2, String string3) {
        // Write code here that turns the phrase above into concrete actions
        throw new cucumber.api.PendingException();
    }

    @When("a librarian lists all the registered books")
    public void a_librarian_lists_all_the_registered_books() {
        // Write code here that turns the phrase above into concrete actions
        throw new cucumber.api.PendingException();
    }

    @Then("that list is returned")
    public void that_list_is_returned() {
        // Write code here that turns the phrase above into concrete actions
        throw new cucumber.api.PendingException();
    }

    @When("a librarian searches by that title")
    public void a_librarian_searches_by_that_title() {
        // Write code here that turns the phrase above into concrete actions
        throw new cucumber.api.PendingException();
    }

    @Then("the system returns its full data")
    public void the_system_returns_its_full_data() {
        // Write code here that turns the phrase above into concrete actions
        throw new cucumber.api.PendingException();
    }

    @Given("a book, {string}, with id of {int}, is currently registered in the system")
    public void a_book_with_id_of_is_currently_registered_in_the_system(String string, Integer int1) {
        // Write code here that turns the phrase above into concrete actions
        throw new cucumber.api.PendingException();
    }

    @When("a librarian searches by that id")
    public void a_librarian_searches_by_that_id() {
        // Write code here that turns the phrase above into concrete actions
        throw new cucumber.api.PendingException();
    }

    @Given("no books are registered in the system")
    public void no_books_are_registered_in_the_system() {
        // Write code here that turns the phrase above into concrete actions
        throw new cucumber.api.PendingException();
    }

    @When("a librarian searches for a book by id {int}")
    public void a_librarian_searches_for_a_book_by_id(Integer int1) {
        // Write code here that turns the phrase above into concrete actions
        throw new cucumber.api.PendingException();
    }

    @Then("the system reports that there are no books with that id")
    public void the_system_reports_that_there_are_no_books_with_that_id() {
        // Write code here that turns the phrase above into concrete actions
        throw new cucumber.api.PendingException();
    }

    @When("a librarian searches for a book by title of {string}")
    public void a_librarian_searches_for_a_book_by_title_of(String string) {
        // Write code here that turns the phrase above into concrete actions
        throw new cucumber.api.PendingException();
    }

    @Then("the system reports that there are no books found with that title")
    public void the_system_reports_that_there_are_no_books_found_with_that_title() {
        // Write code here that turns the phrase above into concrete actions
        throw new cucumber.api.PendingException();
    }

    @When("a librarian lists all the books")
    public void a_librarian_lists_all_the_books() {
        // Write code here that turns the phrase above into concrete actions
        throw new cucumber.api.PendingException();
    }

    @Then("the system reports that there are no books in the system")
    public void the_system_reports_that_there_are_no_books_in_the_system() {
        // Write code here that turns the phrase above into concrete actions
        throw new cucumber.api.PendingException();
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


}
