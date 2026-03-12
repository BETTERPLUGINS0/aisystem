package fr.xephi.authme.libs.com.mysql.cj.jdbc;

import fr.xephi.authme.libs.com.mysql.cj.BindValue;
import fr.xephi.authme.libs.com.mysql.cj.CancelQueryTask;
import fr.xephi.authme.libs.com.mysql.cj.ClientPreparedQuery;
import fr.xephi.authme.libs.com.mysql.cj.Messages;
import fr.xephi.authme.libs.com.mysql.cj.MysqlType;
import fr.xephi.authme.libs.com.mysql.cj.NativeQueryBindValue;
import fr.xephi.authme.libs.com.mysql.cj.NativeQueryBindings;
import fr.xephi.authme.libs.com.mysql.cj.NativeSession;
import fr.xephi.authme.libs.com.mysql.cj.PreparedQuery;
import fr.xephi.authme.libs.com.mysql.cj.Query;
import fr.xephi.authme.libs.com.mysql.cj.QueryBindings;
import fr.xephi.authme.libs.com.mysql.cj.QueryInfo;
import fr.xephi.authme.libs.com.mysql.cj.QueryReturnType;
import fr.xephi.authme.libs.com.mysql.cj.conf.PropertyKey;
import fr.xephi.authme.libs.com.mysql.cj.exceptions.CJException;
import fr.xephi.authme.libs.com.mysql.cj.exceptions.FeatureNotAvailableException;
import fr.xephi.authme.libs.com.mysql.cj.exceptions.StatementIsClosedException;
import fr.xephi.authme.libs.com.mysql.cj.jdbc.exceptions.MySQLStatementCancelledException;
import fr.xephi.authme.libs.com.mysql.cj.jdbc.exceptions.MySQLTimeoutException;
import fr.xephi.authme.libs.com.mysql.cj.jdbc.exceptions.SQLError;
import fr.xephi.authme.libs.com.mysql.cj.jdbc.exceptions.SQLExceptionsMapping;
import fr.xephi.authme.libs.com.mysql.cj.jdbc.result.CachedResultSetMetaData;
import fr.xephi.authme.libs.com.mysql.cj.jdbc.result.ResultSetInternalMethods;
import fr.xephi.authme.libs.com.mysql.cj.protocol.ColumnDefinition;
import fr.xephi.authme.libs.com.mysql.cj.protocol.Message;
import fr.xephi.authme.libs.com.mysql.cj.protocol.Resultset;
import fr.xephi.authme.libs.com.mysql.cj.protocol.a.NativePacketPayload;
import fr.xephi.authme.libs.com.mysql.cj.result.Field;
import fr.xephi.authme.libs.com.mysql.cj.util.Util;
import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.sql.Array;
import java.sql.Date;
import java.sql.JDBCType;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLType;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;

public class ClientPreparedStatement extends StatementImpl implements JdbcPreparedStatement {
   protected boolean batchHasPlainStatements;
   protected MysqlParameterMetadata parameterMetaData;
   private ResultSetMetaData pstmtResultMetaData;
   protected String batchedValuesClause;
   private boolean doPingInstead;
   private boolean compensateForOnDuplicateKeyUpdate;
   protected int rewrittenBatchSize;

   protected static ClientPreparedStatement getInstance(JdbcConnection conn, String sql, String db) throws SQLException {
      return new ClientPreparedStatement(conn, sql, db);
   }

   protected static ClientPreparedStatement getInstance(JdbcConnection conn, String sql, String db, QueryInfo cachedQueryInfo) throws SQLException {
      return new ClientPreparedStatement(conn, sql, db, cachedQueryInfo);
   }

   protected void initQuery() {
      this.query = new ClientPreparedQuery(this.session);
   }

   protected ClientPreparedStatement(JdbcConnection conn, String db) throws SQLException {
      super(conn, db);
      this.batchHasPlainStatements = false;
      this.compensateForOnDuplicateKeyUpdate = false;
      this.rewrittenBatchSize = 0;
      this.setPoolable(true);
      this.compensateForOnDuplicateKeyUpdate = (Boolean)this.session.getPropertySet().getBooleanProperty(PropertyKey.compensateOnDuplicateKeyUpdateCounts).getValue();
   }

   public ClientPreparedStatement(JdbcConnection conn, String sql, String db) throws SQLException {
      this(conn, sql, db, (QueryInfo)null);
   }

   public ClientPreparedStatement(JdbcConnection conn, String sql, String db, QueryInfo cachedQueryInfo) throws SQLException {
      this(conn, db);

      try {
         ((PreparedQuery)this.query).checkNullOrEmptyQuery(sql);
         ((PreparedQuery)this.query).setOriginalSql(sql);
         ((PreparedQuery)this.query).setQueryInfo(cachedQueryInfo != null ? cachedQueryInfo : new QueryInfo(sql, this.session, this.charEncoding));
      } catch (CJException var6) {
         throw SQLExceptionsMapping.translateException(var6, this.exceptionInterceptor);
      }

      this.doPingInstead = sql.startsWith("/* ping */");
      this.initializeFromQueryInfo();
   }

   public QueryBindings getQueryBindings() {
      return ((PreparedQuery)this.query).getQueryBindings();
   }

   public String toString() {
      try {
         StringBuilder buf = new StringBuilder();
         buf.append(this.getClass().getName());
         buf.append(": ");
         buf.append(((PreparedQuery)this.query).asSql());
         return buf.toString();
      } catch (CJException var3) {
         throw SQLExceptionsMapping.translateException(var3, this.getExceptionInterceptor());
      }
   }

   public void addBatch() throws SQLException {
      try {
         synchronized(this.checkClosed().getConnectionMutex()) {
            QueryBindings queryBindings = ((PreparedQuery)this.query).getQueryBindings();
            queryBindings.checkAllParametersSet();
            this.query.addBatch(queryBindings.clone());
         }
      } catch (CJException var6) {
         throw SQLExceptionsMapping.translateException(var6, this.getExceptionInterceptor());
      }
   }

   public void addBatch(String sql) throws SQLException {
      try {
         synchronized(this.checkClosed().getConnectionMutex()) {
            this.batchHasPlainStatements = true;
            super.addBatch(sql);
         }
      } catch (CJException var6) {
         throw SQLExceptionsMapping.translateException(var6, this.getExceptionInterceptor());
      }
   }

   public void clearBatch() throws SQLException {
      try {
         synchronized(this.checkClosed().getConnectionMutex()) {
            this.batchHasPlainStatements = false;
            super.clearBatch();
         }
      } catch (CJException var5) {
         throw SQLExceptionsMapping.translateException(var5, this.getExceptionInterceptor());
      }
   }

   public void clearParameters() throws SQLException {
      try {
         synchronized(this.checkClosed().getConnectionMutex()) {
            BindValue[] var2 = ((PreparedQuery)this.query).getQueryBindings().getBindValues();
            int var3 = var2.length;

            for(int var4 = 0; var4 < var3; ++var4) {
               BindValue bv = var2[var4];
               bv.reset();
            }

         }
      } catch (CJException var9) {
         throw SQLExceptionsMapping.translateException(var9, this.getExceptionInterceptor());
      }
   }

   protected boolean checkReadOnlySafeStatement() throws SQLException {
      try {
         synchronized(this.checkClosed().getConnectionMutex()) {
            return QueryInfo.isReadOnlySafeQuery(((PreparedQuery)this.query).getOriginalSql(), this.session.getServerSession().isNoBackslashEscapesSet()) || !this.connection.isReadOnly();
         }
      } catch (CJException var5) {
         throw SQLExceptionsMapping.translateException(var5, this.getExceptionInterceptor());
      }
   }

