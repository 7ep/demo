package com.coveros.training;

public class LoginUtils {

    private final DatabaseUtils authDb;

    public LoginUtils(DatabaseUtils authDb) {
        this.authDb = authDb;
    }

    public boolean isUserRegistered(String username, String password) {
        return authDb.isUsernameAndPasswordInDatabase(username, password);
    }
}
