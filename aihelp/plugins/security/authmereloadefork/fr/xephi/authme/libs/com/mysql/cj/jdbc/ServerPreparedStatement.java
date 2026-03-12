package fr.xephi.authme.libs.com.mysql.cj.jdbc;

import fr.xephi.authme.libs.com.mysql.cj.BindValue;
import fr.xephi.authme.libs.com.mysql.cj.CancelQueryTask;
import fr.xephi.authme.libs.com.mysql.cj.Messages;
import fr.xephi.authme.libs.com.mysql.cj.NativeSession;
import fr.xephi.authme.libs.com.mysql.cj.PreparedQuery;
import fr.xephi.authme.libs.com.mysql.cj.QueryBindings;
import fr.xephi.authme.libs.com.mysql.cj.QueryInfo;
import fr.xephi.authme.libs.com.mysql.cj.ServerPreparedQuery;
import fr.xephi.authme.libs.com.mysql.cj.conf.PropertyKey;
import fr.xephi.authme.libs.com.mysql.cj.exceptions.CJException;
import fr.xephi.authme.libs.com.mysql.cj.exceptions.ExceptionFactory;
import fr.xephi.authme.libs.com.mysql.cj.exceptions.ExceptionInterceptor;
import fr.xephi.authme.libs.com.mysql.cj.exceptions.WrongArgumentException;
import fr.xephi.authme.libs.com.mysql.cj.jdbc.exceptions.MySQLStatementCancelledException;
import fr.xephi.authme.libs.com.mysql.cj.jdbc.exceptions.MySQLTimeoutException;
import fr.xephi.authme.libs.com.mysql.cj.jdbc.exceptions.SQLError;
import fr.xephi.authme.libs.com.mysql.cj.jdbc.exceptions.SQLExceptionsMapping;
import fr.xephi.authme.libs.com.mysql.cj.jdbc.result.ResultSetInternalMethods;
import fr.xephi.authme.libs.com.mysql.cj.protocol.ColumnDefinition;
import fr.xephi.authme.libs.com.mysql.cj.protocol.Message;
import fr.xephi.authme.libs.com.mysql.cj.protocol.a.NativePacketPayload;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.ParameterMetaData;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

public class ServerPreparedStatement extends ClientPreparedStatement {
   private boolean hasOnDuplicateKeyUpdate = false;
   private boolean invalid = false;
   private CJException invalidationException;
   protected boolean isCacheable = false;
   protected boolean isCached = false;

   protected static ServerPreparedStatement getInstance(JdbcConnection conn, String sql, String db, int resultSetType, int resultSetConcurrency) throws SQLException {
      return new ServerPreparedStatement(conn, sql, db, resultSetType, resultSetConcurrency);
   }

   protected ServerPreparedStatement(JdbcConnection conn, String sql, String db, int resultSetType, int resultSetConcurrency) throws SQLException {
      super(conn, db);
      this.checkNullOrEmptyQuery(sql);
      String statementComment = this.session.getProtocol().getQueryComment();
      PreparedQuery prepQuery = (PreparedQuery)this.query;
      prepQuery.setOriginalSql(statementComment == null ? sql : "/* " + statementComment + " */ " + sql);
      prepQuery.setQueryInfo(new QueryInfo(prepQuery.getOriginalSql(), this.session, this.charEncoding));
      this.hasOnDuplicateKeyUpdate = prepQuery.getQueryInfo().containsOnDuplicateKeyUpdate();

      try {
         this.serverPrepare(sql);
      } catch (SQLException | CJException var9) {
         this.realClose(false, true);
         throw SQLExceptionsMapping.translateException(var9, this.exceptionInterceptor);
      }

      this.setResultSetType(resultSetType);
      this.setResultSetConcurrency(resultSetConcurrency);
   }

   protected void initQuery() {
      this.query = ServerPreparedQuery.getInstance(this.session);
   }

   public String toString() {
      try {
         StringBuilder toStringBuf = new StringBuilder();
         toStringBuf.append(this.getClass().getName() + "[");
         toStringBuf.append(((ServerPreparedQuery)this.query).getServerStatementId());
         toStringBuf.append("]: ");
         toStringBuf.append(((PreparedQuery)this.query).asSql());
         return toStringBuf.toString();
      } catch (CJException var3) {
         throw SQLExceptionsMapping.translateException(var3, this.getExceptionInterceptor());
      }
   }

   public void addBatch() throws SQLException {
      try {
         synchronized(this.checkClosed().getConnectionMutex()) {
            this.query.addBatch(((PreparedQuery)this.query).getQueryBindings().clone());
         }
      } catch (CJException var5) {
         throw SQLExceptionsMapping.translateException(var5, this.getExceptionInterceptor());
      }
   }

