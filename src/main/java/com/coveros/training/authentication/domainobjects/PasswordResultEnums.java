package com.coveros.training.authentication.domainobjects;

/**
 * All the values currently possible for our
 * assessment of a potential password
 */
public enum PasswordResultEnums {
    TOO_SHORT,

    /**
     * ironic, I know, but if it's too long we're open to DOS attacks.
     * It takes too long to process extremely long passwords.
     */
    TOO_LONG,
    EMPTY_PASSWORD,

    /**
     * as measured by a tool.  See implementation where this is used.
     */
    INSUFFICIENT_ENTROPY,
    SUCCESS,

    /**
     * This is only used for empty password results.  See {@link PasswordResult#createEmpty()}
     */
    NULL
}
