package com.coveros.training.library.domainobjects;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.checkerframework.checker.nullness.qual.Nullable;


import java.sql.Date;

/**
 * Represents the data that we consider full and complete to define
 * a particular loan of a book to a borrower in the library.
 * This coincides neatly with the details in the database.
 */
public final class Loan {

    /**
     * The date the book was checked out.
     */
    public final java.sql.Date checkoutDate;

    /**
     * The book that is checked out
     */
    public final Book book;

    /**
     * The borrower that has this book
     */
    public final Borrower borrower;

    /**
     * The identifier of this loan in our database
     */
    public final long id;

    public Loan(Book book, Borrower borrower, long id, Date checkoutDate) {
        this.book = book;
        this.borrower = borrower;
        this.id = id;
        this.checkoutDate = checkoutDate;
    }

    public final boolean equals(@Nullable Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj.getClass() != getClass()) {
            return false;
        }
        Loan rhs = (Loan) obj;
        return new EqualsBuilder()
                .append(id, rhs.id)
                .append(book, rhs.book)
                .append(borrower, rhs.borrower)
                .append(checkoutDate, rhs.checkoutDate)
                .isEquals();
    }

    public final int hashCode() {
        // you pick a hard-coded, randomly chosen, non-zero, odd number
        // ideally different for each class
        return new HashCodeBuilder(5, 21)
                .append(book)
                .append(borrower)
                .append(id)
                .append(checkoutDate)
                .toHashCode();
    }

    public final String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    public static Loan createEmpty() {
        return new Loan(Book.createEmpty(), Borrower.createEmpty(), 0, new Date(0));
    }

    public boolean isEmpty() {
        return this.equals(createEmpty());
    }
}