   protected JdbcConnection checkClosed() {
      if (this.invalid) {
         throw this.invalidationException;
      } else {
         return super.checkClosed();
      }
   }

   public void clearParameters() {
      try {
         synchronized(this.checkClosed().getConnectionMutex()) {
            ((ServerPreparedQuery)this.query).clearParameters(true);
         }
      } catch (CJException var5) {
         throw SQLExceptionsMapping.translateException(var5, this.getExceptionInterceptor());
      }
   }

   protected void setClosed(boolean flag) {
      this.isClosed = flag;
   }

   public void close() throws SQLException {
      try {
         JdbcConnection locallyScopedConn = this.connection;
         if (locallyScopedConn != null) {
            synchronized(locallyScopedConn.getConnectionMutex()) {
               if (!this.isClosed) {
                  if (this.isCacheable && this.isPoolable()) {
                     this.clearParameters();
                     this.clearAttributes();
                     this.isClosed = true;
                     this.connection.recachePreparedStatement(this);
                     this.isCached = true;
                  } else {
                     this.isClosed = false;
                     this.realClose(true, true);
                  }
               }
            }
         }
      } catch (CJException var6) {
         throw SQLExceptionsMapping.translateException(var6, this.getExceptionInterceptor());
      }
   }

   protected long[] executeBatchSerially(int batchTimeout) throws SQLException {
      synchronized(this.checkClosed().getConnectionMutex()) {
         JdbcConnection locallyScopedConn = this.connection;
         if (locallyScopedConn.isReadOnly()) {
            throw SQLError.createSQLException(Messages.getString("ServerPreparedStatement.2") + Messages.getString("ServerPreparedStatement.3"), "S1009", this.exceptionInterceptor);
         } else {
            this.clearWarnings();
            BindValue[] oldBindValues = ((ServerPreparedQuery)this.query).getQueryBindings().getBindValues();

            long[] var38;
            try {
               long[] updateCounts = null;
               if (this.query.getBatchedArgs() != null) {
                  int nbrCommands = this.query.getBatchedArgs().size();
                  updateCounts = new long[nbrCommands];
                  if (this.retrieveGeneratedKeys) {
                     this.batchedGeneratedKeys = new ArrayList(nbrCommands);
                  }

                  for(int i = 0; i < nbrCommands; ++i) {
                     updateCounts[i] = -3L;
                  }

                  SQLException sqlEx = null;
                  int commandIndex = false;
                  BindValue[] previousBindValuesForBatch = null;
                  CancelQueryTask timeoutTask = null;

                  try {
                     timeoutTask = this.startQueryTimer(this, batchTimeout);

                     for(int commandIndex = 0; commandIndex < nbrCommands; ++commandIndex) {
                        Object arg = this.query.getBatchedArgs().get(commandIndex);

                        try {
                           if (arg instanceof String) {
                              updateCounts[commandIndex] = this.executeUpdateInternal((String)arg, true, this.retrieveGeneratedKeys);
                              this.getBatchedGeneratedKeys(this.results.getFirstCharOfQuery() == 'I' && this.containsOnDuplicateKeyInString((String)arg) ? 1 : 0);
                           } else {
                              ((PreparedQuery)this.query).setQueryBindings((QueryBindings)arg);
                              BindValue[] parameterBindings = ((QueryBindings)arg).getBindValues();
                              if (previousBindValuesForBatch != null) {
                                 for(int j = 0; j < parameterBindings.length; ++j) {
                                    if (parameterBindings[j].getMysqlType() != previousBindValuesForBatch[j].getMysqlType()) {
                                       ((ServerPreparedQuery)this.query).getQueryBindings().getSendTypesToServer().set(true);
                                       break;
                                    }
                                 }
                              }

                              try {
                                 updateCounts[commandIndex] = this.executeUpdateInternal(false, true);
                              } finally {
                                 previousBindValuesForBatch = parameterBindings;
                              }

                              this.getBatchedGeneratedKeys(this.containsOnDuplicateKeyUpdate() ? 1 : 0);
                           }
                        } catch (SQLException var34) {
                           updateCounts[commandIndex] = -3L;
                           if (!this.continueBatchOnError || var34 instanceof MySQLTimeoutException || var34 instanceof MySQLStatementCancelledException || this.hasDeadlockOrTimeoutRolledBackTx(var34)) {
                              long[] newUpdateCounts = new long[commandIndex];
                              System.arraycopy(updateCounts, 0, newUpdateCounts, 0, commandIndex);
                              throw SQLError.createBatchUpdateException(var34, newUpdateCounts, this.exceptionInterceptor);
                           }

                           sqlEx = var34;
                        }
                     }
                  } finally {
                     this.stopQueryTimer(timeoutTask, false, false);
                     this.resetCancelledState();
                  }

                  if (sqlEx != null) {
                     throw SQLError.createBatchUpdateException(sqlEx, updateCounts, this.exceptionInterceptor);
                  }
               }

               var38 = updateCounts != null ? updateCounts : new long[0];
            } finally {
               ((ServerPreparedQuery)this.query).getQueryBindings().setBindValues(oldBindValues);
               ((ServerPreparedQuery)this.query).getQueryBindings().getSendTypesToServer().set(true);
               this.clearBatch();
            }

            return var38;
         }
      }
   }

