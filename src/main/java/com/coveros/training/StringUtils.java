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

}
