package fr.xephi.authme.libs.org.postgresql.ds;

import fr.xephi.authme.libs.org.postgresql.ds.common.BaseDataSource;
import fr.xephi.authme.libs.org.postgresql.jdbc.ResourceLock;
import fr.xephi.authme.libs.org.postgresql.util.GT;
import fr.xephi.authme.libs.org.postgresql.util.PSQLException;
import fr.xephi.authme.libs.org.postgresql.util.PSQLState;
import fr.xephi.authme.libs.org.postgresql.util.internal.Nullness;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Stack;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import javax.naming.NamingException;
import javax.naming.Reference;
import javax.naming.StringRefAddr;
import javax.sql.ConnectionEvent;
import javax.sql.ConnectionEventListener;
import javax.sql.DataSource;
import javax.sql.PooledConnection;
import org.checkerframework.checker.nullness.qual.Nullable;

/** @deprecated */
@Deprecated
public class PGPoolingDataSource extends BaseDataSource implements DataSource {
   protected static ConcurrentMap<String, PGPoolingDataSource> dataSources = new ConcurrentHashMap();
   @Nullable
   protected String dataSourceName;
   private int initialConnections;
   private int maxConnections;
   private boolean initialized;
   private final Stack<PooledConnection> available = new Stack();
   private final Stack<PooledConnection> used = new Stack();
   private boolean isClosed;
   private final ResourceLock lock = new ResourceLock();
   private final Condition lockCondition;
   @Nullable
   private PGConnectionPoolDataSource source;
   private final ConnectionEventListener connectionEventListener;

   public PGPoolingDataSource() {
      this.lockCondition = this.lock.newCondition();
      this.connectionEventListener = new ConnectionEventListener() {
         public void connectionClosed(ConnectionEvent event) {
            ((PooledConnection)event.getSource()).removeConnectionEventListener(this);
            ResourceLock ignore = PGPoolingDataSource.this.lock.obtain();

            label44: {
               try {
                  if (PGPoolingDataSource.this.isClosed) {
                     break label44;
                  }

                  boolean removed = PGPoolingDataSource.this.used.remove(event.getSource());
                  if (removed) {
                     PGPoolingDataSource.this.available.push((PooledConnection)event.getSource());
                     PGPoolingDataSource.this.lockCondition.signal();
                  }
               } catch (Throwable var6) {
                  if (ignore != null) {
                     try {
                        ignore.close();
                     } catch (Throwable var5) {
                        var6.addSuppressed(var5);
                     }
                  }

                  throw var6;
               }

               if (ignore != null) {
                  ignore.close();
               }

               return;
            }

            if (ignore != null) {
               ignore.close();
            }

         }

         public void connectionErrorOccurred(ConnectionEvent event) {
            ((PooledConnection)event.getSource()).removeConnectionEventListener(this);
            ResourceLock ignore = PGPoolingDataSource.this.lock.obtain();

            label40: {
               try {
                  if (PGPoolingDataSource.this.isClosed) {
                     break label40;
                  }

                  PGPoolingDataSource.this.used.remove(event.getSource());
                  PGPoolingDataSource.this.lockCondition.signal();
               } catch (Throwable var6) {
                  if (ignore != null) {
                     try {
                        ignore.close();
                     } catch (Throwable var5) {
                        var6.addSuppressed(var5);
                     }
                  }

                  throw var6;
               }

               if (ignore != null) {
                  ignore.close();
               }

               return;
            }

            if (ignore != null) {
               ignore.close();
            }

         }
      };
   }

   @Nullable
   public static PGPoolingDataSource getDataSource(String name) {
      return (PGPoolingDataSource)dataSources.get(name);
   }

   public String getDescription() {
      return "Pooling DataSource '" + this.dataSourceName + " from " + "PostgreSQL JDBC Driver 42.7.3";
   }

   public void setServerName(String serverName) {
      if (this.initialized) {
         throw new IllegalStateException("Cannot set Data Source properties after DataSource has been used");
      } else {
         super.setServerName(serverName);
      }
   }

   public void setDatabaseName(@Nullable String databaseName) {
      if (this.initialized) {
         throw new IllegalStateException("Cannot set Data Source properties after DataSource has been used");
      } else {
         super.setDatabaseName(databaseName);
      }
   }

   public void setUser(@Nullable String user) {
      if (this.initialized) {
         throw new IllegalStateException("Cannot set Data Source properties after DataSource has been used");
      } else {
         super.setUser(user);
      }
   }

   public void setPassword(@Nullable String password) {
      if (this.initialized) {
         throw new IllegalStateException("Cannot set Data Source properties after DataSource has been used");
      } else {
         super.setPassword(password);
      }
   }

   public void setPortNumber(int portNumber) {
      if (this.initialized) {
         throw new IllegalStateException("Cannot set Data Source properties after DataSource has been used");
      } else {
         super.setPortNumber(portNumber);
      }
   }

   public int getInitialConnections() {
      return this.initialConnections;
   }

   public void setInitialConnections(int initialConnections) {
      if (this.initialized) {
         throw new IllegalStateException("Cannot set Data Source properties after DataSource has been used");
      } else {
         this.initialConnections = initialConnections;
      }
   }

   public int getMaxConnections() {
      return this.maxConnections;
   }

   public void setMaxConnections(int maxConnections) {
      if (this.initialized) {
         throw new IllegalStateException("Cannot set Data Source properties after DataSource has been used");
      } else {
         this.maxConnections = maxConnections;
      }
   }

