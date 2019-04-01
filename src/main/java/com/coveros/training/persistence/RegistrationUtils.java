package com.coveros.training.persistence;

import com.coveros.training.domainobjects.PasswordResult;
import com.coveros.training.domainobjects.RegistrationResult;
import me.gosimple.nbvcxz.Nbvcxz;
import me.gosimple.nbvcxz.scoring.Result;
import me.gosimple.nbvcxz.scoring.TimeEstimate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.coveros.training.domainobjects.PasswordResultEnums.EMPTY_PASSWORD;
import static com.coveros.training.domainobjects.PasswordResultEnums.*;
import static com.coveros.training.domainobjects.RegistrationStatusEnums.*;

public class RegistrationUtils {

    private static final Logger logger = LoggerFactory.getLogger(RegistrationUtils.class);

    private final PersistenceLayer persistenceLayer;

    public RegistrationUtils(PersistenceLayer persistenceLayer) {
        this.persistenceLayer = persistenceLayer;
    }

    public RegistrationUtils() {
        this(new PersistenceLayer());
    }

    public RegistrationResult processRegistration(String username, String password) {
        logger.info("Starting registration");
        // first we check if the username is empty
        boolean isUsernameEmpty = username == null || username.isEmpty();

        if (isUsernameEmpty) {
            logger.info("username is empty during registration");
            return new RegistrationResult(false, EMPTY_USERNAME);
        }

        if (isUserInDatabase(username)) {
            logger.info("cannot register this user - they are already registered");
            return new RegistrationResult(false, ALREADY_REGISTERED);
        }

        return registerUser(username, password);
    }

    public static RegistrationUtils createEmpty() {
        return new RegistrationUtils(PersistenceLayer.createEmpty());
    }

    public boolean isEmpty() {
        return persistenceLayer.isEmpty();
    }

    private RegistrationResult registerUser(String username, String password) {

        // then we check if the password is good.
        final PasswordResult passwordResult = isPasswordGood(password);
        if (passwordResult.status != SUCCESS) {
            logger.info("user provided a bad password during registration");
            return new RegistrationResult(false, BAD_PASSWORD, passwordResult.toString());
        }

        // at this point, we feel assured it's ok to save to the database.
        saveToDatabase(username, password);
        logger.info("saving new user, {}, to database", username);
        return new RegistrationResult(true, SUCCESSFULLY_REGISTERED);
    }

    /**
     * Whether we qualify a password as good.
     * <p>
     * See implementation for criteria.
     */
    public static PasswordResult isPasswordGood(String password) {
        if (password.isEmpty()) {
            logger.info("password was empty");
            return PasswordResult.createDefault(EMPTY_PASSWORD);
        }
        if (password.length() < 6) {
            logger.info("password was too short");
            return PasswordResult.createDefault(TOO_SHORT);
        }
        if (password.length() > 100) {
            logger.info("password was too long");
            return PasswordResult.createDefault(TOO_LONG);
        }

        // Nbvcxz is a tool that tests entropy on passwords
        // See github.com/GoSimpleLLC/nbvcxz
        final Nbvcxz nbvcxz = new Nbvcxz();
        final Result result = nbvcxz.estimate(password);
        final String suggestions = String.join(";", result.getFeedback().getSuggestion());
        final Double entropy = result.getEntropy();
        String timeToCrackOff = TimeEstimate.getTimeToCrackFormatted(result, "OFFLINE_BCRYPT_12");
        String timeToCrackOn = TimeEstimate.getTimeToCrackFormatted(result, "ONLINE_THROTTLED");
        if (!result.isMinimumEntropyMet()) {
            logger.info("minimum entropy for password was not met");
            return new PasswordResult(INSUFFICIENT_ENTROPY, entropy, timeToCrackOff, timeToCrackOn, suggestions);
        } else {
            logger.info("password met required entropy");
            return new PasswordResult(SUCCESS, entropy, timeToCrackOff, timeToCrackOn, result.getFeedback().getResult());
        }
    }

    public boolean isUserInDatabase(String username) {
        return !persistenceLayer.searchForUserByName(username).isEmpty();
    }

    private void saveToDatabase(String username, String password) {
        final long userId = persistenceLayer.saveNewUser(username);
        persistenceLayer.updateUserWithPassword(userId, password);
    }

}
