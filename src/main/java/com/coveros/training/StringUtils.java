package com.coveros.training;

import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Some simple helper methods for Strings.
 */
class StringUtils {

    /**
     * checks the String you pass in; if it's null, return an empty String.
     * Otherwise, return the unchanged string.
     */
    static String makeNotNullable(@Nullable String s) {
        return s == null ? "" : s;
    }

    /**
     * A simple helper method to avoid having a exclamation mark
     * to represent "not"
     */
    static boolean isNotEmpty(String s) {
        return !s.isEmpty();
    }
}
