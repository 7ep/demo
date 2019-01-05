package com.coveros.training.persistence;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.PrintWriter;
import java.sql.SQLException;

/**
 * See {@link EmptyDataSource} for more detail.
 * <p>
 * Just confirm that all methods throw a NotImplementedException
 */
public class EmptyDataSourceTests {

  private EmptyDataSource emptyDataSource;

  @Before
  public void init() {
    emptyDataSource = new EmptyDataSource();
  }

  @Test(expected = NotImplementedException.class)
  public void testGetConnection() throws SQLException {
    emptyDataSource.getConnection();
  }

  @Test(expected = NotImplementedException.class)
  public void testGetConnectionWithParams() throws SQLException {
    emptyDataSource.getConnection("", "");
  }

  @Test(expected = NotImplementedException.class)
  public void testUnwrap() throws SQLException {
    emptyDataSource.unwrap(emptyDataSource.getClass());
  }

  @Test(expected = NotImplementedException.class)
  public void testIsWrapperFor() throws SQLException {
    emptyDataSource.isWrapperFor(emptyDataSource.getClass());
  }

  @Test(expected = NotImplementedException.class)
  public void testGetLogWriter() throws SQLException {
    emptyDataSource.getLogWriter();
  }

  @Test(expected = NotImplementedException.class)
  public void testSetLogWriter() throws SQLException {
    emptyDataSource.setLogWriter(Mockito.mock(PrintWriter.class));
  }

  @Test(expected = NotImplementedException.class)
  public void testSetLoginTimeout() throws SQLException {
    emptyDataSource.setLoginTimeout(0);
  }

  @Test(expected = NotImplementedException.class)
  public void testGetLoginTimeout() throws SQLException {
    emptyDataSource.getLoginTimeout();
  }

  @Test(expected = NotImplementedException.class)
  public void testGetParentLogger() throws SQLException {
    emptyDataSource.getParentLogger();
  }

}
