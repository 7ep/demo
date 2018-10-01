package com.coveros.training;

import me.gosimple.nbvcxz.Nbvcxz;
import me.gosimple.nbvcxz.scoring.Result;
import me.gosimple.nbvcxz.scoring.TimeEstimate;

import static com.coveros.training.PasswordResultEnums.*;
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
        if (passwordResult.status() != SUCCESS) return RegistrationResult.create(false, passwordResult.toString());

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
        if (password == null) return PasswordResult.createDefault(EMPTY_PASSWORD);
        if (password.isEmpty()) return PasswordResult.createDefault(EMPTY_PASSWORD);
        if (password.length() < 6) return PasswordResult.createDefault(TOO_SHORT);

        // Nbvcxz is a tool that tests entropy on passwords
        // See github.com/GoSimpleLLC/nbvcxz
        final Nbvcxz nbvcxz = new Nbvcxz();
        final Result result = nbvcxz.estimate(password);
        final String suggestions = String.join(";", result.getFeedback().getSuggestion());
        final Double entropy = result.getEntropy();
        String timeToCrackOff = TimeEstimate.getTimeToCrackFormatted(result, "OFFLINE_BCRYPT_12");
        String timeToCrackOn = TimeEstimate.getTimeToCrackFormatted(result, "ONLINE_THROTTLED");
        if (!result.isMinimumEntropyMet()) return PasswordResult.create(INSUFFICIENT_ENTROPY, entropy, timeToCrackOff, timeToCrackOn, suggestions);;

        return PasswordResult.create(SUCCESS, entropy, timeToCrackOff, timeToCrackOn, result.getFeedback().getResult());
    }

    public boolean isUserInDatabase(String username) {
        return authDb.searchDatabaseForKey(username) != null;
    }

    private void saveToDatabase(String username, String password) {
        authDb.saveTextToFile(username + " " + password);
    }

}
