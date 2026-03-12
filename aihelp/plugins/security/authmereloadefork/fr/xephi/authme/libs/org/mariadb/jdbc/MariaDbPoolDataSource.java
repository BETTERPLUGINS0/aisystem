package fr.xephi.authme.libs.org.mariadb.jdbc;

import fr.xephi.authme.libs.org.mariadb.jdbc.pool.Pool;
import fr.xephi.authme.libs.org.mariadb.jdbc.pool.Pools;
import java.io.Closeable;
import java.io.PrintWriter;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;
import javax.sql.ConnectionPoolDataSource;
import javax.sql.DataSource;
import javax.sql.PooledConnection;
import javax.sql.XAConnection;
import javax.sql.XADataSource;

public class MariaDbPoolDataSource implements DataSource, ConnectionPoolDataSource, XADataSource, Closeable, AutoCloseable {
   private Pool pool;
   private Configuration conf = null;
   private String url = null;
   private String user = null;
   private String password = null;
   private Integer loginTimeout = null;

   public MariaDbPoolDataSource() {
   }

   public MariaDbPoolDataSource(String url) throws SQLException {
      if (Configuration.acceptsUrl(url)) {
         this.url = url;
         this.conf = Configuration.parse(url);
         this.pool = Pools.retrievePool(this.conf);
      } else {
         throw new SQLException(String.format("Wrong mariaDB url: %s", url));
      }
   }

   private void config() throws SQLException {
      if (this.url == null) {
         throw new SQLException("url not set");
      } else {
         this.conf = Configuration.parse(this.url);
         if (this.loginTimeout != null) {
            this.conf.connectTimeout(this.loginTimeout * 1000);
         }

         if (this.user != null || this.password != null) {
            this.conf = this.conf.clone(this.user, this.password);
         }

         if (this.user != null) {
            this.user = this.conf.user();
         }

         if (this.password != null) {
            this.password = this.conf.password();
         }

         this.pool = Pools.retrievePool(this.conf);
      }
   }

   public java.sql.Connection getConnection() throws SQLException {
      if (this.conf == null) {
         this.config();
      }

      return this.pool.getPoolConnection().getConnection();
   }

   public java.sql.Connection getConnection(String username, String password) throws SQLException {
      if (this.conf == null) {
         this.config();
      }

      return this.pool.getPoolConnection(username, password).getConnection();
   }

   public <T> T unwrap(Class<T> iface) throws SQLException {
      if (this.isWrapperFor(iface)) {
         return iface.cast(this);
      } else {
         throw new SQLException("Datasource is not a wrapper for " + iface.getName());
      }
   }

   public boolean isWrapperFor(Class<?> iface) {
      return iface.isInstance(this);
   }

   public PrintWriter getLogWriter() {
      return null;
   }

   public void setLogWriter(PrintWriter out) {
   }

   public int getLoginTimeout() {
      if (this.loginTimeout != null) {
         return this.loginTimeout;
      } else if (this.conf != null) {
         return this.conf.connectTimeout() / 1000;
      } else {
         return DriverManager.getLoginTimeout() > 0 ? DriverManager.getLoginTimeout() : 30;
      }
   }

   public void setLoginTimeout(int seconds) throws SQLException {
      this.loginTimeout = seconds;
      if (this.conf != null) {
         this.config();
      }

   }

   public Logger getParentLogger() {
      return null;
   }

   public PooledConnection getPooledConnection() throws SQLException {
      if (this.conf == null) {
         this.config();
      }

      return this.pool.getPoolConnection();
   }

   public PooledConnection getPooledConnection(String username, String password) throws SQLException {
      if (this.conf == null) {
         this.config();
      }

      return this.pool.getPoolConnection(username, password);
   }

   public XAConnection getXAConnection() throws SQLException {
      if (this.conf == null) {
         this.config();
      }

      return this.pool.getPoolConnection();
   }

   public XAConnection getXAConnection(String username, String password) throws SQLException {
      if (this.conf == null) {
         this.config();
      }

      return this.pool.getPoolConnection(username, password);
   }

   public String getUrl() {
      return this.conf == null ? this.url : this.conf.initialUrl();
   }

   public void setUrl(String url) throws SQLException {
      if (Configuration.acceptsUrl(url)) {
         this.url = url;
         this.config();
      } else {
         throw new SQLException(String.format("Wrong mariaDB url: %s", url));
      }
   }

   public String getUser() {
      return this.user;
   }

   public void setUser(String user) throws SQLException {
      this.user = user;
      if (this.conf != null) {
         this.config();
      }

   }

   public void setPassword(String password) throws SQLException {
      this.password = password;
      if (this.conf != null) {
         this.config();
      }

   }

   public void close() {
      this.pool.close();
   }

   public String getPoolName() {
      return this.pool != null ? this.pool.getPoolTag() : null;
   }

   public List<Long> testGetConnectionIdleThreadIds() {
      return this.pool != null ? this.pool.testGetConnectionIdleThreadIds() : null;
   }
}
