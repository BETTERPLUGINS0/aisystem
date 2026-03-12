package fr.xephi.authme.libs.org.mariadb.jdbc.client.impl;

import fr.xephi.authme.libs.org.mariadb.jdbc.Configuration;
import fr.xephi.authme.libs.org.mariadb.jdbc.HostAddress;
import fr.xephi.authme.libs.org.mariadb.jdbc.ServerPreparedStatement;
import fr.xephi.authme.libs.org.mariadb.jdbc.Statement;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.Client;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.Completion;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.Context;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.ReadableByteBuf;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.context.BaseContext;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.context.RedoContext;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.result.Result;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.result.StreamingResult;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.socket.Reader;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.socket.Writer;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.socket.impl.CompressInputStream;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.socket.impl.CompressOutputStream;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.socket.impl.PacketReader;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.socket.impl.PacketWriter;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.socket.impl.ReadAheadBufferedStream;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.util.MutableByte;
import fr.xephi.authme.libs.org.mariadb.jdbc.export.ExceptionFactory;
import fr.xephi.authme.libs.org.mariadb.jdbc.export.MaxAllowedPacketException;
import fr.xephi.authme.libs.org.mariadb.jdbc.export.Prepare;
import fr.xephi.authme.libs.org.mariadb.jdbc.message.ClientMessage;
import fr.xephi.authme.libs.org.mariadb.jdbc.message.client.ClosePreparePacket;
import fr.xephi.authme.libs.org.mariadb.jdbc.message.client.HandshakeResponse;
import fr.xephi.authme.libs.org.mariadb.jdbc.message.client.QueryPacket;
import fr.xephi.authme.libs.org.mariadb.jdbc.message.client.QuitPacket;
import fr.xephi.authme.libs.org.mariadb.jdbc.message.server.ErrorPacket;
import fr.xephi.authme.libs.org.mariadb.jdbc.message.server.InitialHandshakePacket;
import fr.xephi.authme.libs.org.mariadb.jdbc.message.server.PrepareResultPacket;
import fr.xephi.authme.libs.org.mariadb.jdbc.plugin.Credential;
import fr.xephi.authme.libs.org.mariadb.jdbc.plugin.CredentialPlugin;
import fr.xephi.authme.libs.org.mariadb.jdbc.util.Security;
import fr.xephi.authme.libs.org.mariadb.jdbc.util.log.Logger;
import fr.xephi.authme.libs.org.mariadb.jdbc.util.log.Loggers;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLNonTransientConnectionException;
import java.time.DateTimeException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.Executor;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;
import javax.net.ssl.SSLSocket;

public class StandardClient implements Client, AutoCloseable {
   private static final Logger logger = Loggers.getLogger(StandardClient.class);
   protected final ExceptionFactory exceptionFactory;
   private final Socket socket;
   private final MutableByte sequence = new MutableByte();
   private final MutableByte compressionSequence = new MutableByte();
   private final ReentrantLock lock;
   private final Configuration conf;
   private final HostAddress hostAddress;
   private final boolean disablePipeline;
   protected Context context;
   protected Writer writer;
   private boolean closed = false;
   private Reader reader;
   private Statement streamStmt = null;
   private ClientMessage streamMsg = null;
   private int socketTimeout;

