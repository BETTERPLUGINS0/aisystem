package github.nighter.smartspawner.libs.mariadb;

import java.io.PrintWriter;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;
import javax.sql.ConnectionPoolDataSource;
import javax.sql.DataSource;
import javax.sql.PooledConnection;
import javax.sql.XAConnection;
import javax.sql.XADataSource;

public class MariaDbDataSource implements DataSource, ConnectionPoolDataSource, XADataSource {
   private Configuration conf = null;
   private String url = null;
   private String user = null;
   private String password = null;
   private Integer loginTimeout = null;

   public MariaDbDataSource() {
   }

   public MariaDbDataSource(String url) throws SQLException {
      if (Configuration.acceptsUrl(url)) {
         this.url = url;
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

      }
   }

   public java.sql.Connection getConnection() throws SQLException {
      if (this.conf == null) {
         this.config();
      }

      return Driver.connect(this.conf);
   }

   public java.sql.Connection getConnection(String username, String password) throws SQLException {
      if (this.conf == null) {
         this.config();
      }

      Configuration conf = this.conf.clone(username, password);
      return Driver.connect(conf);
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

      Connection conn = Driver.connect(this.conf);
      MariaDbPoolConnection poolConnection = this.conf.pinGlobalTxToPhysicalConnection() ? new MariaDbPoolPinnedConnection(conn) : new MariaDbPoolConnection(conn);
      return (PooledConnection)poolConnection;
   }

   public PooledConnection getPooledConnection(String username, String password) throws SQLException {
      if (this.conf == null) {
         this.config();
      }

      Configuration conf = this.conf.clone(username, password);
      Connection conn = Driver.connect(conf);
      MariaDbPoolConnection poolConnection = conf.pinGlobalTxToPhysicalConnection() ? new MariaDbPoolPinnedConnection(conn) : new MariaDbPoolConnection(conn);
      return (PooledConnection)poolConnection;
   }

   public XAConnection getXAConnection() throws SQLException {
      return (MariaDbPoolConnection)this.getPooledConnection();
   }

   public XAConnection getXAConnection(String username, String password) throws SQLException {
      return (MariaDbPoolConnection)this.getPooledConnection(username, password);
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
}