   public boolean execute() throws SQLException {
      try {
         synchronized(this.checkClosed().getConnectionMutex()) {
            JdbcConnection locallyScopedConn = this.connection;
            if (!this.doPingInstead && !this.checkReadOnlySafeStatement()) {
               throw SQLError.createSQLException(Messages.getString("PreparedStatement.20") + Messages.getString("PreparedStatement.21"), "S1009", this.exceptionInterceptor);
            } else {
               ResultSetInternalMethods rs = null;
               this.lastQueryIsOnDupKeyUpdate = false;
               if (this.retrieveGeneratedKeys) {
                  this.lastQueryIsOnDupKeyUpdate = this.containsOnDuplicateKeyUpdate();
               }

               this.batchedGeneratedKeys = null;
               this.resetCancelledState();
               this.implicitlyCloseAllOpenResults();
               this.clearWarnings();
               if (this.doPingInstead) {
                  this.doPingInstead();
                  return true;
               } else {
                  this.setupStreamingTimeout(locallyScopedConn);
                  Message sendPacket = ((PreparedQuery)this.query).fillSendPacket(((PreparedQuery)this.query).getQueryBindings());
                  String oldDb = null;
                  if (!locallyScopedConn.getDatabase().equals(this.getCurrentDatabase())) {
                     oldDb = locallyScopedConn.getDatabase();
                     locallyScopedConn.setDatabase(this.getCurrentDatabase());
                  }

                  CachedResultSetMetaData cachedMetadata = null;
                  boolean cacheResultSetMetadata = (Boolean)locallyScopedConn.getPropertySet().getBooleanProperty(PropertyKey.cacheResultSetMetadata).getValue();
                  if (cacheResultSetMetadata) {
                     cachedMetadata = locallyScopedConn.getCachedMetaData(((PreparedQuery)this.query).getOriginalSql());
                  }

                  locallyScopedConn.setSessionMaxRows(this.getQueryInfo().getFirstStmtChar() == 'S' ? this.maxRows : -1);
                  rs = this.executeInternal(this.maxRows, sendPacket, this.createStreamingResultSet(), this.getQueryInfo().getFirstStmtChar() == 'S', cachedMetadata, false);
                  if (cachedMetadata != null) {
                     locallyScopedConn.initializeResultsMetadataFromCache(((PreparedQuery)this.query).getOriginalSql(), cachedMetadata, rs);
                  } else if (rs.hasRows() && cacheResultSetMetadata) {
                     locallyScopedConn.initializeResultsMetadataFromCache(((PreparedQuery)this.query).getOriginalSql(), (CachedResultSetMetaData)null, rs);
                  }

                  if (this.retrieveGeneratedKeys) {
                     rs.setFirstCharOfQuery(this.getQueryInfo().getFirstStmtChar());
                  }

                  if (oldDb != null) {
                     locallyScopedConn.setDatabase(oldDb);
                  }

                  if (rs != null) {
                     this.lastInsertId = rs.getUpdateID();
                     this.results = rs;
                  }

                  return rs != null && rs.hasRows();
               }
            }
         }
      } catch (CJException var11) {
         throw SQLExceptionsMapping.translateException(var11, this.getExceptionInterceptor());
      }
   }

   protected long[] executeBatchInternal() throws SQLException {
      synchronized(this.checkClosed().getConnectionMutex()) {
         if (this.connection.isReadOnly()) {
            throw new SQLException(Messages.getString("PreparedStatement.25") + Messages.getString("PreparedStatement.26"), "S1009");
         } else if (this.query.getBatchedArgs() != null && this.query.getBatchedArgs().size() != 0) {
            int batchTimeout = this.getTimeoutInMillis();
            this.setTimeoutInMillis(0);
            this.resetCancelledState();

            long[] var3;
            try {
               this.statementBegins();
               this.clearWarnings();
               if (!this.batchHasPlainStatements && (Boolean)this.rewriteBatchedStatements.getValue()) {
                  if (this.getQueryInfo().isRewritableWithMultiValuesClause()) {
                     var3 = this.executeBatchWithMultiValuesClause(batchTimeout);
                     return var3;
                  }

                  if (!this.batchHasPlainStatements && this.query.getBatchedArgs() != null && this.query.getBatchedArgs().size() > 3) {
                     var3 = this.executePreparedBatchAsMultiStatement(batchTimeout);
                     return var3;
                  }
               }

               var3 = this.executeBatchSerially(batchTimeout);
            } finally {
               this.query.getStatementExecuting().set(false);
               this.clearBatch();
            }

            return var3;
         } else {
            return new long[0];
         }
      }
   }

   protected long[] executePreparedBatchAsMultiStatement(int batchTimeout) throws SQLException {
      try {
         synchronized(this.checkClosed().getConnectionMutex()) {
            if (this.batchedValuesClause == null) {
               this.batchedValuesClause = ((PreparedQuery)this.query).getOriginalSql() + ";";
            }

            JdbcConnection locallyScopedConn = this.connection;
            boolean multiQueriesEnabled = (Boolean)locallyScopedConn.getPropertySet().getBooleanProperty(PropertyKey.allowMultiQueries).getValue();
            CancelQueryTask timeoutTask = null;

            Object ex;
            try {
               this.clearWarnings();
               int numBatchedArgs = this.query.getBatchedArgs().size();
               if (this.retrieveGeneratedKeys) {
                  this.batchedGeneratedKeys = new ArrayList(numBatchedArgs);
               }

               int numValuesPerBatch = ((PreparedQuery)this.query).computeBatchSize(numBatchedArgs);
               if (numBatchedArgs < numValuesPerBatch) {
                  numValuesPerBatch = numBatchedArgs;
               }

               PreparedStatement batchedStatement = null;
               int batchedParamIndex = 1;
               int numberToExecuteAsMultiValue = false;
               int batchCounter = 0;
               int updateCountCounter = 0;
               long[] updateCounts = new long[numBatchedArgs * this.getQueryInfo().getNumberOfQueries()];
               SQLException sqlEx = null;

               try {
                  if (!multiQueriesEnabled) {
                     ((NativeSession)locallyScopedConn.getSession()).enableMultiQueries();
                  }

                  batchedStatement = this.retrieveGeneratedKeys ? (PreparedStatement)locallyScopedConn.prepareStatement(this.generateMultiStatementForBatch(numValuesPerBatch), 1).unwrap(PreparedStatement.class) : (PreparedStatement)locallyScopedConn.prepareStatement(this.generateMultiStatementForBatch(numValuesPerBatch)).unwrap(PreparedStatement.class);
                  timeoutTask = this.startQueryTimer((StatementImpl)batchedStatement, batchTimeout);
                  int numberToExecuteAsMultiValue = numBatchedArgs < numValuesPerBatch ? numBatchedArgs : numBatchedArgs / numValuesPerBatch;
                  ex = numberToExecuteAsMultiValue * numValuesPerBatch;

                  for(int i = 0; i < ex; ++i) {
                     if (i != 0 && i % numValuesPerBatch == 0) {
                        try {
                           batchedStatement.execute();
                        } catch (SQLException var49) {
                           sqlEx = this.handleExceptionForBatch(batchCounter, numValuesPerBatch, updateCounts, var49);
                        }

                        updateCountCounter = this.processMultiCountsAndKeys((StatementImpl)batchedStatement, updateCountCounter, updateCounts);
                        batchedStatement.clearParameters();
                        batchedParamIndex = 1;
                     }

                     batchedParamIndex = this.setOneBatchedParameterSet(batchedStatement, batchedParamIndex, this.query.getBatchedArgs().get(batchCounter++));
                  }

                  try {
                     batchedStatement.execute();
                  } catch (SQLException var48) {
                     sqlEx = this.handleExceptionForBatch(batchCounter - 1, numValuesPerBatch, updateCounts, var48);
                  }

                  updateCountCounter = this.processMultiCountsAndKeys((StatementImpl)batchedStatement, updateCountCounter, updateCounts);
                  batchedStatement.clearParameters();
                  numValuesPerBatch = numBatchedArgs - batchCounter;
                  if (timeoutTask != null) {
                     ((JdbcPreparedStatement)batchedStatement).checkCancelTimeout();
                  }
               } finally {
                  if (batchedStatement != null) {
                     batchedStatement.close();
                     batchedStatement = null;
                  }

               }

               try {
                  if (numValuesPerBatch > 0) {
                     batchedStatement = this.retrieveGeneratedKeys ? locallyScopedConn.prepareStatement(this.generateMultiStatementForBatch(numValuesPerBatch), 1) : locallyScopedConn.prepareStatement(this.generateMultiStatementForBatch(numValuesPerBatch));
                     if (timeoutTask != null) {
                        timeoutTask.setQueryToCancel((Query)batchedStatement);
                     }

                     for(batchedParamIndex = 1; batchCounter < numBatchedArgs; batchedParamIndex = this.setOneBatchedParameterSet(batchedStatement, batchedParamIndex, this.query.getBatchedArgs().get(batchCounter++))) {
                     }

                     try {
                        batchedStatement.execute();
                     } catch (SQLException var47) {
                        ex = var47;
                        sqlEx = this.handleExceptionForBatch(batchCounter - 1, numValuesPerBatch, updateCounts, var47);
                     }

                     this.processMultiCountsAndKeys((StatementImpl)batchedStatement, updateCountCounter, updateCounts);
                     batchedStatement.clearParameters();
                  }

                  if (timeoutTask != null) {
                     this.stopQueryTimer(timeoutTask, true, true);
                     timeoutTask = null;
                  }

                  if (sqlEx != null) {
                     throw SQLError.createBatchUpdateException(sqlEx, updateCounts, this.exceptionInterceptor);
                  }

                  ex = updateCounts;
               } finally {
                  if (batchedStatement != null) {
                     batchedStatement.close();
                  }

               }
            } finally {
               this.stopQueryTimer(timeoutTask, false, false);
               this.resetCancelledState();
               if (!multiQueriesEnabled) {
                  ((NativeSession)locallyScopedConn.getSession()).disableMultiQueries();
               }

               this.clearBatch();
            }

            return (long[])ex;
         }
      } catch (CJException var54) {
         throw SQLExceptionsMapping.translateException(var54, this.getExceptionInterceptor());
      }
   }

