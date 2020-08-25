package com.coveros.training.authentication;

import com.coveros.training.authentication.domainobjects.*;
import com.coveros.training.persistence.IPersistenceLayer;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RegistrationUtilsTests {

    private static final String GOOD_PASSWORD = "LpcVWwRkWSNVH";
    private static final String ALICE = "alice";
    private static final String BAD_PASSWORD = "abc123horsestaples";
    private final IPersistenceLayer persistenceLayer = mock(IPersistenceLayer.class);
    private final RegistrationUtils registrationUtils = new RegistrationUtils(persistenceLayer);

    /**
     * a really short password can be found by brute force extremely quickly,
     * making this highly insecure
     */
    @Test
    public void testShouldFailOnShortPassword() {
        final PasswordResult result = RegistrationUtils.isPasswordGood("abc");
        Assert.assertEquals(PasswordResultEnums.TOO_SHORT, result.status);
    }

    @Test
    public void testShouldFailOnEmptyPassword_EmptyString() {
        final PasswordResult result = RegistrationUtils.isPasswordGood("");
        Assert.assertEquals(PasswordResultEnums.EMPTY_PASSWORD, result.status);
    }

    /**
     * A lightweight performance test for a slow call
     */
    @Ignore
    @Test
    public void testShouldPerformWell() {
        long start = System.currentTimeMillis();
        final List<String> goodPasswords =
                Arrays.asList(
                        "XaZE}SkOC/@k#blv}U+wOlDfj=a]q",
                        "-MzlAim%Zuh.=B|8N|Vd~`l5?*Cs,ZH\\'l/76t-9]W\\D$Il#vynO+~y_@",
                        "lm=U(#C;@Bl*jvEy_*U1QlR@3sje");
        for (String password : goodPasswords) {
            RegistrationUtils.isPasswordGood(password);
        }
        long finish = System.currentTimeMillis();
        long timeElapsed = finish - start;

        Assert.assertTrue("All these calls should have finished in a short time.  " +
                "Time elapsed was " + timeElapsed, timeElapsed < 1500);
    }

    /**
     * If we provide a good password, then by golly we should get that
     * as a result from our {@link RegistrationUtils#isPasswordGood} method.
     */
    @Test
    public void testShouldHaveSufficientEntropyInPassword() {
        final PasswordResult result = RegistrationUtils.isPasswordGood(GOOD_PASSWORD);
        Assert.assertEquals(PasswordResultEnums.SUCCESS, result.status);
    }

    /**
     * Not really a lot to test here...
     */
    @Test
    public void testShouldDetermineIfUserInDatabase() {
        // mock that a user is found when we search for them
        when(persistenceLayer.searchForUserByName(ALICE)).thenReturn(Optional.of(new User(ALICE, 1)));

        final boolean result = registrationUtils.isUserInDatabase(ALICE);

        Assert.assertTrue(result);
    }

    /**
     * Testing registration without hitting the actual database.
     * The password has to be sufficient to meet the entropy stipulations.
     * We need to mock the calls that will have been sent to the database.
     */
    @Test
    public void testShouldProcessRegistration_HappyPath() {
        // this needs to not find a user
        when(persistenceLayer.searchForUserByName(ALICE)).thenReturn(Optional.empty());
        RegistrationResult expectedResult = new RegistrationResult(true, RegistrationStatusEnums.SUCCESSFULLY_REGISTERED);

        final RegistrationResult registrationResult =
                registrationUtils.processRegistration(ALICE, GOOD_PASSWORD);

        Assert.assertEquals(expectedResult, registrationResult);
    }

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    /**
     * If we pass in an empty username, we should get a response that
     * registration failed.
     */
    @Test
    public void testShouldProcessRegistration_EmptyUsername() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("string must not be null or empty at this point");
        registrationUtils.processRegistration("", GOOD_PASSWORD);
    }

    /**
     * Registration should fail if the password isn't good enough.
     */
    @Test
    public void testShouldProcessRegistration_BadPassword() {
        // this needs to not find a user
        when(persistenceLayer.searchForUserByName(ALICE)).thenReturn(Optional.empty());
        final PasswordResult result = RegistrationUtils.isPasswordGood(BAD_PASSWORD);
        RegistrationResult expectedResult = new RegistrationResult(false, RegistrationStatusEnums.BAD_PASSWORD, result.toPrettyString());

        final RegistrationResult registrationResult =
                registrationUtils.processRegistration(ALICE, BAD_PASSWORD);

        Assert.assertEquals(expectedResult, registrationResult);
    }

    /**
     * Registration should fail if we're trying to register an already-existing user.
     */
    @Test
    public void testShouldProcessRegistration_ExistingUser() {
        // this needs to find an existing user - so they are already registered
        when(persistenceLayer.searchForUserByName(ALICE)).thenReturn(Optional.of(new User(ALICE, 1)));
        RegistrationResult expectedResult = new RegistrationResult(false, RegistrationStatusEnums.ALREADY_REGISTERED);

        final RegistrationResult registrationResult =
                registrationUtils.processRegistration(ALICE, GOOD_PASSWORD);

        Assert.assertEquals(expectedResult, registrationResult);
    }

    @Test
    public void testEmptyObject() {
        final RegistrationUtils registrationUtils = RegistrationUtils.createEmpty();
        Assert.assertTrue(registrationUtils.isEmpty());
    }

}
