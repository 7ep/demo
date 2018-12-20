package com.coveros.training;

import java.sql.*;

public class PersistenceLayer {

    private final Connection connection;

    public PersistenceLayer(Connection connection) {
        this.connection = connection;
    }

    public long saveNewBorrower(String borrowerName) {
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
                throw new RuntimeException("lol");
            }
            return newId;
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

}