   protected int setOneBatchedParameterSet(PreparedStatement batchedStatement, int batchedParamIndex, Object paramSet) throws SQLException {
      BindValue[] bindValues = ((QueryBindings)paramSet).getBindValues();
      QueryBindings batchedStatementBindings = ((PreparedQuery)((ClientPreparedStatement)batchedStatement).getQuery()).getQueryBindings();

      for(int j = 0; j < bindValues.length; ++j) {
         batchedStatementBindings.setFromBindValue(batchedParamIndex++ - 1, bindValues[j]);
      }

      return batchedParamIndex;
   }

   private String generateMultiStatementForBatch(int numBatches) throws SQLException {
      try {
         synchronized(this.checkClosed().getConnectionMutex()) {
            String origSql = ((PreparedQuery)this.query).getOriginalSql();
            StringBuilder newStatementSql = new StringBuilder((origSql.length() + 1) * numBatches);
            newStatementSql.append(origSql);

            for(int i = 0; i < numBatches - 1; ++i) {
               newStatementSql.append(';');
               newStatementSql.append(origSql);
            }

            return newStatementSql.toString();
         }
      } catch (CJException var9) {
         throw SQLExceptionsMapping.translateException(var9, this.getExceptionInterceptor());
      }
   }

   protected long[] executeBatchWithMultiValuesClause(int batchTimeout) throws SQLException {
      try {
         synchronized(this.checkClosed().getConnectionMutex()) {
            JdbcConnection locallyScopedConn = this.connection;
            int numBatchedArgs = this.query.getBatchedArgs().size();
            if (this.retrieveGeneratedKeys) {
               this.batchedGeneratedKeys = new ArrayList(numBatchedArgs);
            }

            int numValuesPerBatch = ((PreparedQuery)this.query).computeBatchSize(numBatchedArgs);
            if (numBatchedArgs < numValuesPerBatch) {
               numValuesPerBatch = numBatchedArgs;
            }

            JdbcPreparedStatement batchedStatement = null;
            int batchedParamIndex = 1;
            long updateCountRunningTotal = 0L;
            int numberToExecuteAsMultiValue = false;
            int batchCounter = 0;
            CancelQueryTask timeoutTask = null;
            SQLException sqlEx = null;
            long[] updateCounts = new long[numBatchedArgs];

            Object ex;
            try {
               try {
                  batchedStatement = this.prepareBatchedInsertSQL(locallyScopedConn, numValuesPerBatch);
                  timeoutTask = this.startQueryTimer(batchedStatement, batchTimeout);
                  int numberToExecuteAsMultiValue = numBatchedArgs < numValuesPerBatch ? numBatchedArgs : numBatchedArgs / numValuesPerBatch;
                  ex = numberToExecuteAsMultiValue * numValuesPerBatch;

                  for(int i = 0; i < ex; ++i) {
                     if (i != 0 && i % numValuesPerBatch == 0) {
                        try {
                           updateCountRunningTotal += batchedStatement.executeLargeUpdate();
                        } catch (SQLException var49) {
                           sqlEx = this.handleExceptionForBatch(batchCounter - 1, numValuesPerBatch, updateCounts, var49);
                        }

                        this.getBatchedGeneratedKeys(batchedStatement);
                        batchedStatement.clearParameters();
                        batchedParamIndex = 1;
                     }

                     batchedParamIndex = this.setOneBatchedParameterSet(batchedStatement, batchedParamIndex, this.query.getBatchedArgs().get(batchCounter++));
                  }

                  try {
                     updateCountRunningTotal += batchedStatement.executeLargeUpdate();
                  } catch (SQLException var48) {
                     sqlEx = this.handleExceptionForBatch(batchCounter - 1, numValuesPerBatch, updateCounts, var48);
                  }

                  this.getBatchedGeneratedKeys(batchedStatement);
                  numValuesPerBatch = numBatchedArgs - batchCounter;
               } finally {
                  if (batchedStatement != null) {
                     batchedStatement.close();
                     batchedStatement = null;
                  }

               }

               try {
                  if (numValuesPerBatch > 0) {
                     batchedStatement = this.prepareBatchedInsertSQL(locallyScopedConn, numValuesPerBatch);
                     if (timeoutTask != null) {
                        timeoutTask.setQueryToCancel(batchedStatement);
                     }

                     for(batchedParamIndex = 1; batchCounter < numBatchedArgs; batchedParamIndex = this.setOneBatchedParameterSet(batchedStatement, batchedParamIndex, this.query.getBatchedArgs().get(batchCounter++))) {
                     }

                     try {
                        updateCountRunningTotal += batchedStatement.executeLargeUpdate();
                     } catch (SQLException var47) {
                        ex = var47;
                        sqlEx = this.handleExceptionForBatch(batchCounter - 1, numValuesPerBatch, updateCounts, var47);
                     }

                     this.getBatchedGeneratedKeys(batchedStatement);
                  }

                  if (sqlEx != null) {
                     throw SQLError.createBatchUpdateException(sqlEx, updateCounts, this.exceptionInterceptor);
                  }

                  if (numBatchedArgs > 1) {
                     long updCount = updateCountRunningTotal > 0L ? -2L : 0L;

                     for(int j = 0; j < numBatchedArgs; ++j) {
                        updateCounts[j] = updCount;
                     }
                  } else {
                     updateCounts[0] = updateCountRunningTotal;
                  }

                  ex = updateCounts;
               } finally {
                  if (batchedStatement != null) {
                     batchedStatement.close();
                  }

               }
            } finally {
               this.stopQueryTimer(timeoutTask, false, false);
               this.resetCancelledState();
            }

            return (long[])ex;
         }
      } catch (CJException var54) {
         throw SQLExceptionsMapping.translateException(var54, this.getExceptionInterceptor());
      }
   }

   protected long[] executeBatchSerially(int batchTimeout) throws SQLException {
      try {
         synchronized(this.checkClosed().getConnectionMutex()) {
            if (this.connection == null) {
               this.checkClosed();
            }

            long[] updateCounts = null;
            if (this.query.getBatchedArgs() != null) {
               int nbrCommands = this.query.getBatchedArgs().size();
               updateCounts = new long[nbrCommands];

               for(int i = 0; i < nbrCommands; ++i) {
                  updateCounts[i] = -3L;
               }

               SQLException sqlEx = null;
               CancelQueryTask timeoutTask = null;

               try {
                  long[] newUpdateCounts;
                  try {
                     timeoutTask = this.startQueryTimer(this, batchTimeout);
                     if (this.retrieveGeneratedKeys) {
                        this.batchedGeneratedKeys = new ArrayList(nbrCommands);
                     }

                     int batchCommandIndex = ((PreparedQuery)this.query).getBatchCommandIndex();

                     for(batchCommandIndex = 0; batchCommandIndex < nbrCommands; ++batchCommandIndex) {
                        ((PreparedQuery)this.query).setBatchCommandIndex(batchCommandIndex);
                        Object arg = this.query.getBatchedArgs().get(batchCommandIndex);

                        try {
                           if (!(arg instanceof String)) {
                              QueryBindings queryBindings = (QueryBindings)arg;
                              updateCounts[batchCommandIndex] = this.executeUpdateInternal(queryBindings, true);
                              this.getBatchedGeneratedKeys(this.containsOnDuplicateKeyUpdate() ? 1 : 0);
                           } else {
                              updateCounts[batchCommandIndex] = this.executeUpdateInternal((String)arg, true, this.retrieveGeneratedKeys);
                              this.getBatchedGeneratedKeys(this.results.getFirstCharOfQuery() == 'I' && this.containsOnDuplicateKeyInString((String)arg) ? 1 : 0);
                           }
                        } catch (SQLException var21) {
                           updateCounts[batchCommandIndex] = -3L;
                           if (!this.continueBatchOnError || var21 instanceof MySQLTimeoutException || var21 instanceof MySQLStatementCancelledException || this.hasDeadlockOrTimeoutRolledBackTx(var21)) {
                              newUpdateCounts = new long[batchCommandIndex];
                              System.arraycopy(updateCounts, 0, newUpdateCounts, 0, batchCommandIndex);
                              throw SQLError.createBatchUpdateException(var21, newUpdateCounts, this.exceptionInterceptor);
                           }

                           sqlEx = var21;
                        }
                     }

                     if (sqlEx != null) {
                        throw SQLError.createBatchUpdateException(sqlEx, updateCounts, this.exceptionInterceptor);
                     }
                  } catch (NullPointerException var22) {
                     try {
                        this.checkClosed();
                     } catch (StatementIsClosedException var20) {
                        int batchCommandIndex = ((PreparedQuery)this.query).getBatchCommandIndex();
                        updateCounts[batchCommandIndex] = -3L;
                        newUpdateCounts = new long[batchCommandIndex];
                        System.arraycopy(updateCounts, 0, newUpdateCounts, 0, batchCommandIndex);
                        throw SQLError.createBatchUpdateException(SQLExceptionsMapping.translateException(var20), newUpdateCounts, this.exceptionInterceptor);
                     }

                     throw var22;
                  }
               } finally {
                  ((PreparedQuery)this.query).setBatchCommandIndex(-1);
                  this.stopQueryTimer(timeoutTask, false, false);
                  this.resetCancelledState();
               }
            }

            return updateCounts != null ? updateCounts : new long[0];
         }
      } catch (CJException var25) {
         throw SQLExceptionsMapping.translateException(var25, this.getExceptionInterceptor());
      }
   }

