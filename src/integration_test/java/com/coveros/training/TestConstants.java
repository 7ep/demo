package com.coveros.training;

import java.nio.file.Paths;

public class TestConstants {

    /**
     * This is a wrapper about System.getenv, except that we will
     * throw an exception if we don't get a value.
     * @param name the name of the environment variable
     * @return a non-empty String, or else we throw an exception
     */
    private static String getEnvironmentVariable(String name) {
        final String value = StringUtils.makeNotNullable(System.getenv(name));
        if (value.isEmpty()) {
            throw new RuntimeException("environment variable returned an empty string: for " + name);
        }
        return value;
    }

    // DEMO_PROJECT_HOME is the absolute path to the demo project.
    // For example, C:\Users\byron\demo\
    public static final String RESTORE_SCRIPTS_PATH = Paths.get( getEnvironmentVariable("DEMO_PROJECT_HOME"), "db_sample_files").toString();

    // POSTGRES_BIN_DIR must be set to the directory for Postgresql's binaries.
    // for example, on Windows it's C:\Program Files\PostgreSQL\10\bin\
    public static final String PATH_TO_PG_RESTORE = Paths.get(getEnvironmentVariable("POSTGRES_BIN_DIR") , "pg_restore").toString();

}
