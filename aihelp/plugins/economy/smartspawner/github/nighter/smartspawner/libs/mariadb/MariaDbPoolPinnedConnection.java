package github.nighter.smartspawner.libs.mariadb;

import github.nighter.smartspawner.libs.mariadb.client.util.ClosableLock;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.transaction.xa.XAException;
import javax.transaction.xa.XAResource;
import javax.transaction.xa.Xid;

public class MariaDbPoolPinnedConnection extends MariaDbPoolConnection {
   private static final Map<Xid, Connection> xidToConnection = new ConcurrentHashMap();
   private Xid currentXid;

   public MariaDbPoolPinnedConnection(Connection connection) {
      super(connection);
   }

   public void close() throws SQLException {
      super.close();
      if (this.currentXid != null) {
         xidToConnection.remove(this.currentXid);
      }

   }

   public XAResource getXAResource() {
      return new MariaDbPoolPinnedConnection.MariaDbXAPinnedResource();
   }

   private class MariaDbXAPinnedResource implements XAResource {
      private MariaDbXAPinnedResource() {
      }

      private void execute(Xid xid, String command, boolean removeMappingAfterExecution) throws XAException {
         if (xid == null) {
            throw new XAException();
         } else {
            try {
               if (xid.equals(MariaDbPoolPinnedConnection.this.currentXid)) {
                  MariaDbPoolPinnedConnection.this.getConnection().createStatement().execute(command);
               } else {
                  Connection con = (Connection)MariaDbPoolPinnedConnection.xidToConnection.get(xid);
                  if (con == null) {
                     con = MariaDbPoolPinnedConnection.this.getConnection();
                     MariaDbPoolPinnedConnection.xidToConnection.putIfAbsent(xid, con);
                     MariaDbPoolPinnedConnection.this.currentXid = xid;
                  }

                  ClosableLock ignore = con.getLock().closeableLock();

                  try {
                     con.createStatement().execute(command);
                     MariaDbPoolPinnedConnection.this.currentXid = null;
                     if (removeMappingAfterExecution) {
                        MariaDbPoolPinnedConnection.xidToConnection.remove(xid);
                     }
                  } catch (Throwable var9) {
                     if (ignore != null) {
                        try {
                           ignore.close();
                        } catch (Throwable var8) {
                           var9.addSuppressed(var8);
                        }
                     }

                     throw var9;
                  }

                  if (ignore != null) {
                     ignore.close();
                  }
               }

            } catch (SQLException var10) {
               throw MariaDbPoolConnection.mapXaException(var10);
            }
         }
      }

      public void commit(Xid xid, boolean onePhase) throws XAException {
         this.execute(xid, "XA COMMIT " + MariaDbPoolConnection.xidToString(xid) + (onePhase ? " ONE PHASE" : ""), true);
      }

      public void end(Xid xid, int flags) throws XAException {
         if (flags != 67108864 && flags != 33554432 && flags != 536870912) {
            throw new XAException(-5);
         } else {
            this.execute(xid, "XA END " + MariaDbPoolConnection.xidToString(xid) + " " + MariaDbPoolConnection.flagsToString(flags), false);
         }
      }

      public void forget(Xid xid) {
         MariaDbPoolPinnedConnection.xidToConnection.remove(xid);
      }

      public int getTransactionTimeout() {
         return 0;
      }

      public Configuration getConf() {
         return MariaDbPoolPinnedConnection.this.getConnection().getContext().getConf();
      }

      public boolean isSameRM(XAResource xaResource) {
         if (xaResource instanceof MariaDbPoolPinnedConnection.MariaDbXAPinnedResource) {
            MariaDbPoolPinnedConnection.MariaDbXAPinnedResource other = (MariaDbPoolPinnedConnection.MariaDbXAPinnedResource)xaResource;
            return other.getConf().equals(this.getConf());
         } else {
            return false;
         }
      }

      public int prepare(Xid xid) throws XAException {
         this.execute(xid, "XA PREPARE " + MariaDbPoolConnection.xidToString(xid), false);
         return 0;
      }

      public Xid[] recover(int flags) throws XAException {
         if ((flags & 16777216) == 0 && (flags & 8388608) == 0 && flags != 0) {
            throw new XAException(-5);
         } else if ((flags & 16777216) == 0) {
            return new MariaDbXid[0];
         } else {
            try {
               ResultSet rs = MariaDbPoolPinnedConnection.this.getConnection().createStatement().executeQuery("XA RECOVER");
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
               throw MariaDbPoolConnection.mapXaException(var10);
            }
         }
      }

      public void rollback(Xid xid) throws XAException {
         this.execute(xid, "XA ROLLBACK " + MariaDbPoolConnection.xidToString(xid), true);
      }

      public boolean setTransactionTimeout(int i) {
         return false;
      }

      public void start(Xid xid, int flags) throws XAException {
         switch(flags) {
         case 0:
            this.execute(xid, "XA START " + MariaDbPoolConnection.xidToString(xid), false);
            break;
         case 2097152:
         case 134217728:
            this.execute(xid, "XA START " + MariaDbPoolConnection.xidToString(xid) + " RESUME", false);
            break;
         default:
            throw new XAException(-5);
         }

      }

      // $FF: synthetic method
      MariaDbXAPinnedResource(Object x1) {
         this();
      }
   }
}