   public StandardClient(Configuration conf, HostAddress hostAddress, ReentrantLock lock, boolean skipPostCommands) throws SQLException {
      this.conf = conf;
      this.lock = lock;
      this.hostAddress = hostAddress;
      this.exceptionFactory = new ExceptionFactory(conf, hostAddress);
      this.disablePipeline = conf.disablePipeline();
      String host = hostAddress != null ? hostAddress.host : null;
      this.socketTimeout = conf.socketTimeout();
      this.socket = ConnectionHelper.connectSocket(conf, hostAddress);

      try {
         OutputStream out = this.socket.getOutputStream();
         InputStream in = conf.useReadAheadInput() ? new ReadAheadBufferedStream(this.socket.getInputStream()) : new BufferedInputStream(this.socket.getInputStream(), 16384);
         this.assignStream((OutputStream)out, (InputStream)in, conf, (Long)null);
         if (conf.connectTimeout() > 0) {
            this.setSocketTimeout(conf.connectTimeout());
         } else if (conf.socketTimeout() > 0) {
            this.setSocketTimeout(conf.socketTimeout());
         }

         ReadableByteBuf buf = this.reader.readReusablePacket(logger.isTraceEnabled());
         if (buf.getByte() == -1) {
            ErrorPacket errorPacket = new ErrorPacket(buf, (Context)null);
            throw this.exceptionFactory.create(errorPacket.getMessage(), errorPacket.getSqlState(), errorPacket.getErrorCode());
         } else {
            InitialHandshakePacket handshake = InitialHandshakePacket.decode(buf);
            this.exceptionFactory.setThreadId(handshake.getThreadId());
            long clientCapabilities = ConnectionHelper.initializeClientCapabilities(conf, handshake.getCapabilities(), hostAddress);
            this.context = (Context)(conf.transactionReplay() ? new RedoContext(hostAddress, handshake, clientCapabilities, conf, this.exceptionFactory, new PrepareCache(conf.prepStmtCacheSize(), this)) : new BaseContext(hostAddress, handshake, clientCapabilities, conf, this.exceptionFactory, conf.cachePrepStmts() ? new PrepareCache(conf.prepStmtCacheSize(), this) : null));
            this.reader.setServerThreadId(handshake.getThreadId(), hostAddress);
            this.writer.setServerThreadId(handshake.getThreadId(), hostAddress);
            SSLSocket sslSocket = ConnectionHelper.sslWrapper(hostAddress, this.socket, clientCapabilities, (byte)handshake.getDefaultCollation(), this.context, this.writer);
            if (sslSocket != null) {
               out = new BufferedOutputStream(sslSocket.getOutputStream(), 16384);
               in = conf.useReadAheadInput() ? new ReadAheadBufferedStream(sslSocket.getInputStream()) : new BufferedInputStream(sslSocket.getInputStream(), 16384);
               this.assignStream((OutputStream)out, (InputStream)in, conf, handshake.getThreadId());
            }

            String authenticationPluginType = handshake.getAuthenticationPluginType();
            CredentialPlugin credentialPlugin = conf.credentialPlugin();
            if (credentialPlugin != null && credentialPlugin.defaultAuthenticationPluginType() != null) {
               authenticationPluginType = credentialPlugin.defaultAuthenticationPluginType();
            }

            Credential credential = ConnectionHelper.loadCredential(credentialPlugin, conf, hostAddress);
            (new HandshakeResponse(credential, authenticationPluginType, this.context.getSeed(), conf, host, clientCapabilities, (byte)handshake.getDefaultCollation())).encode(this.writer, this.context);
            this.writer.flush();
            ConnectionHelper.authenticationHandler(credential, this.writer, this.reader, this.context);
            if ((clientCapabilities & 32L) != 0L) {
               this.assignStream(new CompressOutputStream((OutputStream)out, this.compressionSequence), new CompressInputStream((InputStream)in, this.compressionSequence), conf, handshake.getThreadId());
            }

            if (!skipPostCommands) {
               this.postConnectionQueries();
            }

            this.setSocketTimeout(conf.socketTimeout());
         }
      } catch (IOException var16) {
         this.destroySocket();
         String errorMsg = String.format("Could not connect to %s:%s : %s", host, this.socket.getPort(), var16.getMessage());
         if (host == null) {
            errorMsg = String.format("Could not connect to socket : %s", var16.getMessage());
         }

         throw this.exceptionFactory.create(errorMsg, "08000", var16);
      } catch (SQLException var17) {
         this.destroySocket();
         throw var17;
      }
   }

   private void assignStream(OutputStream out, InputStream in, Configuration conf, Long threadId) {
      this.writer = new PacketWriter(out, conf.maxQuerySizeToLog(), conf.maxAllowedPacket(), this.sequence, this.compressionSequence);
      this.writer.setServerThreadId(threadId, this.hostAddress);
      this.reader = new PacketReader(in, conf, this.sequence);
      this.reader.setServerThreadId(threadId, this.hostAddress);
   }

