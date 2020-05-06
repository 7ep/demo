package com.coveros.training.persistence;

import com.coveros.training.library.domainobjects.Book;
import com.coveros.training.library.domainobjects.Borrower;
import com.coveros.training.library.domainobjects.Loan;
import com.coveros.training.authentication.domainobjects.User;
import org.h2.jdbcx.JdbcConnectionPool;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

/**
 * Test that we have a persistence layer that we can easily mock out.
 * This exists so we can have more control over the persistence process,
 * whether we want to mock those sections, and so on.
 */
public class PersistenceLayerTests {

    private final static String DEFAULT_NAME = "alice";
    private final static Date BORROW_DATE = Date.valueOf(LocalDate.of(2018, Month.JANUARY, 1));
    private static final Book DEFAULT_BOOK = new Book(1, "The DevOps Handbook");
    private static final Borrower DEFAULT_BORROWER = new Borrower(1, DEFAULT_NAME);
    private static final Loan DEFAULT_LOAN = new Loan(DEFAULT_BOOK, DEFAULT_BORROWER, 1, BORROW_DATE);
    private static final User DEFAULT_USER = new User(DEFAULT_NAME, 1);
    IPersistenceLayer pl;

    @Before
    public void initDatabase() {
        pl = new PersistenceLayer(getFileBasedDatabaseConnectionPool());
    }

    /**
     * assert that there is a way to store a borrower
     * in a database.  We don't actually care how this happens,
     * we just care that it exists.  Here we're adding a
     * brand-spanking-new borrower.
     */
    @Test
    public void testShouldSaveBorrowerToDatabase() {
        pl.cleanAndMigrateDatabase();

        long id = pl.saveNewBorrower(DEFAULT_BORROWER.name);

        assertEquals("The first row in a database gets an index of 1", 1, id);
    }

    /**
     * We ought to be able to update a borrower's details,
     * if we know that borrower's id and we have a detail we
     * want to change.
     */
    @Test
    public void testShouldUpdateBorrowerToDatabase() {
        // the borrower with id of 1 is "alice"
        runRestoreOneBookOneBorrower();
        final String newName = "bob";

        // change the borrower's name
        pl.updateBorrower(1, newName);

        String name = pl.getBorrowerName(1).orElseThrow();
        assertEquals(newName, name);
    }

    /**
     * If a borrower is in the database, we should be able
     * to find that person by their name
     */
    @Test
    public void testShouldBeAbleToSearchBorrowerByName() {
        runRestoreOneBookOneBorrower();

        Borrower borrower = pl.searchBorrowerDataByName(DEFAULT_BORROWER.name).orElseThrow();

        assertEquals(DEFAULT_BORROWER, borrower);
    }

    /**
     * If a book is in the database, we should be able to find it by title.
     */
    @Test
    public void testShouldBeAbleToSearchForBooksByTitle() {
        runRestoreOneBookOneBorrower();
        final Book expectedBook = new Book(1, DEFAULT_BOOK.title);

        Book book = pl.searchBooksByTitle(DEFAULT_BOOK.title).orElseThrow();

        assertEquals(expectedBook, book);
    }

    /**
     * If a book is in the database, we should be able to find it by id.
     */
    @Test
    public void testShouldBeAbleToSearchForBooksById() {
        // this will set the default book into the database
        runRestoreOneBookOneBorrower();
        final Book expectedBook = new Book(1, DEFAULT_BOOK.title);

        // search for it by id
        Book book = pl.searchBooksById(DEFAULT_BOOK.id).orElseThrow();

        assertEquals(expectedBook, book);
    }

    /**
     * If a borrower is in the database, we should be able to find it by id.
     */
    @Test
    public void testShouldBeAbleToSearchForBorrowersById() {
        runRestoreOneBookOneBorrower();

        Borrower borrower = pl.searchBorrowersById(DEFAULT_BORROWER.id).orElseThrow();

        assertEquals(DEFAULT_BORROWER, borrower);
    }

    @Test
    public void testShouldBeAbleToSearchAUserByName() {
        runRestoreOneUser();

        User user = pl.searchForUserByName(DEFAULT_USER.name).orElseThrow();

        assertEquals(DEFAULT_USER, user);
    }

    @Test
    public void testThatWeCanUpdateAUsersPassword() {
        runRestoreOneUser();
        final String newPassword = "abc123";

        pl.updateUserWithPassword(1, newPassword);
        final boolean result = pl.areCredentialsValid(DEFAULT_BORROWER.name, newPassword).orElseThrow();

        assertTrue(result);
    }

