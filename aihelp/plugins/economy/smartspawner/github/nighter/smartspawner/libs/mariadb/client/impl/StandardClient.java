package github.nighter.smartspawner.libs.mariadb.client.impl;

import github.nighter.smartspawner.libs.mariadb.Configuration;
import github.nighter.smartspawner.libs.mariadb.HostAddress;
import github.nighter.smartspawner.libs.mariadb.ServerPreparedStatement;
import github.nighter.smartspawner.libs.mariadb.Statement;
import github.nighter.smartspawner.libs.mariadb.client.Client;
import github.nighter.smartspawner.libs.mariadb.client.Completion;
import github.nighter.smartspawner.libs.mariadb.client.Context;
import github.nighter.smartspawner.libs.mariadb.client.ReadableByteBuf;
import github.nighter.smartspawner.libs.mariadb.client.context.BaseContext;
import github.nighter.smartspawner.libs.mariadb.client.context.RedoContext;
import github.nighter.smartspawner.libs.mariadb.client.result.Result;
import github.nighter.smartspawner.libs.mariadb.client.result.StreamingResult;
import github.nighter.smartspawner.libs.mariadb.client.socket.Reader;
import github.nighter.smartspawner.libs.mariadb.client.socket.Writer;
import github.nighter.smartspawner.libs.mariadb.client.socket.impl.CompressInputStream;
import github.nighter.smartspawner.libs.mariadb.client.socket.impl.CompressOutputStream;
import github.nighter.smartspawner.libs.mariadb.client.socket.impl.PacketReader;
import github.nighter.smartspawner.libs.mariadb.client.socket.impl.PacketWriter;
import github.nighter.smartspawner.libs.mariadb.client.socket.impl.ReadAheadBufferedStream;
import github.nighter.smartspawner.libs.mariadb.client.socket.impl.UnixDomainSocket;
import github.nighter.smartspawner.libs.mariadb.client.tls.MariaDbX509EphemeralTrustingManager;
import github.nighter.smartspawner.libs.mariadb.client.util.ClosableLock;
import github.nighter.smartspawner.libs.mariadb.client.util.MutableByte;
import github.nighter.smartspawner.libs.mariadb.export.ExceptionFactory;
import github.nighter.smartspawner.libs.mariadb.export.MaxAllowedPacketException;
import github.nighter.smartspawner.libs.mariadb.export.Prepare;
import github.nighter.smartspawner.libs.mariadb.export.SslMode;
import github.nighter.smartspawner.libs.mariadb.message.ClientMessage;
import github.nighter.smartspawner.libs.mariadb.message.client.ClosePreparePacket;
import github.nighter.smartspawner.libs.mariadb.message.client.HandshakeResponse;
import github.nighter.smartspawner.libs.mariadb.message.client.QueryPacket;
import github.nighter.smartspawner.libs.mariadb.message.client.QuitPacket;
import github.nighter.smartspawner.libs.mariadb.message.client.SslRequestPacket;
import github.nighter.smartspawner.libs.mariadb.message.server.AuthSwitchPacket;
import github.nighter.smartspawner.libs.mariadb.message.server.ErrorPacket;
import github.nighter.smartspawner.libs.mariadb.message.server.InitialHandshakePacket;
import github.nighter.smartspawner.libs.mariadb.message.server.OkPacket;
import github.nighter.smartspawner.libs.mariadb.message.server.PrepareResultPacket;
import github.nighter.smartspawner.libs.mariadb.plugin.AuthenticationPlugin;
import github.nighter.smartspawner.libs.mariadb.plugin.AuthenticationPluginFactory;
import github.nighter.smartspawner.libs.mariadb.plugin.Credential;
import github.nighter.smartspawner.libs.mariadb.plugin.CredentialPlugin;
import github.nighter.smartspawner.libs.mariadb.plugin.TlsSocketPlugin;
import github.nighter.smartspawner.libs.mariadb.plugin.authentication.AuthenticationPluginLoader;
import github.nighter.smartspawner.libs.mariadb.plugin.authentication.addon.ClearPasswordPlugin;
import github.nighter.smartspawner.libs.mariadb.plugin.authentication.standard.NativePasswordPlugin;
import github.nighter.smartspawner.libs.mariadb.plugin.tls.TlsSocketPluginLoader;
import github.nighter.smartspawner.libs.mariadb.util.IPUtility;
import github.nighter.smartspawner.libs.mariadb.util.Security;
import github.nighter.smartspawner.libs.mariadb.util.StringUtils;
import github.nighter.smartspawner.libs.mariadb.util.log.Logger;
import github.nighter.smartspawner.libs.mariadb.util.log.Loggers;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLNonTransientConnectionException;
import java.sql.SQLTimeoutException;
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
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.net.ssl.SNIHostName;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManager;

