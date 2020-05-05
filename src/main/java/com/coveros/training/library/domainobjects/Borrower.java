package com.coveros.training.library.domainobjects;

import com.coveros.training.helpers.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.checkerframework.checker.nullness.qual.Nullable;


/**
 * An immutable data value representing the data for a borrower.
 * <p>
 * A borrower is a person who borrows a book from a library.
 * <p>
 * Note that we make our fields public because they are final,
 * so there's no need to have methods wrapping them.
 */
public final class Borrower {

    /**
     * The identifier for this borrower in the database.
     */
    public final long id;

    /**
     * The name of the borrower
     */
    public final String name;

    public Borrower(long id, String name) {
        this.id = id;
        this.name = name;
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
        Borrower rhs = (Borrower) obj;
        return new EqualsBuilder()
                .append(id, rhs.id)
                .append(name, rhs.name)
                .isEquals();
    }

    public final int hashCode() {
        // you pick a hard-coded, randomly chosen, non-zero, odd number
        // ideally different for each class
        return new HashCodeBuilder(17, 37).
                append(id).
                append(name).
                toHashCode();
    }

    public final String toOutputString() {
        return String.format("{\"Name\": \"%s\", \"Id\": \"%s\"}", StringUtils.escapeForJson(name), id);
    }

    public final String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    public static Borrower createEmpty() {
        return new Borrower(0, "");
    }

    public boolean isEmpty() {
        return this.equals(createEmpty());
    }
}
