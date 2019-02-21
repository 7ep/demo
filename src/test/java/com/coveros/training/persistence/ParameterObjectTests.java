package com.coveros.training.persistence;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Assert;
import org.junit.Test;

public class ParameterObjectTests {

    @Test
    public void testShouldHaveEqualsAndHashcodeImplementedCorrectly() {
        EqualsVerifier.forClass(ParameterObject.class).verify();
    }

    @Test
    public void testShouldOutputGoodString() {
        final ParameterObject parameterObject = createTestParameterObject();
        Assert.assertTrue("toString was: " + parameterObject.toString(), parameterObject.toString().contains("data=abc123,type=class java.lang.String"));
    }

    private static ParameterObject createTestParameterObject() {
        return new ParameterObject("abc123", String.class);
    }

    @Test
    public void testCanCreateEmpty() {
        final ParameterObject parameterObject = ParameterObject.createEmpty();
        Assert.assertTrue(parameterObject.isEmpty());
    }
}