public class StandardClient implements Client, AutoCloseable {
   private static final Logger logger = Loggers.getLogger(StandardClient.class);
   protected final ExceptionFactory exceptionFactory;
   private static final Pattern REDIRECT_PATTERN = Pattern.compile("(mariadb|mysql):\\/\\/(([^/@:]+)?(:([^/]+))?@)?(([^/:]+)(:([0-9]+))?)(\\/([^?]+)(\\?(.*))?)?$", 34);
   private Socket socket;
   private final MutableByte sequence = new MutableByte();
   private final MutableByte compressionSequence = new MutableByte();
   private final ClosableLock lock;
   private Configuration conf;
   private AuthenticationPlugin authPlugin;
   private HostAddress hostAddress;
   private final boolean disablePipeline;
   protected Context context;
   protected Writer writer;
   private boolean closed = false;
   private Reader reader;
   private byte[] certFingerprint = null;
   private Statement streamStmt = null;
   private ClientMessage streamMsg = null;
   private int socketTimeout;
   private final Consumer<String> redirectConsumer = this::redirect;

   public StandardClient(Configuration conf, HostAddress hostAddress, ClosableLock lock, boolean skipPostCommands) throws SQLException {
      this.conf = conf;
      this.lock = lock;
      this.hostAddress = hostAddress;
      this.exceptionFactory = new ExceptionFactory(conf, hostAddress);
      this.disablePipeline = conf.disablePipeline();
      this.socketTimeout = conf.socketTimeout();
      this.socket = ConnectionHelper.connectSocket(conf, hostAddress);

      try {
         this.setupConnection(skipPostCommands);
      } catch (SQLException var6) {
         this.handleConnectionError(var6);
      } catch (SocketTimeoutException var7) {
         this.handleTimeoutError(var7);
      } catch (IOException var8) {
         this.handleIOError(var8);
      }

   }

   private void setupConnection(boolean skipPostCommands) throws SQLException, IOException {
      OutputStream out = this.socket.getOutputStream();
      InputStream in = this.conf.useReadAheadInput() ? new ReadAheadBufferedStream(this.socket.getInputStream()) : new BufferedInputStream(this.socket.getInputStream(), 16384);
      this.assignStream((OutputStream)out, (InputStream)in, this.conf, (Long)null);
      this.configureTimeout();
      InitialHandshakePacket handshake = this.handleServerHandshake();
      long clientCapabilities = this.setupClientCapabilities(handshake);
      SSLSocket sslSocket = this.handleSSLConnection(handshake, clientCapabilities);
      if (sslSocket != null) {
         out = new BufferedOutputStream(sslSocket.getOutputStream(), 16384);
         in = this.conf.useReadAheadInput() ? new ReadAheadBufferedStream(sslSocket.getInputStream()) : new BufferedInputStream(sslSocket.getInputStream(), 16384);
         this.assignStream((OutputStream)out, (InputStream)in, this.conf, handshake.getThreadId());
      }

      this.handleAuthentication(handshake, clientCapabilities);
      this.setupCompression((InputStream)in, (OutputStream)out, clientCapabilities, handshake.getThreadId());
      if (!skipPostCommands) {
         this.postConnectionQueries();
      }

      this.setSocketTimeout(this.conf.socketTimeout());
   }

   private void setupCompression(InputStream in, OutputStream out, long clientCapabilities, long threadId) {
      if ((clientCapabilities & 32L) != 0L) {
         this.assignStream(new CompressOutputStream(out, this.compressionSequence), new CompressInputStream(in, this.compressionSequence), this.conf, threadId);
      }

   }

   private SSLSocket handleSSLConnection(InitialHandshakePacket handshake, long clientCapabilities) throws SQLException, IOException {
      this.updateThreadIds(handshake);
      Configuration conf = this.context.getConf();
      SslMode sslMode = this.determineSslMode(conf);
      if (sslMode == SslMode.DISABLE) {
         return null;
      } else {
         this.validateServerSslCapability();
         this.sendSslRequest(handshake, clientCapabilities);
         TlsSocketPlugin socketPlugin = TlsSocketPluginLoader.get(conf.tlsSocketType());
         TrustManager[] trustManagers = socketPlugin.getTrustManager(conf, this.context.getExceptionFactory(), this.hostAddress);
         SSLSocket sslSocket = this.createSslSocket(conf, socketPlugin, trustManagers);
         this.configureSslSocket(sslSocket, conf);
         this.handleSslHandshake(sslSocket, trustManagers);
         if (this.requiresHostnameVerification(sslMode)) {
            this.verifyHostname(sslSocket, socketPlugin);
         }

         return sslSocket;
      }
   }

