package com.coveros.training;

public class LibraryUtils {

    private DatabaseUtils databaseUtils;

    public LibraryUtils(DatabaseUtils databaseUtils) {
        this.databaseUtils = databaseUtils;
    }

    public String registerBorrower(String username) {
        final String search = databaseUtils.searchDatabaseForKey(username);
        if (search == null) {
            databaseUtils.saveTextToFile(username);
            return "successfully registered";
        } else {
            return "already registered";
        }
    }
}
