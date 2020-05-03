package com.coveros.training.persistence;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.PrintWriter;

/**
 * See {@link EmptyDataSource} for more detail.
 * <p>
 * Just confirm that all methods throw a NotImplementedException
 */
public class EmptyDataSourceTests {

    private EmptyDataSource emptyDataSource = new EmptyDataSource();

    @Before
    public void init() {
        emptyDataSource = new EmptyDataSource();
    }

    @Test(expected = NotImplementedException.class)
    public void testGetConnection() {
        emptyDataSource.getConnection();
    }

    @Test(expected = NotImplementedException.class)
    public void testGetConnectionWithParams() {
        emptyDataSource.getConnection("", "");
    }

    @Test(expected = NotImplementedException.class)
    public void testUnwrap() {
        emptyDataSource.unwrap(emptyDataSource.getClass());
    }

    @Test(expected = NotImplementedException.class)
    public void testIsWrapperFor() {
        emptyDataSource.isWrapperFor(emptyDataSource.getClass());
    }

    @Test(expected = NotImplementedException.class)
    public void testGetLogWriter() {
        emptyDataSource.getLogWriter();
    }

    @Test(expected = NotImplementedException.class)
    public void testSetLogWriter() {
        emptyDataSource.setLogWriter(Mockito.mock(PrintWriter.class));
    }

    @Test(expected = NotImplementedException.class)
    public void testSetLoginTimeout() {
        emptyDataSource.setLoginTimeout(0);
    }

    @Test(expected = NotImplementedException.class)
    public void testGetLoginTimeout() {
        emptyDataSource.getLoginTimeout();
    }

    @Test(expected = NotImplementedException.class)
    public void testGetParentLogger() {
        emptyDataSource.getParentLogger();
    }

}
