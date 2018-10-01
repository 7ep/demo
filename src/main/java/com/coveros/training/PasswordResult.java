package com.coveros.training;

public enum PasswordResult {
    TOO_SHORT,
    EMPTY_PASSWORD,
    INSUFFICIENT_ENTROPY, // as measured by a tool.  See implementation where this is used.
    SUCCESS
}
