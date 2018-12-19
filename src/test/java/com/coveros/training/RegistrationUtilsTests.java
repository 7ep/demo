package com.coveros.training;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static com.coveros.training.PasswordResultEnums.*;

public class RegistrationUtilsTests {

    @Test
    public void testShouldFailOnShortPassword() {
        final PasswordResult result = RegistrationUtils.isPasswordGood("abc");
        Assert.assertEquals(TOO_SHORT, result.status());
    }

    @Test
    public void testShouldFailOnEmptyPassword_EmptyString() {
        final PasswordResult result = RegistrationUtils.isPasswordGood("");
        Assert.assertEquals(EMPTY_PASSWORD, result.status());
    }

    @Test
    public void testShouldFailOnEmptyPassword_NullValue() {
        final PasswordResult result = RegistrationUtils.isPasswordGood(null);
        Assert.assertEquals(EMPTY_PASSWORD, result.status());
    }

    /**
     * While these passwords may look good, they represent
     * examples of passwords that can be easily cracked, as
     * measured by a tool we use.
     */
    @Test
    public void testShouldHaveInsufficientEntropyInPassword() {
        final List<String> badPasswords = Arrays.asList("abc123", "abc123horse", "abc123horsestaples", "typical_password_123");
        for (String password : badPasswords) {
            final PasswordResult result = RegistrationUtils.isPasswordGood(password);
            Assert.assertEquals(INSUFFICIENT_ENTROPY, result.status());
        }
    }

    @Test
    public void testShouldHaveSufficientEntropyInPassword() {
        final PasswordResult result = RegistrationUtils.isPasswordGood("LpcVWwRkWSNVH");
        Assert.assertEquals(SUCCESS, result.status());
    }
}