   private void updateThreadIds(InitialHandshakePacket handshake) {
      this.reader.setServerThreadId(handshake.getThreadId(), this.hostAddress);
      this.writer.setServerThreadId(handshake.getThreadId(), this.hostAddress);
   }

   private SslMode determineSslMode(Configuration conf) {
      return this.hostAddress.sslMode == null ? conf.sslMode() : this.hostAddress.sslMode;
   }

   private void validateServerSslCapability() throws SQLException {
      if (!this.context.hasServerCapability(2048L)) {
         throw this.context.getExceptionFactory().create("Trying to connect with ssl, but ssl not enabled in the server", "08000");
      }
   }

   private void sendSslRequest(InitialHandshakePacket handshake, long clientCapabilities) throws IOException {
      SslRequestPacket.create(clientCapabilities | 2048L, (byte)handshake.getDefaultCollation()).encode(this.writer, this.context);
   }

   private SSLSocket createSslSocket(Configuration conf, TlsSocketPlugin socketPlugin, TrustManager[] trustManagers) throws SQLException, IOException {
      try {
         SSLContext sslContext = SSLContext.getInstance("TLS");
         sslContext.init(socketPlugin.getKeyManager(conf, this.context.getExceptionFactory()), trustManagers, (SecureRandom)null);
         return socketPlugin.createSocket(this.socket, sslContext.getSocketFactory());
      } catch (KeyManagementException var5) {
         throw this.context.getExceptionFactory().create("Could not initialize SSL context", "08000", var5);
      } catch (NoSuchAlgorithmException var6) {
         throw this.context.getExceptionFactory().create("SSLContext TLS Algorithm not unknown", "08000", var6);
      }
   }

   private void configureSslSocket(SSLSocket sslSocket, Configuration conf) throws SQLException {
      ConnectionHelper.enabledSslProtocolSuites(sslSocket, conf);
      ConnectionHelper.enabledSslCipherSuites(sslSocket, conf);
      sslSocket.setUseClientMode(true);
   }

   private void handleSslHandshake(SSLSocket sslSocket, TrustManager[] trustManagers) throws IOException {
      if (this.hostAddress != null && !IPUtility.isInetAddress(this.hostAddress.host)) {
         SSLParameters params = sslSocket.getSSLParameters();
         SNIHostName serverName = new SNIHostName(this.hostAddress.host);
         params.setServerNames(Collections.singletonList(serverName));
         sslSocket.setSSLParameters(params);
      }

      sslSocket.startHandshake();
      if (trustManagers.length > 0 && trustManagers[0] instanceof MariaDbX509EphemeralTrustingManager) {
         this.certFingerprint = ((MariaDbX509EphemeralTrustingManager)trustManagers[0]).getFingerprint();
      }

   }

   private boolean requiresHostnameVerification(SslMode sslMode) {
      return sslMode == SslMode.VERIFY_FULL && this.certFingerprint == null && this.hostAddress.host != null;
   }

   private void verifyHostname(SSLSocket sslSocket, TlsSocketPlugin socketPlugin) throws SQLException {
      try {
         socketPlugin.verify(this.hostAddress.host, sslSocket.getSession(), this.context.getThreadId());
      } catch (SSLException var4) {
         throw this.context.getExceptionFactory().create("SSL hostname verification failed : " + var4.getMessage() + "\nThis verification can be disabled using the sslMode to VERIFY_CA but won't prevent man-in-the-middle attacks anymore", "08006");
      }
   }

   private void configureTimeout() throws SQLException {
      if (this.conf.connectTimeout() > 0) {
         this.setSocketTimeout(this.conf.connectTimeout());
      } else if (this.conf.socketTimeout() > 0) {
         this.setSocketTimeout(this.conf.socketTimeout());
      }

   }

   private InitialHandshakePacket handleServerHandshake() throws SQLException, IOException {
      ReadableByteBuf buf = this.reader.readReusablePacket(logger.isTraceEnabled());
      if (buf.getByte() == -1) {
         this.throwHandshakeError(buf);
      }

      return InitialHandshakePacket.decode(buf);
   }

