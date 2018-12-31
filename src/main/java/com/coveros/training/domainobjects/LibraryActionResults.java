package com.coveros.training.domainobjects;

public enum LibraryActionResults {
    ALREADY_REGISTERED_BOOK,
    ALREADY_REGISTERED_BORROWER,
    BOOK_NOT_REGISTERED,
    BORROWER_NOT_REGISTERED,
    BOOK_CHECKED_OUT, // someone already has this book checked out.
    SUCCESS,
    NULL // may be used when initializing a variable of this type
}
