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
import org.junit.Before;

import java.sql.Date;
import java.time.*;
import java.time.format.DateTimeFormatter;

public class BookCheckOutStepDefs {

    private Book myBook = Book.createEmpty();
    private Borrower myBorrower = Borrower.createEmpty();
    private final Date jan_1st = Date.valueOf(LocalDate.of(2018, Month.JANUARY, 1));
    private LibraryUtils libraryUtils = LibraryUtils.createEmpty();
    private final Date Jan_2nd = Date.valueOf(LocalDate.of(2018, Month.JANUARY, 2));
    private LibraryActionResults libraryActionResults = LibraryActionResults.NULL;
    private PersistenceLayer pl = PersistenceLayer.createEmpty();

    @Before
    public void init() {
        pl = new PersistenceLayer();
    }

    /**
     * Set up the databases, clear them, initialize the Library Utility with them.
     */
    private void initializeEmptyDatabaseAndUtility() {
        pl.cleanAndMigrateDatabase();
        libraryUtils = new LibraryUtils();
    }

    @Given("^a borrower, \"([^\"]*)\", is registered$")
    public void aBorrowerIsRegistered(String borrowerName) {
        initializeEmptyDatabaseAndUtility();

        libraryUtils.registerBorrower(borrowerName);
        myBorrower = libraryUtils.searchForBorrowerByName(borrowerName);
    }

    @And("^a book, \"([^\"]*)\" is available for borrowing$")
    public void aBookIsAvailableForBorrowing(String title) {
        libraryUtils.registerBook(title);
        myBook = libraryUtils.searchForBookByTitle(title);
    }

    @When("^they try to check out the book on \"([^\"]*)\"$")
    public void theyTryToCheckOutTheBookOn(String date) {
        Date borrowTime = Date.valueOf(LocalDate.parse(date, DateTimeFormatter.ofPattern("MMMM dd, yyyy")));
        libraryUtils.lendBook(myBook, myBorrower, borrowTime);
    }

    @Then("^the system indicates the book is loaned to them on that date$")
    public void theSystemIndicatesTheBookIsLoanedToThemOnThatDate() {
        final Loan loan = libraryUtils.searchForLoan(myBook);
        Assert.assertEquals(Date.valueOf("2018-01-31"), loan.checkoutDate);
    }

    @When("^they try to check out the book$")
    public void theyCheckOutTheBook() {
        libraryUtils.lendBook(myBook, myBorrower, Jan_2nd);
    }

    @Then("^the system indicates the book is loaned to them on some date$")
    public void theSystemIndicatesTheBookIsLoanedToThemOnSomeDate() {
        final Loan loan = libraryUtils.searchForLoan(myBook);
        Assert.assertEquals( Date.valueOf("2018-01-31"), loan.checkoutDate);
    }

    @Given("^an individual, \"([^\"]*)\", is not registered$")
    public void anIndividualIsNotRegistered(String borrower) {
        initializeEmptyDatabaseAndUtility();
    }

    @When("^they try to check out a book, \"([^\"]*)\" that is available$")
    public void theyTryToCheckOutABookThatIsAvailable(String title) {
        libraryUtils.registerBook(title);
        myBook = libraryUtils.searchForBookByTitle(title);

        libraryActionResults = libraryUtils.lendBook(myBook, myBorrower, Jan_2nd);
    }

    @Then("^the system indicates that they are not registered$")
    public void theSystemIndicatesThatHeIsNotRegistered() {
        Assert.assertEquals(LibraryActionResults.BORROWER_NOT_REGISTERED, libraryActionResults);
    }

    @Given("^and a book, \"([^\"]*)\" is already checked out to \"([^\"]*)\"$")
    public void andABookIsAlreadyCheckedOutTo(String title, String borrower_b) {
        libraryUtils.registerBorrower(borrower_b);
        libraryUtils.registerBook(title);
        final Book book = libraryUtils.searchForBookByTitle(title);
        final Borrower borrower = libraryUtils.searchForBorrowerByName(borrower_b);

        // a previous person already checked it out.
        libraryUtils.lendBook(book, borrower, jan_1st);

        // now we try to check it out.  It should indicate BOOK_CHECKED_OUT.
        libraryActionResults = libraryUtils.lendBook(book, myBorrower, Jan_2nd);
    }

    @Then("^the system indicates that the book is not available$")
    public void theSystemIndicatesThatTheBookIsNotAvailable() {
        Assert.assertEquals(LibraryActionResults.BOOK_CHECKED_OUT, libraryActionResults);
    }



}
