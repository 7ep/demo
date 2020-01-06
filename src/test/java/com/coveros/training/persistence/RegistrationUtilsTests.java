package com.coveros.training.persistence;

import com.coveros.training.domainobjects.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RegistrationUtilsTests {

    private static final String GOOD_PASSWORD = "LpcVWwRkWSNVH";
    private static final String ALICE = "alice";
    private static final String BAD_PASSWORD = "abc123horsestaples";
    private final PersistenceLayer persistenceLayer = mock(PersistenceLayer.class);
    private final RegistrationUtils registrationUtils = new RegistrationUtils(persistenceLayer);

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
     * While these passwords may look good, they represent
     * examples of passwords that can be easily cracked, as
     * measured by a tool we use.
     */
    @Test
    public void testShouldHaveInsufficientEntropyInPassword() {
        final List<String> badPasswords =
                Arrays.asList(
                        "abc123",
                        "abc123horse",
                        "abc123horsestaples",
                        "typical_password_123");
        for (String password : badPasswords) {
            final PasswordResult result = RegistrationUtils.isPasswordGood(password);
            Assert.assertEquals("password: " + password, PasswordResultEnums.INSUFFICIENT_ENTROPY, result.status);
        }
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
        when(persistenceLayer.searchForUserByName(ALICE)).thenReturn(new User(ALICE, 1));

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
        when(persistenceLayer.searchForUserByName(ALICE)).thenReturn(User.createEmpty());
        RegistrationResult expectedResult = new RegistrationResult(true, RegistrationStatusEnums.SUCCESSFULLY_REGISTERED);

        final RegistrationResult registrationResult =
                registrationUtils.processRegistration(ALICE, GOOD_PASSWORD);

        Assert.assertEquals(expectedResult, registrationResult);
    }

    /**
     * If we pass in an empty username, we should get a response that
     * registration failed.
     */
    @Test
    public void testShouldProcessRegistration_EmptyUsername() {
        RegistrationResult expectedResult = new RegistrationResult(false, RegistrationStatusEnums.EMPTY_USERNAME);

        final RegistrationResult registrationResult =
                registrationUtils.processRegistration("", GOOD_PASSWORD);

        Assert.assertEquals(expectedResult, registrationResult);
    }

    /**
     * Registration should fail if the password isn't good enough.
     */
    @Test
    public void testShouldProcessRegistration_BadPassword() {
        // this needs to not find a user
        when(persistenceLayer.searchForUserByName(ALICE)).thenReturn(User.createEmpty());
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
        when(persistenceLayer.searchForUserByName(ALICE)).thenReturn(new User(ALICE, 1));
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