   protected <M extends Message> ResultSetInternalMethods executeInternal(int maxRowsToRetrieve, M sendPacket, boolean createStreamingResultSet, boolean queryIsSelectOnly, ColumnDefinition metadata, boolean isBatch) throws SQLException {
      try {
         synchronized(this.checkClosed().getConnectionMutex()) {
            ResultSetInternalMethods var10000;
            try {
               JdbcConnection locallyScopedConnection = this.connection;
               ((PreparedQuery)this.query).getQueryBindings().setNumberOfExecutions(((PreparedQuery)this.query).getQueryBindings().getNumberOfExecutions() + 1);
               CancelQueryTask timeoutTask = null;

               ResultSetInternalMethods rs;
               try {
                  timeoutTask = this.startQueryTimer(this, this.getTimeoutInMillis());
                  if (!isBatch) {
                     this.statementBegins();
                  }

                  rs = (ResultSetInternalMethods)((NativeSession)locallyScopedConnection.getSession()).execSQL(this, (String)null, maxRowsToRetrieve, (NativePacketPayload)sendPacket, createStreamingResultSet, this.getResultSetFactory(), metadata, isBatch);
                  if (timeoutTask != null) {
                     this.stopQueryTimer(timeoutTask, true, true);
                     timeoutTask = null;
                  }
               } finally {
                  if (!isBatch) {
                     this.query.getStatementExecuting().set(false);
                  }

                  this.stopQueryTimer(timeoutTask, false, false);
               }

               var10000 = rs;
            } catch (NullPointerException var19) {
               this.checkClosed();
               throw var19;
            }

            return var10000;
         }
      } catch (CJException var21) {
         throw SQLExceptionsMapping.translateException(var21, this.getExceptionInterceptor());
      }
   }

   public ResultSet executeQuery() throws SQLException {
      try {
         synchronized(this.checkClosed().getConnectionMutex()) {
            JdbcConnection locallyScopedConn = this.connection;
            if (!this.doPingInstead) {
               QueryReturnType queryReturnType = this.getQueryInfo().getQueryReturnType();
               if (queryReturnType != QueryReturnType.PRODUCES_RESULT_SET && queryReturnType != QueryReturnType.MAY_PRODUCE_RESULT_SET) {
                  throw SQLError.createSQLException(Messages.getString("Statement.57"), "S1009", this.getExceptionInterceptor());
               }
            }

            this.batchedGeneratedKeys = null;
            this.resetCancelledState();
            this.implicitlyCloseAllOpenResults();
            this.clearWarnings();
            if (this.doPingInstead) {
               this.doPingInstead();
               return this.results;
            } else {
               this.setupStreamingTimeout(locallyScopedConn);
               Message sendPacket = ((PreparedQuery)this.query).fillSendPacket(((PreparedQuery)this.query).getQueryBindings());
               String oldDb = null;
               if (!locallyScopedConn.getDatabase().equals(this.getCurrentDatabase())) {
                  oldDb = locallyScopedConn.getDatabase();
                  locallyScopedConn.setDatabase(this.getCurrentDatabase());
               }

               CachedResultSetMetaData cachedMetadata = null;
               boolean cacheResultSetMetadata = (Boolean)locallyScopedConn.getPropertySet().getBooleanProperty(PropertyKey.cacheResultSetMetadata).getValue();
               String origSql = ((PreparedQuery)this.query).getOriginalSql();
               if (cacheResultSetMetadata) {
                  cachedMetadata = locallyScopedConn.getCachedMetaData(origSql);
               }

               locallyScopedConn.setSessionMaxRows(this.maxRows);
               this.results = this.executeInternal(this.maxRows, sendPacket, this.createStreamingResultSet(), true, cachedMetadata, false);
               if (oldDb != null) {
                  locallyScopedConn.setDatabase(oldDb);
               }

               if (cachedMetadata != null) {
                  locallyScopedConn.initializeResultsMetadataFromCache(origSql, cachedMetadata, this.results);
               } else if (cacheResultSetMetadata) {
                  locallyScopedConn.initializeResultsMetadataFromCache(origSql, (CachedResultSetMetaData)null, this.results);
               }

               this.lastInsertId = this.results.getUpdateID();
               return this.results;
            }
         }
      } catch (CJException var11) {
         throw SQLExceptionsMapping.translateException(var11, this.getExceptionInterceptor());
      }
   }

   public int executeUpdate() throws SQLException {
      try {
         return Util.truncateAndConvertToInt(this.executeLargeUpdate());
      } catch (CJException var2) {
         throw SQLExceptionsMapping.translateException(var2, this.getExceptionInterceptor());
      }
   }

   protected long executeUpdateInternal(boolean clearBatchedGeneratedKeysAndWarnings, boolean isBatch) throws SQLException {
      try {
         synchronized(this.checkClosed().getConnectionMutex()) {
            if (clearBatchedGeneratedKeysAndWarnings) {
               this.clearWarnings();
               this.batchedGeneratedKeys = null;
            }

            return this.executeUpdateInternal(((PreparedQuery)this.query).getQueryBindings(), isBatch);
         }
      } catch (CJException var7) {
         throw SQLExceptionsMapping.translateException(var7, this.getExceptionInterceptor());
      }
   }

   protected long executeUpdateInternal(QueryBindings bindings, boolean isReallyBatch) throws SQLException {
      try {
         synchronized(this.checkClosed().getConnectionMutex()) {
            JdbcConnection locallyScopedConn = this.connection;
            if (locallyScopedConn.isReadOnly(false)) {
               throw SQLError.createSQLException(Messages.getString("PreparedStatement.34") + Messages.getString("PreparedStatement.35"), "S1009", this.exceptionInterceptor);
            } else if (!this.isNonResultSetProducingQuery()) {
               throw SQLError.createSQLException(Messages.getString("PreparedStatement.37"), "01S03", this.exceptionInterceptor);
            } else {
               this.resetCancelledState();
               this.implicitlyCloseAllOpenResults();
               ResultSetInternalMethods rs = null;
               Message sendPacket = ((PreparedQuery)this.query).fillSendPacket(bindings);
               String oldDb = null;
               if (!locallyScopedConn.getDatabase().equals(this.getCurrentDatabase())) {
                  oldDb = locallyScopedConn.getDatabase();
                  locallyScopedConn.setDatabase(this.getCurrentDatabase());
               }

               locallyScopedConn.setSessionMaxRows(-1);
               rs = this.executeInternal(-1, sendPacket, false, false, (ColumnDefinition)null, isReallyBatch);
               if (this.retrieveGeneratedKeys) {
                  rs.setFirstCharOfQuery(this.getQueryInfo().getFirstStmtChar());
               }

               if (oldDb != null) {
                  locallyScopedConn.setDatabase(oldDb);
               }

               this.results = rs;
               this.updateCount = rs.getUpdateCount();
               if (this.containsOnDuplicateKeyUpdate() && this.compensateForOnDuplicateKeyUpdate && (this.updateCount == 2L || this.updateCount == 0L)) {
                  this.updateCount = 1L;
               }

               this.lastInsertId = rs.getUpdateID();
               return this.updateCount;
            }
         }
      } catch (CJException var11) {
         throw SQLExceptionsMapping.translateException(var11, this.getExceptionInterceptor());
      }
   }

