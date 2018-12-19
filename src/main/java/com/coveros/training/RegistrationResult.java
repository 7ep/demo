package com.coveros.training;

import com.google.auto.value.AutoValue;

@AutoValue
abstract class RegistrationResult {

    static RegistrationResult create(boolean wasSuccessfullyRegistered, String message) {
        return new AutoValue_RegistrationResult(wasSuccessfullyRegistered, message);
    }

    abstract boolean wasSuccessfullyRegistered();
    abstract String message();

}