   protected void destroySocket() {
      this.closed = true;

      try {
         this.reader.close();
      } catch (IOException var4) {
      }

      try {
         this.writer.close();
      } catch (IOException var3) {
      }

      try {
         this.socket.close();
      } catch (IOException var2) {
      }

   }

   private String handleTimezone() throws SQLException {
      if (!"disable".equalsIgnoreCase(this.conf.timezone())) {
         String timeZone = null;

         try {
            Result res = (Result)this.execute(new QueryPacket("SELECT @@time_zone, @@system_time_zone"), true).get(0);
            res.next();
            timeZone = res.getString(1);
            if ("SYSTEM".equals(timeZone)) {
               timeZone = res.getString(2);
            }
         } catch (SQLException var5) {
            Result res = (Result)this.execute(new QueryPacket("SHOW VARIABLES WHERE Variable_name in ('system_time_zone','time_zone')"), true).get(0);
            String systemTimeZone = null;

            while(res.next()) {
               if ("system_time_zone".equals(res.getString(1))) {
                  systemTimeZone = res.getString(2);
               } else {
                  timeZone = res.getString(2);
               }
            }

            if ("SYSTEM".equals(timeZone)) {
               timeZone = systemTimeZone;
            }
         }

         return timeZone;
      } else {
         return null;
      }
   }

   private void postConnectionQueries() throws SQLException {
      List<String> commands = new ArrayList();
      List<String> galeraAllowedStates = this.conf.galeraAllowedState() == null ? Collections.emptyList() : Arrays.asList(this.conf.galeraAllowedState().split(","));
      if (this.hostAddress != null && Boolean.TRUE.equals(this.hostAddress.primary) && !galeraAllowedStates.isEmpty()) {
         commands.add("show status like 'wsrep_local_state'");
      }

      String serverTz = this.conf.timezone() != null ? this.handleTimezone() : null;
      String sessionVariableQuery = this.createSessionVariableQuery(serverTz, this.context);
      if (sessionVariableQuery != null) {
         commands.add(sessionVariableQuery);
      }

      if (this.hostAddress != null && !this.hostAddress.primary && this.context.getVersion().versionGreaterOrEqual(5, 6, 5)) {
         commands.add("SET SESSION TRANSACTION READ ONLY");
      }

      if (this.conf.database() != null && this.conf.createDatabaseIfNotExist() && (this.hostAddress == null || this.hostAddress.primary)) {
         String escapedDb = this.conf.database().replace("`", "``");
         commands.add(String.format("CREATE DATABASE IF NOT EXISTS `%s`", escapedDb));
         commands.add(String.format("USE `%s`", escapedDb));
      }

      if (this.context.getCharset() == null || !"utf8mb4".equals(this.context.getCharset())) {
         commands.add("SET NAMES utf8mb4");
      }

      if (this.conf.initSql() != null) {
         commands.add(this.conf.initSql());
      }

      if (this.conf.nonMappedOptions().containsKey("initSql")) {
         String[] initialCommands = this.conf.nonMappedOptions().get("initSql").toString().split(";");
         Collections.addAll(commands, initialCommands);
      }

      if (!commands.isEmpty()) {
         ResultSet rs;
         try {
            ClientMessage[] msgs = new ClientMessage[commands.size()];

            for(int i = 0; i < commands.size(); ++i) {
               msgs[i] = new QueryPacket((String)commands.get(i));
            }

            List<Completion> res = this.executePipeline(msgs, (Statement)null, 0, 0L, 1007, 1003, false, true);
            if (this.hostAddress != null && Boolean.TRUE.equals(this.hostAddress.primary) && !galeraAllowedStates.isEmpty()) {
               rs = (ResultSet)res.get(0);
               if (!rs.next()) {
                  throw this.exceptionFactory.create("fail to validate Galera state (unknown 'wsrep_local_state' state)");
               }

               if (!galeraAllowedStates.contains(rs.getString(2))) {
                  throw this.exceptionFactory.create(String.format("fail to validate Galera state (State is %s)", rs.getString(2)));
               }

               res.remove(0);
            }
         } catch (SQLException var8) {
            if (this.conf.timezone() != null && !"disable".equalsIgnoreCase(this.conf.timezone())) {
               throw this.exceptionFactory.create(String.format("Setting configured timezone '%s' fail on server.\nLook at https://mariadb.com/kb/en/mysql_tzinfo_to_sql/ to load tz data on server, or set timezone=disable to disable setting client timezone.", this.conf.timezone()), "HY000", var8);
            }

            throw this.exceptionFactory.create("Initialization command fail", "08000", var8);
         }

         if (this.conf.returnMultiValuesGeneratedIds()) {
            ClientMessage query = new QueryPacket("SELECT @@auto_increment_increment");
            List<Completion> res = this.execute(query, true);
            rs = (ResultSet)res.get(0);
            if (rs.next()) {
               this.context.setAutoIncrement(rs.getLong(1));
            }
         }
      }

   }

