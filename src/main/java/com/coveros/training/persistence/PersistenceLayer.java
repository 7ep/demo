package com.coveros.training.persistence;

import com.coveros.training.helpers.CheckUtils;
import com.coveros.training.helpers.StringUtils;
import com.coveros.training.library.domainobjects.Book;
import com.coveros.training.library.domainobjects.Borrower;
import com.coveros.training.library.domainobjects.Loan;
import com.coveros.training.authentication.domainobjects.User;
import org.flywaydb.core.Flyway;
import org.h2.jdbcx.JdbcConnectionPool;

import javax.sql.DataSource;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;


public class PersistenceLayer implements IPersistenceLayer {

    /*
     * ==========================================================
     * ==========================================================
     *
     *  Class construction - details of making this class
     *
     * ==========================================================
     * ==========================================================
     */

    private final DataSource dataSource;

    public PersistenceLayer() {
        this(obtainConnectionPool());
    }

    PersistenceLayer(DataSource ds) {
        dataSource = ds;
    }

    private static JdbcConnectionPool obtainConnectionPool() {
        return JdbcConnectionPool.create(
                "jdbc:h2:mem:training;MODE=PostgreSQL", "", "");
    }

    /*
     * ==========================================================
     * ==========================================================
     *
     *  Micro ORM
     *    Demo has a simplistic Object Relational Mapper (ORM)
     *    implementation.  These are the methods that comprise
     *    the mechanisms for that.
     *
     *    In comparison, a gargantuan project like Hibernate
     *    would consist of a heckuva-lot-more than this.  That's
     *    why this one is termed, "micro"
     *
     * ==========================================================
     * ==========================================================
     */