   protected boolean containsOnDuplicateKeyUpdate() {
      return this.getQueryInfo().containsOnDuplicateKeyUpdate();
   }

   protected ClientPreparedStatement prepareBatchedInsertSQL(JdbcConnection localConn, int numBatches) throws SQLException {
      try {
         synchronized(this.checkClosed().getConnectionMutex()) {
            ClientPreparedStatement pstmt = new ClientPreparedStatement(localConn, "Rewritten batch of: " + ((PreparedQuery)this.query).getOriginalSql(), this.getCurrentDatabase(), this.getQueryInfo().getQueryInfoForBatch(numBatches));
            pstmt.setRetrieveGeneratedKeys(this.retrieveGeneratedKeys);
            pstmt.rewrittenBatchSize = numBatches;
            this.getQueryAttributesBindings().runThroughAll((a) -> {
               pstmt.setAttribute(a.getName(), a.getValue());
            });
            return pstmt;
         }
      } catch (CJException var8) {
         throw SQLExceptionsMapping.translateException(var8, this.getExceptionInterceptor());
      }
   }

   protected void setRetrieveGeneratedKeys(boolean flag) throws SQLException {
      try {
         synchronized(this.checkClosed().getConnectionMutex()) {
            this.retrieveGeneratedKeys = flag;
         }
      } catch (CJException var6) {
         throw SQLExceptionsMapping.translateException(var6, this.getExceptionInterceptor());
      }
   }

   public byte[] getBytesRepresentation(int parameterIndex) throws SQLException {
      try {
         synchronized(this.checkClosed().getConnectionMutex()) {
            return ((PreparedQuery)this.query).getQueryBindings().getBytesRepresentation(this.getCoreParameterIndex(parameterIndex));
         }
      } catch (CJException var6) {
         throw SQLExceptionsMapping.translateException(var6, this.getExceptionInterceptor());
      }
   }

   public ResultSetMetaData getMetaData() throws SQLException {
      try {
         synchronized(this.checkClosed().getConnectionMutex()) {
            if (!this.isResultSetProducingQuery()) {
               return null;
            } else {
               JdbcPreparedStatement mdStmt = null;
               ResultSet mdRs = null;
               if (this.pstmtResultMetaData == null) {
                  boolean var18 = false;

                  try {
                     var18 = true;
                     mdStmt = new ClientPreparedStatement(this.connection, ((PreparedQuery)this.query).getOriginalSql(), this.getCurrentDatabase(), this.getQueryInfo());
                     mdStmt.setMaxRows(1);
                     int paramCount = ((PreparedQuery)this.query).getParameterCount();

                     for(int i = 1; i <= paramCount; ++i) {
                        mdStmt.setString(i, (String)null);
                     }

                     boolean hadResults = mdStmt.execute();
                     if (hadResults) {
                        mdRs = mdStmt.getResultSet();
                        this.pstmtResultMetaData = mdRs.getMetaData();
                        var18 = false;
                     } else {
                        this.pstmtResultMetaData = new fr.xephi.authme.libs.com.mysql.cj.jdbc.result.ResultSetMetaData(this.session, new Field[0], (Boolean)this.session.getPropertySet().getBooleanProperty(PropertyKey.useOldAliasMetadataBehavior).getValue(), (Boolean)this.session.getPropertySet().getBooleanProperty(PropertyKey.yearIsDateType).getValue(), this.exceptionInterceptor);
                        var18 = false;
                     }
                  } finally {
                     if (var18) {
                        SQLException sqlExRethrow = null;
                        if (mdRs != null) {
                           try {
                              mdRs.close();
                           } catch (SQLException var20) {
                              sqlExRethrow = var20;
                           }

                           mdRs = null;
                        }

                        if (mdStmt != null) {
                           try {
                              mdStmt.close();
                           } catch (SQLException var19) {
                              sqlExRethrow = var19;
                           }

                           mdStmt = null;
                        }

                        if (sqlExRethrow != null) {
                           throw sqlExRethrow;
                        }

                     }
                  }

                  SQLException sqlExRethrow = null;
                  if (mdRs != null) {
                     try {
                        mdRs.close();
                     } catch (SQLException var22) {
                        sqlExRethrow = var22;
                     }

                     mdRs = null;
                  }

                  if (mdStmt != null) {
                     try {
                        mdStmt.close();
                     } catch (SQLException var21) {
                        sqlExRethrow = var21;
                     }

                     mdStmt = null;
                  }

                  if (sqlExRethrow != null) {
                     throw sqlExRethrow;
                  }
               }

               return this.pstmtResultMetaData;
            }
         }
      } catch (CJException var25) {
         throw SQLExceptionsMapping.translateException(var25, this.getExceptionInterceptor());
      }
   }

   protected boolean isResultSetProducingQuery() {
      QueryReturnType queryReturnType = this.getQueryInfo().getQueryReturnType();
      return queryReturnType == QueryReturnType.PRODUCES_RESULT_SET || queryReturnType == QueryReturnType.MAY_PRODUCE_RESULT_SET;
   }

   private boolean isNonResultSetProducingQuery() {
      QueryReturnType queryReturnType = this.getQueryInfo().getQueryReturnType();
      return queryReturnType == QueryReturnType.DOES_NOT_PRODUCE_RESULT_SET || queryReturnType == QueryReturnType.MAY_PRODUCE_RESULT_SET;
   }

   public ParameterMetaData getParameterMetaData() throws SQLException {
      try {
         synchronized(this.checkClosed().getConnectionMutex()) {
            if (this.parameterMetaData == null) {
               if ((Boolean)this.session.getPropertySet().getBooleanProperty(PropertyKey.generateSimpleParameterMetadata).getValue()) {
                  this.parameterMetaData = new MysqlParameterMetadata(((PreparedQuery)this.query).getParameterCount());
               } else {
                  this.parameterMetaData = new MysqlParameterMetadata(this.session, (Field[])null, ((PreparedQuery)this.query).getParameterCount(), this.exceptionInterceptor);
               }
            }

            return this.parameterMetaData;
         }
      } catch (CJException var5) {
         throw SQLExceptionsMapping.translateException(var5, this.getExceptionInterceptor());
      }
   }

   public QueryInfo getQueryInfo() {
      return ((PreparedQuery)this.query).getQueryInfo();
   }

   private void initializeFromQueryInfo() throws SQLException {
      try {
         synchronized(this.checkClosed().getConnectionMutex()) {
            int parameterCount = this.getQueryInfo().getStaticSqlParts().length - 1;
            ((PreparedQuery)this.query).setParameterCount(parameterCount);
            ((PreparedQuery)this.query).setQueryBindings(new NativeQueryBindings(parameterCount, this.session, NativeQueryBindValue::new));
            this.clearParameters();
         }
      } catch (CJException var6) {
         throw SQLExceptionsMapping.translateException(var6, this.getExceptionInterceptor());
      }
   }

   public boolean isNull(int paramIndex) throws SQLException {
      try {
         synchronized(this.checkClosed().getConnectionMutex()) {
            return ((PreparedQuery)this.query).getQueryBindings().getBindValues()[this.getCoreParameterIndex(paramIndex)].isNull();
         }
      } catch (CJException var6) {
         throw SQLExceptionsMapping.translateException(var6, this.getExceptionInterceptor());
      }
   }

   public void realClose(boolean calledExplicitly, boolean closeOpenResults) throws SQLException {
      JdbcConnection locallyScopedConn = this.connection;
      if (locallyScopedConn != null) {
         synchronized(locallyScopedConn.getConnectionMutex()) {
            if (!this.isClosed) {
               if (this.useUsageAdvisor) {
                  QueryBindings qb = ((PreparedQuery)this.query).getQueryBindings();
                  if (qb == null || qb.getNumberOfExecutions() <= 1) {
                     this.session.getProfilerEventHandler().processEvent((byte)0, this.session, this, (Resultset)null, 0L, new Throwable(), Messages.getString("PreparedStatement.43"));
                  }
               }

               super.realClose(calledExplicitly, closeOpenResults);
               ((PreparedQuery)this.query).setOriginalSql((String)null);
               ((PreparedQuery)this.query).setQueryBindings((QueryBindings)null);
            }
         }
      }
   }

   public String getPreparedSql() {
      synchronized(this.checkClosed().getConnectionMutex()) {
         return this.rewrittenBatchSize == 0 ? ((PreparedQuery)this.query).getOriginalSql() : this.getQueryInfo().getSqlForBatch();
      }
   }