   private void throwHandshakeError(ReadableByteBuf buf) throws SQLException {
      ErrorPacket errorPacket = new ErrorPacket(buf, (Context)null);
      throw this.exceptionFactory.create(errorPacket.getMessage(), errorPacket.getSqlState(), errorPacket.getErrorCode());
   }

   private long setupClientCapabilities(InitialHandshakePacket handshake) {
      this.exceptionFactory.setThreadId(handshake.getThreadId());
      long capabilities = ConnectionHelper.initializeClientCapabilities(this.conf, handshake.getCapabilities(), this.hostAddress);
      this.initializeContext(handshake, capabilities);
      this.reader.setServerThreadId(handshake.getThreadId(), this.hostAddress);
      this.writer.setServerThreadId(handshake.getThreadId(), this.hostAddress);
      return capabilities;
   }

   private void initializeContext(InitialHandshakePacket handshake, long clientCapabilities) {
      PrepareCache cache = this.conf.cachePrepStmts() ? new PrepareCache(this.conf.prepStmtCacheSize(), this) : null;
      Boolean isLoopback = null;
      if (this.socket.getInetAddress() != null) {
         isLoopback = this.socket.getInetAddress().isLoopbackAddress();
      }

      this.context = (Context)(this.conf.transactionReplay() ? new RedoContext(this.hostAddress, handshake, clientCapabilities, this.conf, this.exceptionFactory, cache, isLoopback) : new BaseContext(this.hostAddress, handshake, clientCapabilities, this.conf, this.exceptionFactory, cache, isLoopback));
   }

   private void handleAuthentication(InitialHandshakePacket handshake, long clientCapabilities) throws IOException, SQLException {
      String authType = this.determineAuthType(handshake);
      Credential credential = ConnectionHelper.loadCredential(this.conf.credentialPlugin(), this.conf, this.hostAddress);
      this.sendHandshakeResponse(handshake, clientCapabilities, credential, authType);
      this.createAuthPlugin(handshake, credential, authType);
      this.writer.flush();
      this.authenticationHandler(credential, this.hostAddress);
   }

   private String determineAuthType(InitialHandshakePacket handshake) {
      String authType = handshake.getAuthenticationPluginType();
      CredentialPlugin credPlugin = this.conf.credentialPlugin();
      if (credPlugin != null && credPlugin.defaultAuthenticationPluginType() != null) {
         authType = credPlugin.defaultAuthenticationPluginType();
      }

      return authType;
   }

   private void handleConnectionError(SQLException e) throws SQLException {
      this.destroySocket();
      throw e;
   }

   private void handleTimeoutError(SocketTimeoutException e) throws SQLTimeoutException {
      this.destroySocket();
      throw new SQLTimeoutException(String.format("Socket timeout when connecting to %s. %s", this.hostAddress, e.getMessage()), "08000", e);
   }

   private void handleIOError(IOException e) throws SQLException {
      this.destroySocket();
      throw this.exceptionFactory.create(String.format("Could not connect to %s : %s", this.hostAddress, e.getMessage()), "08000", e);
   }

   private void sendHandshakeResponse(InitialHandshakePacket handshake, long clientCapabilities, Credential credential, String authType) throws IOException {
      (new HandshakeResponse(credential, authType, this.context.getSeed(), this.conf, this.hostAddress.host, clientCapabilities, (byte)handshake.getDefaultCollation())).encode(this.writer, this.context);
   }

   private void createAuthPlugin(InitialHandshakePacket handshake, Credential credential, String authType) {
      this.authPlugin = (AuthenticationPlugin)("mysql_clear_password".equals(authType) ? new ClearPasswordPlugin(credential.getPassword(), this.hostAddress, this.conf) : new NativePasswordPlugin(credential.getPassword(), handshake.getSeed()));
   }

