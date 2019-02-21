package com.coveros.training.persistence;

import com.coveros.training.domainobjects.Book;
import com.coveros.training.domainobjects.Borrower;
import com.coveros.training.domainobjects.Loan;
import com.coveros.training.domainobjects.User;
import org.h2.jdbcx.JdbcConnectionPool;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;
import java.time.Month;

import static org.mockito.Mockito.when;

/**
 * Test that we have a persistence layer that we can easily mock out.
 * This exists so we can have more control over the persistence process,
 * whether we want to mock those sections, and so on.
 */
public class PersistenceLayerTests {

    private final static Date BORROW_DATE = Date.valueOf(LocalDate.of(2018, Month.JANUARY, 1));
    private static final Book DEFAULT_BOOK = new Book(1, "The DevOps Handbook");
    private static final Borrower DEFAULT_BORROWER = new Borrower(1, "alice");
    PersistenceLayer pl;

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

        Assert.assertEquals(1, id);
    }

    /**
     * We ought to be able to update a borrower's details,
     * if we know that borrower's id and we have a detail we
     * want to change.
     */
    @Test
    public void testShouldUpdateBorrowerToDatabase() {
        runRestoreOneBookOneBorrower();

        pl.updateBorrower(1, "bob");

        String name = pl.getBorrowerName(1);
        Assert.assertEquals("bob", name);
    }

    /**
     * If a borrower is in the database, we should be able
     * to find that person by their name
     */
    @Test
    public void testShouldBeAbleToSearchBorrowerByName() {
        runRestoreOneBookOneBorrower();

        Borrower bd = pl.searchBorrowerDataByName("alice");

        Assert.assertEquals("alice", bd.name);
        Assert.assertEquals(1, bd.id);
    }

    /**
     * If a book is in the database, we should be able to find it by title.
     */
    @Test
    public void testShouldBeAbleToSearchForBooksByTitle() {
        runRestoreOneBookOneBorrower();

        Book book = pl.searchBooksByTitle(DEFAULT_BOOK.title);

        Assert.assertEquals(DEFAULT_BOOK.title, book.title);
        Assert.assertEquals(1, book.id);
    }

    @Test
    public void testShouldBeAbleToSearchAUserByName() {
        runRestoreOneUser();

        User user = pl.searchForUserByName("alice");

        Assert.assertEquals("alice", user.name);
        Assert.assertEquals(1, user.id);
    }

    @Test
    public void testThatWeCanUpdateAUsersPassword() {
        runRestoreOneUser();

        pl.updateUserWithPassword(1, "abc123");
        final boolean result = pl.areCredentialsValid("alice", "abc123");

        Assert.assertTrue(result);
    }

    @Test
    public void testWeCanCreateLoan() {
        runRestoreOneBookOneBorrower();

        final long loanId = pl.createLoan(DEFAULT_BOOK, DEFAULT_BORROWER, BORROW_DATE);

        Assert.assertEquals(1, loanId);
    }


    @Test
    public void testWeCanSearchForALoanByABook() {
        runRestoreOneLoan();

        Loan loan = pl.searchForLoan(DEFAULT_BOOK);

        Assert.assertEquals(DEFAULT_BOOK, loan.book);
        Assert.assertEquals(DEFAULT_BORROWER, loan.borrower);
    }

    @Test
    public void testWeCanSaveANewUser() {
        pl.cleanAndMigrateDatabase();

        long id = pl.saveNewUser("alice");

        Assert.assertEquals(1, id);
    }

    @Test
    public void testWeCanSaveABook() {
        pl.cleanAndMigrateDatabase();

        long id = pl.saveNewBook("The DevOps Handbook");

        Assert.assertEquals(1, id);
    }

    @Test
    public void testWeCanCreateALoan() {
        runRestoreOneBookOneBorrower();

        long id = pl.createLoan(DEFAULT_BOOK, DEFAULT_BORROWER, BORROW_DATE);

        Assert.assertEquals(1, id);
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

        final String borrowerName = persistenceLayer.getBorrowerName(1);

        Assert.assertEquals("", borrowerName);
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
     * when an error occurs in the {@link PersistenceLayer#executeUpdateTemplate(SqlData)} method.
     */
    @Test(expected = SqlRuntimeException.class)
    public void testExecuteUpdateTemplate_ExceptionThrown() throws SQLException {
        final DataSource dataSource = Mockito.mock(DataSource.class);
        when(dataSource.getConnection()).thenThrow(new SQLException());
        final PersistenceLayer persistenceLayer = new PersistenceLayer(dataSource);

        persistenceLayer.executeUpdateTemplate(SqlData.createEmpty());
    }

    /**
     * This can be run here, simply put @Test on top.
     */
    public void runBackup() {
        pl.runBackup("db_sample_files/v2_one_loan.sql");
    }

    /**
     * This can be run here, simply put @Test on top.
     */
    public void setState() {
        runRestoreOneBookOneBorrower();
    }

    private void runRestoreOneBookOneBorrower() {
        runRestore("db_sample_files/v2_one_book_one_borrower.sql");
    }

    private void runRestoreOneUser() {
        runRestore("db_sample_files/v2_one_user.sql");
    }

    private void runRestoreOneLoan() {
        runRestore("db_sample_files/v2_one_loan.sql");
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