   public int getUpdateCount() throws SQLException {
      try {
         int count = super.getUpdateCount();
         if (this.containsOnDuplicateKeyUpdate() && this.compensateForOnDuplicateKeyUpdate && (count == 2 || count == 0)) {
            count = 1;
         }

         return count;
      } catch (CJException var3) {
         throw SQLExceptionsMapping.translateException(var3, this.getExceptionInterceptor());
      }
   }

   public long executeLargeUpdate() throws SQLException {
      try {
         return this.executeUpdateInternal(true, false);
      } catch (CJException var2) {
         throw SQLExceptionsMapping.translateException(var2, this.getExceptionInterceptor());
      }
   }

   public ParameterBindings getParameterBindings() throws SQLException {
      try {
         synchronized(this.checkClosed().getConnectionMutex()) {
            return new ParameterBindingsImpl((PreparedQuery)this.query, this.session, this.resultSetFactory);
         }
      } catch (CJException var5) {
         throw SQLExceptionsMapping.translateException(var5, this.getExceptionInterceptor());
      }
   }

   protected int getParameterIndexOffset() {
      return 0;
   }

   protected void checkBounds(int paramIndex, int parameterIndexOffset) throws SQLException {
      try {
         synchronized(this.checkClosed().getConnectionMutex()) {
            if (paramIndex < 1) {
               throw SQLError.createSQLException(Messages.getString("PreparedStatement.49") + paramIndex + Messages.getString("PreparedStatement.50"), "S1009", this.exceptionInterceptor);
            } else if (paramIndex > ((PreparedQuery)this.query).getParameterCount()) {
               throw SQLError.createSQLException(Messages.getString("PreparedStatement.51") + paramIndex + Messages.getString("PreparedStatement.52") + ((PreparedQuery)this.query).getParameterCount() + Messages.getString("PreparedStatement.53"), "S1009", this.exceptionInterceptor);
            } else if (parameterIndexOffset == -1 && paramIndex == 1) {
               throw SQLError.createSQLException(Messages.getString("PreparedStatement.63"), "S1009", this.exceptionInterceptor);
            }
         }
      } catch (CJException var7) {
         throw SQLExceptionsMapping.translateException(var7, this.getExceptionInterceptor());
      }
   }

   public final int getCoreParameterIndex(int paramIndex) throws SQLException {
      int parameterIndexOffset = this.getParameterIndexOffset();
      this.checkBounds(paramIndex, parameterIndexOffset);
      return paramIndex - 1 + parameterIndexOffset;
   }

   public void setArray(int i, Array x) throws SQLException {
      try {
         throw SQLError.createSQLFeatureNotSupportedException();
      } catch (CJException var4) {
         throw SQLExceptionsMapping.translateException(var4, this.getExceptionInterceptor());
      }
   }

   public void setAsciiStream(int parameterIndex, InputStream x) throws SQLException {
      try {
         synchronized(this.checkClosed().getConnectionMutex()) {
            ((PreparedQuery)this.query).getQueryBindings().setAsciiStream(this.getCoreParameterIndex(parameterIndex), x, -1);
         }
      } catch (CJException var7) {
         throw SQLExceptionsMapping.translateException(var7, this.getExceptionInterceptor());
      }
   }

   public void setAsciiStream(int parameterIndex, InputStream x, int length) throws SQLException {
      try {
         synchronized(this.checkClosed().getConnectionMutex()) {
            ((PreparedQuery)this.query).getQueryBindings().setAsciiStream(this.getCoreParameterIndex(parameterIndex), x, length);
         }
      } catch (CJException var8) {
         throw SQLExceptionsMapping.translateException(var8, this.getExceptionInterceptor());
      }
   }

   public void setAsciiStream(int parameterIndex, InputStream x, long length) throws SQLException {
      try {
         synchronized(this.checkClosed().getConnectionMutex()) {
            ((PreparedQuery)this.query).getQueryBindings().setAsciiStream(this.getCoreParameterIndex(parameterIndex), x, (int)length);
         }
      } catch (CJException var9) {
         throw SQLExceptionsMapping.translateException(var9, this.getExceptionInterceptor());
      }
   }

   public void setBigDecimal(int parameterIndex, BigDecimal x) throws SQLException {
      try {
         synchronized(this.checkClosed().getConnectionMutex()) {
            ((PreparedQuery)this.query).getQueryBindings().setBigDecimal(this.getCoreParameterIndex(parameterIndex), x);
         }
      } catch (CJException var7) {
         throw SQLExceptionsMapping.translateException(var7, this.getExceptionInterceptor());
      }
   }

   public void setBinaryStream(int parameterIndex, InputStream x) throws SQLException {
      try {
         synchronized(this.checkClosed().getConnectionMutex()) {
            ((PreparedQuery)this.query).getQueryBindings().setBinaryStream(this.getCoreParameterIndex(parameterIndex), x, -1);
         }
      } catch (CJException var7) {
         throw SQLExceptionsMapping.translateException(var7, this.getExceptionInterceptor());
      }
   }

   public void setBinaryStream(int parameterIndex, InputStream x, int length) throws SQLException {
      try {
         synchronized(this.checkClosed().getConnectionMutex()) {
            ((PreparedQuery)this.query).getQueryBindings().setBinaryStream(this.getCoreParameterIndex(parameterIndex), x, length);
         }
      } catch (CJException var8) {
         throw SQLExceptionsMapping.translateException(var8, this.getExceptionInterceptor());
      }
   }

   public void setBinaryStream(int parameterIndex, InputStream x, long length) throws SQLException {
      try {
         synchronized(this.checkClosed().getConnectionMutex()) {
            ((PreparedQuery)this.query).getQueryBindings().setBinaryStream(this.getCoreParameterIndex(parameterIndex), x, (int)length);
         }
      } catch (CJException var9) {
         throw SQLExceptionsMapping.translateException(var9, this.getExceptionInterceptor());
      }
   }

   public void setBlob(int i, java.sql.Blob x) throws SQLException {
      try {
         synchronized(this.checkClosed().getConnectionMutex()) {
            ((PreparedQuery)this.query).getQueryBindings().setBlob(this.getCoreParameterIndex(i), x);
         }
      } catch (CJException var7) {
         throw SQLExceptionsMapping.translateException(var7, this.getExceptionInterceptor());
      }
   }

   public void setBlob(int parameterIndex, InputStream inputStream) throws SQLException {
      try {
         synchronized(this.checkClosed().getConnectionMutex()) {
            ((PreparedQuery)this.query).getQueryBindings().setBinaryStream(this.getCoreParameterIndex(parameterIndex), inputStream, -1);
         }
      } catch (CJException var7) {
         throw SQLExceptionsMapping.translateException(var7, this.getExceptionInterceptor());
      }
   }

   public void setBlob(int parameterIndex, InputStream inputStream, long length) throws SQLException {
      try {
         synchronized(this.checkClosed().getConnectionMutex()) {
            ((PreparedQuery)this.query).getQueryBindings().setBinaryStream(this.getCoreParameterIndex(parameterIndex), inputStream, (int)length);
         }
      } catch (CJException var9) {
         throw SQLExceptionsMapping.translateException(var9, this.getExceptionInterceptor());
      }
   }

   public void setBoolean(int parameterIndex, boolean x) throws SQLException {
      try {
         synchronized(this.checkClosed().getConnectionMutex()) {
            ((PreparedQuery)this.query).getQueryBindings().setBoolean(this.getCoreParameterIndex(parameterIndex), x);
         }
      } catch (CJException var7) {
         throw SQLExceptionsMapping.translateException(var7, this.getExceptionInterceptor());
      }
   }

   public void setByte(int parameterIndex, byte x) throws SQLException {
      try {
         synchronized(this.checkClosed().getConnectionMutex()) {
            ((PreparedQuery)this.query).getQueryBindings().setByte(this.getCoreParameterIndex(parameterIndex), x);
         }
      } catch (CJException var7) {
         throw SQLExceptionsMapping.translateException(var7, this.getExceptionInterceptor());
      }
   }

   public void setBytes(int parameterIndex, byte[] x) throws SQLException {
      try {
         synchronized(this.checkClosed().getConnectionMutex()) {
            ((PreparedQuery)this.query).getQueryBindings().setBytes(this.getCoreParameterIndex(parameterIndex), x, true);
         }
      } catch (CJException var7) {
         throw SQLExceptionsMapping.translateException(var7, this.getExceptionInterceptor());
      }
   }

   public void setBytes(int parameterIndex, byte[] x, boolean escapeIfNeeded) throws SQLException {
      synchronized(this.checkClosed().getConnectionMutex()) {
         ((PreparedQuery)this.query).getQueryBindings().setBytes(this.getCoreParameterIndex(parameterIndex), x, escapeIfNeeded);
      }
   }

