package com.coveros.training.authentication;

import com.coveros.training.helpers.CheckUtils;
import com.coveros.training.persistence.IPersistenceLayer;
import com.coveros.training.persistence.PersistenceLayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Provides business-layer for determining if entered credentials qualify for authentication
 */
public class LoginUtils {

    private static final Logger logger = LoggerFactory.getLogger(LoginUtils.class);
    private final IPersistenceLayer persistenceLayer;

    public LoginUtils(IPersistenceLayer persistenceLayer) {
        this.persistenceLayer = persistenceLayer;
    }

    public LoginUtils() {
        this(new PersistenceLayer());
    }

    /**
     * Determine if input is correct credentials for a registered user.
     * @return true if the credentials are valid, false otherwise
     */
    public boolean isUserRegistered(String username, String password) {
        CheckUtils.StringMustNotBeNullOrEmpty(username, password);
        logger.info("checking if credentials for {} are valid for login", username);
        boolean isValid = persistenceLayer.areCredentialsValid(username, password).orElse(false);
        if (isValid) {
            logger.info("credentials for {} are valid - granting access", username);
        } else {
            logger.info("credentials for {} were invalid - denying access", username);
        }
        return isValid;
    }

    public static LoginUtils createEmpty() {
        return new LoginUtils(PersistenceLayer.createEmpty());
    }

    public boolean isEmpty() {
        return persistenceLayer.isEmpty();
    }

}
