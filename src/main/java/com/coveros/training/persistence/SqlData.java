package com.coveros.training.persistence;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class encapsulates some of the actions related to
 * injecting data into a prepared SQL statement, so that
 * we are able to summarize what we want done without
 * all the annoying boilerplate.  See examples like {@link PersistenceLayer#saveNewBorrower}
 */
class SqlData {

  /**
   * A summary description of what this SQL is doing.
   */
  final String description;

  /**
   * This is the String text of the SQL prepared statement.  We're using PostgreSQL,
   * see https://jdbc.postgresql.org/documentation/81/server-prepare.html
   */
  final String preparedStatement;

  /**
   * The data that we will inject to the SQL statement.
   */
  final private List<ParameterObject> params;


  SqlData(String description, String preparedStatement) {
    this.description = description;
    this.preparedStatement = preparedStatement;
    this.params = new ArrayList<>();
  }


  /**
   * A list of the parameters to a particular SQL statement.
   * Add to this list in the order of the statement.
   * For example,
   *    for SELECT * FROM USERS WHERE a = ? and b = ?
   *
   * first add the parameter for a, then for b.
    * @param data a particular item of data.  Any object will do.  Look at {@link #applyParametersToPreparedStatement(PreparedStatement)}
   *             to see what we can process.
   * @param clazz the class of the thing.  I would rather not use reflection, let's keep it above board for now.
   */
  void addParameter(Object data, Class clazz) {
    params.add(new ParameterObject(data, clazz));
  }

  /**
   * Loop through the parameters that have been added and
   * serially add them to the prepared statement.
   * @param st a prepared statement
   */
  void applyParametersToPreparedStatement(PreparedStatement st) {
    try {
      for (int i = 1; i <= params.size(); i++) {
        ParameterObject p = params.get(i - 1);
        if (p.type == String.class) {
          st.setString(i, (String) p.data);
          continue;
        }
        if (p.type == Integer.class) {
          st.setInt(i, (Integer) p.data);
          continue;
        }
        if (p.type == Long.class) {
          st.setLong(i, (Long) p.data);
          continue;
        }
        if (p.type == Date.class) {
          st.setDate(i, (Date) p.data);
          continue;
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
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
    SqlData rhs = (SqlData) obj;
    return new EqualsBuilder()
        .append(description, rhs.description)
        .append(preparedStatement, rhs.preparedStatement)
        .append(params, rhs.params)
        .isEquals();
  }

  public final int hashCode() {
    // you pick a hard-coded, randomly chosen, non-zero, odd number
    // ideally different for each class
    return new HashCodeBuilder(53, 97)
        .append(description)
        .append(preparedStatement)
        .append(params)
        .toHashCode();
  }

  public final String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

  public static SqlData createEmpty() {
    return new SqlData("", "");
  }

  public boolean isEmpty() {
    return this.equals(SqlData.createEmpty());
  }
}
