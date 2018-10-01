package com.coveros.training;

import me.gosimple.nbvcxz.Nbvcxz;
import me.gosimple.nbvcxz.scoring.Result;

import static com.coveros.training.PasswordResult.*;
import static com.coveros.training.RegistrationStatusEnums.*;

public class RegistrationUtils {

    private final DatabaseUtils authDb;

    public RegistrationUtils(DatabaseUtils authDb) {
        this.authDb = authDb;
    }

    public RegistrationResult processRegistration(String username, String password) {
        if (isUserInDatabase(username)) {
            return RegistrationResult.create(false, ALREADY_REGISTERED.toString());
        }

        return registerUser(username, password);
    }

    private RegistrationResult registerUser(String username, String password) {

        // first we check if the username is empty
        boolean isUsernameEmpty = username == null || username.isEmpty();
        if (isUsernameEmpty) return RegistrationResult.create(false, EMPTY_USERNAME.toString());

        // then we check if the password is good.
        final PasswordResult passwordResult = isPasswordGood(password);
        if (passwordResult != SUCCESS) return RegistrationResult.create(false, passwordResult.toString());

        // finally, we try saving to the database.
        saveToDatabase(username, password);
        return RegistrationResult.create(true, SUCCESSFULLY_REGISTERED.toString());
    }

    /**
     * Whether we qualify a password as good.
     *
     * See implementation for criteria.
     */
    protected static PasswordResult isPasswordGood(String password) {
        if (password == null) return EMPTY_PASSWORD;
        if (password.isEmpty()) return EMPTY_PASSWORD;
        if (password.length() < 6) return TOO_SHORT;

        // Nbvcxz is a tool that tests entropy on passwords
        // See github.com/GoSimpleLLC/nbvcxz
        final Nbvcxz nbvcxz = new Nbvcxz();
        final Result result = nbvcxz.estimate(password);
        if (!result.isMinimumEntropyMet()) return INSUFFICIENT_ENTROPY;

        return SUCCESS;
    }

    public boolean isUserInDatabase(String username) {
        return authDb.searchDatabaseForKey(username) != null;
    }

    private void saveToDatabase(String username, String password) {
        authDb.saveTextToFile(username + " " + password);
    }

}
