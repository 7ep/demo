package com.coveros.training.authentication;

import com.coveros.training.authentication.domainobjects.PasswordResult;
import com.coveros.training.authentication.domainobjects.RegistrationResult;
import com.coveros.training.persistence.PersistenceLayer;
import me.gosimple.nbvcxz.Nbvcxz;
import me.gosimple.nbvcxz.scoring.Result;
import me.gosimple.nbvcxz.scoring.TimeEstimate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.coveros.training.authentication.domainobjects.PasswordResultEnums.EMPTY_PASSWORD;
import static com.coveros.training.authentication.domainobjects.PasswordResultEnums.*;
import static com.coveros.training.authentication.domainobjects.RegistrationStatusEnums.*;
import static com.coveros.training.helpers.CheckUtils.checkStringNotNullOrEmpty;

/**
 * Provides logic for registering a new user
 */
public class RegistrationUtils {

    private static final Logger logger = LoggerFactory.getLogger(RegistrationUtils.class);

    private final PersistenceLayer persistenceLayer;

    public RegistrationUtils(PersistenceLayer persistenceLayer) {
        this.persistenceLayer = persistenceLayer;
    }

    public RegistrationUtils() {
        this(new PersistenceLayer());
    }

    /**
     * A business process to evaluate an attempt to register a new user.
     * <ol>
     *     <li>Make sure the username and password aren't null or empty</li>
     *     <li>Make sure the user isn't already in the database</li>
     *     <li>Check if the password is sufficiently complex to be secure</li>
     *     <li>Create a new account for this user, saving the user's credentials</li>
     * </ol>
     */
    public RegistrationResult processRegistration(String username, String password) {
        logger.info("Starting registration");
        checkStringNotNullOrEmpty(username);
        checkStringNotNullOrEmpty(password);

        if (isUserInDatabase(username)) {
            logger.info("cannot register this user - they are already registered");
            return new RegistrationResult(false, ALREADY_REGISTERED);
        }

        // then we check if the password is good.
        final PasswordResult passwordResult = isPasswordGood(password);
        if (passwordResult.status != SUCCESS) {
            logger.info("user provided a bad password during registration");
            return new RegistrationResult(false, BAD_PASSWORD, passwordResult.toPrettyString());
        }

        // at this point, we feel assured it's ok to save to the database.
        saveToDatabase(username, password);
        logger.info("saving new user, {}, to database", username);
        return new RegistrationResult(true, SUCCESSFULLY_REGISTERED);
    }

    public static RegistrationUtils createEmpty() {
        return new RegistrationUtils(PersistenceLayer.createEmpty());
    }

    public boolean isEmpty() {
        return persistenceLayer.isEmpty();
    }

    /**
     * Whether we qualify a password as good.
     *
     * <p>
     *     To summarize - we check the following here:
     *  </p>
     *  <ol>
     *    <li>The password must not be empty</li>
     *    <li>it must not be too short (less than 6 characters)</li>
     *    <li>cannot be too long (more than 100 characters - this is because the framework</li>
     *    <li>that analyzes it slows to a crawl with more than 100 characters)</li>
     *    <li>Whether the entropy framework we're using considers the password good.</li>
     * </ol>
     *  <p>See {@link PasswordResult}</p>
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
