package com.coveros.training;

import com.coveros.training.domainobjects.Book;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.sql.Date;

class Loan {

  /**
   * The date the book was checked out.
   */
  final java.sql.Date checkoutDate;

  /**
   * The book that is checked out
   */
  final Book book;

  /**
   * The borrower that has this book
   */
  final Borrower borrower;

  /**
   * The identifier of this loan in our database
   */
  final long id;

  Loan(Book book, Borrower borrower, long id, Date checkoutDate) {
    this.book = book;
    this.borrower = borrower;
    this.id = id;
    this.checkoutDate = checkoutDate;
  }

  static Loan createEmpty() {
    return new Loan(Book.createEmpty(), Borrower.createEmpty(), 0, new Date(0));
  }

  public final boolean equals(@Nullable Object obj) {
    if (obj == null) { return false; }
    if (obj == this) { return true; }
    if (obj.getClass() != getClass()) {
      return false;
    }
    Loan rhs = (Loan) obj;
    return new EqualsBuilder()
        .append(id, rhs.id)
        .append(book, rhs.book)
        .append(borrower, rhs.borrower)
        .isEquals();
  }

  public final int hashCode() {
    // you pick a hard-coded, randomly chosen, non-zero, odd number
    // ideally different for each class
    return new HashCodeBuilder(5, 21).
        append(book).
        append(borrower).
        append(id).
        toHashCode();
  }


  boolean isEmpty() {
    return this.equals(Loan.createEmpty());
  }
}