   public String createSessionVariableQuery(String serverTz, Context context) {
      List<String> sessionCommands = new ArrayList();
      boolean canRelyOnConnectionFlag = context.getVersion().isMariaDBServer() && (context.getVersion().versionFixedMajorMinorGreaterOrEqual(10, 4, 33) || context.getVersion().versionFixedMajorMinorGreaterOrEqual(10, 5, 24) || context.getVersion().versionFixedMajorMinorGreaterOrEqual(10, 6, 17) || context.getVersion().versionFixedMajorMinorGreaterOrEqual(10, 11, 7) || context.getVersion().versionFixedMajorMinorGreaterOrEqual(11, 0, 5) || context.getVersion().versionFixedMajorMinorGreaterOrEqual(11, 1, 4) || context.getVersion().versionFixedMajorMinorGreaterOrEqual(11, 2, 3));
      if (this.conf.autocommit() == null && (context.getServerStatus() & 2) == 0 || this.conf.autocommit() != null && !canRelyOnConnectionFlag || this.conf.autocommit() != null && canRelyOnConnectionFlag && (context.getServerStatus() & 2) > 0 != this.conf.autocommit()) {
         sessionCommands.add("autocommit=" + (this.conf.autocommit() != null && !this.conf.autocommit() ? "0" : "1"));
      }

      if (this.conf.returnMultiValuesGeneratedIds() && (context.getVersion().isMariaDBServer() && context.getVersion().versionGreaterOrEqual(10, 2, 2) || context.getVersion().versionGreaterOrEqual(5, 7, 0))) {
         sessionCommands.add("session_track_system_variables = CONCAT(@@global.session_track_system_variables,',auto_increment_increment')");
      }

      if (this.conf.sessionVariables() != null) {
         sessionCommands.add(Security.parseSessionVariables(this.conf.sessionVariables()));
      }

      if (this.conf.timezone() != null && !"disable".equalsIgnoreCase(this.conf.timezone())) {
         boolean mustSetTimezone = true;
         TimeZone connectionTz = "auto".equalsIgnoreCase(this.conf.timezone()) ? TimeZone.getDefault() : TimeZone.getTimeZone(ZoneId.of(this.conf.timezone()).normalized());
         ZoneId clientZoneId = connectionTz.toZoneId();

         try {
            ZoneId serverZoneId = ZoneId.of(serverTz);
            if (serverZoneId.normalized().equals(clientZoneId) || ZoneId.of(serverTz, ZoneId.SHORT_IDS).equals(clientZoneId)) {
               mustSetTimezone = false;
            }
         } catch (DateTimeException var9) {
         }

         if (mustSetTimezone) {
            if (clientZoneId.getRules().isFixedOffset()) {
               ZoneOffset zoneOffset = clientZoneId.getRules().getOffset(Instant.now());
               if (zoneOffset.getTotalSeconds() == 0) {
                  sessionCommands.add("time_zone='+00:00'");
               } else {
                  sessionCommands.add("time_zone='" + zoneOffset.getId() + "'");
               }
            } else {
               sessionCommands.add("time_zone='" + clientZoneId.normalized() + "'");
            }
         }
      }

      if (this.conf.transactionIsolation() != null) {
         int major = context.getVersion().getMajorVersion();
         if (context.getVersion().isMariaDBServer() || (major < 8 || !context.getVersion().versionGreaterOrEqual(8, 0, 3)) && (major >= 8 || !context.getVersion().versionGreaterOrEqual(5, 7, 20))) {
            sessionCommands.add("@@session.tx_isolation='" + this.conf.transactionIsolation().getValue() + "'");
         } else {
            sessionCommands.add("@@session.transaction_isolation='" + this.conf.transactionIsolation().getValue() + "'");
         }
      }

      return !sessionCommands.isEmpty() ? "set " + (String)sessionCommands.stream().collect(Collectors.joining(",")) : null;
   }

