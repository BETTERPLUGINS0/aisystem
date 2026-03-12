package fr.xephi.authme.libs.org.postgresql.ds;

import fr.xephi.authme.libs.org.postgresql.PGConnection;
import fr.xephi.authme.libs.org.postgresql.PGStatement;
import fr.xephi.authme.libs.org.postgresql.util.GT;
import fr.xephi.authme.libs.org.postgresql.util.PSQLException;
import fr.xephi.authme.libs.org.postgresql.util.PSQLState;
import fr.xephi.authme.libs.org.postgresql.util.internal.Nullness;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import javax.sql.ConnectionEvent;
import javax.sql.ConnectionEventListener;
import javax.sql.PooledConnection;
import javax.sql.StatementEventListener;
import org.checkerframework.checker.nullness.qual.Nullable;

public class PGPooledConnection implements PooledConnection {
   private final List<ConnectionEventListener> listeners;
   @Nullable
   private Connection con;
   private PGPooledConnection.ConnectionHandler last;
   private final boolean autoCommit;
   private final boolean isXA;
   private static final String[] fatalClasses = new String[]{"08", "53", "57P01", "57P02", "57P03", "58", "60", "99", "F0", "XX"};

   public PGPooledConnection(Connection con, boolean autoCommit, boolean isXA) {
      this.listeners = new LinkedList();
      this.con = con;
      this.autoCommit = autoCommit;
      this.isXA = isXA;
   }

   public PGPooledConnection(Connection con, boolean autoCommit) {
      this(con, autoCommit, false);
   }

   public void addConnectionEventListener(ConnectionEventListener connectionEventListener) {
      this.listeners.add(connectionEventListener);
   }

   public void removeConnectionEventListener(ConnectionEventListener connectionEventListener) {
      this.listeners.remove(connectionEventListener);
   }

   public void close() throws SQLException {
      Connection con = this.con;
      PGPooledConnection.ConnectionHandler last = this.last;
      if (last != null) {
         last.close();
         if (con != null && !con.isClosed() && !con.getAutoCommit()) {
            try {
               con.rollback();
            } catch (SQLException var8) {
            }
         }
      }

      if (con != null) {
         try {
            con.close();
         } finally {
            this.con = null;
         }

      }
   }

