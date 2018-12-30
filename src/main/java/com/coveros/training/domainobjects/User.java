package com.coveros.training.domainobjects;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.checkerframework.checker.nullness.qual.Nullable;

public class User {

  /**
   * The username of the user
   */
  public final String name;

  /**
   * The identifier of the user in the database
   */
  public final long id;

  public User(String name, long id) {
    this.name = name;
    this.id = id;
  }

  public static User createEmpty() {
    return new User("", 0);
  }

  public final boolean equals(@Nullable Object obj) {
    if (obj == null) { return false; }
    if (obj == this) { return true; }
    if (obj.getClass() != getClass()) {
      return false;
    }
    User rhs = (User) obj;
    return new EqualsBuilder()
        .append(id, rhs.id)
        .append(name, rhs.name)
        .isEquals();
  }

  public final int hashCode() {
    // you pick a hard-coded, randomly chosen, non-zero, odd number
    // ideally different for each class
    return new HashCodeBuilder(19, 3).
        append(name).
        append(id).
        toHashCode();
  }

  public boolean isEmpty() {
    return this.equals(User.createEmpty());
  }
}
