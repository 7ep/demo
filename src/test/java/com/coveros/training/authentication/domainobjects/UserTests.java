package com.coveros.training.authentication.domainobjects;

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
        final String s = user.toString();
        Assert.assertTrue("toString was: " + user.toString(),
                s.contains("name=alice") &&
                s.contains("id=1"));
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
