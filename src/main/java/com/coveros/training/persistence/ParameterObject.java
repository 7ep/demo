package com.coveros.training.persistence;

import com.coveros.training.domainobjects.Book;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.checkerframework.checker.nullness.qual.Nullable;

public class ParameterObject {

    final Object data;
    final Class type;

    public ParameterObject(Object data, Class type) {
      this.data = data;
      this.type = type;
    }

  public final boolean equals(@Nullable Object obj) {
    if (obj == null) { return false; }
    if (obj == this) { return true; }
    if (obj.getClass() != getClass()) {
      return false;
    }
    ParameterObject rhs = (ParameterObject) obj;
    return new EqualsBuilder()
        .append(data, rhs.data)
        .append(type, rhs.type)
        .isEquals();
  }

  public final int hashCode() {
    // you pick a hard-coded, randomly chosen, non-zero, odd number
    // ideally different for each class
    return new HashCodeBuilder(63, 7).
        append(data).
        append(type).
        toHashCode();
  }

  public static ParameterObject createEmpty() {
    return new ParameterObject(new Object(), Class.class);
  }

  public boolean isEmpty() {
    return this.equals(ParameterObject.createEmpty());
  }

}