    @Test
    public void testWeCanCreateLoan() {
        runRestoreOneBookOneBorrower();

        final long loanId = pl.createLoan(DEFAULT_BOOK, DEFAULT_BORROWER, BORROW_DATE);

        assertEquals(1, loanId);
    }


    @Test
    public void testWeCanSearchForALoanByABook() {
        runRestoreOneLoan();

        Loan loan = pl.searchForLoanByBook(DEFAULT_BOOK).orElseThrow();

        assertEquals(DEFAULT_LOAN, loan);
    }

    @Test
    public void testWeCanSearchForALoanByABorrower() {
        runRestoreOneLoan();

        Loan loan = pl.searchForLoanByBorrower(DEFAULT_BORROWER).get().get(0);

        assertEquals(DEFAULT_LOAN, loan);
    }

    @Test
    public void testWeCanSaveANewUser() {
        pl.cleanAndMigrateDatabase();

        long id = pl.saveNewUser(DEFAULT_USER.name);

        assertEquals(DEFAULT_USER.id, id);
    }

    @Test
    public void testWeCanSaveABook() {
        pl.cleanAndMigrateDatabase();

        long id = pl.saveNewBook(DEFAULT_BOOK.title);

        assertEquals(DEFAULT_BOOK.id, id);
    }

    @Test
    public void testWeCanCreateALoan() {
        runRestoreOneBookOneBorrower();

        long id = pl.createLoan(DEFAULT_BOOK, DEFAULT_BORROWER, BORROW_DATE);

        assertEquals(DEFAULT_LOAN.id, id);
    }

    @Test
    public void testShouldBeAbleToDeleteBook() {
        runRestoreOneBookOneBorrower();

        pl.deleteBook(DEFAULT_BOOK.id);
        final Optional<Book> book = pl.searchBooksByTitle(DEFAULT_BOOK.title);

        assertTrue(book.isEmpty());
    }

    @Test
    public void testShouldBeAbleToDeleteBorrower() {
        runRestoreOneBookOneBorrower();

        pl.deleteBorrower(DEFAULT_BORROWER.id);
        final Optional<Borrower> borrower = pl.searchBorrowerDataByName(DEFAULT_BORROWER.name);

        assertTrue(borrower.isEmpty());
    }

    @Test
    public void testShouldListAllBooks() {
        runRestoreOneBookOneBorrower();
        List<Book> expectedList = Arrays.asList(DEFAULT_BOOK);

        final List<Book> books = pl.listAllBooks().orElseThrow();

        assertEquals(expectedList, books);
    }

    @Test
    public void testShouldListAvailableBooks() {
        runRestoreThreeBooksThreeBorrowers();
        // loan out a book
        pl.createLoan(pl.searchBooksByTitle("b").orElseThrow(), pl.searchBorrowerDataByName("alice").orElseThrow(), BORROW_DATE);

        // create expected book list
        final List<Book> expectedBooks = new ArrayList<>();
        expectedBooks.add(new Book(1, "a"));
        expectedBooks.add(new Book(3, "c"));

        // act
        final List<Book> books = pl.listAvailableBooks().orElseThrow();

        // assert
        assertEquals(expectedBooks, books);
    }


    /**
     * If you check them all out - you'll get nothing in the available list.
     */
    @Test
    public void testShouldListNoAvailableBooksIfAllCheckedOut() {
        runRestoreThreeBooksThreeBorrowers();
        // loan out a book
        pl.createLoan(pl.searchBooksByTitle("a").orElseThrow(), pl.searchBorrowerDataByName("alice").orElseThrow(), BORROW_DATE);
        pl.createLoan(pl.searchBooksByTitle("b").orElseThrow(), pl.searchBorrowerDataByName("alice").orElseThrow(), BORROW_DATE);
        pl.createLoan(pl.searchBooksByTitle("c").orElseThrow(), pl.searchBorrowerDataByName("alice").orElseThrow(), BORROW_DATE);

        // act
        final Optional<List<Book>> books = pl.listAvailableBooks();

        // assert
        assertTrue(books.isEmpty());
    }

    /**
     * If none are checked out - you'll get everything in the available list.
     */
    @Test
    public void testShouldListAllBooksIfNoneCheckedOut() {
        runRestoreThreeBooksThreeBorrowers();
        // create expected book list
        final List<Book> expectedBooks = new ArrayList<>();
        expectedBooks.add(new Book(1, "a"));
        expectedBooks.add(new Book(2, "b"));
        expectedBooks.add(new Book(3, "c"));

        // act
        final List<Book> books = pl.listAvailableBooks().orElseThrow();

        // assert
        assertEquals(expectedBooks, books);
    }