   public void setCharacterStream(int parameterIndex, Reader reader) throws SQLException {
      try {
         synchronized(this.checkClosed().getConnectionMutex()) {
            ((PreparedQuery)this.query).getQueryBindings().setCharacterStream(this.getCoreParameterIndex(parameterIndex), reader, -1);
         }
      } catch (CJException var7) {
         throw SQLExceptionsMapping.translateException(var7, this.getExceptionInterceptor());
      }
   }

   public void setCharacterStream(int parameterIndex, Reader reader, int length) throws SQLException {
      try {
         synchronized(this.checkClosed().getConnectionMutex()) {
            ((PreparedQuery)this.query).getQueryBindings().setCharacterStream(this.getCoreParameterIndex(parameterIndex), reader, length);
         }
      } catch (CJException var8) {
         throw SQLExceptionsMapping.translateException(var8, this.getExceptionInterceptor());
      }
   }

   public void setCharacterStream(int parameterIndex, Reader reader, long length) throws SQLException {
      try {
         synchronized(this.checkClosed().getConnectionMutex()) {
            ((PreparedQuery)this.query).getQueryBindings().setCharacterStream(this.getCoreParameterIndex(parameterIndex), reader, (int)length);
         }
      } catch (CJException var9) {
         throw SQLExceptionsMapping.translateException(var9, this.getExceptionInterceptor());
      }
   }

   public void setClob(int parameterIndex, Reader reader) throws SQLException {
      try {
         synchronized(this.checkClosed().getConnectionMutex()) {
            ((PreparedQuery)this.query).getQueryBindings().setCharacterStream(this.getCoreParameterIndex(parameterIndex), reader, -1);
         }
      } catch (CJException var7) {
         throw SQLExceptionsMapping.translateException(var7, this.getExceptionInterceptor());
      }
   }

   public void setClob(int parameterIndex, Reader reader, long length) throws SQLException {
      try {
         synchronized(this.checkClosed().getConnectionMutex()) {
            ((PreparedQuery)this.query).getQueryBindings().setCharacterStream(this.getCoreParameterIndex(parameterIndex), reader, (int)length);
         }
      } catch (CJException var9) {
         throw SQLExceptionsMapping.translateException(var9, this.getExceptionInterceptor());
      }
   }

   public void setClob(int i, java.sql.Clob x) throws SQLException {
      try {
         synchronized(this.checkClosed().getConnectionMutex()) {
            ((PreparedQuery)this.query).getQueryBindings().setClob(this.getCoreParameterIndex(i), x);
         }
      } catch (CJException var7) {
         throw SQLExceptionsMapping.translateException(var7, this.getExceptionInterceptor());
      }
   }

   public void setDate(int parameterIndex, Date x) throws SQLException {
      try {
         synchronized(this.checkClosed().getConnectionMutex()) {
            ((PreparedQuery)this.query).getQueryBindings().setDate(this.getCoreParameterIndex(parameterIndex), x, (Calendar)null);
         }
      } catch (CJException var7) {
         throw SQLExceptionsMapping.translateException(var7, this.getExceptionInterceptor());
      }
   }

   public void setDate(int parameterIndex, Date x, Calendar cal) throws SQLException {
      try {
         synchronized(this.checkClosed().getConnectionMutex()) {
            ((PreparedQuery)this.query).getQueryBindings().setDate(this.getCoreParameterIndex(parameterIndex), x, cal);
         }
      } catch (CJException var8) {
         throw SQLExceptionsMapping.translateException(var8, this.getExceptionInterceptor());
      }
   }

   public void setDouble(int parameterIndex, double x) throws SQLException {
      try {
         synchronized(this.checkClosed().getConnectionMutex()) {
            ((PreparedQuery)this.query).getQueryBindings().setDouble(this.getCoreParameterIndex(parameterIndex), x);
         }
      } catch (CJException var8) {
         throw SQLExceptionsMapping.translateException(var8, this.getExceptionInterceptor());
      }
   }

   public void setFloat(int parameterIndex, float x) throws SQLException {
      try {
         ((PreparedQuery)this.query).getQueryBindings().setFloat(this.getCoreParameterIndex(parameterIndex), x);
      } catch (CJException var4) {
         throw SQLExceptionsMapping.translateException(var4, this.getExceptionInterceptor());
      }
   }

   public void setInt(int parameterIndex, int x) throws SQLException {
      try {
         synchronized(this.checkClosed().getConnectionMutex()) {
            ((PreparedQuery)this.query).getQueryBindings().setInt(this.getCoreParameterIndex(parameterIndex), x);
         }
      } catch (CJException var7) {
         throw SQLExceptionsMapping.translateException(var7, this.getExceptionInterceptor());
      }
   }

   public void setLong(int parameterIndex, long x) throws SQLException {
      try {
         synchronized(this.checkClosed().getConnectionMutex()) {
            ((PreparedQuery)this.query).getQueryBindings().setLong(this.getCoreParameterIndex(parameterIndex), x);
         }
      } catch (CJException var8) {
         throw SQLExceptionsMapping.translateException(var8, this.getExceptionInterceptor());
      }
   }

   public void setBigInteger(int parameterIndex, BigInteger x) throws SQLException {
      synchronized(this.checkClosed().getConnectionMutex()) {
         ((PreparedQuery)this.query).getQueryBindings().setBigInteger(this.getCoreParameterIndex(parameterIndex), x);
      }
   }

   public void setNCharacterStream(int parameterIndex, Reader value) throws SQLException {
      try {
         synchronized(this.checkClosed().getConnectionMutex()) {
            ((PreparedQuery)this.query).getQueryBindings().setNCharacterStream(this.getCoreParameterIndex(parameterIndex), value, -1L);
         }
      } catch (CJException var7) {
         throw SQLExceptionsMapping.translateException(var7, this.getExceptionInterceptor());
      }
   }

   public void setNCharacterStream(int parameterIndex, Reader reader, long length) throws SQLException {
      try {
         synchronized(this.checkClosed().getConnectionMutex()) {
            ((PreparedQuery)this.query).getQueryBindings().setNCharacterStream(this.getCoreParameterIndex(parameterIndex), reader, length);
         }
      } catch (CJException var9) {
         throw SQLExceptionsMapping.translateException(var9, this.getExceptionInterceptor());
      }
   }

   public void setNClob(int parameterIndex, Reader reader) throws SQLException {
      try {
         synchronized(this.checkClosed().getConnectionMutex()) {
            ((PreparedQuery)this.query).getQueryBindings().setNCharacterStream(this.getCoreParameterIndex(parameterIndex), reader, -1L);
         }
      } catch (CJException var7) {
         throw SQLExceptionsMapping.translateException(var7, this.getExceptionInterceptor());
      }
   }

   public void setNClob(int parameterIndex, Reader reader, long length) throws SQLException {
      try {
         synchronized(this.checkClosed().getConnectionMutex()) {
            ((PreparedQuery)this.query).getQueryBindings().setNCharacterStream(this.getCoreParameterIndex(parameterIndex), reader, length);
         }
      } catch (CJException var9) {
         throw SQLExceptionsMapping.translateException(var9, this.getExceptionInterceptor());
      }
   }

   public void setNClob(int parameterIndex, java.sql.NClob value) throws SQLException {
      try {
         synchronized(this.checkClosed().getConnectionMutex()) {
            ((PreparedQuery)this.query).getQueryBindings().setNClob(this.getCoreParameterIndex(parameterIndex), value);
         }
      } catch (CJException var7) {
         throw SQLExceptionsMapping.translateException(var7, this.getExceptionInterceptor());
      }
   }

   public void setNString(int parameterIndex, String x) throws SQLException {
      try {
         synchronized(this.checkClosed().getConnectionMutex()) {
            ((PreparedQuery)this.query).getQueryBindings().setNString(this.getCoreParameterIndex(parameterIndex), x);
         }
      } catch (CJException var7) {
         throw SQLExceptionsMapping.translateException(var7, this.getExceptionInterceptor());
      }
   }

   public void setNull(int parameterIndex, int sqlType) throws SQLException {
      try {
         synchronized(this.checkClosed().getConnectionMutex()) {
            ((PreparedQuery)this.query).getQueryBindings().setNull(this.getCoreParameterIndex(parameterIndex));
         }
      } catch (CJException var7) {
         throw SQLExceptionsMapping.translateException(var7, this.getExceptionInterceptor());
      }
   }