   @Nullable
   public String getDataSourceName() {
      return this.dataSourceName;
   }

   public void setDataSourceName(String dataSourceName) {
      if (this.initialized) {
         throw new IllegalStateException("Cannot set Data Source properties after DataSource has been used");
      } else if (this.dataSourceName == null || dataSourceName == null || !dataSourceName.equals(this.dataSourceName)) {
         PGPoolingDataSource previous = (PGPoolingDataSource)dataSources.putIfAbsent(dataSourceName, this);
         if (previous != null) {
            throw new IllegalArgumentException("DataSource with name '" + dataSourceName + "' already exists!");
         } else {
            if (this.dataSourceName != null) {
               dataSources.remove(this.dataSourceName);
            }

            this.dataSourceName = dataSourceName;
         }
      }
   }

   public void initialize() throws SQLException {
      ResourceLock ignore = this.lock.obtain();

      try {
         PGConnectionPoolDataSource source = this.createConnectionPool();
         this.source = source;

         try {
            source.initializeFrom(this);
         } catch (Exception var5) {
            throw new PSQLException(GT.tr("Failed to setup DataSource."), PSQLState.UNEXPECTED_ERROR, var5);
         }

         while(true) {
            if (this.available.size() >= this.initialConnections) {
               this.initialized = true;
               break;
            }

            this.available.push(source.getPooledConnection());
         }
      } catch (Throwable var6) {
         if (ignore != null) {
            try {
               ignore.close();
            } catch (Throwable var4) {
               var6.addSuppressed(var4);
            }
         }

         throw var6;
      }

      if (ignore != null) {
         ignore.close();
      }

   }

   protected boolean isInitialized() {
      return this.initialized;
   }

   protected PGConnectionPoolDataSource createConnectionPool() {
      return new PGConnectionPoolDataSource();
   }

   public Connection getConnection(@Nullable String user, @Nullable String password) throws SQLException {
      if (user != null && (!user.equals(this.getUser()) || (password != null || this.getPassword() != null) && (password == null || !password.equals(this.getPassword())))) {
         if (!this.initialized) {
            this.initialize();
         }

         return super.getConnection(user, password);
      } else {
         return this.getConnection();
      }
   }

   public Connection getConnection() throws SQLException {
      if (!this.initialized) {
         this.initialize();
      }

      return this.getPooledConnection();
   }

   public void close() {
      ResourceLock ignore = this.lock.obtain();

      try {
         this.isClosed = true;

         PooledConnection pci;
         while(!this.available.isEmpty()) {
            pci = (PooledConnection)this.available.pop();

            try {
               pci.close();
            } catch (SQLException var6) {
            }
         }

         while(!this.used.isEmpty()) {
            pci = (PooledConnection)this.used.pop();
            pci.removeConnectionEventListener(this.connectionEventListener);

            try {
               pci.close();
            } catch (SQLException var5) {
            }
         }
      } catch (Throwable var7) {
         if (ignore != null) {
            try {
               ignore.close();
            } catch (Throwable var4) {
               var7.addSuppressed(var4);
            }
         }

         throw var7;
      }

      if (ignore != null) {
         ignore.close();
      }

      this.removeStoredDataSource();
   }

   protected void removeStoredDataSource() {
      dataSources.remove(Nullness.castNonNull(this.dataSourceName));
   }

   protected void addDataSource(String dataSourceName) {
      dataSources.put(dataSourceName, this);
   }

   private Connection getPooledConnection() throws SQLException {
      PooledConnection pc = null;
      ResourceLock ignore = this.lock.obtain();

      try {
         if (this.isClosed) {
            throw new PSQLException(GT.tr("DataSource has been closed."), PSQLState.CONNECTION_DOES_NOT_EXIST);
         }

         while(true) {
            if (!this.available.isEmpty()) {
               pc = (PooledConnection)this.available.pop();
               this.used.push(pc);
               break;
            }

            if (this.maxConnections == 0 || this.used.size() < this.maxConnections) {
               pc = ((PGConnectionPoolDataSource)Nullness.castNonNull(this.source)).getPooledConnection();
               this.used.push(pc);
               break;
            }

            try {
               this.lockCondition.await(1000L, TimeUnit.MILLISECONDS);
            } catch (InterruptedException var6) {
            }
         }
      } catch (Throwable var7) {
         if (ignore != null) {
            try {
               ignore.close();
            } catch (Throwable var5) {
               var7.addSuppressed(var5);
            }
         }

         throw var7;
      }

      if (ignore != null) {
         ignore.close();
      }

      pc.addConnectionEventListener(this.connectionEventListener);
      return pc.getConnection();
   }

   public Reference getReference() throws NamingException {
      Reference ref = super.getReference();
      ref.add(new StringRefAddr("dataSourceName", this.dataSourceName));
      if (this.initialConnections > 0) {
         ref.add(new StringRefAddr("initialConnections", Integer.toString(this.initialConnections)));
      }

      if (this.maxConnections > 0) {
         ref.add(new StringRefAddr("maxConnections", Integer.toString(this.maxConnections)));
      }

      return ref;
   }

   public boolean isWrapperFor(Class<?> iface) throws SQLException {
      return iface.isAssignableFrom(this.getClass());
   }

   public <T> T unwrap(Class<T> iface) throws SQLException {
      if (iface.isAssignableFrom(this.getClass())) {
         return iface.cast(this);
      } else {
         throw new SQLException("Cannot unwrap to " + iface.getName());
      }
   }
}
