package com.coveros.training.helpers;

import org.junit.Test;

import static com.coveros.training.helpers.StringUtils.*;
import static org.junit.Assert.assertEquals;

public class StringUtilsTests {

    @Test
    public void testShouldConvertNullToEmptyString() {
        final String s = StringUtils.makeNotNullable(null);
        assertEquals("", s);
    }

    @Test
    public void testShouldNotAlterNonNullString() {
        final String s = StringUtils.makeNotNullable("abc");
        assertEquals("abc", s);
    }

    /**
     * JSON won't parse if it has a double quote in a string
     */
    @Test
    public void testEscapeForJson_ShouldEscapeDoubleQuote() {
        final String expectedResult = new String(new byte[]{BACKSLASH, DOUBLE_QUOTE});
        final String inputString = new String(new byte[]{DOUBLE_QUOTE});
        String result = StringUtils.escapeForJson(inputString);
        assertEquals(expectedResult, result);
    }

    /**
     * Testing another potential problem with a JSON string
     */
    @Test
    public void testEscapeForJson_ShouldEscapeBackslash() {
        final String expectedResult = new String(new byte[]{BACKSLASH, BACKSLASH});
        final String inputString = new String(new byte[]{BACKSLASH});
        String result = StringUtils.escapeForJson(inputString);
        assertEquals(expectedResult, result);
    }

}