   private static SQLException appendMessageToException(SQLException sqlEx, String messageToAppend, ExceptionInterceptor interceptor) {
      String sqlState = sqlEx.getSQLState();
      int vendorErrorCode = sqlEx.getErrorCode();
      SQLException sqlExceptionWithNewMessage = SQLError.createSQLException(sqlEx.getMessage() + messageToAppend, sqlState, vendorErrorCode, interceptor);
      sqlExceptionWithNewMessage.setStackTrace(sqlEx.getStackTrace());
      return sqlExceptionWithNewMessage;
   }

   protected <M extends Message> ResultSetInternalMethods executeInternal(int maxRowsToRetrieve, M sendPacket, boolean createStreamingResultSet, boolean queryIsSelectOnly, ColumnDefinition metadata, boolean isBatch) throws SQLException {
      try {
         synchronized(this.checkClosed().getConnectionMutex()) {
            ((PreparedQuery)this.query).getQueryBindings().setNumberOfExecutions(((PreparedQuery)this.query).getQueryBindings().getNumberOfExecutions() + 1);

            ResultSetInternalMethods var10000;
            try {
               var10000 = this.serverExecute(maxRowsToRetrieve, createStreamingResultSet, metadata);
            } catch (SQLException var14) {
               SQLException sqlEx = var14;
               if ((Boolean)this.session.getPropertySet().getBooleanProperty(PropertyKey.enablePacketDebug).getValue()) {
                  this.session.dumpPacketRingBuffer();
               }

               if ((Boolean)this.dumpQueriesOnException.getValue()) {
                  String extractedSql = this.toString();
                  StringBuilder messageBuf = new StringBuilder(extractedSql.length() + 32);
                  messageBuf.append("\n\nQuery being executed when exception was thrown:\n");
                  messageBuf.append(extractedSql);
                  messageBuf.append("\n\n");
                  sqlEx = appendMessageToException(var14, messageBuf.toString(), this.exceptionInterceptor);
               }

               throw sqlEx;
            } catch (Exception var15) {
               if ((Boolean)this.session.getPropertySet().getBooleanProperty(PropertyKey.enablePacketDebug).getValue()) {
                  this.session.dumpPacketRingBuffer();
               }

               SQLException sqlEx = SQLError.createSQLException(var15.toString(), "S1000", var15, this.exceptionInterceptor);
               if ((Boolean)this.dumpQueriesOnException.getValue()) {
                  String extractedSql = this.toString();
                  StringBuilder messageBuf = new StringBuilder(extractedSql.length() + 32);
                  messageBuf.append("\n\nQuery being executed when exception was thrown:\n");
                  messageBuf.append(extractedSql);
                  messageBuf.append("\n\n");
                  sqlEx = appendMessageToException(sqlEx, messageBuf.toString(), this.exceptionInterceptor);
               }

               throw sqlEx;
            }

            return var10000;
         }
      } catch (CJException var17) {
         throw SQLExceptionsMapping.translateException(var17, this.getExceptionInterceptor());
      }
   }

   protected BindValue getBinding(int parameterIndex, boolean forLongData) throws SQLException {
      try {
         synchronized(this.checkClosed().getConnectionMutex()) {
            int i = this.getCoreParameterIndex(parameterIndex);
            return ((ServerPreparedQuery)this.query).getQueryBindings().getBinding(i, forLongData);
         }
      } catch (CJException var8) {
         throw SQLExceptionsMapping.translateException(var8, this.getExceptionInterceptor());
      }
   }

