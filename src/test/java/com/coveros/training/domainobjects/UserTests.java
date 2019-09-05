package com.coveros.training.domainobjects;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Assert;
import org.junit.Test;

public class UserTests {

    @Test
    public void testShouldHaveEqualsAndHashcodeImplementedCorrectly() {
        EqualsVerifier.forClass(User.class).verify();
    }

    @Test
    public void testShouldOutputGoodString() {
        final User user = createTestUser();
        Assert.assertTrue("toString was: " + user.toString(), user.toString().contains("name=alice,id=1"));
    }

    @Test
    public void testCanCreateEmpty() {
        final User user = User.createEmpty();
        Assert.assertTrue(user.isEmpty());
    }

    private static User createTestUser() {
        return new User("alice", 1);
    }

}