   public void setReadOnly(boolean readOnly) throws SQLException {
      if (this.closed) {
         throw new SQLNonTransientConnectionException("Connection is closed", "08000", 1220);
      }
   }

   public int sendQuery(ClientMessage message) throws SQLException {
      this.checkNotClosed();

      try {
         if (logger.isDebugEnabled() && message.description() != null) {
            logger.debug("execute query: {}", message.description());
         }

         return message.encode(this.writer, this.context);
      } catch (IOException var3) {
         if (var3 instanceof MaxAllowedPacketException) {
            if (((MaxAllowedPacketException)var3).isMustReconnect()) {
               this.destroySocket();
               throw this.exceptionFactory.withSql(message.description()).create("Packet too big for current server max_allowed_packet value", "08000", var3);
            } else {
               throw this.exceptionFactory.withSql(message.description()).create("Packet too big for current server max_allowed_packet value", "HZ000", var3);
            }
         } else {
            this.destroySocket();
            throw this.exceptionFactory.withSql(message.description()).create("Socket error", "08000", var3);
         }
      }
   }

   public List<Completion> execute(ClientMessage message, boolean canRedo) throws SQLException {
      return this.execute(message, (Statement)null, 0, 0L, 1007, 1003, false, canRedo);
   }

   public List<Completion> execute(ClientMessage message, Statement stmt, boolean canRedo) throws SQLException {
      return this.execute(message, stmt, 0, 0L, 1007, 1003, false, canRedo);
   }

   public List<Completion> executePipeline(ClientMessage[] messages, Statement stmt, int fetchSize, long maxRows, int resultSetConcurrency, int resultSetType, boolean closeOnCompletion, boolean canRedo) throws SQLException {
      List<Completion> results = new ArrayList();
      int perMsgCounter = 0;
      int readCounter = 0;
      int[] responseMsg = new int[messages.length];

      int perMsgCounter;
      try {
         if (this.disablePipeline) {
            for(readCounter = 0; readCounter < messages.length; ++readCounter) {
               results.addAll(this.execute(messages[readCounter], stmt, fetchSize, maxRows, resultSetConcurrency, resultSetType, closeOnCompletion, canRedo));
            }
         } else {
            for(int i = 0; i < messages.length; ++i) {
               responseMsg[i] = this.sendQuery(messages[i]);
            }

            while(readCounter < messages.length) {
               ++readCounter;

               for(perMsgCounter = 0; perMsgCounter < responseMsg[readCounter - 1]; ++perMsgCounter) {
                  results.addAll(this.readResponse(stmt, messages[readCounter - 1], fetchSize, maxRows, resultSetConcurrency, resultSetType, closeOnCompletion));
               }
            }
         }

         return results;
      } catch (SQLException var23) {
         int i;
         if (!this.closed) {
            results.add((Object)null);

            for(perMsgCounter = perMsgCounter + 1; perMsgCounter < responseMsg[readCounter - 1]; ++perMsgCounter) {
               try {
                  results.addAll(this.readResponse(stmt, messages[readCounter - 1], fetchSize, maxRows, resultSetConcurrency, resultSetType, closeOnCompletion));
               } catch (SQLException var22) {
               }
            }

            for(i = readCounter; i < messages.length; ++i) {
               for(int j = 0; j < responseMsg[i]; ++j) {
                  try {
                     results.addAll(this.readResponse(stmt, messages[i], fetchSize, maxRows, resultSetConcurrency, resultSetType, closeOnCompletion));
                  } catch (SQLException var21) {
                     results.add((Object)null);
                  }
               }
            }

            Iterator var25 = results.iterator();

            while(var25.hasNext()) {
               Completion result = (Completion)var25.next();
               if (result instanceof PrepareResultPacket && stmt instanceof ServerPreparedStatement) {
                  try {
                     ((PrepareResultPacket)result).decrementUse(this, (ServerPreparedStatement)stmt);
                  } catch (SQLException var20) {
                  }
               }
            }
         }

         i = 0;
         ClientMessage[] var27 = messages;
         int var17 = messages.length;

         for(int var18 = 0; var18 < var17; ++var18) {
            ClientMessage message = var27[var18];
            i += message.batchUpdateLength();
         }

         throw this.exceptionFactory.createBatchUpdate(results, i, responseMsg, var23);
      }
   }

