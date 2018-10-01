package com.coveros.training;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Assert;
import org.junit.Test;

/**
 * A test to make sure all our value objects follow rules.
 *
 * Values should be simple containers that are immutable and simple.
 * When comparing two values, they should be considered the same
 * if all their subcomponents equal each other.
 */
public class ValueTests {

    @Test
    public void testShouldHaveEqualsAndHashcodeImplementedCorrectly() {
        EqualsVerifier.forClass(RegistrationResult.class);
    }

    @Test
    public void testShouldOutputGoodString() {
        final RegistrationResult registrationResult = RegistrationResult.create(false, PasswordResult.EMPTY_PASSWORD.toString());
        Assert.assertEquals("RegistrationResult{wasSuccessfullyRegistered=false, message=EMPTY_PASSWORD}", registrationResult.toString());
    }
}