   public void authenticationHandler(Credential credential, HostAddress hostAddress) throws IOException, SQLException {
      this.writer.permitTrace(true);
      Configuration conf = this.context.getConf();
      ReadableByteBuf buf = this.reader.readReusablePacket();

      while(true) {
         switch(buf.getByte() & 255) {
         case 0:
            label59: {
               OkPacket okPacket = OkPacket.parseWithInfo(buf, this.context);
               if (this.certFingerprint != null) {
                  if (this.socket instanceof UnixDomainSocket) {
                     break label59;
                  }

                  if (!this.authPlugin.isMitMProof() || credential.getPassword() == null || credential.getPassword().isEmpty() || !validateFingerPrint(this.authPlugin, okPacket.getInfo(), this.certFingerprint, credential, this.context.getSeed())) {
                     throw this.context.getExceptionFactory().create("Self signed certificates. Either set sslMode=trust, use password with a MitM-Proof authentication plugin or provide server certificate to client", "08000");
                  }
               }

               if (this.context.getRedirectUrl() != null && (conf.permitRedirect() == null && conf.sslMode() == SslMode.VERIFY_FULL || conf.permitRedirect())) {
                  this.redirect(this.context.getRedirectUrl());
               }
            }

            this.writer.permitTrace(true);
            return;
         case 254:
            AuthSwitchPacket authSwitchPacket = AuthSwitchPacket.decode(buf);
            AuthenticationPluginFactory authPluginFactory = AuthenticationPluginLoader.get(authSwitchPacket.getPlugin(), conf);
            if (authPluginFactory.requireSsl() && !this.context.hasClientCapability(2048L)) {
               throw this.context.getExceptionFactory().create("Cannot use authentication plugin " + authPluginFactory.type() + " if SSL is not enabled.", "08000");
            }

            this.authPlugin = authPluginFactory.initialize(credential.getPassword(), authSwitchPacket.getSeed(), conf, hostAddress);
            if (this.certFingerprint != null && (!this.authPlugin.isMitMProof() || credential.getPassword() == null || credential.getPassword().isEmpty())) {
               throw this.context.getExceptionFactory().create(String.format("Cannot use authentication plugin %s with a Self signed certificates. Either set sslMode=trust, use password with a MitM-Proof authentication plugin or provide server certificate to client", authPluginFactory.type()));
            }

            buf = this.authPlugin.process(this.writer, this.reader, this.context, this.certFingerprint != null);
            break;
         case 255:
            ErrorPacket errorPacket = new ErrorPacket(buf, this.context);
            throw this.context.getExceptionFactory().create(errorPacket.getMessage(), errorPacket.getSqlState(), errorPacket.getErrorCode());
         default:
            throw this.context.getExceptionFactory().create("unexpected data during authentication (header=" + buf.getUnsignedByte(), "08000");
         }
      }
   }

   private static boolean validateFingerPrint(AuthenticationPlugin authPlugin, byte[] validationHash, byte[] fingerPrint, Credential credential, byte[] seed) {
      if (validationHash.length == 0) {
         return false;
      } else {
         try {
            assert validationHash[0] == 1;

            byte[] hash = authPlugin.hash(credential);
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(hash);
            messageDigest.update(seed);
            messageDigest.update(fingerPrint);
            byte[] digest = messageDigest.digest();
            String hashHex = StringUtils.byteArrayToHexString(digest);
            String serverValidationHex = new String(validationHash, 1, validationHash.length - 1, StandardCharsets.US_ASCII);
            return hashHex.equals(serverValidationHex);
         } catch (NoSuchAlgorithmException var10) {
            throw new IllegalStateException("SHA-256 MessageDigest expected to be not available", var10);
         }
      }
   }

