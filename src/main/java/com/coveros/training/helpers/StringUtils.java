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
    static final byte SINGLE_QUOTE    = 39;
    static final byte DOUBLE_QUOTE    = 34;
    static final byte BACKSLASH       = 92;
    static final byte NEW_LINE        = 10;
    static final byte CARRIAGE_RETURN = 13;
    static final byte TAB             = 9;
    static final byte BACKSPACE       = 8;
    static final byte FORM_FEED       = 12;

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
