package com.coveros.training.persistence;

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
        logger.info("checking if credentials for {} are valid", username);
        return persistenceLayer.areCredentialsValid(username, password);
    }

    public static LoginUtils createEmpty() {
        return new LoginUtils(PersistenceLayer.createEmpty());
    }

    public boolean isEmpty() {
        return persistenceLayer.isEmpty();
    }

}
