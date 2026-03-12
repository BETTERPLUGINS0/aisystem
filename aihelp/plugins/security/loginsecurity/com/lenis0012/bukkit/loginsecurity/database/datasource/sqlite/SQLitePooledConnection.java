package com.lenis0012.bukkit.loginsecurity.database.datasource.sqlite;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.sql.ConnectionEvent;
import javax.sql.ConnectionEventListener;
import javax.sql.PooledConnection;
import javax.sql.StatementEventListener;

public class SQLitePooledConnection implements PooledConnection {
   protected Connection physicalConn;
   protected volatile Connection handleConn;
   protected List<ConnectionEventListener> listeners = new ArrayList();

   protected SQLitePooledConnection(Connection physicalConn) {
      this.physicalConn = physicalConn;
   }

   public void close() throws SQLException {
      if (this.handleConn != null) {
         this.listeners.clear();
         this.handleConn.close();
      }

      if (this.physicalConn != null) {
         try {
            this.physicalConn.close();
         } finally {
            this.physicalConn = null;
         }
      }

   }

   public Connection getConnection() throws SQLException {
      if (this.handleConn != null && !this.handleConn.isClosed()) {
         this.handleConn.close();
      }

      this.handleConn = (Connection)Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[]{Connection.class}, new InvocationHandler() {
         boolean isClosed;

         public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            ConnectionEvent event;
            int i;
            try {
               String name = method.getName();
               if (!"close".equals(name)) {
                  if ("isClosed".equals(name)) {
                     if (!this.isClosed) {
                        this.isClosed = (Boolean)method.invoke(SQLitePooledConnection.this.physicalConn, args);
                     }

                     return this.isClosed;
                  } else if (this.isClosed) {
                     throw new SQLException("Connection is closed");
                  } else {
                     return method.invoke(SQLitePooledConnection.this.physicalConn, args);
                  }
               } else {
                  event = new ConnectionEvent(SQLitePooledConnection.this);

                  for(i = SQLitePooledConnection.this.listeners.size() - 1; i >= 0; --i) {
                     ((ConnectionEventListener)SQLitePooledConnection.this.listeners.get(i)).connectionClosed(event);
                  }

                  if (!SQLitePooledConnection.this.physicalConn.getAutoCommit()) {
                     SQLitePooledConnection.this.physicalConn.rollback();
                  }

                  SQLitePooledConnection.this.physicalConn.setAutoCommit(true);
                  this.isClosed = true;
                  return null;
               }
            } catch (SQLException var7) {
               if ("database connection closed".equals(var7.getMessage())) {
                  event = new ConnectionEvent(SQLitePooledConnection.this, var7);

                  for(i = SQLitePooledConnection.this.listeners.size() - 1; i >= 0; --i) {
                     ((ConnectionEventListener)SQLitePooledConnection.this.listeners.get(i)).connectionErrorOccurred(event);
                  }
               }

               throw var7;
            } catch (InvocationTargetException var8) {
               throw var8.getCause();
            }
         }
      });
      return this.handleConn;
   }

   public void addConnectionEventListener(ConnectionEventListener listener) {
      this.listeners.add(listener);
   }

   public void removeConnectionEventListener(ConnectionEventListener listener) {
      this.listeners.remove(listener);
   }

   public void addStatementEventListener(StatementEventListener listener) {
      throw new UnsupportedOperationException();
   }

   public void removeStatementEventListener(StatementEventListener listener) {
      throw new UnsupportedOperationException();
   }
}
