package com.coveros.training;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import static com.coveros.training.Constants.DATABASE_URL;

public class DataUtils {

  static Connection createConnection() {
    Properties props = new Properties();
    props.setProperty("user","postgres");
    props.setProperty("password","postgres");
    Connection conn;
    try {
      conn = DriverManager.getConnection(DATABASE_URL, props);
    } catch (SQLException ex) {
      throw new RuntimeException(ex);
    }
    return conn;
  }

}
