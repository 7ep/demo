package com.coveros.training;

public enum PasswordResultEnums {
    TOO_SHORT,
    EMPTY_PASSWORD,
    INSUFFICIENT_ENTROPY, // as measured by a tool.  See implementation where this is used.
    SUCCESS,
    NULL // used to represent the initial state when initializing variables, so we don't have to use null.
}
