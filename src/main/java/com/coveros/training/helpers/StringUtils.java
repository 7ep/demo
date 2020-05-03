package com.coveros.training.helpers;


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

}
