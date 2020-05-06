package com.coveros.training.authentication.domainobjects;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Assert;
import org.junit.Test;

public class PasswordResultTests {

    @Test
    public void testShouldHaveEqualsAndHashcodeImplementedCorrectly() {
        EqualsVerifier.forClass(PasswordResult.class).verify();
    }

    @Test
    public void testShouldOutputGoodString() {
        final PasswordResult passwordResult = createTestPasswordResult();
        final String result = passwordResult.toString();
        Assert.assertTrue("was " + passwordResult,
                result.contains("status=SUCCESS") &&
                result.contains("entropy=0.0") &&
                result.contains("timeToCrackOffline=") &&
                result.contains("timeToCrackOnline=") &&
                result.contains("message=BASIC_PASSWORD_CHECKS_FAILED"));
    }

    private static PasswordResult createTestPasswordResult() {
        return PasswordResult.createDefault(PasswordResultEnums.SUCCESS);
    }

    @Test
    public void testCanCreateEmpty() {
        final PasswordResult passwordResult = PasswordResult.createEmpty();
        Assert.assertTrue(passwordResult.isEmpty());
    }
}