   public List<Completion> execute(ClientMessage message, Statement stmt, int fetchSize, long maxRows, int resultSetConcurrency, int resultSetType, boolean closeOnCompletion, boolean canRedo) throws SQLException {
      int nbResp = this.sendQuery(message);
      if (nbResp == 1) {
         return this.readResponse(stmt, message, fetchSize, maxRows, resultSetConcurrency, resultSetType, closeOnCompletion);
      } else {
         if (this.streamStmt != null) {
            this.streamStmt.fetchRemaining();
            this.streamStmt = null;
         }

         ArrayList completions = new ArrayList();

         try {
            while(nbResp-- > 0) {
               this.readResults(stmt, message, completions, fetchSize, maxRows, resultSetConcurrency, resultSetType, closeOnCompletion);
            }

            return completions;
         } catch (SQLException var15) {
            while(nbResp-- > 0) {
               try {
                  this.readResults(stmt, message, completions, fetchSize, maxRows, resultSetConcurrency, resultSetType, closeOnCompletion);
               } catch (SQLException var14) {
               }
            }

            throw var15;
         }
      }
   }

   public List<Completion> readResponse(Statement stmt, ClientMessage message, int fetchSize, long maxRows, int resultSetConcurrency, int resultSetType, boolean closeOnCompletion) throws SQLException {
      this.checkNotClosed();
      if (this.streamStmt != null) {
         this.streamStmt.fetchRemaining();
         this.streamStmt = null;
      }

      List<Completion> completions = new ArrayList();
      this.readResults(stmt, message, completions, fetchSize, maxRows, resultSetConcurrency, resultSetType, closeOnCompletion);
      return completions;
   }

   public void readResponse(ClientMessage message) throws SQLException {
      this.checkNotClosed();
      if (this.streamStmt != null) {
         this.streamStmt.fetchRemaining();
         this.streamStmt = null;
      }

      List<Completion> completions = new ArrayList();
      this.readResults((Statement)null, message, completions, 0, 0L, 1007, 1003, false);
   }

   public void closePrepare(Prepare prepare) throws SQLException {
      this.checkNotClosed();

      try {
         (new ClosePreparePacket(prepare.getStatementId())).encode(this.writer, this.context);
      } catch (IOException var3) {
         this.destroySocket();
         throw this.exceptionFactory.create("Socket error during post connection queries: " + var3.getMessage(), "08000", var3);
      }
   }

   public void readStreamingResults(List<Completion> completions, int fetchSize, long maxRows, int resultSetConcurrency, int resultSetType, boolean closeOnCompletion) throws SQLException {
      if (this.streamStmt != null) {
         this.readResults(this.streamStmt, this.streamMsg, completions, fetchSize, maxRows, resultSetConcurrency, resultSetType, closeOnCompletion);
      }

   }

   private void readResults(Statement stmt, ClientMessage message, List<Completion> completions, int fetchSize, long maxRows, int resultSetConcurrency, int resultSetType, boolean closeOnCompletion) throws SQLException {
      completions.add(this.readPacket(stmt, message, fetchSize, maxRows, resultSetConcurrency, resultSetType, closeOnCompletion));

      while((this.context.getServerStatus() & 8) > 0) {
         completions.add(this.readPacket(stmt, message, fetchSize, maxRows, resultSetConcurrency, resultSetType, closeOnCompletion));
      }

   }

   public Completion readPacket(ClientMessage message) throws SQLException {
      return this.readPacket((Statement)null, message, 0, 0L, 1007, 1003, false);
   }

