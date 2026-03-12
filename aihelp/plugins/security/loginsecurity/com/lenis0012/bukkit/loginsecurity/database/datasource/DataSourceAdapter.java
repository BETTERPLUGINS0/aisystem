package com.lenis0012.bukkit.loginsecurity.database.datasource;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;
import javax.sql.DataSource;

public abstract class DataSourceAdapter implements DataSource {
   public Connection getConnection() throws SQLException {
      throw new SQLFeatureNotSupportedException();
   }

   public Connection getConnection(String username, String password) throws SQLException {
      throw new SQLFeatureNotSupportedException();
   }

   public PrintWriter getLogWriter() throws SQLException {
      throw new SQLFeatureNotSupportedException();
   }

   public void setLogWriter(PrintWriter out) throws SQLException {
      throw new SQLFeatureNotSupportedException();
   }

   public void setLoginTimeout(int seconds) throws SQLException {
      throw new SQLFeatureNotSupportedException();
   }

   public int getLoginTimeout() throws SQLException {
      throw new SQLFeatureNotSupportedException();
   }

   public Logger getParentLogger() throws SQLFeatureNotSupportedException {
      throw new SQLFeatureNotSupportedException();
   }

   public <T> T unwrap(Class<T> iface) throws SQLException {
      throw new SQLFeatureNotSupportedException();
   }

   public boolean isWrapperFor(Class<?> iface) throws SQLException {
      throw new SQLFeatureNotSupportedException();
   }
}
