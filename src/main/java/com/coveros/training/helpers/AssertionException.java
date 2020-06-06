package com.coveros.training.helpers;

/**
 * This exception is thrown whenever something is
 * set to be true as an invariant, but isn't actually true.
 * See https://en.wikipedia.org/wiki/Invariant_(mathematics)#Invariants_in_computer_science
 */
public class AssertionException extends RuntimeException {

    public AssertionException(String message) {
        super(message);
    }
}
