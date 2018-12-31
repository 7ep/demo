package com.coveros.training.domainobjects;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.checkerframework.checker.nullness.qual.Nullable;

public final class RegistrationResult {

    private final boolean wasSuccessfullyRegistered;
    private final String message;

    public RegistrationResult(boolean wasSuccessfullyRegistered, String message) {
        this.wasSuccessfullyRegistered = wasSuccessfullyRegistered;
        this.message = message;
    }

    public static RegistrationResult createEmpty() {
        return new RegistrationResult(false, "");
    }

    public final boolean equals(@Nullable Object obj) {
        if (obj == null) { return false; }
        if (obj == this) { return true; }
        if (obj.getClass() != getClass()) {
            return false;
        }
        RegistrationResult rhs = (RegistrationResult) obj;
        return new EqualsBuilder()
                .append(wasSuccessfullyRegistered, rhs.wasSuccessfullyRegistered)
                .append(message, rhs.message)
                .isEquals();
    }

    public final int hashCode() {
        // you pick a hard-coded, randomly chosen, non-zero, odd number
        // ideally different for each class
        return new HashCodeBuilder(15, 33).
                append(wasSuccessfullyRegistered).
                append(message).
                toHashCode();
    }

    public final String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
