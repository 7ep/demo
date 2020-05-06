package com.coveros.training.authentication;

import com.coveros.training.persistence.IPersistenceLayer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.mockito.internal.verification.VerificationModeFactory.times;

public class LoginUtilsTests {

    private IPersistenceLayer mockPersistenceLayer = Mockito.mock(IPersistenceLayer.class);
    private LoginUtils loginUtils = Mockito.spy(new LoginUtils(mockPersistenceLayer));


    @Before
    public void init() {
        mockPersistenceLayer = Mockito.mock(IPersistenceLayer.class);
        loginUtils = Mockito.spy(new LoginUtils(mockPersistenceLayer));
    }

    @Test
    public void testCanCreateEmpty() {
        final LoginUtils loginUtils = LoginUtils.createEmpty();
        Assert.assertTrue(loginUtils.isEmpty());
    }

    @Test
    public void testCanSeeIfUserRegistered() {
        loginUtils.isUserRegistered("alice", "abc123");
        Mockito.verify(mockPersistenceLayer, times(1)).areCredentialsValid("alice", "abc123");
    }

}
