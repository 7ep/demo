package com.coveros.training.persistence;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Month;

public class SqlDataTests {

  private static final Date BORROW_DATE = Date.valueOf(LocalDate.of(2018, Month.JANUARY, 1));
  private PreparedStatement preparedStatement;

  @Before
  public void init() {
    preparedStatement = Mockito.mock(PreparedStatement.class);
  }

  @Test
  public void testShouldOutputGoodString() {
    final SqlData sqlData = createTestSqlData();
    Assert.assertTrue("toString was: " + sqlData.toString(), sqlData.toString().contains("description=this is the description,preparedStatement=this is the prepared statement = ?;,params=[]"));
  }

  @Test
  public void testCanCreateEmpty() {
    final SqlData sqlData = SqlData.createEmpty();
    Assert.assertTrue(sqlData.isEmpty());
  }

  private SqlData createTestSqlData() {
    return new SqlData("this is the description", "this is the prepared statement = ?;");
  }

  @Test
  public void testCanApplyParamsToPreparedStatement_Long() throws SQLException {
    applyParam(1l, Long.class);
    Mockito.verify(preparedStatement, Mockito.times(1)).setLong(1, 1);
  }

  @Test
  public void testCanApplyParamsToPreparedStatement_String() throws SQLException {
    applyParam("1", String.class);
    Mockito.verify(preparedStatement, Mockito.times(1)).setString(1, "1");
  }

  @Test
  public void testCanApplyParamsToPreparedStatement_Integer() throws SQLException {
    applyParam(1, Integer.class);
    Mockito.verify(preparedStatement, Mockito.times(1)).setInt(1, 1);
  }

  @Test
  public void testCanApplyParamsToPreparedStatement_Date() throws SQLException {
    applyParam(BORROW_DATE, Date.class);
    Mockito.verify(preparedStatement, Mockito.times(1)).setDate(1, BORROW_DATE);
  }

  private SqlData applyParam(Object o, Class clazz) {
    final SqlData sqlData = new SqlData("just a test", "SELECT * FROM user WHERE id = ?");
    sqlData.addParameter(o, clazz);

    sqlData.applyParametersToPreparedStatement(preparedStatement);
    return sqlData;
  }

}
