package com.coveros.training.persistence;

import com.coveros.training.domainobjects.PasswordResult;
import com.coveros.training.domainobjects.RegistrationResult;
import com.coveros.training.domainobjects.RegistrationStatusEnums;
import com.coveros.training.domainobjects.User;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static com.coveros.training.domainobjects.PasswordResultEnums.EMPTY_PASSWORD;
import static com.coveros.training.domainobjects.PasswordResultEnums.*;
import static com.coveros.training.domainobjects.RegistrationStatusEnums.*;
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
        Assert.assertEquals(TOO_SHORT, result.status);
    }

    @Test
    public void testShouldFailOnEmptyPassword_EmptyString() {
        final PasswordResult result = RegistrationUtils.isPasswordGood("");
        Assert.assertEquals(EMPTY_PASSWORD, result.status);
    }

    /**
     * While these passwords may look good, they represent
     * examples of passwords that can be easily cracked, as
     * measured by a tool we use.
     */
    public void testShouldHaveInsufficientEntropyInPassword() {
        final List<String> badPasswords =
                Arrays.asList(
                        "abc123",
                        "abc123horse",
                        "abc123horsestaples",
                        "typical_password_123");
        for (String password : badPasswords) {
            final PasswordResult result = RegistrationUtils.isPasswordGood(password);
            Assert.assertEquals("password: " + password, INSUFFICIENT_ENTROPY, result.status);
        }
    }

    @Test
    public void testShouldHaveSufficientEntropyInPassword() {
        final PasswordResult result = RegistrationUtils.isPasswordGood(GOOD_PASSWORD);
        Assert.assertEquals(SUCCESS, result.status);
    }

    @Test
    public void testShouldDetermineIfUserInDatabase() {
        when(persistenceLayer.searchForUserByName(ALICE)).thenReturn(new User(ALICE, 1));
        final boolean result = registrationUtils.isUserInDatabase(ALICE);
        Assert.assertTrue(result);
    }

    /**
     * Testing registration without hitting the actual database.
     * <p>
     * The password has to be sufficient to meet the entropy stipulations.
     * <p>
     * We need to mock the calls that will have been sent to the database.
     */
    @Test
    public void testShouldProcessRegistration_HappyPath() {
        // this needs to not find a user
        when(persistenceLayer.searchForUserByName(ALICE)).thenReturn(User.createEmpty());
        RegistrationResult expectedResult = new RegistrationResult(true, SUCCESSFULLY_REGISTERED);

        final RegistrationResult registrationResult =
                registrationUtils.processRegistration(ALICE, GOOD_PASSWORD);

        Assert.assertEquals(expectedResult, registrationResult);
    }

    @Test
    public void testShouldProcessRegistration_EmptyUsername() {
        RegistrationResult expectedResult = new RegistrationResult(false, EMPTY_USERNAME);

        final RegistrationResult registrationResult =
                registrationUtils.processRegistration("", GOOD_PASSWORD);

        Assert.assertEquals(expectedResult, registrationResult);
    }

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

    @Test
    public void testShouldProcessRegistration_ExistingUser() {
        // this needs to find an existing user - so they are already registered
        when(persistenceLayer.searchForUserByName(ALICE)).thenReturn(new User(ALICE, 1));
        RegistrationResult expectedResult = new RegistrationResult(false, ALREADY_REGISTERED);

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
