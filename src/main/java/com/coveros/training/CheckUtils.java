package com.coveros.training;

/**
 * helper methods to assert that certain things in our code are true,
 * or else we throw an exception.
 */
class CheckUtils {

    static void checkIntParamNotNull(long parameter) {
        if (parameter <= 0) {
            throw new IllegalArgumentException("int value must be 1 or above.");
        }
    }

    static void checkStringNotNull(String parameter) {
        if (StringUtils.isNullOrEmpty(parameter)) {
            throw new IllegalArgumentException("String value cannot be null or empty");
        }
    }
}