    @Test
    public void testShouldListAllBorrowers() {
        runRestoreOneBookOneBorrower();
        List<Borrower> expectedList = Arrays.asList(DEFAULT_BORROWER);

        final List<Borrower> borrowers = pl.listAllBorrowers().orElseThrow();

        assertEquals(expectedList, borrowers);
    }

    @Test(expected = SqlRuntimeException.class)
    public void testThatExecuteInsertOnPreparedStatementHandlesExceptions() throws SQLException {
        final PersistenceLayer persistenceLayer = new PersistenceLayer();
        final PreparedStatement preparedStatement = Mockito.mock(PreparedStatement.class);
        final ResultSet resultSet = Mockito.mock(ResultSet.class);
        when(resultSet.next()).thenReturn(false);
        when(preparedStatement.getGeneratedKeys()).thenReturn(resultSet);

        persistenceLayer.executeInsertOnPreparedStatement(SqlData.createEmpty(), preparedStatement);
    }

    /**
     * Test what happens if no value is returned when we provide an
     * id to a particular user.
     */
    @Test
    public void testGetBorrowerName_WhenNoValueReturned() throws SQLException {
        final DataSource dataSource = Mockito.mock(DataSource.class);
        final PersistenceLayer persistenceLayer = new PersistenceLayer(dataSource);
        final Connection connection = Mockito.mock(Connection.class);
        final PreparedStatement preparedStatement = Mockito.mock(PreparedStatement.class);
        when(connection.prepareStatement(Mockito.anyString())).thenReturn(preparedStatement);
        when(dataSource.getConnection()).thenReturn(connection);
        final ResultSet resultSet = Mockito.mock(ResultSet.class);
        when(resultSet.next()).thenReturn(false);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);

        final Optional<String> borrowerName = persistenceLayer.getBorrowerName(1);

        assertTrue(borrowerName.isEmpty());
    }

    /**
     * Test what happens when an exception occurs in getBorrowerName
     */
    @Test(expected = SqlRuntimeException.class)
    public void testGetBorrowerName_WhenExceptionThrown() throws SQLException {
        final DataSource dataSource = Mockito.mock(DataSource.class);
        when(dataSource.getConnection()).thenThrow(new SQLException());
        final PersistenceLayer persistenceLayer = new PersistenceLayer(dataSource);

        persistenceLayer.getBorrowerName(1);
    }

    /**
     * An exception of the right type should be thrown
     * when an error occurs in the {@link PersistenceLayer#executeUpdateTemplate} method.
     */
    @Test(expected = SqlRuntimeException.class)
    public void testExecuteUpdateTemplate_ExceptionThrown() throws SQLException {
        final DataSource dataSource = Mockito.mock(DataSource.class);
        when(dataSource.getConnection()).thenThrow(new SQLException());
        final PersistenceLayer persistenceLayer = new PersistenceLayer(dataSource);

        persistenceLayer.executeUpdateTemplate("","");
    }

    /**
     * This can be run here, simply put @Test on top.
     */
    public void runBackup() {
        pl.runBackup("v2_three_books_three_borrowers.sql");
    }

    /**
     * This can be run here, simply put @Test on top.
     */
    public void setState() {
        runRestoreOneBookOneBorrower();
    }

    /**
     * this will set "alice" with id of 1 into the database as a borrower
     * and "The DevOps Handbook" with id of 1 as a book.
     */
    private void runRestoreOneBookOneBorrower() {
        runRestore("v2_one_book_one_borrower.sql");
    }

    private void runRestoreOneUser() {
        runRestore("v2_one_user.sql");
    }

    private void runRestoreOneLoan() {
        runRestore("v2_one_loan.sql");
    }

    /**
     * This backup has books: a, b, and c.  The borrowers are alice, bob, and carol
     */
    private void runRestoreThreeBooksThreeBorrowers() {
        runRestore("v2_three_books_three_borrowers.sql");
    }

    private void runRestore(String scriptName) {
        pl.runRestore(scriptName);
    }

    /**
     * Get a file-based {@link JdbcConnectionPool}, which makes it easier
     * to debug database tests when they are running.
     * <p>
     * Because we set AUTO_SERVER to true, we can access this database
     * from multiple places when it starts.
     * <p>
     * This method is solely meant to be used by database tests.
     */
    private static JdbcConnectionPool getFileBasedDatabaseConnectionPool() {
        return JdbcConnectionPool.create(
                "jdbc:h2:./build/db/training;AUTO_SERVER=TRUE;MODE=PostgreSQL", "", "");
    }


}
