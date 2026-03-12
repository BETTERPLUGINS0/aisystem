package com.lenis0012.bukkit.loginsecurity.database.datasource.sqlite;

import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;
import javax.sql.ConnectionPoolDataSource;
import javax.sql.PooledConnection;
import org.sqlite.SQLiteConfig;
import org.sqlite.SQLiteDataSource;

public class SQLiteConnectionPoolDataSource extends SQLiteDataSource implements ConnectionPoolDataSource {
   public SQLiteConnectionPoolDataSource() {
   }

   public SQLiteConnectionPoolDataSource(SQLiteConfig config) {
      super(config);
   }

   public PooledConnection getPooledConnection() throws SQLException {
      return this.getPooledConnection((String)null, (String)null);
   }

   public PooledConnection getPooledConnection(String user, String password) throws SQLException {
      return new SQLitePooledConnection(this.getConnection(user, password));
   }

   public Logger getParentLogger() throws SQLFeatureNotSupportedException {
      throw new SQLFeatureNotSupportedException();
   }
}
