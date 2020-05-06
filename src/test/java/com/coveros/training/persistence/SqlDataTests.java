package com.coveros.training.persistence;

import com.coveros.training.authentication.domainobjects.User;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Month;

import static org.mockito.Mockito.doThrow;

public class SqlDataTests {

    private static final Date BORROW_DATE = Date.valueOf(LocalDate.of(2018, Month.JANUARY, 1));
    private PreparedStatement preparedStatement = Mockito.mock(PreparedStatement.class);

    @Before
    public void init() {
        preparedStatement = Mockito.mock(PreparedStatement.class);
    }

    @Test
    public void testShouldHaveEqualsAndHashcodeImplementedCorrectly() {
        EqualsVerifier.forClass(SqlData.class).verify();
    }

    @Test
    public void testShouldOutputGoodString() {
        final SqlData<String> sqlData = createTestSqlData();
        Assert.assertTrue("toString was: " + sqlData.toString(), sqlData.toString().contains("description=this is the description,params=,prepared statement=this is the prepared statement = ?;"));
    }

    @Test
    public void testCanCreateEmpty() {
        final SqlData<String> sqlData = SqlData.createEmpty();
        Assert.assertTrue(sqlData.isEmpty());
    }

    private <T> SqlData<T> createTestSqlData() {
        return new SqlData<>("this is the description", "this is the prepared statement = ?;");
    }

    @Test
    public void testCanApplyParamsToPreparedStatement_Long() throws SQLException {
        applyParam(1L, Long.class);
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

    /**
     * If for some reason the prepared statement throws a SQLException, we
     * will catch it and throw it as a SqlRuntimeException
     */
    @Test(expected = SqlRuntimeException.class)
    public void testCanApplyParamsToPreparedStatement_NegativeCase() throws SQLException {
        doThrow(new SQLException()).when(preparedStatement).setString(1, "");
        applyParam("", String.class);
    }

    private <T> void applyParam(Object o, Class<T> clazz) {
        final SqlData<User> sqlData = new SqlData<>("just a test", "SELECT * FROM user WHERE id = ?");
        sqlData.addParameter(o, clazz);

        sqlData.applyParametersToPreparedStatement(preparedStatement);
    }

}