   public Completion readPacket(Statement stmt, ClientMessage message, int fetchSize, long maxRows, int resultSetConcurrency, int resultSetType, boolean closeOnCompletion) throws SQLException {
      try {
         boolean traceEnable = logger.isTraceEnabled();
         Completion completion = message.readPacket(stmt, fetchSize, maxRows, resultSetConcurrency, resultSetType, closeOnCompletion, this.reader, this.writer, this.context, this.exceptionFactory, this.lock, traceEnable, message);
         if (completion instanceof StreamingResult && !((StreamingResult)completion).loaded()) {
            this.streamStmt = stmt;
            this.streamMsg = message;
         }

         return completion;
      } catch (IOException var11) {
         this.destroySocket();
         throw this.exceptionFactory.withSql(message.description()).create(var11 instanceof SocketTimeoutException ? "Socket timout error" : "Socket error", "08000", var11);
      }
   }

   protected void checkNotClosed() throws SQLException {
      if (this.closed) {
         throw this.exceptionFactory.create("Connection is closed", "08000", 1220);
      }
   }

   private void closeSocket() {
      try {
         try {
            long maxCurrentMillis = System.currentTimeMillis() + 10L;
            this.socket.shutdownOutput();
            this.socket.setSoTimeout(3);
            InputStream is = this.socket.getInputStream();

            while(is.read() != -1 && System.currentTimeMillis() < maxCurrentMillis) {
            }
         } catch (Throwable var13) {
         }

         this.writer.close();
         this.reader.close();
      } catch (IOException var14) {
      } finally {
         try {
            this.socket.close();
         } catch (IOException var12) {
         }

      }

   }

   public boolean isClosed() {
      return this.closed;
   }

   public Context getContext() {
      return this.context;
   }

   public void abort(Executor executor) throws SQLException {
      if (executor == null) {
         throw this.exceptionFactory.create("Cannot abort the connection: null executor passed");
      } else {
         boolean lockStatus = this.lock.tryLock();
         if (!this.closed) {
            this.closed = true;
            logger.debug("aborting connection {}", this.context.getThreadId());
            if (!lockStatus) {
               try {
                  StandardClient cli = new StandardClient(this.conf, this.hostAddress, new ReentrantLock(), true);

                  try {
                     cli.execute(new QueryPacket("KILL " + this.context.getThreadId()), false);
                  } catch (Throwable var8) {
                     try {
                        cli.close();
                     } catch (Throwable var6) {
                        var8.addSuppressed(var6);
                     }

                     throw var8;
                  }

                  cli.close();
               } catch (SQLException var9) {
               }
            } else {
               try {
                  QuitPacket.INSTANCE.encode(this.writer, this.context);
               } catch (IOException var7) {
               }
            }

            if (this.streamStmt != null) {
               this.streamStmt.abort();
            }

            this.closeSocket();
         }

         if (lockStatus) {
            this.lock.unlock();
         }

      }
   }

   public int getSocketTimeout() {
      return this.socketTimeout;
   }

   public void setSocketTimeout(int milliseconds) throws SQLException {
      try {
         this.socketTimeout = milliseconds;
         this.socket.setSoTimeout(milliseconds);
      } catch (SocketException var3) {
         throw this.exceptionFactory.create("Cannot set the network timeout", "42000", var3);
      }
   }

   public void close() {
      boolean locked = this.lock.tryLock();
      if (!this.closed) {
         this.closed = true;

         try {
            QuitPacket.INSTANCE.encode(this.writer, this.context);
         } catch (IOException var3) {
         }

         this.closeSocket();
      }

      if (locked) {
         this.lock.unlock();
      }

   }

   public String getSocketIp() {
      return this.socket.getInetAddress() == null ? null : this.socket.getInetAddress().getHostAddress();
   }

   public boolean isPrimary() {
      return this.hostAddress.primary;
   }

   public ExceptionFactory getExceptionFactory() {
      return this.exceptionFactory;
   }

   public HostAddress getHostAddress() {
      return this.hostAddress;
   }

   public void reset() {
      this.context.resetStateFlag();
      this.context.resetPrepareCache();
   }
}
