package com.coveros.training.persistence;

import com.coveros.training.domainobjects.PasswordResult;
import com.coveros.training.domainobjects.RegistrationResult;
import com.coveros.training.domainobjects.User;
import me.gosimple.nbvcxz.Nbvcxz;
import me.gosimple.nbvcxz.scoring.Result;
import me.gosimple.nbvcxz.scoring.TimeEstimate;

import static com.coveros.training.domainobjects.PasswordResultEnums.*;
import static com.coveros.training.domainobjects.RegistrationStatusEnums.*;

public class RegistrationUtils {

    private final PersistenceLayer persistenceLayer;

    public RegistrationUtils(PersistenceLayer persistenceLayer) {
        this.persistenceLayer = persistenceLayer;
    }

    public RegistrationResult processRegistration(String username, String password) {
        if (isUserInDatabase(username)) {
            return new RegistrationResult(false, ALREADY_REGISTERED.toString());
        }

        return registerUser(username, password);
    }

    public static RegistrationUtils createEmpty() {
        return new RegistrationUtils(new PersistenceLayer(new EmptyConnection()));
    }

    private RegistrationResult registerUser(String username, String password) {

        // first we check if the username is empty
        boolean isUsernameEmpty = username == null || username.isEmpty();
        if (isUsernameEmpty) return new RegistrationResult(false, EMPTY_USERNAME.toString());

        // then we check if the password is good.
        final PasswordResult passwordResult = isPasswordGood(password);
        if (passwordResult.status != SUCCESS) return new RegistrationResult(false, passwordResult.toString());

        // then we check if the username already exists
        final User user = persistenceLayer.searchForUserByName(username);
        if (!user.isEmpty()) {
            return new RegistrationResult(false, ALREADY_REGISTERED.toString());
        }

        // at this point, we feel assured it's ok to save to the database.
        saveToDatabase(username, password);
        return new RegistrationResult(true, SUCCESSFULLY_REGISTERED.toString());
    }

    /**
     * Whether we qualify a password as good.
     *
     * See implementation for criteria.
     */
    public static PasswordResult isPasswordGood(String password) {
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
        if (!result.isMinimumEntropyMet()) return new PasswordResult(INSUFFICIENT_ENTROPY, entropy, timeToCrackOff, timeToCrackOn, suggestions);

        return new PasswordResult(SUCCESS, entropy, timeToCrackOff, timeToCrackOn, result.getFeedback().getResult());
    }

    public boolean isUserInDatabase(String username) {
        return ! persistenceLayer.searchForUserByName(username).isEmpty();
    }

    private void saveToDatabase(String username, String password) {
        final long userId = persistenceLayer.saveNewUser(username);
        persistenceLayer.updateUserWithPassword(userId, password);
    }

}