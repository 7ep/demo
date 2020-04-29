package com.coveros.training.authentication;

import com.coveros.training.persistence.PersistenceLayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoginUtils {

    private static final Logger logger = LoggerFactory.getLogger(LoginUtils.class);
    private final PersistenceLayer persistenceLayer;

    public LoginUtils(PersistenceLayer persistenceLayer) {
        this.persistenceLayer = persistenceLayer;
    }

    public LoginUtils() {
        this(new PersistenceLayer());
    }

    public boolean isUserRegistered(String username, String password) {
        logger.info("checking if credentials for {} are valid for login", username);
        boolean isValid = persistenceLayer.areCredentialsValid(username, password);
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
