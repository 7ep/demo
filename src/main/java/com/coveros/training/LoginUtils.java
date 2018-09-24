package com.coveros.training;

public class LoginUtils {

    public static boolean isUserRegistered(String username, String password) {
        return DatabaseUtils.isUsernameAndPasswordInDatabase(username, password);
    }
}