   public void redirect(String redirectUrl) {
      if (redirectUrl == null || (this.conf.permitRedirect() != null || this.conf.sslMode() != SslMode.VERIFY_FULL) && !this.conf.permitRedirect()) {
         this.context.setRedirectUrl((String)null);
      } else if ((this.context.getServerStatus() & 1) == 0) {
         this.context.setRedirectUrl((String)null);
         Matcher matcher = REDIRECT_PATTERN.matcher(redirectUrl);
         if (!matcher.matches()) {
            logger.error("error parsing redirection string '" + redirectUrl + "'. format must be 'mariadb/mysql://[<user>[:<password>]@]<host>[:<port>]/[<db>[?<opt1>=<value1>[&<opt2>=<value2>]]]'");
            return;
         }

         try {
            String redirectHost = matcher.group(7) != null ? URLDecoder.decode(matcher.group(7), "utf8") : matcher.group(6);
            int redirectPort = matcher.group(9) != null ? Integer.parseInt(matcher.group(9)) : 3306;
            if (this.getHostAddress() != null && redirectHost.equals(this.getHostAddress().host) && redirectPort == this.getHostAddress().port) {
               return;
            }

            String redirectUser = matcher.group(3);
            String redirectPwd = matcher.group(5);
            Configuration.Builder redirectConfBuilder = this.context.getConf().toBuilder().addresses(HostAddress.from(redirectHost, redirectPort, true));
            if (redirectUser != null) {
               redirectConfBuilder.user(redirectUser);
            }

            if (redirectPwd != null) {
               redirectConfBuilder.password(redirectPwd);
            }

            try {
               Configuration redirectConf = redirectConfBuilder.build();
               HostAddress redirectHostAddress = (HostAddress)redirectConf.addresses().get(0);
               StandardClient redirectClient = new StandardClient(redirectConf, redirectHostAddress, this.lock, false);
               this.close();
               logger.info("redirecting connection " + this.hostAddress + " to " + redirectUrl);
               this.closed = false;
               this.socket = redirectClient.socket;
               this.conf = redirectConf;
               this.hostAddress = redirectHostAddress;
               this.context = redirectClient.context;
               this.writer = redirectClient.writer;
               this.reader = redirectClient.reader;
            } catch (SQLException var11) {
               logger.error("fail to redirect to '" + redirectUrl + "'");
            }
         } catch (UnsupportedEncodingException var12) {
         }
      } else {
         this.context.setRedirectUrl(redirectUrl);
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

   private void handleTimezone() throws SQLException {
      if (this.conf.connectionTimeZone() != null && !"LOCAL".equalsIgnoreCase(this.conf.connectionTimeZone())) {
         String zoneId = this.conf.connectionTimeZone();
         if ("SERVER".equalsIgnoreCase(zoneId)) {
            try {
               Result res = (Result)this.execute(new QueryPacket("SELECT @@time_zone, @@system_time_zone"), true).get(0);
               res.next();
               zoneId = res.getString(1);
               if ("SYSTEM".equals(zoneId)) {
                  zoneId = res.getString(2);
               }
            } catch (SQLException var7) {
               Result res = (Result)this.execute(new QueryPacket("SHOW VARIABLES WHERE Variable_name in ('system_time_zone','time_zone')"), true).get(0);
               String systemTimeZone = null;

               while(res.next()) {
                  if ("system_time_zone".equals(res.getString(1))) {
                     systemTimeZone = res.getString(2);
                  } else {
                     zoneId = res.getString(2);
                  }
               }

               if ("SYSTEM".equals(zoneId)) {
                  zoneId = systemTimeZone;
               }
            }
         }

         try {
            this.context.setConnectionTimeZone(TimeZone.getTimeZone(ZoneId.of(zoneId).normalized()));
         } catch (DateTimeException var6) {
            try {
               this.context.setConnectionTimeZone(TimeZone.getTimeZone(ZoneId.of(zoneId, ZoneId.SHORT_IDS).normalized()));
            } catch (DateTimeException var5) {
               throw new SQLException(String.format("Unknown zoneId %s", zoneId), var6);
            }
         }
      } else {
         this.context.setConnectionTimeZone(TimeZone.getDefault());
      }

   }

   private void postConnectionQueries() throws SQLException {
      List<String> commands = new ArrayList(8);
      List<String> galeraAllowedStates = this.conf.galeraAllowedState() == null ? Collections.emptyList() : Arrays.asList(this.conf.galeraAllowedState().split(","));
      if (this.hostAddress != null && Boolean.TRUE.equals(this.hostAddress.primary) && !galeraAllowedStates.isEmpty()) {
         commands.add("show status like 'wsrep_local_state'");
      }

      this.handleTimezone();
      String sessionVariableQuery = this.createSessionVariableQuery(this.context);
      if (sessionVariableQuery != null) {
         commands.add(sessionVariableQuery);
      }

      if (this.conf.database() != null && this.conf.createDatabaseIfNotExist() && (this.hostAddress == null || this.hostAddress.primary)) {
         String escapedDb = this.conf.database().replace("`", "``");
         commands.add(String.format("CREATE DATABASE IF NOT EXISTS `%s`", escapedDb));
         commands.add(String.format("USE `%s`", escapedDb));
      }

      if (this.conf.initSql() != null && !this.conf.initSql().isEmpty()) {
         String[] initialCommands = this.conf.initSql().split(";");
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
         } catch (SQLException var7) {
            if (this.conf.disconnectOnExpiredPasswords() || var7.getErrorCode() != 1862 && var7.getErrorCode() != 1820) {
               if (this.conf.timezone() != null && !"disable".equalsIgnoreCase(this.conf.timezone())) {
                  throw this.exceptionFactory.create(String.format("Setting configured timezone '%s' fail on server.\nLook at https://mariadb.com/kb/en/mysql_tzinfo_to_sql/ to load tz data on server, or set timezone=disable to disable setting client timezone.", this.conf.timezone()), "HY000", var7);
               }

               throw this.exceptionFactory.create("Initialization command fail", "08000", var7);
            }

            logger.info("connected in sandbox mode. only password change is permitted");
            return;
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

   public String createSessionVariableQuery(Context context) {
      List<String> sessionCommands = new ArrayList(8);
      this.addAutoCommitCommand(context, sessionCommands);
      this.addTruncationCommand(sessionCommands);
      this.addSessionTrackingCommand(context, sessionCommands);
      this.addTimeZoneCommand(context, sessionCommands);
      this.addTransactionIsolationCommand(context, sessionCommands);
      this.addReadOnlyCommand(context, sessionCommands);
      this.addCharsetCommand(context, sessionCommands);
      this.addCustomSessionVariables(sessionCommands);
      return this.buildFinalQuery(sessionCommands);
   }

   private void addAutoCommitCommand(Context context, List<String> commands) {
      boolean canRelyOnConnectionFlag = this.isReliableConnectionFlag(context);
      if (this.isAutoCommitUpdateRequired(context, canRelyOnConnectionFlag)) {
         boolean autoCommitValue = this.conf.autocommit() == null || this.conf.autocommit();
         commands.add("autocommit=" + (autoCommitValue ? "1" : "0"));
      }

   }

   private boolean isReliableConnectionFlag(Context context) {
      return context.getVersion().isMariaDBServer() && (context.getVersion().versionFixedMajorMinorGreaterOrEqual(10, 4, 33) || context.getVersion().versionFixedMajorMinorGreaterOrEqual(10, 5, 24) || context.getVersion().versionFixedMajorMinorGreaterOrEqual(10, 6, 17) || context.getVersion().versionFixedMajorMinorGreaterOrEqual(10, 11, 7) || context.getVersion().versionFixedMajorMinorGreaterOrEqual(11, 0, 5) || context.getVersion().versionFixedMajorMinorGreaterOrEqual(11, 1, 4) || context.getVersion().versionFixedMajorMinorGreaterOrEqual(11, 2, 3));
   }

   private boolean isAutoCommitUpdateRequired(Context context, boolean canRelyOnConnectionFlag) {
      return this.conf.autocommit() == null && (context.getServerStatus() & 2) == 0 || this.conf.autocommit() != null && !canRelyOnConnectionFlag || this.conf.autocommit() != null && canRelyOnConnectionFlag && (context.getServerStatus() & 2) > 0 != this.conf.autocommit();
   }

   private void addTruncationCommand(List<String> commands) {
      if (this.conf.jdbcCompliantTruncation()) {
         commands.add("sql_mode=CONCAT(@@sql_mode,',STRICT_TRANS_TABLES')");
      }

   }

   private void addSessionTrackingCommand(Context context, List<String> commands) {
      if (this.isSessionTrackingSupported(context)) {
         StringBuilder concatValues = (new StringBuilder(",")).append(context.canUseTransactionIsolation() ? "transaction_isolation" : "tx_isolation");
         if (this.conf.returnMultiValuesGeneratedIds()) {
            concatValues.append(",auto_increment_increment");
         }

         commands.add("session_track_system_variables = CONCAT(@@global.session_track_system_variables,'" + concatValues + "')");
      }
   }

   private boolean isSessionTrackingSupported(Context context) {
      return context.hasClientCapability(8388608L) && (context.getVersion().isMariaDBServer() && context.getVersion().versionGreaterOrEqual(10, 2, 2) || context.getVersion().versionGreaterOrEqual(5, 7, 0));
   }

   private void addTimeZoneCommand(Context context, List<String> commands) {
      if (Boolean.TRUE.equals(this.conf.forceConnectionTimeZoneToSession())) {
         TimeZone connectionTz = context.getConnectionTimeZone();
         ZoneId connectionZoneId = connectionTz.toZoneId();
         if (!"SERVER".equalsIgnoreCase(this.conf.connectionTimeZone()) || !connectionZoneId.normalized().equals(TimeZone.getDefault().toZoneId())) {
            if (connectionZoneId.getRules().isFixedOffset()) {
               this.addFixedOffsetTimeZone(connectionZoneId, commands);
            } else {
               commands.add("time_zone='" + connectionZoneId.normalized() + "'");
            }

         }
      }
   }

   private void addFixedOffsetTimeZone(ZoneId connectionZoneId, List<String> commands) {
      ZoneOffset zoneOffset = connectionZoneId.getRules().getOffset(Instant.now());
      if (zoneOffset.getTotalSeconds() == 0) {
         commands.add("time_zone='+00:00'");
      } else {
         commands.add("time_zone='" + zoneOffset.getId() + "'");
      }

   }

   private void addTransactionIsolationCommand(Context context, List<String> commands) {
      if (this.conf.transactionIsolation() != null) {
         String isolationVariable = context.canUseTransactionIsolation() ? "transaction_isolation" : "tx_isolation";
         commands.add(String.format("@@session.%s='%s'", isolationVariable, this.conf.transactionIsolation().getValue()));
      }

   }

   private void addReadOnlyCommand(Context context, List<String> commands) {
      if (this.hostAddress != null && !this.hostAddress.primary && context.getVersion().versionGreaterOrEqual(5, 6, 5)) {
         String readOnlyVariable = context.canUseTransactionIsolation() ? "transaction_read_only" : "tx_read_only";
         commands.add(String.format("@@session.%s=1", readOnlyVariable));
      }

   }

   private void addCharsetCommand(Context context, List<String> commands) {
      if (!this.isDefaultCharsetSufficient(context)) {
         StringBuilder charsetCommand = new StringBuilder("NAMES utf8mb4");
         if (this.conf.connectionCollation() != null) {
            charsetCommand.append(" COLLATE ").append(this.conf.connectionCollation());
         }

         commands.add(charsetCommand.toString());
      }

   }

   private boolean isDefaultCharsetSufficient(Context context) {
      return context.getCharset() != null && "utf8mb4".equals(context.getCharset()) && this.conf.connectionCollation() == null;
   }

   private void addCustomSessionVariables(List<String> commands) {
      if (this.conf.sessionVariables() != null) {
         commands.add(Security.parseSessionVariables(this.conf.sessionVariables()));
      }

   }

   private String buildFinalQuery(List<String> commands) {
      return commands.isEmpty() ? null : "set " + String.join(",", commands);
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
      } catch (MaxAllowedPacketException var3) {
         if (var3.isMustReconnect()) {
            this.destroySocket();
            throw this.exceptionFactory.withSql(message.description()).create("Packet too big for current server max_allowed_packet value", "08000", var3);
         } else {
            throw this.exceptionFactory.withSql(message.description()).create("Packet too big for current server max_allowed_packet value", "HZ000", var3);
         }
      } catch (IOException var4) {
         this.destroySocket();
         throw this.exceptionFactory.withSql(message.description()).create("Socket error", "08000", var4);
      }
   }

   public List<Completion> execute(ClientMessage message, boolean canRedo) throws SQLException {
      return this.execute(message, (Statement)null, 0, 0L, 1007, 1003, false, canRedo);
   }

   public List<Completion> execute(ClientMessage message, Statement stmt, boolean canRedo) throws SQLException {
      return this.execute(message, stmt, 0, 0L, 1007, 1003, false, canRedo);
   }

   public List<Completion> executePipeline(ClientMessage[] messages, Statement stmt, int fetchSize, long maxRows, int resultSetConcurrency, int resultSetType, boolean closeOnCompletion, boolean canRedo) throws SQLException {
      List<Completion> results = new ArrayList(messages.length);
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

            for(perMsgCounter = perMsgCounter + 1; readCounter > 0 && perMsgCounter < responseMsg[readCounter - 1]; ++perMsgCounter) {
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

         ArrayList completions = new ArrayList(1);

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

      List<Completion> completions = new ArrayList(1);
      this.readResults(stmt, message, completions, fetchSize, maxRows, resultSetConcurrency, resultSetType, closeOnCompletion);
      return completions;
   }

   public void readResponse(ClientMessage message) throws SQLException {
      this.checkNotClosed();
      if (this.streamStmt != null) {
         this.streamStmt.fetchRemaining();
         this.streamStmt = null;
      }

      List<Completion> completions = new ArrayList(1);
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
         Completion completion = message.readPacket(stmt, fetchSize, maxRows, resultSetConcurrency, resultSetType, closeOnCompletion, this.reader, this.writer, this.context, this.exceptionFactory, this.lock, traceEnable, message, this.redirectConsumer);
         if (completion instanceof StreamingResult && !((StreamingResult)completion).loaded()) {
            this.streamStmt = stmt;
            this.streamMsg = message;
         }

         return completion;
      } catch (SocketTimeoutException var11) {
         this.destroySocket();
         throw this.exceptionFactory.withSql(message.description()).create("Socket timout error", "08000", var11);
      } catch (IOException var12) {
         this.destroySocket();
         throw this.exceptionFactory.withSql(message.description()).create("Socket error", "08000", var12);
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
                  StandardClient cli = new StandardClient(this.conf, this.hostAddress, new ClosableLock(), true);

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