   public Connection getConnection() throws SQLException {
      Connection con = this.con;
      if (con == null) {
         PSQLException sqlException = new PSQLException(GT.tr("This PooledConnection has already been closed."), PSQLState.CONNECTION_DOES_NOT_EXIST);
         this.fireConnectionFatalError(sqlException);
         throw sqlException;
      } else {
         PGPooledConnection.ConnectionHandler handler;
         try {
            handler = this.last;
            if (handler != null) {
               handler.close();
               if (con != null) {
                  if (!con.getAutoCommit()) {
                     try {
                        con.rollback();
                     } catch (SQLException var4) {
                     }
                  }

                  con.clearWarnings();
               }
            }

            if (!this.isXA && con != null) {
               con.setAutoCommit(this.autoCommit);
            }
         } catch (SQLException var5) {
            this.fireConnectionFatalError(var5);
            throw (SQLException)var5.fillInStackTrace();
         }

         handler = new PGPooledConnection.ConnectionHandler((Connection)Nullness.castNonNull(con));
         this.last = handler;
         Connection proxyCon = (Connection)Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[]{Connection.class, PGConnection.class}, handler);
         handler.setProxy(proxyCon);
         return proxyCon;
      }
   }

   void fireConnectionClosed() {
      ConnectionEvent evt = null;
      ConnectionEventListener[] local = (ConnectionEventListener[])this.listeners.toArray(new ConnectionEventListener[0]);
      ConnectionEventListener[] var3 = local;
      int var4 = local.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         ConnectionEventListener listener = var3[var5];
         if (evt == null) {
            evt = this.createConnectionEvent((SQLException)null);
         }

         listener.connectionClosed(evt);
      }

   }

   void fireConnectionFatalError(SQLException e) {
      ConnectionEvent evt = null;
      ConnectionEventListener[] local = (ConnectionEventListener[])this.listeners.toArray(new ConnectionEventListener[0]);
      ConnectionEventListener[] var4 = local;
      int var5 = local.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         ConnectionEventListener listener = var4[var6];
         if (evt == null) {
            evt = this.createConnectionEvent(e);
         }

         listener.connectionErrorOccurred(evt);
      }

   }

   protected ConnectionEvent createConnectionEvent(@Nullable SQLException e) {
      return e == null ? new ConnectionEvent(this) : new ConnectionEvent(this, e);
   }

   private static boolean isFatalState(@Nullable String state) {
      if (state == null) {
         return true;
      } else if (state.length() < 2) {
         return true;
      } else {
         String[] var1 = fatalClasses;
         int var2 = var1.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            String fatalClass = var1[var3];
            if (state.startsWith(fatalClass)) {
               return true;
            }
         }

         return false;
      }
   }

   private void fireConnectionError(SQLException e) {
      if (isFatalState(e.getSQLState())) {
         this.fireConnectionFatalError(e);
      }
   }

   public void removeStatementEventListener(StatementEventListener listener) {
   }

   public void addStatementEventListener(StatementEventListener listener) {
   }

   private class ConnectionHandler implements InvocationHandler {
      @Nullable
      private Connection con;
      @Nullable
      private Connection proxy;
      private boolean automatic;

      ConnectionHandler(Connection con) {
         this.con = con;
      }

      @Nullable
      public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
         String methodName = method.getName();
         if (method.getDeclaringClass() == Object.class) {
            if ("toString".equals(methodName)) {
               return "Pooled connection wrapping physical connection " + this.con;
            } else if ("equals".equals(methodName)) {
               return proxy == args[0];
            } else if ("hashCode".equals(methodName)) {
               return System.identityHashCode(proxy);
            } else {
               try {
                  return method.invoke(this.con, args);
               } catch (InvocationTargetException var8) {
                  throw var8.getTargetException();
               }
            }
         } else {
            Connection con = this.con;
            if (!"isClosed".equals(methodName)) {
               if ("close".equals(methodName)) {
                  if (con == null) {
                     return null;
                  } else {
                     SQLException ex = null;
                     if (!con.isClosed()) {
                        if (!PGPooledConnection.this.isXA && !con.getAutoCommit()) {
                           try {
                              con.rollback();
                           } catch (SQLException var9) {
                              ex = var9;
                           }
                        }

                        con.clearWarnings();
                     }

                     this.con = null;
                     this.proxy = null;
                     PGPooledConnection.this.last = null;
                     PGPooledConnection.this.fireConnectionClosed();
                     if (ex != null) {
                        throw ex;
                     } else {
                        return null;
                     }
                  }
               } else if (con != null && !con.isClosed()) {
                  try {
                     Statement st;
                     if ("createStatement".equals(methodName)) {
                        st = (Statement)Nullness.castNonNull((Statement)method.invoke(con, args));
                        return Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[]{Statement.class, PGStatement.class}, PGPooledConnection.this.new StatementHandler(this, st));
                     } else if ("prepareCall".equals(methodName)) {
                        st = (Statement)Nullness.castNonNull((Statement)method.invoke(con, args));
                        return Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[]{CallableStatement.class, PGStatement.class}, PGPooledConnection.this.new StatementHandler(this, st));
                     } else if ("prepareStatement".equals(methodName)) {
                        st = (Statement)Nullness.castNonNull((Statement)method.invoke(con, args));
                        return Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[]{PreparedStatement.class, PGStatement.class}, PGPooledConnection.this.new StatementHandler(this, st));
                     } else {
                        return method.invoke(con, args);
                     }
                  } catch (InvocationTargetException var10) {
                     Throwable te = var10.getTargetException();
                     if (te instanceof SQLException) {
                        PGPooledConnection.this.fireConnectionError((SQLException)te);
                     }

                     throw te;
                  }
               } else {
                  throw new PSQLException(this.automatic ? GT.tr("Connection has been closed automatically because a new connection was opened for the same PooledConnection or the PooledConnection has been closed.") : GT.tr("Connection has been closed."), PSQLState.CONNECTION_DOES_NOT_EXIST);
               }
            } else {
               return con == null || con.isClosed();
            }
         }
      }

      Connection getProxy() {
         return (Connection)Nullness.castNonNull(this.proxy);
      }

      void setProxy(Connection proxy) {
         this.proxy = proxy;
      }

      public void close() {
         if (this.con != null) {
            this.automatic = true;
         }

         this.con = null;
         this.proxy = null;
      }

      public boolean isClosed() {
         return this.con == null;
      }
   }

   private class StatementHandler implements InvocationHandler {
      private PGPooledConnection.ConnectionHandler con;
      @Nullable
      private Statement st;

      StatementHandler(PGPooledConnection.ConnectionHandler con, Statement st) {
         this.con = con;
         this.st = st;
      }

      @Nullable
      public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
         String methodName = method.getName();
         if (method.getDeclaringClass() == Object.class) {
            if ("toString".equals(methodName)) {
               return "Pooled statement wrapping physical statement " + this.st;
            } else if ("hashCode".equals(methodName)) {
               return System.identityHashCode(proxy);
            } else {
               return "equals".equals(methodName) ? proxy == args[0] : method.invoke(this.st, args);
            }
         } else {
            Statement st = this.st;
            if (!"isClosed".equals(methodName)) {
               if ("close".equals(methodName)) {
                  if (st != null && !st.isClosed()) {
                     this.con = null;
                     this.st = null;
                     st.close();
                     return null;
                  } else {
                     return null;
                  }
               } else if (st != null && !st.isClosed()) {
                  if ("getConnection".equals(methodName)) {
                     return ((PGPooledConnection.ConnectionHandler)Nullness.castNonNull(this.con)).getProxy();
                  } else {
                     try {
                        return method.invoke(st, args);
                     } catch (InvocationTargetException var8) {
                        Throwable te = var8.getTargetException();
                        if (te instanceof SQLException) {
                           PGPooledConnection.this.fireConnectionError((SQLException)te);
                        }

                        throw te;
                     }
                  }
               } else {
                  throw new PSQLException(GT.tr("Statement has been closed."), PSQLState.OBJECT_NOT_IN_STATE);
               }
            } else {
               return st == null || st.isClosed();
            }
         }
      }
   }
}
