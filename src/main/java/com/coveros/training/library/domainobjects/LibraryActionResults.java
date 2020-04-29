package com.coveros.training.library.domainobjects;

public enum LibraryActionResults {
    ALREADY_REGISTERED_BOOK,
    NON_REGISTERED_BOOK_CANNOT_BE_DELETED, // if a book isn't registered, then of course we cannot delete it.
    NON_REGISTERED_BORROWER_CANNOT_BE_DELETED, // if a borrower isn't registered, then of course we cannot delete it.
    ALREADY_REGISTERED_BORROWER,
    BOOK_NOT_REGISTERED,
    BORROWER_NOT_REGISTERED,
    BOOK_CHECKED_OUT, // someone already has this book checked out.
    SUCCESS,
    NO_BOOK_TITLE_PROVIDED,  // if a method needs a book and none is provided
    NO_BORROWER_PROVIDED,   // if a method needs a borrower and none is provided
    NULL // may be used when initializing a variable of this type
}
