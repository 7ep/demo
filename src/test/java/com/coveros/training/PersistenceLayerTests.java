package com.coveros.training;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Test that we have a persistence layer that we can easily mock out.
 * This exists so we can have more control over the persistence process,
 * whether we want to mock those sections, and so on.
 */
public class PersistenceLayerTests {

    private Connection createConnection() {
        String url = "jdbc:postgresql://localhost/training";
        Properties props = new Properties();
        props.setProperty("user","postgres");
        props.setProperty("password","postgres");
        Connection conn;
        try {
            conn = DriverManager.getConnection(url, props);
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return conn;
    }
    
    /**
     * assert that there is a way to store a borrower
     * in a database.  We don't actually care how this happens,
     * we just care that it exists.  Here we're adding a
     * brand-spanking-new borrower.
     */
    @Test
    public void testShouldSaveBorrowerToDatabase() {
        setDatabaseStateEmpty();
        final Connection connection = createConnection();
        PersistenceLayer pl = new PersistenceLayer(connection);

        long id = pl.saveNewBorrower("alice");

        Assert.assertEquals(1, id);
    }

    /**
     * use a "restore" command to set the database into a state
     * of empty tables.  This operation must happen very quickly.
     */
    private void setDatabaseStateEmpty() {
        Runtime r = Runtime.getRuntime();
        Process p;
        String[] cmd = {
                // TODO: how to make path to pg_restore more sophisticated?  Should each dev need to alter this?
                "C:\\Program Files\\PostgreSQL\\10\\bin\\pg_restore.exe",
                "--host", "localhost",
                "--port", "5432",
                "--username", "postgres",
                "--dbname", "training",
                "--role", "postgres",
                "--no-password",
                "--clean",  // necessary to enable running again and again without problems.
                // TODO: come up for more sophisticated method to specify directory than absolute path.
                "C:\\Users\\byron\\demo\\db_sample_files\\sample_db_v1.dump"
        };
        try {
            p = r.exec(cmd);
        } catch (IOException e) {
            // stop the world if this breaks, and fix it.
            e.printStackTrace();
        }
    }




}
