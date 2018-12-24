package com.coveros.training;

import java.nio.file.Paths;

/**
 * These values are important and dependent on correctly set
 * environment variables.
 */
class Constants {

    /**
     * This is a wrapper about System.getenv, except that we will
     * throw an exception if we don't get a value.
     * @param name the name of the environment variable
     * @return a non-empty String, or else we throw an exception
     */
    private static String getEnvironmentVariable(String name) {
        final String value = StringUtils.makeNotNullable(System.getenv(name));
        if (value.isEmpty()) {
            throw new RuntimeException("environment variable returned an empty string.");
        }
        return value;
    }

    // DEMO_PROJECT_HOME is the absolute path to the demo project.
    // For example, C:\Users\byron\demo\
    static final String RESTORE_SCRIPTS_PATH = Paths.get( getEnvironmentVariable("DEMO_PROJECT_HOME"), "db_sample_files").toString();

    // POSTGRES_BIN_DIR must be set to the directory for Postgresql's binaries.
    // for example, on Windows it's C:\Program Files\PostgreSQL\10\bin\
    static final String PATH_TO_PG_RESTORE = Paths.get(getEnvironmentVariable("POSTGRES_BIN_DIR") , "pg_restore").toString();

    // we'll default to the following URL for our Poostgresql database, unless
    // it is set in the environment differently.
    private static final String DEFAULT_DB_URL = "jdbc:postgresql://localhost/training";
    static final String DATABASE_URL = System.getProperty("POSTGRES_URL", DEFAULT_DB_URL);
}
