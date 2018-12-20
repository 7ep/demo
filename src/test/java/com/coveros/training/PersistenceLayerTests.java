package com.coveros.training;

import org.junit.Assert;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import static com.coveros.training.Constants.DATABASE_URL;
import static com.coveros.training.Constants.PATH_TO_PG_RESTORE;
import static com.coveros.training.Constants.RESTORE_SCRIPTS_PATH;
import static com.coveros.training.database_backup_constants.ONE_PERSON_IN_TABLE_ALREADY_V1_DUMP;
import static com.coveros.training.database_backup_constants.SAMPLE_DB_V1_DUMP;

/**
 * Test that we have a persistence layer that we can easily mock out.
 * This exists so we can have more control over the persistence process,
 * whether we want to mock those sections, and so on.
 */
public class PersistenceLayerTests {

    private Connection createConnection() {
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
     * assert that there is a way to store a borrower
     * in a database.  We don't actually care how this happens,
     * we just care that it exists.  Here we're adding a
     * brand-spanking-new borrower.
     */
    @Test
    public void testShouldSaveBorrowerToDatabase() {
        setDatabaseState(SAMPLE_DB_V1_DUMP);
        final Connection connection = createConnection();
        PersistenceLayer pl = new PersistenceLayer(connection);

        long id = pl.saveNewBorrower("alice");

        Assert.assertEquals(1, id);
    }

    /**
     * We ought to be able to update a borrower's details,
     * if we know that borrower's id and we have a detail we
     * want to change.
     */
    @Test
    public void testShouldUupdateBorrowerToDatabase() {
        setDatabaseState(ONE_PERSON_IN_TABLE_ALREADY_V1_DUMP);
        final Connection connection = createConnection();
        PersistenceLayer pl = new PersistenceLayer(connection);

        pl.updateBorrower(1, "bob");

        String name = pl.getBorrowerName(1);
        Assert.assertEquals("bob", name);
    }

    @Test
    public void testShouldBeAbleToSearchBorrowerByName() {
        setDatabaseState(ONE_PERSON_IN_TABLE_ALREADY_V1_DUMP);
        final Connection connection = createConnection();
        PersistenceLayer pl = new PersistenceLayer(connection);

        BorrowerData bd = pl.searchBorrowerDataByName("alice");

        Assert.assertEquals("alice", bd.name());
        Assert.assertEquals(1, bd.id());
    }

    /**
     * use a "restore" command to set the database into a state
     * of empty tables.  This operation must happen very quickly.
     */
    static void setDatabaseState(String restoreScriptName) {
        Runtime r = Runtime.getRuntime();
        Process p;
        String[] cmd = {
                PATH_TO_PG_RESTORE,
                "--host", "localhost",
                "--port", "5432",
                "--username", "postgres",
                "--dbname", "training",
                "--role", "postgres",
                "--no-password",
                "--clean",  // necessary to enable running again and again without problems.
                RESTORE_SCRIPTS_PATH + restoreScriptName
        };
        try {
            p = r.exec(cmd);
            // following command is necessary to cause the system to wait until the command is done.
            p.waitFor();
        } catch (Exception e) {
            // stop the world if this breaks, and fix it.
            e.printStackTrace();
        }
    }




}
