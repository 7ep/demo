package com.coveros.training.domainobjects;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Assert;
import org.junit.Test;

public class PasswordResultTests {

    @Test
    public void testShouldHaveEqualsAndHashcodeImplementedCorrectly() {
        EqualsVerifier.forClass(PasswordResult.class);
    }

    @Test
    public void testShouldOutputGoodString() {
        final PasswordResult passwordResult = PasswordResult.createDefault(PasswordResultEnums.SUCCESS);
        Assert.assertTrue(passwordResult.toString().contains("status=SUCCESS,entropy=0.0,timeToCrackOffline=,timeToCrackOnline=,message=BASIC_PASSWORD_CHECKS_FAILED"));
    }
}
