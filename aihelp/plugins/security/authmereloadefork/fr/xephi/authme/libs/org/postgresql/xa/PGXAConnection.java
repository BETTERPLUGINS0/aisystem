package fr.xephi.authme.libs.org.postgresql.xa;

import fr.xephi.authme.libs.org.postgresql.PGConnection;
import fr.xephi.authme.libs.org.postgresql.core.BaseConnection;
import fr.xephi.authme.libs.org.postgresql.core.TransactionState;
import fr.xephi.authme.libs.org.postgresql.ds.PGPooledConnection;
import fr.xephi.authme.libs.org.postgresql.util.GT;
import fr.xephi.authme.libs.org.postgresql.util.PSQLException;
import fr.xephi.authme.libs.org.postgresql.util.PSQLState;
import fr.xephi.authme.libs.org.postgresql.util.internal.Nullness;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.XAConnection;
import javax.transaction.xa.XAException;
import javax.transaction.xa.XAResource;
import javax.transaction.xa.Xid;
import org.checkerframework.checker.nullness.qual.Nullable;

public class PGXAConnection extends PGPooledConnection implements XAConnection, XAResource {
   private static final Logger LOGGER = Logger.getLogger(PGXAConnection.class.getName());
   private final BaseConnection conn;
   @Nullable
   private Xid currentXid;
   private PGXAConnection.State state;
   @Nullable
   private Xid preparedXid;
   private boolean committedOrRolledBack;
   private boolean localAutoCommitMode = true;

   private void debug(String s) {
      if (LOGGER.isLoggable(Level.FINEST)) {
         LOGGER.log(Level.FINEST, "XAResource {0}: {1}", new Object[]{Integer.toHexString(this.hashCode()), s});
      }

   }

   public PGXAConnection(BaseConnection conn) throws SQLException {
      super(conn, true, true);
      this.conn = conn;
      this.state = PGXAConnection.State.IDLE;
   }

