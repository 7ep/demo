package com.coveros.training.authentication.domainobjects;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.checkerframework.checker.nullness.qual.Nullable;


/**
 * This contains the results of checking the complexity of a given
 * password - that is, how good of a password, how secure, is it?
 * See {@link com.coveros.training.authentication.RegistrationUtils#isPasswordGood}
 * for the calculation that generates this.
 */
public final class PasswordResult {

    public final PasswordResultEnums status;
    private final Double entropy;
    public final String timeToCrackOffline;
    private final String timeToCrackOnline;
    private final String message;

    private static final String BASIC_PASSWORD_CHECKS_FAILED = "BASIC_PASSWORD_CHECKS_FAILED";

    public PasswordResult(PasswordResultEnums status,
                          Double entropy,
                          String timeToCrackOffline,
                          String timeToCrackOnline,
                          String message) {

        this.status = status;
        this.entropy = entropy;
        this.timeToCrackOffline = timeToCrackOffline;
        this.timeToCrackOnline = timeToCrackOnline;
        this.message = message;
    }

    /**
     * Return this if any of our very basic attempt to validate
     * the password field fail.  Like passing an empty string, for example.
     */
    public static PasswordResult createDefault(PasswordResultEnums resultStatus) {
        return new PasswordResult(resultStatus, 0d, "", "", BASIC_PASSWORD_CHECKS_FAILED);
    }

    @Override
    public final boolean equals(@Nullable Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj.getClass() != getClass()) {
            return false;
        }
        PasswordResult rhs = (PasswordResult) obj;
        return new EqualsBuilder()
                .append(status, rhs.status)
                .append(entropy, rhs.entropy)
                .append(timeToCrackOffline, rhs.timeToCrackOffline)
                .append(timeToCrackOnline, rhs.timeToCrackOnline)
                .append(message, rhs.message)
                .isEquals();
    }

    @Override
    public final int hashCode() {
        // you pick a hard-coded, randomly chosen, non-zero, odd number
        // ideally different for each class
        return new HashCodeBuilder(15, 33).
                append(status).
                append(entropy).
                append(timeToCrackOffline).
                append(timeToCrackOnline).
                append(message).
                toHashCode();
    }

    public final String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    public final String toPrettyString() {
        return String.format("status: %s%n", status) +
                String.format("entropy: %s%n", entropy) +
                String.format("time to crack offline: %s%n", timeToCrackOffline) +
                String.format("time to crack online: %s%n", timeToCrackOnline) +
                String.format("Nbvcxz response: %s%n", message);
    }

    /**
     * Return this to represent an empty result.  Used primarily
     * when we are initializing a variable and don't want to use null.
     */
    public static PasswordResult createEmpty() {
        return new PasswordResult(PasswordResultEnums.NULL, 0d, "", "", "");
    }

    public boolean isEmpty() {
        return this.equals(createEmpty());
    }

}
