package com.coveros.training.helpers;


import org.apache.logging.log4j.core.util.JsonUtils;
import org.checkerframework.checker.nullness.qual.Nullable;



/**
 * Some simple helper methods for Strings.
 */
public class StringUtils {

    private StringUtils() {
        // using a private constructor to hide the implicit public one.
    }

    /**
     * checks the String you pass in; if it's null, return an empty String.
     * Otherwise, return the unchanged string.
     */
    public static String makeNotNullable(@Nullable String s) {
        return s == null ? "" : s;
    }


    // a table of some of the values that may need to be
    // escaped in JSON strings
    final static byte SINGLE_QUOTE    = 39;
    final static byte DOUBLE_QUOTE    = 34;
    final static byte BACKSLASH       = 92;
    final static byte NEW_LINE        = 10;
    final static byte CARRIAGE_RETURN = 13;
    final static byte TAB             = 9;
    final static byte BACKSPACE       = 8;
    final static byte FORM_FEED       = 12;

    /**
     * Given a string, replaces characters so that it is safe to use
     * as JSON
     * @param value String value to convert
     * @return a properly escaped string, usable in JSON
     */
    public static String escapeForJson(String value) {
        StringBuilder sb = new StringBuilder();
        JsonUtils.quoteAsString(value, sb);
        return sb.toString();
    }


}
