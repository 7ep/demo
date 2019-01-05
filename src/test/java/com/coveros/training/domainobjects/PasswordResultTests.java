package com.coveros.training.domainobjects;

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
        Assert.assertTrue(passwordResult.toString().contains("status=SUCCESS,entropy=0.0,timeToCrackOffline=,timeToCrackOnline=,message=BASIC_PASSWORD_CHECKS_FAILED"));
    }

    static PasswordResult createTestPasswordResult() {
        return PasswordResult.createDefault(PasswordResultEnums.SUCCESS);
    }

    @Test
    public void testCanCreateEmpty() {
        final PasswordResult passwordResult = PasswordResult.createEmpty();
        Assert.assertTrue(passwordResult.isEmpty());
    }
}
