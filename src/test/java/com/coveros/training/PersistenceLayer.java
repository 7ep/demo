package com.coveros.training;

import java.sql.*;

class PersistenceLayer {

    private final Connection connection;

    PersistenceLayer(Connection connection) {
        this.connection = connection;
    }

    long saveNewBorrower(String borrowerName) {
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
            } else {
                throw new RuntimeException("failed to save a new Borrower");
            }
            return newId;
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    void updateBorrower(long id, String borrowerName) {
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


    String getBorrowerName(int id) {
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
}