   public ResultSetMetaData getMetaData() throws SQLException {
      try {
         synchronized(this.checkClosed().getConnectionMutex()) {
            ColumnDefinition resultFields = ((ServerPreparedQuery)this.query).getResultFields();
            return resultFields != null && resultFields.getFields() != null ? new fr.xephi.authme.libs.com.mysql.cj.jdbc.result.ResultSetMetaData(this.session, resultFields.getFields(), (Boolean)this.session.getPropertySet().getBooleanProperty(PropertyKey.useOldAliasMetadataBehavior).getValue(), (Boolean)this.session.getPropertySet().getBooleanProperty(PropertyKey.yearIsDateType).getValue(), this.exceptionInterceptor) : null;
         }
      } catch (CJException var6) {
         throw SQLExceptionsMapping.translateException(var6, this.getExceptionInterceptor());
      }
   }

   public ParameterMetaData getParameterMetaData() throws SQLException {
      try {
         synchronized(this.checkClosed().getConnectionMutex()) {
            if (this.parameterMetaData == null) {
               this.parameterMetaData = new MysqlParameterMetadata(this.session, ((ServerPreparedQuery)this.query).getParameterFields(), ((PreparedQuery)this.query).getParameterCount(), this.exceptionInterceptor);
            }

            return this.parameterMetaData;
         }
      } catch (CJException var5) {
         throw SQLExceptionsMapping.translateException(var5, this.getExceptionInterceptor());
      }
   }

   public boolean isNull(int paramIndex) {
      throw new IllegalArgumentException(Messages.getString("ServerPreparedStatement.7"));
   }

   public void realClose(boolean calledExplicitly, boolean closeOpenResults) throws SQLException {
      try {
         JdbcConnection locallyScopedConn = this.connection;
         if (locallyScopedConn != null) {
            synchronized(locallyScopedConn.getConnectionMutex()) {
               if (this.connection != null) {
                  CJException exceptionDuringClose = null;
                  if (this.isCached) {
                     locallyScopedConn.decachePreparedStatement(this);
                     this.isCached = false;
                  }

                  super.realClose(calledExplicitly, closeOpenResults);
                  ((ServerPreparedQuery)this.query).clearParameters(false);
                  if (calledExplicitly && !locallyScopedConn.isClosed()) {
                     synchronized(locallyScopedConn.getConnectionMutex()) {
                        try {
                           ((NativeSession)locallyScopedConn.getSession()).getProtocol().sendCommand(this.commandBuilder.buildComStmtClose((NativePacketPayload)null, ((ServerPreparedQuery)this.query).getServerStatementId()), true, 0);
                        } catch (CJException var11) {
                           exceptionDuringClose = var11;
                        }
                     }
                  }

                  if (exceptionDuringClose != null) {
                     throw exceptionDuringClose;
                  }
               }

            }
         }
      } catch (CJException var14) {
         throw SQLExceptionsMapping.translateException(var14, this.getExceptionInterceptor());
      }
   }

   protected void rePrepare() {
      synchronized(this.checkClosed().getConnectionMutex()) {
         this.invalidationException = null;

         try {
            this.serverPrepare(((PreparedQuery)this.query).getOriginalSql());
         } catch (Exception var7) {
            this.invalidationException = ExceptionFactory.createException((String)var7.getMessage(), (Throwable)var7);
         }

         if (this.invalidationException != null) {
            this.invalid = true;
            this.query.closeQuery();
            if (this.results != null) {
               try {
                  this.results.close();
               } catch (Exception var6) {
               }
            }

            if (this.generatedKeysResults != null) {
               try {
                  this.generatedKeysResults.close();
               } catch (Exception var5) {
               }
            }

            try {
               this.closeAllOpenResults();
            } catch (Exception var4) {
            }

            if (this.connection != null && !(Boolean)this.dontTrackOpenResources.getValue()) {
               this.connection.unregisterStatement(this);
            }
         }

      }
   }

   protected ResultSetInternalMethods serverExecute(int maxRowsToRetrieve, boolean createStreamingResultSet, ColumnDefinition metadata) throws SQLException {
      try {
         synchronized(this.checkClosed().getConnectionMutex()) {
            this.results = (ResultSetInternalMethods)((ServerPreparedQuery)this.query).serverExecute(maxRowsToRetrieve, createStreamingResultSet, metadata, this.resultSetFactory);
            return this.results;
         }
      } catch (CJException var8) {
         throw SQLExceptionsMapping.translateException(var8, this.getExceptionInterceptor());
      }
   }

