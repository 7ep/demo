package com.coveros.training.tomcat;

import com.coveros.training.persistence.IPersistenceLayer;
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

    private final IPersistenceLayer pl;

    public WebAppListener() {
        pl = new PersistenceLayer();
    }

    public WebAppListener(IPersistenceLayer pl) {
        this.pl = pl;
    }

    /**
     * Cleans and migrates the database using Flyway.
     * See database migration files like V2__Rest_of_tables_for_auth_and_library.sql
     */
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        //  clean the database and configure the schema
        pl.cleanAndMigrateDatabase();
    }

    /**
     * This does nothing, but it's required to implement per the interface.
     */
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // do nothing.
    }

}