   public void setNull(int parameterIndex, int sqlType, String typeName) throws SQLException {
      try {
         synchronized(this.checkClosed().getConnectionMutex()) {
            ((PreparedQuery)this.query).getQueryBindings().setNull(this.getCoreParameterIndex(parameterIndex));
         }
      } catch (CJException var8) {
         throw SQLExceptionsMapping.translateException(var8, this.getExceptionInterceptor());
      }
   }

   public void setNull(int parameterIndex, MysqlType mysqlType) throws SQLException {
      this.setNull(parameterIndex, mysqlType.getJdbcType());
   }

   public void setObject(int parameterIndex, Object parameterObj) throws SQLException {
      try {
         synchronized(this.checkClosed().getConnectionMutex()) {
            ((PreparedQuery)this.query).getQueryBindings().setObject(this.getCoreParameterIndex(parameterIndex), parameterObj);
         }
      } catch (CJException var7) {
         throw SQLExceptionsMapping.translateException(var7, this.getExceptionInterceptor());
      }
   }

   public void setObject(int parameterIndex, Object parameterObj, int targetSqlType) throws SQLException {
      try {
         synchronized(this.checkClosed().getConnectionMutex()) {
            try {
               ((PreparedQuery)this.query).getQueryBindings().setObject(this.getCoreParameterIndex(parameterIndex), parameterObj, MysqlType.getByJdbcType(targetSqlType), -1);
            } catch (FeatureNotAvailableException var8) {
               throw SQLError.createSQLFeatureNotSupportedException(Messages.getString("Statement.UnsupportedSQLType") + JDBCType.valueOf(targetSqlType), "S1C00", this.exceptionInterceptor);
            }

         }
      } catch (CJException var10) {
         throw SQLExceptionsMapping.translateException(var10, this.getExceptionInterceptor());
      }
   }

   public void setObject(int parameterIndex, Object parameterObj, SQLType targetSqlType) throws SQLException {
      try {
         synchronized(this.checkClosed().getConnectionMutex()) {
            if (targetSqlType instanceof MysqlType) {
               ((PreparedQuery)this.query).getQueryBindings().setObject(this.getCoreParameterIndex(parameterIndex), parameterObj, (MysqlType)targetSqlType, -1);
            } else {
               this.setObject(parameterIndex, parameterObj, targetSqlType.getVendorTypeNumber());
            }

         }
      } catch (CJException var8) {
         throw SQLExceptionsMapping.translateException(var8, this.getExceptionInterceptor());
      }
   }

   public void setObject(int parameterIndex, Object parameterObj, int targetSqlType, int scale) throws SQLException {
      try {
         synchronized(this.checkClosed().getConnectionMutex()) {
            try {
               ((PreparedQuery)this.query).getQueryBindings().setObject(this.getCoreParameterIndex(parameterIndex), parameterObj, MysqlType.getByJdbcType(targetSqlType), scale);
            } catch (FeatureNotAvailableException var9) {
               throw SQLError.createSQLFeatureNotSupportedException(Messages.getString("Statement.UnsupportedSQLType") + JDBCType.valueOf(targetSqlType), "S1C00", this.exceptionInterceptor);
            }

         }
      } catch (CJException var11) {
         throw SQLExceptionsMapping.translateException(var11, this.getExceptionInterceptor());
      }
   }

   public void setObject(int parameterIndex, Object x, SQLType targetSqlType, int scaleOrLength) throws SQLException {
      try {
         synchronized(this.checkClosed().getConnectionMutex()) {
            if (targetSqlType instanceof MysqlType) {
               ((PreparedQuery)this.query).getQueryBindings().setObject(this.getCoreParameterIndex(parameterIndex), x, (MysqlType)targetSqlType, scaleOrLength);
            } else {
               this.setObject(parameterIndex, x, targetSqlType.getVendorTypeNumber(), scaleOrLength);
            }

         }
      } catch (CJException var9) {
         throw SQLExceptionsMapping.translateException(var9, this.getExceptionInterceptor());
      }
   }

   public void setRef(int i, Ref x) throws SQLException {
      try {
         throw SQLError.createSQLFeatureNotSupportedException();
      } catch (CJException var4) {
         throw SQLExceptionsMapping.translateException(var4, this.getExceptionInterceptor());
      }
   }

   public void setRowId(int parameterIndex, RowId x) throws SQLException {
      try {
         throw SQLError.createSQLFeatureNotSupportedException();
      } catch (CJException var4) {
         throw SQLExceptionsMapping.translateException(var4, this.getExceptionInterceptor());
      }
   }

   public void setShort(int parameterIndex, short x) throws SQLException {
      try {
         synchronized(this.checkClosed().getConnectionMutex()) {
            ((PreparedQuery)this.query).getQueryBindings().setShort(this.getCoreParameterIndex(parameterIndex), x);
         }
      } catch (CJException var7) {
         throw SQLExceptionsMapping.translateException(var7, this.getExceptionInterceptor());
      }
   }

   public void setSQLXML(int parameterIndex, SQLXML xmlObject) throws SQLException {
      try {
         if (xmlObject == null) {
            this.setNull(parameterIndex, MysqlType.VARCHAR);
         } else {
            this.setCharacterStream(parameterIndex, ((MysqlSQLXML)xmlObject).serializeAsCharacterStream());
         }

      } catch (CJException var4) {
         throw SQLExceptionsMapping.translateException(var4, this.getExceptionInterceptor());
      }
   }

   public void setString(int parameterIndex, String x) throws SQLException {
      try {
         synchronized(this.checkClosed().getConnectionMutex()) {
            ((PreparedQuery)this.query).getQueryBindings().setString(this.getCoreParameterIndex(parameterIndex), x);
         }
      } catch (CJException var7) {
         throw SQLExceptionsMapping.translateException(var7, this.getExceptionInterceptor());
      }
   }

   public void setTime(int parameterIndex, Time x) throws SQLException {
      try {
         synchronized(this.checkClosed().getConnectionMutex()) {
            ((PreparedQuery)this.query).getQueryBindings().setTime(this.getCoreParameterIndex(parameterIndex), x, (Calendar)null);
         }
      } catch (CJException var7) {
         throw SQLExceptionsMapping.translateException(var7, this.getExceptionInterceptor());
      }
   }

   public void setTime(int parameterIndex, Time x, Calendar cal) throws SQLException {
      try {
         synchronized(this.checkClosed().getConnectionMutex()) {
            ((PreparedQuery)this.query).getQueryBindings().setTime(this.getCoreParameterIndex(parameterIndex), x, cal);
         }
      } catch (CJException var8) {
         throw SQLExceptionsMapping.translateException(var8, this.getExceptionInterceptor());
      }
   }

   public void setTimestamp(int parameterIndex, Timestamp x) throws SQLException {
      try {
         synchronized(this.checkClosed().getConnectionMutex()) {
            ((PreparedQuery)this.query).getQueryBindings().setTimestamp(this.getCoreParameterIndex(parameterIndex), x, (Calendar)null, (Field)null, MysqlType.TIMESTAMP);
         }
      } catch (CJException var7) {
         throw SQLExceptionsMapping.translateException(var7, this.getExceptionInterceptor());
      }
   }

   public void setTimestamp(int parameterIndex, Timestamp x, Calendar cal) throws SQLException {
      try {
         synchronized(this.checkClosed().getConnectionMutex()) {
            ((PreparedQuery)this.query).getQueryBindings().setTimestamp(this.getCoreParameterIndex(parameterIndex), x, cal, (Field)null, MysqlType.TIMESTAMP);
         }
      } catch (CJException var8) {
         throw SQLExceptionsMapping.translateException(var8, this.getExceptionInterceptor());
      }
   }

   /** @deprecated */
   @Deprecated
   public void setUnicodeStream(int parameterIndex, InputStream x, int length) throws SQLException {
      try {
         this.setBinaryStream(parameterIndex, x, length);
         ((PreparedQuery)this.query).getQueryBindings().getBindValues()[this.getCoreParameterIndex(parameterIndex)].setMysqlType(MysqlType.TEXT);
      } catch (CJException var5) {
         throw SQLExceptionsMapping.translateException(var5, this.getExceptionInterceptor());
      }
   }

   public void setURL(int parameterIndex, URL arg) throws SQLException {
      try {
         if (arg == null) {
            this.setNull(parameterIndex, MysqlType.VARCHAR);
         } else {
            this.setString(parameterIndex, arg.toString());
            ((PreparedQuery)this.query).getQueryBindings().getBindValues()[this.getCoreParameterIndex(parameterIndex)].setMysqlType(MysqlType.VARCHAR);
         }

      } catch (CJException var4) {
         throw SQLExceptionsMapping.translateException(var4, this.getExceptionInterceptor());
      }
   }
}