   public Connection getConnection() throws SQLException {
      Connection conn = super.getConnection();
      if (this.state == PGXAConnection.State.IDLE) {
         conn.setAutoCommit(true);
      }

      PGXAConnection.ConnectionHandler handler = new PGXAConnection.ConnectionHandler(conn);
      return (Connection)Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[]{Connection.class, PGConnection.class}, handler);
   }

   public XAResource getXAResource() {
      return this;
   }

   public void start(Xid xid, int flags) throws XAException {
      if (LOGGER.isLoggable(Level.FINEST)) {
         this.debug("starting transaction xid = " + xid);
      }

      if (flags != 0 && flags != 134217728 && flags != 2097152) {
         throw new PGXAException(GT.tr("Invalid flags {0}", flags), -5);
      } else if (xid == null) {
         throw new PGXAException(GT.tr("xid must not be null"), -5);
      } else if (this.state == PGXAConnection.State.ACTIVE) {
         throw new PGXAException(GT.tr("Connection is busy with another transaction"), -6);
      } else if (flags == 134217728) {
         throw new PGXAException(GT.tr("suspend/resume not implemented"), -3);
      } else {
         if (flags == 2097152) {
            if (this.state != PGXAConnection.State.ENDED) {
               throw new PGXAException(GT.tr("Invalid protocol state requested. Attempted transaction interleaving is not supported. xid={0}, currentXid={1}, state={2}, flags={3}", xid, this.currentXid, this.state, flags), -3);
            }

            if (!xid.equals(this.currentXid)) {
               throw new PGXAException(GT.tr("Invalid protocol state requested. Attempted transaction interleaving is not supported. xid={0}, currentXid={1}, state={2}, flags={3}", xid, this.currentXid, this.state, flags), -3);
            }
         } else if (this.state == PGXAConnection.State.ENDED) {
            throw new PGXAException(GT.tr("Invalid protocol state requested. Attempted transaction interleaving is not supported. xid={0}, currentXid={1}, state={2}, flags={3}", xid, this.currentXid, this.state, flags), -3);
         }

         if (flags == 0) {
            try {
               this.localAutoCommitMode = this.conn.getAutoCommit();
               this.conn.setAutoCommit(false);
            } catch (SQLException var4) {
               throw new PGXAException(GT.tr("Error disabling autocommit"), var4, -3);
            }
         }

         this.state = PGXAConnection.State.ACTIVE;
         this.currentXid = xid;
         this.preparedXid = null;
         this.committedOrRolledBack = false;
      }
   }

   public void end(Xid xid, int flags) throws XAException {
      if (LOGGER.isLoggable(Level.FINEST)) {
         this.debug("ending transaction xid = " + xid);
      }

      if (flags != 33554432 && flags != 536870912 && flags != 67108864) {
         throw new PGXAException(GT.tr("Invalid flags {0}", flags), -5);
      } else if (xid == null) {
         throw new PGXAException(GT.tr("xid must not be null"), -5);
      } else if (this.state == PGXAConnection.State.ACTIVE && xid.equals(this.currentXid)) {
         if (flags == 33554432) {
            throw new PGXAException(GT.tr("suspend/resume not implemented"), -3);
         } else {
            this.state = PGXAConnection.State.ENDED;
         }
      } else {
         throw new PGXAException(GT.tr("tried to call end without corresponding start call. state={0}, start xid={1}, currentXid={2}, preparedXid={3}", this.state, xid, this.currentXid, this.preparedXid), -6);
      }
   }

   public int prepare(Xid xid) throws XAException {
      if (LOGGER.isLoggable(Level.FINEST)) {
         this.debug("preparing transaction xid = " + xid);
      }

      if (this.currentXid == null && this.preparedXid != null) {
         if (LOGGER.isLoggable(Level.FINEST)) {
            this.debug("Prepare xid " + xid + " but current connection is not attached to a transaction while it was prepared in past with prepared xid " + this.preparedXid);
         }

         throw new PGXAException(GT.tr("Preparing already prepared transaction, the prepared xid {0}, prepare xid={1}", this.preparedXid, xid), -6);
      } else if (this.currentXid == null) {
         throw new PGXAException(GT.tr("Current connection does not have an associated xid. prepare xid={0}", xid), -4);
      } else if (!this.currentXid.equals(xid)) {
         if (LOGGER.isLoggable(Level.FINEST)) {
            this.debug("Error to prepare xid " + xid + ", the current connection already bound with xid " + this.currentXid);
         }

         throw new PGXAException(GT.tr("Not implemented: Prepare must be issued using the same connection that started the transaction. currentXid={0}, prepare xid={1}", this.currentXid, xid), -3);
      } else if (this.state != PGXAConnection.State.ENDED) {
         throw new PGXAException(GT.tr("Prepare called before end. prepare xid={0}, state={1}", xid), -5);
      } else {
         this.state = PGXAConnection.State.IDLE;
         this.preparedXid = this.currentXid;
         this.currentXid = null;

         try {
            String s = RecoveredXid.xidToString(xid);
            Statement stmt = this.conn.createStatement();

            try {
               stmt.executeUpdate("PREPARE TRANSACTION '" + s + "'");
            } finally {
               stmt.close();
            }

            this.conn.setAutoCommit(this.localAutoCommitMode);
            return 0;
         } catch (SQLException var8) {
            throw new PGXAException(GT.tr("Error preparing transaction. prepare xid={0}", xid), var8, this.mapSQLStateToXAErrorCode(var8));
         }
      }
   }

   public Xid[] recover(int flag) throws XAException {
      if (flag != 16777216 && flag != 8388608 && flag != 0 && flag != 25165824) {
         throw new PGXAException(GT.tr("Invalid flags {0}", flag), -5);
      } else if ((flag & 16777216) == 0) {
         return new Xid[0];
      } else {
         try {
            Statement stmt = this.conn.createStatement();

            try {
               ResultSet rs = stmt.executeQuery("SELECT gid FROM pg_prepared_xacts where database = current_database()");
               LinkedList l = new LinkedList();

               while(rs.next()) {
                  Xid recoveredXid = RecoveredXid.stringToXid((String)Nullness.castNonNull(rs.getString(1)));
                  if (recoveredXid != null) {
                     l.add(recoveredXid);
                  }
               }

               rs.close();
               Xid[] var11 = (Xid[])l.toArray(new Xid[0]);
               return var11;
            } finally {
               stmt.close();
            }
         } catch (SQLException var10) {
            throw new PGXAException(GT.tr("Error during recover"), var10, -3);
         }
      }
   }

   public void rollback(Xid xid) throws XAException {
      if (LOGGER.isLoggable(Level.FINEST)) {
         this.debug("rolling back xid = " + xid);
      }

      try {
         if (this.currentXid != null && this.currentXid.equals(xid)) {
            this.state = PGXAConnection.State.IDLE;
            this.currentXid = null;
            this.conn.rollback();
            this.conn.setAutoCommit(this.localAutoCommitMode);
         } else {
            String s = RecoveredXid.xidToString(xid);
            this.conn.setAutoCommit(true);
            Statement stmt = this.conn.createStatement();

            try {
               stmt.executeUpdate("ROLLBACK PREPARED '" + s + "'");
            } finally {
               stmt.close();
            }
         }

         this.committedOrRolledBack = true;
      } catch (SQLException var8) {
         int errorCode = -3;
         if (PSQLState.UNDEFINED_OBJECT.getState().equals(var8.getSQLState()) && (this.committedOrRolledBack || !xid.equals(this.preparedXid))) {
            if (LOGGER.isLoggable(Level.FINEST)) {
               this.debug("rolling back xid " + xid + " while the connection prepared xid is " + this.preparedXid + (this.committedOrRolledBack ? ", but the connection was already committed/rolled-back" : ""));
            }

            errorCode = -4;
         }

         if (PSQLState.isConnectionError(var8.getSQLState())) {
            if (LOGGER.isLoggable(Level.FINEST)) {
               this.debug("rollback connection failure (sql error code " + var8.getSQLState() + "), reconnection could be expected");
            }

            errorCode = -7;
         }

         throw new PGXAException(GT.tr("Error rolling back prepared transaction. rollback xid={0}, preparedXid={1}, currentXid={2}", xid, this.preparedXid, this.currentXid), var8, errorCode);
      }
   }

   public void commit(Xid xid, boolean onePhase) throws XAException {
      if (LOGGER.isLoggable(Level.FINEST)) {
         this.debug("committing xid = " + xid + (onePhase ? " (one phase) " : " (two phase)"));
      }

      if (xid == null) {
         throw new PGXAException(GT.tr("xid must not be null"), -5);
      } else {
         if (onePhase) {
            this.commitOnePhase(xid);
         } else {
            this.commitPrepared(xid);
         }

      }
   }

   private void commitOnePhase(Xid xid) throws XAException {
      try {
         if (xid.equals(this.preparedXid)) {
            throw new PGXAException(GT.tr("One-phase commit called for xid {0} but connection was prepared with xid {1}", xid, this.preparedXid), -6);
         } else if (this.currentXid == null && !this.committedOrRolledBack) {
            throw new PGXAException(GT.tr("Not implemented: one-phase commit must be issued using the same connection that was used to start it", xid), -3);
         } else if (xid.equals(this.currentXid) && !this.committedOrRolledBack) {
            if (this.state != PGXAConnection.State.ENDED) {
               throw new PGXAException(GT.tr("commit called before end. commit xid={0}, state={1}", xid, this.state), -6);
            } else {
               this.state = PGXAConnection.State.IDLE;
               this.currentXid = null;
               this.committedOrRolledBack = true;
               this.conn.commit();
               this.conn.setAutoCommit(this.localAutoCommitMode);
            }
         } else {
            throw new PGXAException(GT.tr("One-phase commit with unknown xid. commit xid={0}, currentXid={1}", xid, this.currentXid), -4);
         }
      } catch (SQLException var3) {
         throw new PGXAException(GT.tr("Error during one-phase commit. commit xid={0}", xid), var3, this.mapSQLStateToXAErrorCode(var3));
      }
   }

   private void commitPrepared(Xid xid) throws XAException {
      try {
         if (this.state == PGXAConnection.State.IDLE && this.conn.getTransactionState() == TransactionState.IDLE) {
            String s = RecoveredXid.xidToString(xid);
            this.localAutoCommitMode = this.conn.getAutoCommit();
            this.conn.setAutoCommit(true);
            Statement stmt = this.conn.createStatement();

            try {
               stmt.executeUpdate("COMMIT PREPARED '" + s + "'");
            } finally {
               stmt.close();
               this.conn.setAutoCommit(this.localAutoCommitMode);
            }

            this.committedOrRolledBack = true;
         } else {
            throw new PGXAException(GT.tr("Not implemented: 2nd phase commit must be issued using an idle connection. commit xid={0}, currentXid={1}, state={2}, transactionState={3}", xid, this.currentXid, this.state, this.conn.getTransactionState()), -3);
         }
      } catch (SQLException var8) {
         int errorCode = -3;
         if (PSQLState.UNDEFINED_OBJECT.getState().equals(var8.getSQLState()) && (this.committedOrRolledBack || !xid.equals(this.preparedXid))) {
            if (LOGGER.isLoggable(Level.FINEST)) {
               this.debug("committing xid " + xid + " while the connection prepared xid is " + this.preparedXid + (this.committedOrRolledBack ? ", but the connection was already committed/rolled-back" : ""));
            }

            errorCode = -4;
         }

         if (PSQLState.isConnectionError(var8.getSQLState())) {
            if (LOGGER.isLoggable(Level.FINEST)) {
               this.debug("commit connection failure (sql error code " + var8.getSQLState() + "), reconnection could be expected");
            }

            errorCode = -7;
         }

         throw new PGXAException(GT.tr("Error committing prepared transaction. commit xid={0}, preparedXid={1}, currentXid={2}", xid, this.preparedXid, this.currentXid), var8, errorCode);
      }
   }

   public boolean isSameRM(XAResource xares) throws XAException {
      return xares == this;
   }

   public void forget(Xid xid) throws XAException {
      throw new PGXAException(GT.tr("Heuristic commit/rollback not supported. forget xid={0}", xid), -4);
   }

   public int getTransactionTimeout() {
      return 0;
   }

   public boolean setTransactionTimeout(int seconds) {
      return false;
   }

   private int mapSQLStateToXAErrorCode(SQLException sqlException) {
      return this.isPostgreSQLIntegrityConstraintViolation(sqlException) ? 103 : -7;
   }

   private boolean isPostgreSQLIntegrityConstraintViolation(SQLException sqlException) {
      if (!(sqlException instanceof PSQLException)) {
         return false;
      } else {
         String sqlState = sqlException.getSQLState();
         return sqlState != null && sqlState.length() == 5 && sqlState.startsWith("23");
      }
   }

   private static enum State {
      IDLE,
      ACTIVE,
      ENDED;

      // $FF: synthetic method
      private static PGXAConnection.State[] $values() {
         return new PGXAConnection.State[]{IDLE, ACTIVE, ENDED};
      }
   }

   private class ConnectionHandler implements InvocationHandler {
      private final Connection con;

      ConnectionHandler(Connection con) {
         this.con = con;
      }

      @Nullable
      public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
         if (PGXAConnection.this.state != PGXAConnection.State.IDLE) {
            String methodName = method.getName();
            if ("commit".equals(methodName) || "rollback".equals(methodName) || "setSavePoint".equals(methodName) || "setAutoCommit".equals(methodName) && (Boolean)Nullness.castNonNull((Boolean)args[0])) {
               throw new PSQLException(GT.tr("Transaction control methods setAutoCommit(true), commit, rollback and setSavePoint not allowed while an XA transaction is active."), PSQLState.OBJECT_NOT_IN_STATE);
            }
         }

         try {
            if ("equals".equals(method.getName()) && args.length == 1) {
               Object arg = args[0];
               if (arg != null && Proxy.isProxyClass(arg.getClass())) {
                  InvocationHandler h = Proxy.getInvocationHandler(arg);
                  if (h instanceof PGXAConnection.ConnectionHandler) {
                     args = new Object[]{((PGXAConnection.ConnectionHandler)h).con};
                  }
               }
            }

            return method.invoke(this.con, args);
         } catch (InvocationTargetException var6) {
            throw var6.getTargetException();
         }
      }
   }
}
