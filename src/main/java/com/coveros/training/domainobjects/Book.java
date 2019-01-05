package com.coveros.training.domainobjects;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.checkerframework.checker.nullness.qual.Nullable;

public class Book {

  /**
   * The title of the book
   */
  public final String title;

  /**
   * The identifier number in our database
   */
  public final long id;

  public Book(long id, String title) {
    this.title = title;
    this.id = id;
  }

  public static Book createEmpty() {
    return new Book(0, "");
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

  public final String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

  public boolean isEmpty() {
    return this.equals(Book.createEmpty());
  }

}