    /**
     * This command provides a template to execute updates (including inserts) on the database
     */
    void executeUpdateTemplate(String description, String preparedStatement, Object ... params) {
        final SqlData<Object> sqlData = new SqlData<>(description, preparedStatement, params);
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement st = prepareStatementWithKeys(sqlData, connection)) {
                executeUpdateOnPreparedStatement(sqlData, st);
            }
        } catch (SQLException ex) {
            throw new SqlRuntimeException(ex);
        }
    }


    public long executeInsertTemplate(
            String description,
            String preparedStatement,
            Object ... params) {
        final SqlData<Object> sqlData = new SqlData<>(description, preparedStatement, params);
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement st = prepareStatementWithKeys(sqlData, connection)) {
                return executeInsertOnPreparedStatement(sqlData, st);
            }
        } catch (SQLException ex) {
            throw new SqlRuntimeException(ex);
        }
    }


    <T> long executeInsertOnPreparedStatement(SqlData<T> sqlData, PreparedStatement st) throws SQLException {
        sqlData.applyParametersToPreparedStatement(st);
        st.executeUpdate();
        try (ResultSet generatedKeys = st.getGeneratedKeys()) {
            long newId;
            if (generatedKeys.next()) {
                newId = generatedKeys.getLong(1);
                assert (newId > 0);
            } else {
                throw new SqlRuntimeException("failed Sql.  Description: " + sqlData.description + " SQL code: " + sqlData.preparedStatement);
            }
            return newId;
        }
    }


    private <T> void executeUpdateOnPreparedStatement(SqlData<T> sqlData, PreparedStatement st) throws SQLException {
        sqlData.applyParametersToPreparedStatement(st);
        st.executeUpdate();
    }


    /**
     * A helper method.  Simply creates a prepared statement that
     * always returns the generated keys from the database, like
     * when you insert a new row of data in a table with auto-generating primary key.
     *
     * @param sqlData    see {@link SqlData}
     * @param connection a typical {@link Connection}
     */
    private <T> PreparedStatement prepareStatementWithKeys(SqlData<T> sqlData, Connection connection) throws SQLException {
        return connection.prepareStatement(
                sqlData.preparedStatement,
                Statement.RETURN_GENERATED_KEYS);
    }


    <R> Optional<R> runQuery(SqlData<R> sqlData) {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement st =
                         connection.prepareStatement(sqlData.preparedStatement)) {
                sqlData.applyParametersToPreparedStatement(st);
                try (ResultSet resultSet = st.executeQuery()) {
                    return sqlData.extractor.apply(resultSet);
                }
            }
        } catch (SQLException ex) {
            throw new SqlRuntimeException(ex);
        }

    }


    /**
     * This is an interface to a wrapper around {@link Function} so we can catch exceptions
     * in the generic function.
     *
     * @param <R> The return type
     * @param <E> The type of the exception
     */
    @FunctionalInterface
    private interface ThrowingFunction<R, E extends Exception> {
        R apply(ResultSet resultSet) throws E;
    }


    /**
     * This wraps the throwing function, so that we are not forced to
     * catch an exception in our ordinary code - it's caught and handled
     * here.
     * @param throwingFunction a lambda that throws a checked exception we have to handle.
     *                         specifically in this case that's a SqlRuntimeException
     * @param <R> the type of value returned
     * @return returns a function that runs and returns a function wrapped with an exception handler
     */
    static <R> Function<ResultSet, R> throwingFunctionWrapper(
            ThrowingFunction<R, Exception> throwingFunction) {

        return resultSet -> {
            try {
                return throwingFunction.apply(resultSet);
            } catch (Exception ex) {
                throw new SqlRuntimeException(ex);
            }
        };
    }


    /**
     * Accepts a function to extract data from a {@link ResultSet} and
     * removes some boilerplate with handling its response.
     * Works in conjunction with {@link #throwingFunctionWrapper}
     * @param extractorFunction a function that extracts data from a {@link ResultSet}
     * @param <T> the type of data we'll retrieve from the {@link ResultSet}
     * @return either the type of data wrapped with an optional, or {@link Optional#empty}
     */
    private <T> Function<ResultSet, Optional<T>> createExtractor(
            ThrowingFunction<Optional<T>, Exception> extractorFunction) {
        return throwingFunctionWrapper(rs -> {
            if (rs.next()) {
                return extractorFunction.apply(rs);
            } else {
                return Optional.empty();
            }
        });
    }


    /*
     * ==========================================================
     * ==========================================================
     *
     *  Business functions
     *     loaning out books, registering users, etc
     *
     * ==========================================================
     * ==========================================================
     */


    // Library functions

    @Override
    public long saveNewBorrower(String borrowerName) {
        CheckUtils.StringMustNotBeNullOrEmpty(borrowerName);
        return executeInsertTemplate(
                "adds a new library borrower",
                "INSERT INTO library.borrower (name) VALUES (?);", borrowerName);
    }


    @Override
    public long createLoan(Book book, Borrower borrower, Date borrowDate) {
        return executeInsertTemplate(
                "Creates a new loan of a book to a borrower",
                "INSERT INTO library.loan (book, borrower, borrow_date) VALUES (?, ?, ?);", book.id, borrower.id, borrowDate);
    }


    @Override
    public long saveNewBook(String bookTitle) {
        CheckUtils.StringMustNotBeNullOrEmpty(bookTitle);
        return executeInsertTemplate(
                "Creates a new book in the database",
                "INSERT INTO library.book (title) VALUES (?);", bookTitle);
    }


    @Override
    public void updateBorrower(long id, String borrowerName) {
        CheckUtils.IntParameterMustBePositive(id);
        CheckUtils.StringMustNotBeNullOrEmpty(borrowerName);
        executeUpdateTemplate(
                "Updates the borrower's data",
                "UPDATE library.borrower SET name = ? WHERE id = ?;", borrowerName, id);
    }


    @Override
    public void deleteBook(long id) {
        CheckUtils.IntParameterMustBePositive(id);
        executeUpdateTemplate(
                "Deletes a book from the database",
                "DELETE FROM library.book WHERE id = ?;", id);
    }


    @Override
    public void deleteBorrower(long id) {
        CheckUtils.IntParameterMustBePositive(id);
        executeUpdateTemplate(
                "Deletes a borrower from the database",
                "DELETE FROM library.borrower WHERE id = ?;", id);
    }


    @Override
    public Optional<String> getBorrowerName(long id) {
        CheckUtils.IntParameterMustBePositive(id);
        Function<ResultSet, Optional<String>> extractor =
                createExtractor(rs -> Optional.of(StringUtils.makeNotNullable(rs.getString(1))));

        return runQuery(new SqlData<>(
                        "get a borrower's name by their id",
                        "SELECT name FROM library.borrower WHERE id = ?;",
                        extractor, id));
    }


    @Override
    public Optional<Borrower> searchBorrowerDataByName(String borrowerName) {
        CheckUtils.StringMustNotBeNullOrEmpty(borrowerName);
        Function<ResultSet, Optional<Borrower>> extractor = createExtractor(rs -> {
            long id = rs.getLong(1);
            String name = StringUtils.makeNotNullable(rs.getString(2));
            return Optional.of(new Borrower(id, name));
        });

        return runQuery(new SqlData<>(
                        "search for details on a borrower by name",
                        "SELECT id, name FROM library.borrower WHERE name = ?;",
                        extractor, borrowerName));
    }


    @Override
    public Optional<Book> searchBooksByTitle(String bookTitle) {
        CheckUtils.StringMustNotBeNullOrEmpty(bookTitle);
        Function<ResultSet, Optional<Book>> extractor = createExtractor(rs -> {
            long id = rs.getLong(1);
            return Optional.of(new Book(id, bookTitle));
        });

        return runQuery(new SqlData<>(
                        "search for a book by title",
                        "SELECT id FROM library.book WHERE title = ?;",
                        extractor, bookTitle));
    }


    @Override
    public Optional<Book> searchBooksById(long id) {
        CheckUtils.IntParameterMustBePositive(id);
        Function<ResultSet, Optional<Book>> extractor = createExtractor(rs -> {
            long bookId = rs.getLong(1);
            String title = StringUtils.makeNotNullable(rs.getString(2));
            return Optional.of(new Book(bookId, title));
        });

        return runQuery(new SqlData<>(
                        "search for a book by title",
                        "SELECT id, title FROM library.book WHERE id = ?;",
                        extractor, id));
    }


    @Override
    public Optional<Borrower> searchBorrowersById(long id) {
        CheckUtils.IntParameterMustBePositive(id);
        Function<ResultSet, Optional<Borrower>> extractor = createExtractor(rs -> {
            long borrowerId = rs.getLong(1);
            String name = StringUtils.makeNotNullable(rs.getString(2));
            return Optional.of(new Borrower(borrowerId, name));
        });

        return runQuery(new SqlData<>(
                        "search for a borrower by name",
                        "SELECT id, name FROM library.borrower WHERE id = ?;",
                        extractor, id));
    }


    @Override
    public Optional<List<Book>> listAllBooks() {
        return listBooks("get all books", "SELECT id, title FROM library.book;");
    }


    @Override
    public Optional<List<Book>> listAvailableBooks() {
        return listBooks("get all available books", "SELECT b.id, b.title FROM library.book b LEFT JOIN library.loan l ON b.id = l.book WHERE l.borrow_date IS NULL;");
    }


    private Optional<List<Book>> listBooks(String description, String sqlCode) {
        Function<ResultSet, Optional<List<Book>>> extractor = createExtractor(rs -> {
            List<Book> bookList = new ArrayList<>();
            do {
                long id = rs.getLong(1);
                String title = StringUtils.makeNotNullable(rs.getString(2));
                bookList.add(new Book(id, title));
            } while (rs.next());
            return Optional.of(bookList);
        });

        return runQuery(new SqlData<>(
                        description,
                        sqlCode,
                        extractor));
    }


    @Override
    public Optional<List<Borrower>> listAllBorrowers() {
        Function<ResultSet, Optional<List<Borrower>>> extractor = createExtractor(rs -> {
            List<Borrower> borrowerList = new ArrayList<>();
            do {
                long id = rs.getLong(1);
                String name = StringUtils.makeNotNullable(rs.getString(2));
                borrowerList.add(new Borrower(id, name));
            } while (rs.next());
            return Optional.of(borrowerList);
        });

        return runQuery(new SqlData<>(
                        "get all borrowers",
                        "SELECT id, name FROM library.borrower;",
                        extractor));
    }


    @Override
    public Optional<List<Loan>> searchForLoanByBorrower(Borrower borrower) {
        Function<ResultSet, Optional<List<Loan>>> extractor = createExtractor(rs -> {
            List<Loan> loans = new ArrayList<>();
            do {
                final long loanId = rs.getLong(1);
                final Date borrowDate = rs.getDate(2);
                final long bookId = rs.getLong(3);
                final String bookTitle = StringUtils.makeNotNullable(rs.getString(4));
                final Date borrowDateNotNullable = borrowDate == null ? Date.valueOf("0000-01-01") : borrowDate;
                loans.add(new Loan(new Book(bookId, bookTitle), borrower, loanId, borrowDateNotNullable));
            } while (rs.next());
            return Optional.of(loans);
        });

        return runQuery(new SqlData<>(
                "search for all loans by borrower",
                "SELECT loan.id, loan.borrow_date, loan.book, book.title " +
                        "FROM library.loan loan " +
                        "JOIN library.book book ON book.id = loan.book " +
                        "WHERE loan.borrower = ?;",
                extractor, borrower.id));
    }


    @Override
    public Optional<Loan> searchForLoanByBook(Book book) {
        Function<ResultSet, Optional<Loan>> extractor = createExtractor(rs -> {
            final long loanId = rs.getLong(1);
            final Date borrowDate = rs.getDate(2);
            final long borrowerId = rs.getLong(3);
            final String borrowerName = StringUtils.makeNotNullable(rs.getString(4));
            final Date borrowDateNotNullable = borrowDate == null ? Date.valueOf("0000-01-01") : borrowDate;
            return Optional.of(new Loan(book, new Borrower(borrowerId, borrowerName), loanId, borrowDateNotNullable));
        });

        return runQuery(new SqlData<>(
                "search for a loan by book",
                "SELECT loan.id, loan.borrow_date, loan.borrower, bor.name " +
                        "FROM library.loan loan " +
                        "JOIN library.borrower bor ON bor.id = loan.borrower " +
                        "WHERE loan.book = ?;",
                extractor, book.id));
    }


    // authentication functions


    @Override
    public long saveNewUser(String username) {
        CheckUtils.StringMustNotBeNullOrEmpty(username);
        return executeInsertTemplate(
                "Creates a new user in the database",
                "INSERT INTO auth.user (name) VALUES (?);", username);
    }


    @Override
    public Optional<User> searchForUserByName(String username) {
        CheckUtils.StringMustNotBeNullOrEmpty(username);
        Function<ResultSet, Optional<User>> extractor = createExtractor(rs -> {
            final long id = rs.getLong(1);
            return Optional.of(new User(username, id));
        });

        return runQuery(new SqlData<>(
                "search for a user by id, return that user if found, otherwise return an empty user",
                "SELECT id  FROM auth.user WHERE name = ?;",
                extractor, username));
    }


    @Override
    public Optional<Boolean> areCredentialsValid(String username, String password) {
        Function<ResultSet, Optional<Boolean>> extractor = createExtractor(rs -> {
            final long id = rs.getLong(1);
            assert (id > 0);
            return Optional.of(true);
        });

        final String hexHash = createHashedValueFromPassword(password);
        return runQuery(new SqlData<>(
                "check to see if the credentials for a user are valid",
                "SELECT id FROM auth.user WHERE name = ? AND password_hash = ?;",
                extractor, username, hexHash));
    }


    @Override
    public void updateUserWithPassword(long id, String password) {
        CheckUtils.IntParameterMustBePositive(id);
        String hashedPassword = createHashedValueFromPassword(password);
        executeUpdateTemplate(
                "Updates the user's password field with a new hash",
                "UPDATE auth.user SET password_hash = ? WHERE id = ?;", hashedPassword, id);
    }


    /**
     * Given a password (for example, "password123"), return a
     * hash of that.
     * @param password a user's password
     * @return a hash of the password value.  a one-way function that returns a unique value,
     *          but different than the original, cannot be converted back to its original value.
     */
    private String createHashedValueFromPassword(String password) {
        CheckUtils.StringMustNotBeNullOrEmpty(password);
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedhash = digest.digest(
                    password.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(encodedhash);
        } catch (NoSuchAlgorithmException e) {
            throw new SqlRuntimeException(e);
        }
    }


    /**
     * Converts an array of bytes to their corresponding hex string
     * @param bytes an array of bytes
     * @return a hex string of that array
     */
    private static String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }


    /*
     * ==========================================================
     * ==========================================================
     *
     *  General utility methods
     *
     * ==========================================================
     * ==========================================================
     */


    public static IPersistenceLayer createEmpty() {
        return new PersistenceLayer(new EmptyDataSource());
    }


    @Override
    public boolean isEmpty() {
        return this.dataSource.getClass().equals(EmptyDataSource.class);
    }

    @Override
    public void runBackup(String backupFileName) {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement st = connection.prepareStatement("SCRIPT TO ?")) {
                st.setString(1, backupFileName);
                st.execute();
            }
        } catch (SQLException ex) {
            throw new SqlRuntimeException(ex);
        }
    }

    @Override
    public void runRestore(String backupFileName) {
        String dbScriptsDirectory="src/integration_test/resources/db_sample_files/";
        String fullPathToBackup = dbScriptsDirectory + backupFileName;
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement st = connection.prepareStatement(
                    "DROP SCHEMA IF EXISTS ADMINISTRATIVE CASCADE;" +
                            "DROP SCHEMA IF EXISTS AUTH CASCADE;" +
                            "DROP SCHEMA IF EXISTS LIBRARY CASCADE;")) {
                st.execute();
            }
            try (PreparedStatement st = connection.prepareStatement("RUNSCRIPT FROM ?")) {
                st.setString(1, fullPathToBackup);
                st.execute();
            }
        } catch (SQLException ex) {
            throw new SqlRuntimeException(ex);
        }
    }


    /*
     * ==========================================================
     * ==========================================================
     *
     *  Database migration code - using FlywayDb
     *
     * ==========================================================
     * ==========================================================
     */


    @Override
    public void cleanAndMigrateDatabase() {
        cleanDatabase();
        migrateDatabase();
    }

    @Override
    public void cleanDatabase() {
        Flyway flyway = configureFlyway();
        flyway.clean();
    }

    @Override
    public void migrateDatabase() {
        Flyway flyway = configureFlyway();
        flyway.migrate();
    }

    private Flyway configureFlyway() {
        return Flyway.configure()
                .schemas("ADMINISTRATIVE", "LIBRARY", "AUTH")
                .dataSource(this.dataSource)
                .load();
    }

}
