package com.coveros.training;

/**
 * helper methods to assert that certain things in our code are true,
 * or else we throw an exception.
 */
class CheckUtils {

    static void checkIntParamPositive(long parameter) {
        if (parameter <= 0) {
            throw new IllegalArgumentException("int value must be 1 or above.");
        }
    }

}
