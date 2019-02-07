package com.coveros.training.domainobjects;

public enum PasswordResultEnums {
    TOO_SHORT,
    EMPTY_PASSWORD,
    INSUFFICIENT_ENTROPY, // as measured by a tool.  See implementation where this is used.
    SUCCESS,
    ANALYSIS_TIMED_OUT, // if the tool timed out, then we cannot make any assertion about the password quality
    NULL // used to represent the initial state when initializing variables, so we don't have to use null.
}
