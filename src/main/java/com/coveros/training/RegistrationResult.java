package com.coveros.training;

public enum RegistrationResult {
    ALREADY_REGISTERED, // the user is already registered in the system
    SUCCESSFUL_REGISTRATION,
    EMPTY_USERNAME, // the user entered an empty username
    PASSWORD_BAD // the password chosen by the user doesn't meet specifications.  See RegistrationUtils.isPasswordGood
}
