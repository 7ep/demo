package com.coveros.training;

import java.nio.file.Paths;

/**
 * These values are important and dependent on correctly set
 * environment variables.
 */
public class Constants {

    // we'll default to the following URL for our Poostgresql database, unless
    // it is set in the environment differently.
    private static final String DEFAULT_DB_URL = "jdbc:postgresql://localhost/training";
    public static final String DATABASE_URL = System.getProperty("POSTGRES_URL", DEFAULT_DB_URL);
}
