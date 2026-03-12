package fr.xephi.authme.libs.org.mariadb.jdbc;

import fr.xephi.authme.libs.org.mariadb.jdbc.util.StringUtils;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.sql.ConnectionEvent;
import javax.sql.ConnectionEventListener;
import javax.sql.PooledConnection;
import javax.sql.StatementEvent;
import javax.sql.StatementEventListener;
import javax.sql.XAConnection;
import javax.transaction.xa.XAException;
import javax.transaction.xa.XAResource;
import javax.transaction.xa.Xid;

public class MariaDbPoolConnection implements PooledConnection, XAConnection {
   private final Connection connection;
   private final List<ConnectionEventListener> connectionEventListeners;
   private final List<StatementEventListener> statementEventListeners;

   public MariaDbPoolConnection(Connection connection) {
      this.connection = connection;
      this.connection.setPoolConnection(this);
      this.statementEventListeners = new CopyOnWriteArrayList();
      this.connectionEventListeners = new CopyOnWriteArrayList();
   }

   public static String xidToString(Xid xid) {
      return "0x" + StringUtils.byteArrayToHexString(xid.getGlobalTransactionId()) + ",0x" + StringUtils.byteArrayToHexString(xid.getBranchQualifier()) + ",0x" + Integer.toHexString(xid.getFormatId());
   }

   public Connection getConnection() {
      return this.connection;
   }

   public void addConnectionEventListener(ConnectionEventListener listener) {
      this.connectionEventListeners.add(listener);
   }

   public void removeConnectionEventListener(ConnectionEventListener listener) {
      this.connectionEventListeners.remove(listener);
   }

   public void addStatementEventListener(StatementEventListener listener) {
      this.statementEventListeners.add(listener);
   }

   public void removeStatementEventListener(StatementEventListener listener) {
      this.statementEventListeners.remove(listener);
   }

   public void fireStatementClosed(PreparedStatement statement) {
      StatementEvent event = new StatementEvent(this, statement);
      Iterator var3 = this.statementEventListeners.iterator();

      while(var3.hasNext()) {
         StatementEventListener listener = (StatementEventListener)var3.next();
         listener.statementClosed(event);
      }

   }

   public void fireStatementErrorOccurred(PreparedStatement statement, SQLException returnEx) {
      StatementEvent event = new StatementEvent(this, statement, returnEx);
      Iterator var4 = this.statementEventListeners.iterator();

      while(var4.hasNext()) {
         StatementEventListener listener = (StatementEventListener)var4.next();
         listener.statementErrorOccurred(event);
      }

   }

   public void fireConnectionClosed(ConnectionEvent event) {
      Iterator var2 = this.connectionEventListeners.iterator();

      while(var2.hasNext()) {
         ConnectionEventListener listener = (ConnectionEventListener)var2.next();
         listener.connectionClosed(event);
      }

   }

   public void fireConnectionErrorOccurred(SQLException returnEx) {
      ConnectionEvent event = new ConnectionEvent(this, returnEx);
      Iterator var3 = this.connectionEventListeners.iterator();

      while(var3.hasNext()) {
         ConnectionEventListener listener = (ConnectionEventListener)var3.next();
         listener.connectionErrorOccurred(event);
      }

   }

   public void close() throws SQLException {
      this.fireConnectionClosed(new ConnectionEvent(this));
      this.connection.setPoolConnection((MariaDbPoolConnection)null);
      this.connection.close();
   }

   public XAResource getXAResource() {
      return new MariaDbPoolConnection.MariaDbXAResource();
   }

   private class MariaDbXAResource implements XAResource {
      private MariaDbXAResource() {
      }

      private String flagsToString(int flags) {
         switch(flags) {
         case 2097152:
            return "JOIN";
         case 33554432:
            return "SUSPEND";
         case 134217728:
            return "RESUME";
         case 1073741824:
            return "ONE PHASE";
         default:
            return "";
         }
      }

