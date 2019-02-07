package com.coveros.training;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.coveros.training.persistence.PersistenceLayer.cleanAndMigrateDatabase;

@WebListener
public class WebAppListener implements ServletContextListener {

  @Override
  public void contextInitialized ( ServletContextEvent sce ) {
    cleanAndMigrateDatabase();
  }

  @Override
  public void contextDestroyed(ServletContextEvent sce) {
    // do nothing.
  }

}