package com.coveros.training.tomcat;

import com.coveros.training.persistence.PersistenceLayer;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * The purpose of this class is to run certain commands at the
 * start-up of the application.
 */
@WebListener
public class WebAppListener implements ServletContextListener {

    private final PersistenceLayer pl;

    public WebAppListener() {
        pl = new PersistenceLayer();
    }

    public WebAppListener(PersistenceLayer pl) {
        this.pl = pl;
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        //  clean the database and configure the schema
        pl.cleanAndMigrateDatabase();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // do nothing.
    }

}