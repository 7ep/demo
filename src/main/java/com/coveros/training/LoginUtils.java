package com.coveros.training;

class LoginUtils {

    private final PersistenceLayer persistenceLayer;

    LoginUtils(PersistenceLayer persistenceLayer) {
        this.persistenceLayer = persistenceLayer;
    }

    boolean isUserRegistered(String username, String password) {
        return persistenceLayer.areCredentialsValid(username, password);
    }

    static LoginUtils createEmpty() {
        return new LoginUtils(new PersistenceLayer(new EmptyConnection()));
    }

}
