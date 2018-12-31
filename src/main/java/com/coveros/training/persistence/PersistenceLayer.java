package com.coveros.training.persistence;

import com.coveros.training.CheckUtils;
import com.coveros.training.StringUtils;
import com.coveros.training.domainobjects.Book;
import com.coveros.training.domainobjects.Borrower;
import com.coveros.training.domainobjects.Loan;
import com.coveros.training.domainobjects.User;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.Properties;

import static com.coveros.training.Constants.DATABASE_URL;

public class PersistenceLayer {

    private final Connection connection;

    public PersistenceLayer(Connection connection) {
        this.connection = connection;
    }

    public static PersistenceLayer createEmpty() {
        return new PersistenceLayer(new EmptyConnection());
    }

    public static Connection createConnection() {
        Properties props = new Properties();
        props.setProperty("user","postgres");
        props.setProperty("password","postgres");
        Connection conn;
        try {
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection(DATABASE_URL, props);
        } catch (SQLException | ClassNotFoundException ex) {
            throw new RuntimeException(ex);
        }
        return conn;
    }

    /**
     * This command saves a borrower to our table and generates a new id for them.
     * @param borrowerName the name of the borrower.
     * @return the generated id
     */
    long saveNewBorrower(String borrowerName) {
        try (PreparedStatement st =
                     connection.prepareStatement(
                             "INSERT INTO library.borrower (name) VALUES (?);",
                             Statement.RETURN_GENERATED_KEYS) ) {
            st.setString(1, borrowerName);
            st.executeUpdate();
            try (ResultSet generatedKeys = st.getGeneratedKeys()) {
                long newId;
                if (generatedKeys.next()) {
                    newId = generatedKeys.getLong(1);
                    assert (newId > 0);
                } else {
                    throw new RuntimeException("failed to save a new Borrower");
                }
                return newId;
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * If we already have a borrower, this command allows us to change
     * their values (except for their id)
     * @param id the id of a borrower (a constant)
     * @param borrowerName the name of a borrower, which we can change.
     */
    void updateBorrower(long id, String borrowerName) {
        CheckUtils.checkIntParamPositive(id);
        try (PreparedStatement st =
                     connection.prepareStatement(
                             "UPDATE library.borrower SET name = ? WHERE id = ?;") ) {
            st.setString(1, borrowerName);
            st.setLong(2, id);
            st.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Given the id for a borrower, this command returns their name.
     * @param id a borrower's id.
     * @return the borrower's name, or an empty string if not found
     */
    String getBorrowerName(int id) {
        CheckUtils.checkIntParamPositive(id);
        try (PreparedStatement st =
                     connection.prepareStatement(
                             "SELECT name FROM library.borrower WHERE id = ?;") ) {
            st.setLong(1, id);
            try (ResultSet resultSet = st.executeQuery()) {
                if (resultSet.next()) {
                    final String name = resultSet.getString(1);
                    return StringUtils.makeNotNullable(name);
                } else {
                    return "";
                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Searches for a borrower by name.  Returns full details
     * if found.  return empty borrower data if not found.
     * @param borrowerName the name of a borrower
     * @return a valid borrower, or an empty borrower if not found
     */
    Borrower searchBorrowerDataByName(String borrowerName) {
        try (PreparedStatement st =
                     connection.prepareStatement(
                             "SELECT id, name FROM library.borrower WHERE name = ?;") ) {
            st.setString(1, borrowerName);
            try (ResultSet resultSet = st.executeQuery()) {
                if (resultSet.next()) {
                    long id = resultSet.getLong(1);
                    String name = StringUtils.makeNotNullable(resultSet.getString(2));
                    return new Borrower(id, name);
                } else {
                    return Borrower.createEmpty();
                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    Book searchBooksByTitle(String bookTitle) {
        try (PreparedStatement st =
                 connection.prepareStatement(
                     "SELECT id FROM library.book WHERE title = ?;") ) {
            st.setString(1, bookTitle);
            try (ResultSet resultSet = st.executeQuery()) {
                if (resultSet.next()) {
                    long id = resultSet.getLong(1);
                    return new Book(bookTitle, id);
                } else {
                    return Book.createEmpty();
                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    long createLoan(Book book, Borrower borrower, Date borrowDate) {
        try (PreparedStatement st =
                 connection.prepareStatement(
                     "INSERT INTO library.loan (book, borrower, borrow_date) VALUES (?, ?, ?);",
                     Statement.RETURN_GENERATED_KEYS) ) {
            st.setLong(1, book.id);
            st.setLong(2, borrower.id);
            st.setDate(3, borrowDate);
            st.executeUpdate();
            try (ResultSet generatedKeys = st.getGeneratedKeys()) {
                long newId;
                if (generatedKeys.next()) {
                    newId = generatedKeys.getLong(1);
                    assert (newId > 0);
                } else {
                    throw new RuntimeException("failed to create a new book loan");
                }
                return newId;
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    long saveNewBook(String bookTitle) {
        try (PreparedStatement st =
                 connection.prepareStatement(
                     "INSERT INTO library.book (title) VALUES (?);",
                     Statement.RETURN_GENERATED_KEYS) ) {
            st.setString(1, bookTitle);
            st.executeUpdate();
            try (ResultSet generatedKeys = st.getGeneratedKeys()) {
                long newId;
                if (generatedKeys.next()) {
                    newId = generatedKeys.getLong(1);
                    assert (newId > 0);
                } else {
                    throw new RuntimeException("failed to create a new book");
                }
                return newId;
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    User searchForUserByName(String username) {
        try (PreparedStatement st =
                 connection.prepareStatement(
                     "SELECT id  FROM auth.user WHERE name = ?;") ) {
            st.setString(1, username);
            try (ResultSet resultSet = st.executeQuery()) {
                if (resultSet.next()) {
                    final long id = resultSet.getLong(1);
                    return new User(username, id);
                } else {
                    return User.createEmpty();
                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    boolean areCredentialsValid(String username, String password) {
        try (PreparedStatement st =
                 connection.prepareStatement(
                     "SELECT id FROM auth.user WHERE name = ? AND password_hash = ?;") ) {
            final String hexHash = createHashedValueFromPassword(password);
            st.setString(1, username);
            st.setString(2, hexHash);
            try (ResultSet resultSet = st.executeQuery()) {
                if (resultSet.next()) {
                    final long id = resultSet.getLong(1);
                    assert (id > 0);
                    return true;
                } else {
                    return false;
                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    long saveNewUser(String username) {
        try (PreparedStatement st =
                 connection.prepareStatement(
                     "INSERT INTO auth.user (name) VALUES (?);",
                     Statement.RETURN_GENERATED_KEYS) ) {
            st.setString(1, username);
            st.executeUpdate();
            try (ResultSet generatedKeys = st.getGeneratedKeys()) {
                long newId;
                if (generatedKeys.next()) {
                    newId = generatedKeys.getLong(1);
                    assert (newId > 0);
                } else {
                    throw new RuntimeException("failed to create a new user");
                }
                return newId;
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    void updateUserWithPassword(long id, String password) {
        CheckUtils.checkIntParamPositive(id);
        try (PreparedStatement st =
                 connection.prepareStatement(
                     "UPDATE auth.user SET password_hash = ? WHERE id = ?;") ) {
            final String hexHash = createHashedValueFromPassword(password);
            st.setString(1, hexHash);
            st.setLong(2, id);
            st.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }

    }

    private String createHashedValueFromPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedhash = digest.digest(
                password.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(encodedhash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    Loan searchForLoan(Book book) {
        try (PreparedStatement st =
                 connection.prepareStatement(
                     "select loan.id, loan.borrow_date, loan.borrower, bor.name FROM library.loan loan JOIN library.borrower bor ON bor.id = loan.id WHERE loan.book = ?;") ) {
            st.setLong(1, book.id);
            try (ResultSet resultSet = st.executeQuery()) {
                if (resultSet.next()) {
                    final long loanId = resultSet.getLong(1);
                    final Date borrowDate = resultSet.getDate(2);
                    final long borrowerId = resultSet.getLong(3);
                    final String borrowerName = StringUtils.makeNotNullable(resultSet.getString(4));
                    final Date borrowDateNotNullable = borrowDate == null ? Date.valueOf("0000-01-01") : borrowDate;
                    return new Loan(book, new Borrower(borrowerId, borrowerName), loanId, borrowDateNotNullable);
                } else {
                    return Loan.createEmpty();
                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    private static String bytesToHex(byte[] hash) {
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if(hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
