package com.coveros.training;

import java.sql.*;
import java.util.Properties;

import static com.coveros.training.Constants.DATABASE_URL;

class PersistenceLayer {

    private final Connection connection;

    PersistenceLayer(Connection connection) {
        this.connection = connection;
    }

    static Connection createConnection() {
        Properties props = new Properties();
        props.setProperty("user","postgres");
        props.setProperty("password","postgres");
        Connection conn;
        try {
            conn = DriverManager.getConnection(DATABASE_URL, props);
        } catch (SQLException ex) {
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
        CheckUtils.checkStringNotNull(borrowerName);
        try (PreparedStatement st =
                     connection.prepareStatement(
                             "INSERT INTO library.Person (name) VALUES (?);",
                             Statement.RETURN_GENERATED_KEYS) ) {
            st.setString(1, borrowerName);
            st.executeUpdate();
            final ResultSet generatedKeys = st.getGeneratedKeys();
            long newId;
            if (generatedKeys.next()) {
                newId = generatedKeys.getLong(1);
                assert(newId > 0);
            } else {
                throw new RuntimeException("failed to save a new Borrower");
            }
            return newId;
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
        CheckUtils.checkStringNotNull(borrowerName);
        CheckUtils.checkIntParamNotNull(id);
        try (PreparedStatement st =
                     connection.prepareStatement(
                             "UPDATE library.Person SET name = ? WHERE id = ?;") ) {
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
     * @return the borrower's name.
     */
    String getBorrowerName(int id) {
        CheckUtils.checkIntParamNotNull(id);
        try (PreparedStatement st =
                     connection.prepareStatement(
                             "SELECT name FROM library.Person WHERE id = ?;") ) {
            st.setLong(1, id);
            final ResultSet resultSet = st.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString(1);
            } else {
                throw new RuntimeException("Failed to get Borrower name");
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Searches for a borrower by name.  Returns full details
     * if found.  null if not found.
     * @param borrowerName the name of a borrower.
     */
    BorrowerData searchBorrowerDataByName(String borrowerName) {
        CheckUtils.checkStringNotNull(borrowerName);
        try (PreparedStatement st =
                     connection.prepareStatement(
                             "SELECT id, name FROM library.Person WHERE name = ?;") ) {
            st.setString(1, borrowerName);
            final ResultSet resultSet = st.executeQuery();
            if (resultSet.next()) {
                long id = resultSet.getLong(1);
                String name = resultSet.getString(2);
                return BorrowerData.create(id, name);
            } else {
                return null;
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }
}
