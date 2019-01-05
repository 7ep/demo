package com.coveros.training.domainobjects;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Assert;
import org.junit.Test;

import static com.coveros.training.domainobjects.PasswordResultEnums.EMPTY_PASSWORD;

public class RegistrationResultTests {

    @Test
    public void testShouldHaveEqualsAndHashcodeImplementedCorrectly() {
        EqualsVerifier.forClass(RegistrationResult.class).verify();
    }

    @Test
    public void testShouldOutputGoodString() {
        final RegistrationResult registrationResult = new RegistrationResult(false, EMPTY_PASSWORD.toString());
        Assert.assertTrue(registrationResult.toString().contains("[wasSuccessfullyRegistered=false,message=EMPTY_PASSWORD]"));
    }

}
