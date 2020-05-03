package com.coveros.training.authentication.domainobjects;

/**
 * All the values possible for the results of an
 * attempted registration
 */
public enum RegistrationStatusEnums {
    ALREADY_REGISTERED,
    EMPTY_USERNAME,
    EMPTY_PASSWORD,
    SUCCESSFULLY_REGISTERED,
    BAD_PASSWORD,
    EMPTY // represents the state of no status.  Used by the empty registration result.

}
