package com.coveros.training;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.checkerframework.checker.nullness.qual.Nullable;

class Book {

  /**
   * The title of the book
   */
  final String title;

  /**
   * The identifier number in our database
   */
  final long id;

  Book(String title, long id) {
    this.title = title;
    this.id = id;
  }

  static Book createEmpty() {
    return new Book("", 0);
  }

  public final boolean equals(@Nullable Object obj) {
    if (obj == null) { return false; }
    if (obj == this) { return true; }
    if (obj.getClass() != getClass()) {
      return false;
    }
    Book rhs = (Book) obj;
    return new EqualsBuilder()
        .append(id, rhs.id)
        .append(title, rhs.title)
        .isEquals();
  }

  public final int hashCode() {
    // you pick a hard-coded, randomly chosen, non-zero, odd number
    // ideally different for each class
    return new HashCodeBuilder(13, 33).
        append(id).
        append(title).
        toHashCode();
  }


  boolean isEmpty() {
    return this.equals(Book.createEmpty());
  }

}
