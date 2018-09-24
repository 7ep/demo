package com.coveros.training;

public class RegistrationUtils {

    public static RegistrationResult processRegistration(String username, String password) {
        if (RegistrationUtils.isUserInDatabase(username)) {
            return RegistrationResult.ALREADY_REGISTERED;
        }

        return registerUser(username, password);
    }

    private static RegistrationResult registerUser(String username, String password) {

        // first we check if the username is empty
        boolean isUsernameEmpty = username == null || username.isEmpty();
        if (isUsernameEmpty) return RegistrationResult.EMPTY_USERNAME;

        // then we check if the password is good.
        if (!isPasswordGood(password)) return RegistrationResult.PASSWORD_BAD;

        // finally, we try saving to the database.
        saveToDatabase(username, password);
        return RegistrationResult.SUCCESSFUL_REGISTRATION;
    }

    /**
     * Whether we qualify a password as good.
     *
     * See implementation for criteria.
     */
    public static boolean isPasswordGood(String password) {
        if (password == null) return false;
        if (password.isEmpty()) return false;
        if (password.length() < 6) return false;
        return true;
    }

    public static boolean isUserInDatabase(String username) {
        return DatabaseUtils.searchDatabaseForKey(username);
    }

    private static void saveToDatabase(String username, String password) {
        DatabaseUtils.saveTextToFile(username + " " + password);
    }

}
