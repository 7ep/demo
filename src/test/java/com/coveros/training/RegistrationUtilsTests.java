package com.coveros.training;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class RegistrationUtilsTests {

    @Test
    public void testShouldFailOnShortPassword() {
        final PasswordResult result = RegistrationUtils.isPasswordGood("abc");
        Assert.assertEquals(PasswordResult.TOO_SHORT, result);
    }

    @Test
    public void testShouldFailOnEmptyPassword_EmptyString() {
        final PasswordResult result = RegistrationUtils.isPasswordGood("");
        Assert.assertEquals(PasswordResult.EMPTY_PASSWORD, result);
    }

    @Test
    public void testShouldFailOnEmptyPassword_NullValue() {
        final PasswordResult result = RegistrationUtils.isPasswordGood(null);
        Assert.assertEquals(PasswordResult.EMPTY_PASSWORD, result);
    }

    /**
     * While these passwords may look good, they represent
     * examples of passwords that can be easily cracked, as
     * measured by a tool we use.
     */
    @Test
    public void testShouldHaveInsufficientEntropyInPassword() {
        final List<String> badPasswords = Arrays.asList("abc123", "typical_password_123");
        for (String password : badPasswords) {
            final PasswordResult result = RegistrationUtils.isPasswordGood(password);
            Assert.assertEquals(PasswordResult.INSUFFICIENT_ENTROPY, result);
        }
    }

    @Test
    public void testShouldHaveSufficientEntropyInPassword() {
        final PasswordResult result = RegistrationUtils.isPasswordGood("LpcVWwRkWSNVH");
        Assert.assertEquals(PasswordResult.SUCCESS, result);
    }
}
