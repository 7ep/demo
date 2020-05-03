package com.coveros.training.tomcat;

import com.coveros.training.persistence.IPersistenceLayer;
import org.junit.Test;
import org.mockito.Mockito;

import javax.servlet.ServletContextEvent;

public class WebAppListenerTests {

    private final IPersistenceLayer pl = Mockito.mock(IPersistenceLayer.class);
    private final WebAppListener webAppListener = Mockito.spy(new WebAppListener(pl));
    private final ServletContextEvent servletContextEvent = Mockito.mock(ServletContextEvent.class);

    /**
     * Directly calling the code that Tomcat will run when the
     * application starts.  It should clean and migrate the database.
     */
    @Test
    public void testContextInitialized() {
        webAppListener.contextInitialized(servletContextEvent);

        Mockito.verify(pl).cleanAndMigrateDatabase();
    }

    @Test
    public void testContextDestroyed() {
        webAppListener.contextDestroyed(servletContextEvent);
        Mockito.verifyNoInteractions(pl);
    }
}
