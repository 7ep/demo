package com.coveros.training.authentication.domainobjects;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Assert;
import org.junit.Test;


public class RegistrationResultTests {

    @Test
    public void testShouldHaveEqualsAndHashcodeImplementedCorrectly() {
        EqualsVerifier.forClass(RegistrationResult.class).verify();
    }

    @Test
    public void testShouldOutputGoodString() {
        final RegistrationResult registrationResult = RegistrationResult.createEmpty();
        final String result = registrationResult.toString();
        Assert.assertTrue("was " + registrationResult,
                result.contains("wasSuccessfullyRegistered=false") &&
                        result.contains("status=EMPTY") &&
                        result.contains("message="));
    }

    @Test
    public void testCanCreateEmpty() {
        final RegistrationResult registrationResult = RegistrationResult.createEmpty();
        Assert.assertTrue(registrationResult.isEmpty());
    }

}
