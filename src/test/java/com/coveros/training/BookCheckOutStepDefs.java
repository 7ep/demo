package com.coveros.training;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Date;

public class BookCheckOutStepDefs {

    String myBook;
    String myBorrower;
    String bookInfo;
    OffsetDateTime jan_1st = OffsetDateTime.of(LocalDateTime.of(2018, Month.JANUARY, 1, 0, 0), ZoneOffset.UTC);
    DatabaseUtils borrowersDb;
    DatabaseUtils booksDb;
    DatabaseUtils lendingDb;
    LibraryUtils libraryUtils;
    OffsetDateTime jan_31st = OffsetDateTime.of(LocalDateTime.of(2018, Month.JANUARY, 31, 0, 0), ZoneOffset.UTC);
    OffsetDateTime currentDateTime;
    LibraryActionResults libraryActionResults;

    /**
     * Set up the databases, clear them, initialize the Library Utility with them.
     */
    private void initializeEmptyDatabaseAndUtility() {
        borrowersDb = DatabaseUtils.obtainDatabaseAccess(DatabaseUtils.LIBRARY_BORROWER_DATABASE_NAME);
        lendingDb = DatabaseUtils.obtainDatabaseAccess(DatabaseUtils.LIBRARY_LENDING_DATABASE);
        booksDb = DatabaseUtils.obtainDatabaseAccess(DatabaseUtils.LIBRARY_BOOKS_DATABASE_NAME);

        borrowersDb.clearDatabaseContents();
        lendingDb.clearDatabaseContents();
        booksDb.clearDatabaseContents();

        libraryUtils = new LibraryUtils(borrowersDb, booksDb, lendingDb);
    }

    @Given("^a borrower, \"([^\"]*)\", is registered and a book, \"([^\"]*)\" is available for borrowing$")
    public void aBorrowerIsRegisteredAndABookIsAvailableForBorrowing(String borrower, String book) {
        initializeEmptyDatabaseAndUtility();

        libraryUtils.registerBorrower(borrower);
        myBorrower = borrower;

        libraryUtils.registerBook(book);
        myBook = book;
    }

    @When("^they check out the book$")
    public void theyCheckOutTheBook() {
        libraryUtils.lendBook(myBook, myBorrower, jan_31st);
    }

    @Then("^the database indicates the book is loaned to them on that date$")
    public void theDatabaseIndicatesTheBookIsLoanedToHerOnThatDate() {
        final String result = lendingDb.searchDatabaseForKey(myBook);
        Assert.assertTrue(result.contains("2018-01-31T00:00Z"));
    }

    @Given("^an individual, \"([^\"]*)\", is not registered as a borrower$")
    public void anIndividualIsNotRegisteredAsABorrower(String borrower) {
        initializeEmptyDatabaseAndUtility();
        myBorrower = borrower;
    }

    @When("^they try to check out a book, \"([^\"]*)\" that is available$")
    public void theyTryToCheckOutABookThatIsAvailable(String book) {
        libraryUtils.registerBook(book);
        myBook = book;

        libraryActionResults = libraryUtils.lendBook(myBook, myBorrower, jan_31st);
    }

    @Then("^the system indicates that they are not registered$")
    public void theSystemIndicatesThatHeIsNotRegistered() {
        Assert.assertEquals(LibraryActionResults.BORROWER_NOT_REGISTERED, libraryActionResults);
    }

    private OffsetDateTime convertStringToOffsetDateTime(String date) {
        try {
            Date tempDate = new SimpleDateFormat("MM/dd/yyyy").parse(date);
            return OffsetDateTime.ofInstant(tempDate.toInstant(), ZoneOffset.UTC);
        } catch (Exception ex) {
            throw new RuntimeException("Test failure.  Date format probably wrong.");
        }
    }

    @Given("^today is \"([^\"]*)\"$")
    public void todayIs(String todayDate) {
        initializeEmptyDatabaseAndUtility();
        currentDateTime = convertStringToOffsetDateTime(todayDate);
    }

    @And("^the book, \"([^\"]*)\", was lent out to \"([^\"]*)\" on \"([^\"]*)\"$")
    public void theBookWasLentOutToOn(String book, String borrower, String lendingDate) {
        final OffsetDateTime lendingDateConverted = convertStringToOffsetDateTime(lendingDate);
        libraryUtils.registerBook(book);
        libraryUtils.registerBorrower(borrower);
        libraryUtils.lendBook(book, borrower, lendingDateConverted);
        myBook = book;
    }

    @When("^the database is checked for details on that book$")
    public void theDatabaseIsCheckedForDetailsOnThatBook() {
        bookInfo = libraryUtils.queryBookInfo(myBook);
    }

    @Then("^it indicates it is checked out to them on that date$")
    public void itIndicatesItIsCheckedOutToThemOnThatDate() {
        Assert.assertTrue(bookInfo.contains("2018-01-01"));
    }

}
