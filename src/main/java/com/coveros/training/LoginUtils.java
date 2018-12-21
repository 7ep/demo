package com.coveros.training;

class LoginUtils {

    private final DatabaseUtils authDb;

    LoginUtils(DatabaseUtils authDb) {
        this.authDb = authDb;
    }

    boolean isUserRegistered(String username, String password) {
        return authDb.isUsernameAndPasswordInDatabase(username, password);
    }

    static LoginUtils createEmpty() {
        return new LoginUtils(DatabaseUtils.createEmpty());
    }

}
