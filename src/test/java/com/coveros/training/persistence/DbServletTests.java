package com.coveros.training.persistence;

import org.junit.Test;
import org.mockito.Mockito;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.mockito.Mockito.*;

public class DbServletTests {

    private final IPersistenceLayer pl = Mockito.mock(IPersistenceLayer.class);
    private final DbServlet dbServlet = Mockito.spy(new DbServlet(pl));
    private final HttpServletRequest request = Mockito.mock(HttpServletRequest.class, RETURNS_DEEP_STUBS);
    private final HttpServletResponse response = Mockito.mock(HttpServletResponse.class);

    @Test
    public void testClean() {
        when(request.getParameter("action")).thenReturn("clean");

        dbServlet.doGet(request, response);

        verify(pl).cleanDatabase();
    }

    @Test
    public void testMigrate() {
        when(request.getParameter("action")).thenReturn("migrate");

        dbServlet.doGet(request, response);

        verify(pl).migrateDatabase();
    }

    @Test
    public void testCleanAndMigrate() {
        // the default action is to clean and migrate, so if I send nothing, I'll get that result.
        when(request.getParameter("action")).thenReturn("");

        dbServlet.doGet(request, response);

        verify(pl).cleanAndMigrateDatabase();
    }
}
