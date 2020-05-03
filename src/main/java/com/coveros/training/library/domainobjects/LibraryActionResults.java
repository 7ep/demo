package com.coveros.training.library.domainobjects;

/**
 * These are the possible results of business functions
 * related to the library.  For example, you might
 * get ALREADY_REGISTERED_BOOK if you are trying to register
 * a book and it's already in the library.
 */
public enum LibraryActionResults {

    ALREADY_REGISTERED_BOOK,

    /**
     * if a book isn't registered, then of course we cannot delete it.
     */
    NON_REGISTERED_BOOK_CANNOT_BE_DELETED,

    /**
     * if a borrower isn't registered, then of course we cannot delete it.
     */
    NON_REGISTERED_BORROWER_CANNOT_BE_DELETED,
    ALREADY_REGISTERED_BORROWER,
    BOOK_NOT_REGISTERED,
    BORROWER_NOT_REGISTERED,
    /**
     * someone already has this book checked out.
     */
    BOOK_CHECKED_OUT,
    SUCCESS,
    /**
     * if a method needs a book and none is provided
     */
    NO_BOOK_TITLE_PROVIDED,
    /**
     * if a method needs a borrower and none is provided
     */
    NO_BORROWER_PROVIDED,
    /**
     * may be used when initializing a variable of this type
     */
    NULL
}
