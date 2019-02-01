package com.coveros.training;

import org.flywaydb.core.Flyway;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class WebAppListener implements ServletContextListener {

  @Override
  public void contextInitialized ( ServletContextEvent sce ) {
    Flyway flyway = Flyway.configure().dataSource("jdbc:h2:~/training", "sa", "sa").load();
    flyway.migrate();
  }

  @Override
  public void contextDestroyed(ServletContextEvent sce) {
    // do nothing
  }

}