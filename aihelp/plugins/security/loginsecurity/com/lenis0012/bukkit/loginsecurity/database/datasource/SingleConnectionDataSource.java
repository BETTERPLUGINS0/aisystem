package com.lenis0012.bukkit.loginsecurity.database.datasource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import javax.sql.ConnectionEvent;
import javax.sql.ConnectionEventListener;
import javax.sql.ConnectionPoolDataSource;
import javax.sql.PooledConnection;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

public class SingleConnectionDataSource extends DataSourceAdapter implements ConnectionEventListener, Runnable {
   private static final int VALID_CHECK_BYPASS = 500;
   private static final int DEFAULT_TIMEOUT = 5000;
   private static final long DEFAULT_MAX_LIFETIME;
   private final Plugin plugin;
   private final ConnectionPoolDataSource dataSource;
   private final ReentrantLock lock;
   private final int timeout;
   private final long maxLifetime;
   private volatile PooledConnection pooledConnection;
   private volatile long lastUsedTime;
   private volatile boolean obtainingConnection;
   private BukkitTask recreateTask;
   private boolean closing;

   public SingleConnectionDataSource(Plugin plugin, ConnectionPoolDataSource dataSource) {
      this(plugin, dataSource, 5000, DEFAULT_MAX_LIFETIME);
   }

   public SingleConnectionDataSource(Plugin plugin, ConnectionPoolDataSource dataSource, int timeout, long maxLifetime) {
      this.closing = false;
      this.plugin = plugin;
      this.dataSource = dataSource;
      this.lock = new ReentrantLock(true);
      this.maxLifetime = maxLifetime;
      this.timeout = timeout;
   }

   public Connection getConnection() throws SQLException {
      if (this.closing) {
         throw new SQLException("Database is shutting down");
      } else {
         this.lock.lock();
         this.obtainingConnection = true;

         try {
            Connection connection;
            if (this.pooledConnection != null) {
               try {
                  connection = this.pooledConnection.getConnection();
                  if (!connection.isClosed()) {
                     if (this.lastUsedTime - System.currentTimeMillis() <= 500L || connection.isValid(this.timeout)) {
                        this.obtainingConnection = false;
                        return connection;
                     }

                     this.tryClose(this.pooledConnection);
                  }
               } catch (SQLException var2) {
                  this.tryClose(this.pooledConnection);
               }
            }

            this.createConnection();
            connection = this.pooledConnection.getConnection();
            this.obtainingConnection = false;
            return connection;
         } catch (Throwable var3) {
            this.lock.unlock();
            throw var3;
         }
      }
   }

   public void createConnection() throws SQLException {
      if (this.recreateTask != null) {
         this.recreateTask.cancel();
      }

      if (this.pooledConnection != null) {
         this.tryClose(this.pooledConnection);
      }

      this.pooledConnection = this.dataSource.getPooledConnection();
      this.pooledConnection.addConnectionEventListener(this);
      this.recreateTask = Bukkit.getScheduler().runTaskLaterAsynchronously(this.plugin, this, this.maxLifetime / 50L);
   }

   public void run() {
      this.recreateTask = null;
      this.lock.lock();

      try {
         this.tryClose(this.pooledConnection);
         this.createConnection();
      } catch (SQLException var5) {
         this.plugin.getLogger().log(Level.SEVERE, "Failed to recreate database connection", var5);
      } finally {
         this.lock.unlock();
      }

   }

   private void tryClose(PooledConnection connection) {
      this.pooledConnection = null;
      if (connection != null) {
         try {
            connection.close();
         } catch (SQLException var3) {
         }

      }
   }

   public void shutdown() throws SQLException {
      this.closing = true;
      if (this.recreateTask != null) {
         this.recreateTask.cancel();
      }

      this.lock.lock();
      if (this.pooledConnection != null) {
         this.pooledConnection.close();
         this.pooledConnection = null;
      }

   }

   public void connectionClosed(ConnectionEvent event) {
      this.lastUsedTime = System.currentTimeMillis();
      if (!this.obtainingConnection) {
         this.lock.unlock();
      }

   }

   public void connectionErrorOccurred(ConnectionEvent event) {
      PooledConnection brokenConnection = event.getSource() instanceof PooledConnection ? (PooledConnection)event.getSource() : this.pooledConnection;
      this.pooledConnection = null;
      if (!this.obtainingConnection) {
         this.lock.unlock();
      }

      this.tryClose(brokenConnection);
   }

   static {
      DEFAULT_MAX_LIFETIME = TimeUnit.MINUTES.toMillis(30L);
   }
}
