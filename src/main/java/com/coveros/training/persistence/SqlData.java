package com.coveros.training.persistence;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
  public void addParameter(Object data, Class clazz) {
    params.add(new ParameterObject(data, clazz));
  }

  /**
   * Loop through the parameters that have been added and
   * serially add them to the prepared statement.
   * @param st a prepared statement
   * @throws SQLException If anything happens during processing, we're not handling it here.
   */
  public void applyParametersToPreparedStatement(PreparedStatement st) throws SQLException {
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
  }
}