      private XAException mapXaException(SQLException sqle) {
         byte xaErrorCode;
         switch(sqle.getErrorCode()) {
         case 1397:
            xaErrorCode = -4;
            break;
         case 1398:
            xaErrorCode = -5;
            break;
         case 1399:
            xaErrorCode = -7;
            break;
         case 1400:
            xaErrorCode = -9;
            break;
         case 1401:
            xaErrorCode = -3;
            break;
         case 1402:
            xaErrorCode = 100;
            break;
         default:
            xaErrorCode = 0;
         }

         XAException xaException;
         if (xaErrorCode != 0) {
            xaException = new XAException(xaErrorCode);
         } else {
            xaException = new XAException(sqle.getMessage());
         }

         xaException.initCause(sqle);
         return xaException;
      }

      private void execute(String command) throws XAException {
         try {
            MariaDbPoolConnection.this.connection.createStatement().execute(command);
         } catch (SQLException var3) {
            throw this.mapXaException(var3);
         }
      }

      public void commit(Xid xid, boolean onePhase) throws XAException {
         this.execute("XA COMMIT " + MariaDbPoolConnection.xidToString(xid) + (onePhase ? " ONE PHASE" : ""));
      }

      public void end(Xid xid, int flags) throws XAException {
         if (flags != 67108864 && flags != 33554432 && flags != 536870912) {
            throw new XAException(-5);
         } else {
            this.execute("XA END " + MariaDbPoolConnection.xidToString(xid) + " " + this.flagsToString(flags));
         }
      }

      public void forget(Xid xid) {
      }

      public int getTransactionTimeout() {
         return 0;
      }

      public Configuration getConf() {
         return MariaDbPoolConnection.this.connection.getContext().getConf();
      }

      public boolean isSameRM(XAResource xaResource) {
         if (xaResource instanceof MariaDbPoolConnection.MariaDbXAResource) {
            MariaDbPoolConnection.MariaDbXAResource other = (MariaDbPoolConnection.MariaDbXAResource)xaResource;
            return other.getConf().equals(this.getConf());
         } else {
            return false;
         }
      }

      public int prepare(Xid xid) throws XAException {
         this.execute("XA PREPARE " + MariaDbPoolConnection.xidToString(xid));
         return 0;
      }

      public Xid[] recover(int flags) throws XAException {
         if ((flags & 16777216) == 0 && (flags & 8388608) == 0 && flags != 0) {
            throw new XAException(-5);
         } else if ((flags & 16777216) == 0) {
            return new MariaDbXid[0];
         } else {
            try {
               ResultSet rs = MariaDbPoolConnection.this.connection.createStatement().executeQuery("XA RECOVER");
               ArrayList xidList = new ArrayList();

               while(rs.next()) {
                  int formatId = rs.getInt(1);
                  int len1 = rs.getInt(2);
                  int len2 = rs.getInt(3);
                  byte[] arr = rs.getBytes(4);
                  byte[] globalTransactionId = new byte[len1];
                  byte[] branchQualifier = new byte[len2];
                  System.arraycopy(arr, 0, globalTransactionId, 0, len1);
                  System.arraycopy(arr, len1, branchQualifier, 0, len2);
                  xidList.add(new MariaDbXid(formatId, globalTransactionId, branchQualifier));
               }

               Xid[] xids = new Xid[xidList.size()];
               xidList.toArray(xids);
               return xids;
            } catch (SQLException var10) {
               throw this.mapXaException(var10);
            }
         }
      }

      public void rollback(Xid xid) throws XAException {
         this.execute("XA ROLLBACK " + MariaDbPoolConnection.xidToString(xid));
      }

      public boolean setTransactionTimeout(int i) {
         return false;
      }

      public void start(Xid xid, int flags) throws XAException {
         if (flags != 2097152 && flags != 134217728 && flags != 0) {
            throw new XAException(-5);
         } else {
            this.execute("XA START " + MariaDbPoolConnection.xidToString(xid) + " " + this.flagsToString(flags));
         }
      }

      // $FF: synthetic method
      MariaDbXAResource(Object x1) {
         this();
      }
   }
}
