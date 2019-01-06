package com.coveros.training.persistence;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

/**
 * This class only exists because we want no nulls in our
 * system.  In order to do this, we have to be able to create
 * "empty" versions of our classes.  To create an empty String, for
 * example, is simply "".  But an empty DataSource would look like this.
 */
public class EmptyDataSource implements DataSource {
  @Override
  public Connection getConnection() throws SQLException {
    throw new NotImplementedException();
  }

  @Override
  public Connection getConnection(String username, String password) throws SQLException {
    throw new NotImplementedException();
  }

  @Override
  public <T> T unwrap(Class<T> iface) throws SQLException {
    throw new NotImplementedException();
  }

  @Override
  public boolean isWrapperFor(Class<?> iface) throws SQLException {
    throw new NotImplementedException();
  }

  @Override
  public PrintWriter getLogWriter() throws SQLException {
    throw new NotImplementedException();
  }

  @Override
  public void setLogWriter(PrintWriter out) throws SQLException {
    throw new NotImplementedException();
  }

  @Override
  public void setLoginTimeout(int seconds) throws SQLException {
    throw new NotImplementedException();
  }

  @Override
  public int getLoginTimeout() throws SQLException {
    throw new NotImplementedException();
  }

  @Override
  public Logger getParentLogger() throws SQLFeatureNotSupportedException {
    throw new NotImplementedException();
  }

}