   protected void serverPrepare(String sql) throws SQLException {
      try {
         synchronized(this.checkClosed().getConnectionMutex()) {
            SQLException t = null;

            try {
               ServerPreparedQuery q = (ServerPreparedQuery)this.query;
               q.serverPrepare(sql);
            } catch (IOException var21) {
               t = SQLError.createCommunicationsException(this.connection, this.session.getProtocol().getPacketSentTimeHolder(), this.session.getProtocol().getPacketReceivedTimeHolder(), var21, this.exceptionInterceptor);
            } catch (CJException var22) {
               SQLException ex = SQLExceptionsMapping.translateException(var22);
               if ((Boolean)this.dumpQueriesOnException.getValue()) {
                  StringBuilder messageBuf = new StringBuilder(((PreparedQuery)this.query).getOriginalSql().length() + 32);
                  messageBuf.append("\n\nQuery being prepared when exception was thrown:\n\n");
                  messageBuf.append(((PreparedQuery)this.query).getOriginalSql());
                  ex = appendMessageToException(ex, messageBuf.toString(), this.exceptionInterceptor);
               }

               t = ex;
            } finally {
               try {
                  this.session.clearInputStream();
               } catch (Exception var20) {
                  if (t == null) {
                     t = SQLError.createCommunicationsException(this.connection, this.session.getProtocol().getPacketSentTimeHolder(), this.session.getProtocol().getPacketReceivedTimeHolder(), var20, this.exceptionInterceptor);
                  }
               }

               if (t != null) {
                  throw t;
               }

            }

         }
      } catch (CJException var25) {
         throw SQLExceptionsMapping.translateException(var25, this.getExceptionInterceptor());
      }
   }

   protected void checkBounds(int parameterIndex, int parameterIndexOffset) throws SQLException {
      int paramCount = ((PreparedQuery)this.query).getParameterCount();
      if (paramCount == 0) {
         throw (WrongArgumentException)ExceptionFactory.createException(WrongArgumentException.class, Messages.getString("ServerPreparedStatement.8"), this.session.getExceptionInterceptor());
      } else if (parameterIndex < 0 || parameterIndex > paramCount) {
         throw (WrongArgumentException)ExceptionFactory.createException(WrongArgumentException.class, Messages.getString("ServerPreparedStatement.9") + (parameterIndex + 1) + Messages.getString("ServerPreparedStatement.10") + paramCount, this.session.getExceptionInterceptor());
      }
   }

   /** @deprecated */
   @Deprecated
   public void setUnicodeStream(int parameterIndex, InputStream x, int length) throws SQLException {
      try {
         this.checkClosed();
         throw SQLError.createSQLFeatureNotSupportedException();
      } catch (CJException var5) {
         throw SQLExceptionsMapping.translateException(var5, this.getExceptionInterceptor());
      }
   }

   public void setURL(int parameterIndex, URL x) throws SQLException {
      try {
         this.checkClosed();
         this.setString(parameterIndex, x.toString());
      } catch (CJException var4) {
         throw SQLExceptionsMapping.translateException(var4, this.getExceptionInterceptor());
      }
   }

   public long getServerStatementId() {
      return ((ServerPreparedQuery)this.query).getServerStatementId();
   }

   protected boolean containsOnDuplicateKeyUpdate() {
      return this.hasOnDuplicateKeyUpdate;
   }

   protected ClientPreparedStatement prepareBatchedInsertSQL(JdbcConnection localConn, int numBatches) throws SQLException {
      synchronized(this.checkClosed().getConnectionMutex()) {
         ClientPreparedStatement var10000;
         try {
            ClientPreparedStatement pstmt = (ClientPreparedStatement)localConn.prepareStatement(((PreparedQuery)this.query).getQueryInfo().getSqlForBatch(numBatches), this.resultSetConcurrency, this.query.getResultType().getIntValue()).unwrap(ClientPreparedStatement.class);
            pstmt.setRetrieveGeneratedKeys(this.retrieveGeneratedKeys);
            this.getQueryAttributesBindings().runThroughAll((a) -> {
               pstmt.setAttribute(a.getName(), a.getValue());
            });
            var10000 = pstmt;
         } catch (CJException var7) {
            SQLException sqlEx = SQLError.createSQLException(Messages.getString("ServerPreparedStatement.27"), "S1000", this.exceptionInterceptor);
            sqlEx.initCause(var7);
            throw sqlEx;
         }

         return var10000;
      }
   }

   public void setPoolable(boolean poolable) throws SQLException {
      try {
         super.setPoolable(poolable);
         if (!poolable && this.isCached) {
            this.connection.decachePreparedStatement(this);
            this.isCached = false;
            if (this.isClosed) {
               this.isClosed = false;
               this.realClose(true, true);
            }
         }

      } catch (CJException var3) {
         throw SQLExceptionsMapping.translateException(var3, this.getExceptionInterceptor());
      }
   }
}
