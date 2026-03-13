package github.nighter.smartspawner.libs.mariadb.export;

import github.nighter.smartspawner.libs.mariadb.Configuration;
import github.nighter.smartspawner.libs.mariadb.Connection;
import github.nighter.smartspawner.libs.mariadb.HostAddress;
import github.nighter.smartspawner.libs.mariadb.MariaDbPoolConnection;
import github.nighter.smartspawner.libs.mariadb.client.Completion;
import github.nighter.smartspawner.libs.mariadb.message.server.OkPacket;
import github.nighter.smartspawner.libs.mariadb.util.ThreadUtils;
import java.sql.BatchUpdateException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.SQLInvalidAuthorizationSpecException;
import java.sql.SQLNonTransientConnectionException;
import java.sql.SQLSyntaxErrorException;
import java.sql.SQLTimeoutException;
import java.sql.SQLTransactionRollbackException;
import java.sql.SQLTransientConnectionException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ExceptionFactory {
   private static final Set<Integer> LOCK_DEADLOCK_ERROR_CODES = new HashSet(Arrays.asList(1205, 1213, 1614));
   private final Configuration conf;
   private final HostAddress hostAddress;
   private Connection connection;
   private MariaDbPoolConnection poolConnection;
   private long threadId;
   private Statement statement;

   public ExceptionFactory(Configuration conf, HostAddress hostAddress) {
      this.conf = conf;
      this.hostAddress = hostAddress;
   }

   private ExceptionFactory(Connection connection, MariaDbPoolConnection poolConnection, Configuration conf, HostAddress hostAddress, long threadId, Statement statement) {
      this.connection = connection;
      this.poolConnection = poolConnection;
      this.conf = conf;
      this.hostAddress = hostAddress;
      this.threadId = threadId;
      this.statement = statement;
   }

   private static String buildMsgText(String initialMessage, long threadId, Configuration conf, String sql, int errorCode, Connection connection) {
      StringBuilder msg = new StringBuilder();
      if (threadId != 0L) {
         msg.append("(conn=").append(threadId).append(") ");
      }

      msg.append(initialMessage);
      if (conf.dumpQueriesOnException() && sql != null) {
         if (conf.maxQuerySizeToLog() != 0 && sql.length() > conf.maxQuerySizeToLog() - 3) {
            msg.append("\nQuery is: ").append(sql, 0, conf.maxQuerySizeToLog() - 3).append("...");
         } else {
            msg.append("\nQuery is: ").append(sql);
         }
      }

      if (conf.includeInnodbStatusInDeadlockExceptions() && LOCK_DEADLOCK_ERROR_CODES.contains(errorCode) && connection != null) {
         github.nighter.smartspawner.libs.mariadb.Statement stmt = connection.createStatement();

         try {
            ResultSet rs = stmt.executeQuery("SHOW ENGINE INNODB STATUS");
            rs.next();
            msg.append("\ndeadlock information: ").append(rs.getString(3));
         } catch (SQLException var10) {
         }
      }

      if (conf.includeThreadDumpInDeadlockExceptions() && LOCK_DEADLOCK_ERROR_CODES.contains(errorCode)) {
         msg.append("\nthread name: ").append(Thread.currentThread().getName());
         msg.append("\ncurrent threads: ");
         Thread.getAllStackTraces().forEach((thread, traces) -> {
            msg.append("\n  name:\"").append(thread.getName()).append("\" pid:").append(ThreadUtils.getId(thread)).append(" status:").append(thread.getState());
            StackTraceElement[] arr$ = traces;
            int len$ = traces.length;

            for(int i$ = 0; i$ < len$; ++i$) {
               StackTraceElement trace = arr$[i$];
               msg.append("\n    ").append(trace);
            }

         });
      }

      return msg.toString();
   }

   public void setConnection(ExceptionFactory oldExceptionFactory) {
      this.connection = oldExceptionFactory.connection;
   }

   public ExceptionFactory setConnection(Connection connection) {
      this.connection = connection;
      return this;
   }

   public ExceptionFactory setPoolConnection(MariaDbPoolConnection internalPoolConnection) {
      this.poolConnection = internalPoolConnection;
      return this;
   }

   public void setThreadId(long threadId) {
      this.threadId = threadId;
   }

   public BatchUpdateException createBatchUpdate(List<Completion> res, int length, SQLException sqle) {
      int[] updateCounts = new int[length];

      for(int i = 0; i < length; ++i) {
         if (i < res.size()) {
            if (res.get(i) instanceof OkPacket) {
               updateCounts[i] = (int)((OkPacket)res.get(i)).getAffectedRows();
            } else {
               updateCounts[i] = -2;
            }
         } else {
            updateCounts[i] = -3;
         }
      }

      return new BatchUpdateException(sqle.getMessage(), sqle.getSQLState(), sqle.getErrorCode(), updateCounts, sqle);
   }

   public BatchUpdateException createBatchUpdate(List<Completion> res, int length, int[] responseMsg, SQLException sqle) {
      int[] updateCounts = new int[length];

      for(int i = 0; i < length; ++i) {
         if (i >= responseMsg.length) {
            Arrays.fill(updateCounts, i, length, -3);
            break;
         }

         int MsgResponseNo = responseMsg[i];
         if (MsgResponseNo < 1) {
            updateCounts[0] = -3;
            return new BatchUpdateException(updateCounts, sqle);
         }

         if (MsgResponseNo == 1) {
            if (i < res.size() && res.get(i) != null) {
               if (res.get(i) instanceof OkPacket) {
                  updateCounts[i] = (int)((OkPacket)res.get(i)).getAffectedRows();
               } else {
                  updateCounts[i] = -2;
               }
            } else {
               updateCounts[i] = -3;
            }
         } else {
            updateCounts[i] = -2;
         }
      }

      return new BatchUpdateException(sqle.getMessage(), sqle.getSQLState(), sqle.getErrorCode(), updateCounts, sqle);
   }

   public ExceptionFactory of(Statement statement) {
      return new ExceptionFactory(this.connection, this.poolConnection, this.conf, this.hostAddress, this.threadId, statement);
   }

   public ExceptionFactory withSql(String sql) {
      return new ExceptionFactory.SqlExceptionFactory(this.connection, this.poolConnection, this.conf, this.hostAddress, this.threadId, this.statement, sql);
   }

   private SQLException createException(String initialMessage, String sqlState, int errorCode, Exception cause, boolean isPrepare) {
      String msg = buildMsgText(initialMessage, this.threadId, this.conf, this.getSql(), errorCode, this.connection);
      if ("70100".equals(sqlState)) {
         return new SQLTimeoutException(msg, sqlState, errorCode);
      } else if ((errorCode == 4166 || errorCode == 3948 || errorCode == 1148) && !this.conf.allowLocalInfile()) {
         return new SQLException("Local infile is disabled by connector. Enable `allowLocalInfile` to allow local infile commands", sqlState, errorCode, cause);
      } else if (errorCode != 1264 && errorCode != 1265 && errorCode != 1292 && errorCode != 1406) {
         String sqlClass = sqlState == null ? "42" : sqlState.substring(0, 2);
         byte var10 = -1;
         switch(sqlClass.hashCode()) {
         case 1544:
            if (sqlClass.equals("08")) {
               var10 = 11;
            }
            break;
         case 1553:
            if (sqlClass.equals("0A")) {
               var10 = 0;
            }
            break;
         case 1598:
            if (sqlClass.equals("20")) {
               var10 = 4;
            }
            break;
         case 1599:
            if (sqlClass.equals("21")) {
               var10 = 9;
            }
            break;
         case 1600:
            if (sqlClass.equals("22")) {
               var10 = 1;
            }
            break;
         case 1601:
            if (sqlClass.equals("23")) {
               var10 = 10;
            }
            break;
         case 1603:
            if (sqlClass.equals("25")) {
               var10 = 7;
            }
            break;
         case 1604:
            if (sqlClass.equals("26")) {
               var10 = 2;
            }
            break;
         case 1606:
            if (sqlClass.equals("28")) {
               var10 = 8;
            }
            break;
         case 1620:
            if (sqlClass.equals("2F")) {
               var10 = 3;
            }
            break;
         case 1660:
            if (sqlClass.equals("40")) {
               var10 = 12;
            }
            break;
         case 1662:
            if (sqlClass.equals("42")) {
               var10 = 5;
            }
            break;
         case 2321:
            if (sqlClass.equals("HY")) {
               var10 = 13;
            }
            break;
         case 2793:
            if (sqlClass.equals("XA")) {
               var10 = 6;
            }
         }

         Object returnEx;
         switch(var10) {
         case 0:
            returnEx = new SQLFeatureNotSupportedException(msg, sqlState, errorCode, cause);
            break;
         case 1:
         case 2:
         case 3:
         case 4:
         case 5:
         case 6:
            if (isPrepare) {
               returnEx = new SQLPrepareException(msg, sqlState, errorCode, cause);
            } else {
               returnEx = new SQLSyntaxErrorException(msg, sqlState, errorCode, cause);
            }
            break;
         case 7:
         case 8:
            returnEx = new SQLInvalidAuthorizationSpecException(msg, sqlState, errorCode, cause);
            break;
         case 9:
         case 10:
            returnEx = new SQLIntegrityConstraintViolationException(msg, sqlState, errorCode, cause);
            break;
         case 11:
            returnEx = new SQLNonTransientConnectionException(msg, sqlState, errorCode, cause);
            break;
         case 12:
            returnEx = new SQLTransactionRollbackException(msg, sqlState, errorCode, cause);
            break;
         case 13:
            if (isPrepare) {
               returnEx = new SQLPrepareException(msg, sqlState, errorCode, cause);
            } else {
               returnEx = new SQLException(msg, sqlState, errorCode, cause);
            }
            break;
         default:
            returnEx = new SQLTransientConnectionException(msg, sqlState, errorCode, cause);
         }

         if (this.poolConnection != null) {
            if (this.statement != null && this.statement instanceof PreparedStatement) {
               this.poolConnection.fireStatementErrorOccurred((PreparedStatement)this.statement, (SQLException)returnEx);
            }

            if (returnEx instanceof SQLNonTransientConnectionException || returnEx instanceof SQLTransientConnectionException) {
               this.poolConnection.fireConnectionErrorOccurred((SQLException)returnEx);
            }
         }

         return (SQLException)returnEx;
      } else {
         return new MariaDbDataTruncation(msg, sqlState, errorCode, cause);
      }
   }

   public SQLException notSupported(String message) {
      return this.createException(message, "0A000", -1, (Exception)null, false);
   }

   public SQLException create(String message) {
      return this.createException(message, "42000", -1, (Exception)null, false);
   }

   public SQLException create(String message, String sqlState) {
      return this.createException(message, sqlState, -1, (Exception)null, false);
   }

   public SQLException create(String message, String sqlState, Exception cause) {
      return this.createException(message, sqlState, -1, cause, false);
   }

   public SQLException create(String message, String sqlState, int errorCode) {
      return this.createException(message, sqlState, errorCode, (Exception)null, false);
   }

   public SQLException create(String message, String sqlState, int errorCode, boolean isPrepare) {
      return this.createException(message, sqlState, errorCode, (Exception)null, isPrepare);
   }

   public String getSql() {
      return null;
   }

   // $FF: synthetic method
   ExceptionFactory(Connection x0, MariaDbPoolConnection x1, Configuration x2, HostAddress x3, long x4, Statement x5, Object x6) {
      this(x0, x1, x2, x3, x4, x5);
   }

   public class SqlExceptionFactory extends ExceptionFactory {
      private final String sql;

      public SqlExceptionFactory(Connection param2, MariaDbPoolConnection param3, Configuration param4, HostAddress param5, long param6, Statement param8, String param9) {
         super(connection, poolConnection, conf, hostAddress, threadId, statement, null);
         this.sql = sql;
      }

      public String getSql() {
         return this.sql;
      }
   }
}
