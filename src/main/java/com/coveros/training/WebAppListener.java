package com.coveros.training;

import com.coveros.training.persistence.PersistenceLayer;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class WebAppListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        final PersistenceLayer persistenceLayer = new PersistenceLayer();
        persistenceLayer.cleanAndMigrateDatabase();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // do nothing.
    }

}