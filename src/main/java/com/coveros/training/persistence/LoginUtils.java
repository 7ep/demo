package com.coveros.training.persistence;

public class LoginUtils {

    private final PersistenceLayer persistenceLayer;

    public LoginUtils(PersistenceLayer persistenceLayer) {
        this.persistenceLayer = persistenceLayer;
    }

    public LoginUtils() {
        this(new PersistenceLayer());
    }

    public boolean isUserRegistered(String username, String password) {
        return persistenceLayer.areCredentialsValid(username, password);
    }

    public static LoginUtils createEmpty() {
        return new LoginUtils(PersistenceLayer.createEmpty());
    }

    public boolean isEmpty() {
        return persistenceLayer.isEmpty();
    }

}
