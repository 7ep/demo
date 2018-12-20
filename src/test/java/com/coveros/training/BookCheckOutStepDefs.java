package com.coveros.training;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;

import java.sql.Connection;
import java.time.*;
import java.time.format.DateTimeFormatter;

import static com.coveros.training.database_backup_constants.SAMPLE_DB_V1_DUMP;

public class BookCheckOutStepDefs {

    private String myBook;
    private String myBorrower;
    private OffsetDateTime borrowTime;
    private OffsetDateTime jan_1st = OffsetDateTime.of(LocalDateTime.of(2018, Month.JANUARY, 1, 0, 0), ZoneOffset.UTC);
    private DatabaseUtils borrowersDb;
    private DatabaseUtils booksDb;
    private DatabaseUtils lendingDb;
    private LibraryUtils libraryUtils;
    private OffsetDateTime Jan_2nd = OffsetDateTime.of(LocalDateTime.of(2018, Month.JANUARY, 2, 0, 0), ZoneOffset.UTC);
    private LibraryActionResults libraryActionResults;

    /**
     * Set up the databases, clear them, initialize the Library Utility with them.
     */
    private void initializeEmptyDatabaseAndUtility() {
        PersistenceLayerTests.setDatabaseState(SAMPLE_DB_V1_DUMP);
        lendingDb = DatabaseUtils.obtainDatabaseAccess(DatabaseUtils.LIBRARY_LENDING_DATABASE);
        booksDb = DatabaseUtils.obtainDatabaseAccess(DatabaseUtils.LIBRARY_BOOKS_DATABASE_NAME);

        lendingDb.clearDatabaseContents();
        booksDb.clearDatabaseContents();

        final Connection connection = PersistenceLayer.createConnection();
        final PersistenceLayer persistenceLayer = new PersistenceLayer(connection);
        libraryUtils = new LibraryUtils(persistenceLayer, booksDb, lendingDb);
    }

    @Given("^a borrower, \"([^\"]*)\", is registered$")
    public void aBorrowerIsRegistered(String borrower) {
        initializeEmptyDatabaseAndUtility();

        libraryUtils.registerBorrower(borrower);
        myBorrower = borrower;
    }

    @And("^a book, \"([^\"]*)\" is available for borrowing$")
    public void aBookIsAvailableForBorrowing(String book) {
        libraryUtils.registerBook(book);
        myBook = book;
    }

    @When("^they try to check out the book on \"([^\"]*)\"$")
    public void theyTryToCheckOutTheBookOn(String date) {
        borrowTime = OffsetDateTime.of(LocalDate.parse(date, DateTimeFormatter.ofPattern("MMMM dd, yyyy")).atStartOfDay(), ZoneOffset.UTC);
        libraryUtils.lendBook(myBook, myBorrower, borrowTime);
    }

    @Then("^the system indicates the book is loaned to them on that date$")
    public void theSystemIndicatesTheBookIsLoanedToThemOnThatDate() {
        final String result = lendingDb.searchDatabaseForKey(myBook);
        Assert.assertTrue(result.contains("2018-01-31T00:00Z"));
    }

    @When("^they try to check out the book$")
    public void theyCheckOutTheBook() {
        libraryUtils.lendBook(myBook, myBorrower, Jan_2nd);
    }

    @Then("^the system indicates the book is loaned to them on some date$")
    public void theSystemIndicatesTheBookIsLoanedToThemOnSomeDate() {
        final String result = lendingDb.searchDatabaseForKey(myBook);
        Assert.assertTrue(result.contains("2018-01-31T00:00Z"));
    }

    @Given("^an individual, \"([^\"]*)\", is not registered$")
    public void anIndividualIsNotRegistered(String borrower) {
        initializeEmptyDatabaseAndUtility();
        myBorrower = borrower;
    }

    @When("^they try to check out a book, \"([^\"]*)\" that is available$")
    public void theyTryToCheckOutABookThatIsAvailable(String book) {
        libraryUtils.registerBook(book);
        myBook = book;

        libraryActionResults = libraryUtils.lendBook(myBook, myBorrower, Jan_2nd);
    }

    @Then("^the system indicates that they are not registered$")
    public void theSystemIndicatesThatHeIsNotRegistered() {
        Assert.assertEquals(LibraryActionResults.BORROWER_NOT_REGISTERED, libraryActionResults);
    }

    @Given("^and a book, \"([^\"]*)\" is already checked out to \"([^\"]*)\"$")
    public void andABookIsAlreadyCheckedOutTo(String book, String borrower_b) {
        libraryUtils.registerBorrower(borrower_b);
        libraryUtils.registerBook(book);
        myBook = book;

        // a previous person already checked it out.
        libraryUtils.lendBook(book, borrower_b, jan_1st);

        // now we try to check it out.  It should indicate BOOK_CHECKED_OUT.
        libraryActionResults = libraryUtils.lendBook(book, myBorrower, Jan_2nd);
    }

    @Then("^the system indicates that the book is not available$")
    public void theSystemIndicatesThatTheBookIsNotAvailable() {
        Assert.assertEquals(LibraryActionResults.BOOK_CHECKED_OUT, libraryActionResults);
    }



}
