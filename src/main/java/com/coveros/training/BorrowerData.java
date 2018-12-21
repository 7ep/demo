package com.coveros.training;

import com.google.auto.value.AutoValue;

/**
 * An immutable object that contains data for a borrower.  That is, someone
 * who wants to borrow a book from a library.
 */
@AutoValue
abstract class BorrowerData {

    static BorrowerData create(long id, String name) {
        return new AutoValue_BorrowerData(id, name);
    }

    /**
     * The identifier for this borrower in the database.
     */
    abstract long id();

    /**
     * The name of the borrower
     */
    abstract String name();
}
