package com.coveros.training;

import com.google.auto.value.AutoValue;

@AutoValue
abstract class PasswordResult {

    public static final String BASIC_PASSWORD_CHECKS_FAILED = "BASIC_PASSWORD_CHECKS_FAILED";

    static PasswordResult create(PasswordResultEnums status,
                                 Double entropy,
                                 String timeToCrackOffline,
                                 String timeToCrackOnline,
                                 String message) {
        return new AutoValue_PasswordResult(status, entropy, timeToCrackOffline, timeToCrackOnline, message);
    }

    /**
     * Return this if any of our very basic attempt to validate
     * the password field fail.  Like passing an empty string, for example.
     */
    static PasswordResult createDefault(PasswordResultEnums resultStatus) {
        return create(resultStatus, 0d, "", "",  BASIC_PASSWORD_CHECKS_FAILED);
    }

    abstract PasswordResultEnums status();
    abstract Double entropy();
    abstract String timeToCrackOffline();
    abstract String timeToCrackOnline();
    abstract String message();
}
