package com.coveros.training;

import java.nio.file.Paths;

public class TestConstants {

    /**
     * user.dir should be the home directory of this project.
     */
    public static final String RESTORE_SCRIPTS_PATH = Paths.get( System.getProperty("user.dir"), "db_sample_files").toString();

    /**
     * pg_restore needs to be on the path
     */
    public static final String PG_RESTORE =  "pg_restore";

}
