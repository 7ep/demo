package com.coveros.training;

/**
 * Some simple helper methods for Strings.
 */
class StringUtils {

    /**
     * Simply checks if the string is null or empty.
     * @param s the string
     * @return a boolean value for whether the String in question is null or empty (empty meaning size of 0)
     */
    static boolean isNullOrEmpty(String s) {
        return (s == null || s.isEmpty());
    }
}
