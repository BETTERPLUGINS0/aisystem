package fr.xephi.authme.libs.org.postgresql.core.v3;

import fr.xephi.authme.libs.org.postgresql.PGProperty;
import fr.xephi.authme.libs.org.postgresql.core.ConnectionFactory;
import fr.xephi.authme.libs.org.postgresql.core.PGStream;
import fr.xephi.authme.libs.org.postgresql.core.QueryExecutor;
import fr.xephi.authme.libs.org.postgresql.core.ServerVersion;
import fr.xephi.authme.libs.org.postgresql.core.SetupQueryRunner;
import fr.xephi.authme.libs.org.postgresql.core.SocketFactoryFactory;
import fr.xephi.authme.libs.org.postgresql.core.Tuple;
import fr.xephi.authme.libs.org.postgresql.core.Utils;
import fr.xephi.authme.libs.org.postgresql.core.Version;
import fr.xephi.authme.libs.org.postgresql.gss.MakeGSS;
import fr.xephi.authme.libs.org.postgresql.hostchooser.CandidateHost;
import fr.xephi.authme.libs.org.postgresql.hostchooser.GlobalHostStatusTracker;
import fr.xephi.authme.libs.org.postgresql.hostchooser.HostChooser;
import fr.xephi.authme.libs.org.postgresql.hostchooser.HostChooserFactory;
import fr.xephi.authme.libs.org.postgresql.hostchooser.HostRequirement;
import fr.xephi.authme.libs.org.postgresql.hostchooser.HostStatus;
import fr.xephi.authme.libs.org.postgresql.jdbc.GSSEncMode;
import fr.xephi.authme.libs.org.postgresql.jdbc.SslMode;
import fr.xephi.authme.libs.org.postgresql.jre7.sasl.ScramAuthenticator;
import fr.xephi.authme.libs.org.postgresql.plugin.AuthenticationRequestType;
import fr.xephi.authme.libs.org.postgresql.ssl.MakeSSL;
import fr.xephi.authme.libs.org.postgresql.sspi.ISSPIClient;
import fr.xephi.authme.libs.org.postgresql.util.GT;
import fr.xephi.authme.libs.org.postgresql.util.HostSpec;
import fr.xephi.authme.libs.org.postgresql.util.MD5Digest;
import fr.xephi.authme.libs.org.postgresql.util.PSQLException;
import fr.xephi.authme.libs.org.postgresql.util.PSQLState;
import fr.xephi.authme.libs.org.postgresql.util.ServerErrorMessage;
import fr.xephi.authme.libs.org.postgresql.util.internal.Nullness;
import java.io.IOException;
import java.net.ConnectException;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import javax.net.SocketFactory;
import org.checkerframework.checker.nullness.qual.Nullable;

public class ConnectionFactoryImpl extends ConnectionFactory {
   private static final Logger LOGGER = Logger.getLogger(ConnectionFactoryImpl.class.getName());
   private static final int AUTH_REQ_OK = 0;
   private static final int AUTH_REQ_KRB4 = 1;
   private static final int AUTH_REQ_KRB5 = 2;
   private static final int AUTH_REQ_PASSWORD = 3;
   private static final int AUTH_REQ_CRYPT = 4;
   private static final int AUTH_REQ_MD5 = 5;
   private static final int AUTH_REQ_SCM = 6;
   private static final int AUTH_REQ_GSS = 7;
   private static final int AUTH_REQ_GSS_CONTINUE = 8;
   private static final int AUTH_REQ_SSPI = 9;
   private static final int AUTH_REQ_SASL = 10;
   private static final int AUTH_REQ_SASL_CONTINUE = 11;
   private static final int AUTH_REQ_SASL_FINAL = 12;
   private static final String IN_HOT_STANDBY = "in_hot_standby";

   private ISSPIClient createSSPI(PGStream pgStream, @Nullable String spnServiceClass, boolean enableNegotiate) {
      try {
         Class<ISSPIClient> c = Class.forName("fr.xephi.authme.libs.org.postgresql.sspi.SSPIClient");
         return (ISSPIClient)c.getDeclaredConstructor(PGStream.class, String.class, Boolean.TYPE).newInstance(pgStream, spnServiceClass, enableNegotiate);
      } catch (Exception var5) {
         throw new IllegalStateException("Unable to load org.postgresql.sspi.SSPIClient. Please check that SSPIClient is included in your pgjdbc distribution.", var5);
      }
   }

   private PGStream tryConnect(Properties info, SocketFactory socketFactory, HostSpec hostSpec, SslMode sslMode, GSSEncMode gssEncMode) throws SQLException, IOException {
      int connectTimeout = PGProperty.CONNECT_TIMEOUT.getInt(info) * 1000;
      String user = PGProperty.USER.getOrDefault(info);
      String database = PGProperty.PG_DBNAME.getOrDefault(info);
      if (user == null) {
         throw new PSQLException(GT.tr("User cannot be null"), PSQLState.INVALID_NAME);
      } else if (database == null) {
         throw new PSQLException(GT.tr("Database cannot be null"), PSQLState.INVALID_NAME);
      } else {
         PGStream newStream = new PGStream(socketFactory, hostSpec, connectTimeout);

         try {
            int socketTimeout = PGProperty.SOCKET_TIMEOUT.getInt(info);
            if (socketTimeout > 0) {
               newStream.setNetworkTimeout(socketTimeout * 1000);
            }

            String maxResultBuffer = PGProperty.MAX_RESULT_BUFFER.getOrDefault(info);
            newStream.setMaxResultBuffer(maxResultBuffer);
            boolean requireTCPKeepAlive = PGProperty.TCP_KEEP_ALIVE.getBoolean(info);
            newStream.getSocket().setKeepAlive(requireTCPKeepAlive);
            boolean requireTCPNoDelay = PGProperty.TCP_NO_DELAY.getBoolean(info);
            newStream.getSocket().setTcpNoDelay(requireTCPNoDelay);
            int receiveBufferSize = PGProperty.RECEIVE_BUFFER_SIZE.getInt(info);
            if (receiveBufferSize > -1) {
               if (receiveBufferSize > 0) {
                  newStream.getSocket().setReceiveBufferSize(receiveBufferSize);
               } else {
                  LOGGER.log(Level.WARNING, "Ignore invalid value for receiveBufferSize: {0}", receiveBufferSize);
               }
            }

            int sendBufferSize = PGProperty.SEND_BUFFER_SIZE.getInt(info);
            if (sendBufferSize > -1) {
               if (sendBufferSize > 0) {
                  newStream.getSocket().setSendBufferSize(sendBufferSize);
               } else {
                  LOGGER.log(Level.WARNING, "Ignore invalid value for sendBufferSize: {0}", sendBufferSize);
               }
            }

            if (LOGGER.isLoggable(Level.FINE)) {
               LOGGER.log(Level.FINE, "Receive Buffer Size is {0}", newStream.getSocket().getReceiveBufferSize());
               LOGGER.log(Level.FINE, "Send Buffer Size is {0}", newStream.getSocket().getSendBufferSize());
            }

            newStream = this.enableGSSEncrypted(newStream, gssEncMode, hostSpec.getHost(), info, connectTimeout);
            if (!newStream.isGssEncrypted()) {
               newStream = this.enableSSL(newStream, sslMode, info, connectTimeout);
            }

            if (socketTimeout > 0) {
               newStream.setNetworkTimeout(socketTimeout * 1000);
            }

            List<ConnectionFactoryImpl.StartupParam> paramList = this.getParametersForStartup(user, database, info);
            this.sendStartupPacket(newStream, paramList);
            this.doAuthentication(newStream, hostSpec.getHost(), user, info);
            return newStream;
         } catch (Exception var17) {
            this.closeStream(newStream);
            throw var17;
         }
      }
   }

   public QueryExecutor openConnectionImpl(HostSpec[] hostSpecs, Properties info) throws SQLException {
      SslMode sslMode = SslMode.of(info);
      GSSEncMode gssEncMode = GSSEncMode.of(info);
      String targetServerTypeStr = (String)Nullness.castNonNull(PGProperty.TARGET_SERVER_TYPE.getOrDefault(info));

      HostRequirement targetServerType;
      try {
         targetServerType = HostRequirement.getTargetServerType(targetServerTypeStr);
      } catch (IllegalArgumentException var21) {
         throw new PSQLException(GT.tr("Invalid targetServerType value: {0}", targetServerTypeStr), PSQLState.CONNECTION_UNABLE_TO_CONNECT);
      }

      SocketFactory socketFactory = SocketFactoryFactory.getSocketFactory(info);
      HostChooser hostChooser = HostChooserFactory.createHostChooser(hostSpecs, targetServerType, info);
      Iterator<CandidateHost> hostIter = hostChooser.iterator();
      HashMap knownStates = new HashMap();

      while(true) {
         while(hostIter.hasNext()) {
            CandidateHost candidateHost = (CandidateHost)hostIter.next();
            HostSpec hostSpec = candidateHost.hostSpec;
            LOGGER.log(Level.FINE, "Trying to establish a protocol version 3 connection to {0}", hostSpec);
            HostStatus knownStatus = (HostStatus)knownStates.get(hostSpec);
            if (knownStatus != null && !candidateHost.targetServerType.allowConnectingTo(knownStatus)) {
               if (LOGGER.isLoggable(Level.FINER)) {
                  LOGGER.log(Level.FINER, "Known status of host {0} is {1}, and required status was {2}. Will try next host", new Object[]{hostSpec, knownStatus, candidateHost.targetServerType});
               }
            } else {
               PGStream newStream = null;

               try {
                  try {
                     newStream = this.tryConnect(info, socketFactory, hostSpec, sslMode, gssEncMode);
                  } catch (SQLException var22) {
                     if (sslMode == SslMode.PREFER && PSQLState.INVALID_AUTHORIZATION_SPECIFICATION.getState().equals(var22.getSQLState())) {
                        SQLException ex = null;

                        try {
                           newStream = this.tryConnect(info, socketFactory, hostSpec, SslMode.DISABLE, gssEncMode);
                           LOGGER.log(Level.FINE, "Downgraded to non-encrypted connection for host {0}", hostSpec);
                        } catch (IOException | SQLException var20) {
                           ex = var20;
                        }

                        if (ex != null) {
                           log(Level.FINE, "sslMode==PREFER, however non-SSL connection failed as well", ex);
                           var22.addSuppressed(ex);
                           throw var22;
                        }
                     } else {
                        if (sslMode != SslMode.ALLOW || !PSQLState.INVALID_AUTHORIZATION_SPECIFICATION.getState().equals(var22.getSQLState())) {
                           throw var22;
                        }

                        Object ex = null;

                        try {
                           newStream = this.tryConnect(info, socketFactory, hostSpec, SslMode.REQUIRE, gssEncMode);
                           LOGGER.log(Level.FINE, "Upgraded to encrypted connection for host {0}", hostSpec);
                        } catch (SQLException var18) {
                           ex = var18;
                        } catch (IOException var19) {
                           ex = var19;
                        }

                        if (ex != null) {
                           log(Level.FINE, "sslMode==ALLOW, however SSL connection failed as well", (Throwable)ex);
                           var22.addSuppressed((Throwable)ex);
                           throw var22;
                        }
                     }
                  }

                  int cancelSignalTimeout = PGProperty.CANCEL_SIGNAL_TIMEOUT.getInt(info) * 1000;
                  Nullness.castNonNull(newStream);
                  QueryExecutor queryExecutor = new QueryExecutorImpl(newStream, cancelSignalTimeout, info);
                  HostStatus hostStatus = HostStatus.ConnectOK;
                  if (candidateHost.targetServerType != HostRequirement.any) {
                     hostStatus = this.isPrimary(queryExecutor) ? HostStatus.Primary : HostStatus.Secondary;
                  }

                  GlobalHostStatusTracker.reportHostStatus(hostSpec, hostStatus);
                  knownStates.put(hostSpec, hostStatus);
                  if (candidateHost.targetServerType.allowConnectingTo(hostStatus)) {
                     this.runInitialQueries(queryExecutor, info);
                     return queryExecutor;
                  }

                  queryExecutor.close();
               } catch (ConnectException var23) {
                  GlobalHostStatusTracker.reportHostStatus(hostSpec, HostStatus.ConnectFail);
                  knownStates.put(hostSpec, HostStatus.ConnectFail);
                  if (!hostIter.hasNext()) {
                     throw new PSQLException(GT.tr("Connection to {0} refused. Check that the hostname and port are correct and that the postmaster is accepting TCP/IP connections.", hostSpec), PSQLState.CONNECTION_UNABLE_TO_CONNECT, var23);
                  }

                  log(Level.FINE, "ConnectException occurred while connecting to {0}", var23, hostSpec);
               } catch (IOException var24) {
                  this.closeStream(newStream);
                  GlobalHostStatusTracker.reportHostStatus(hostSpec, HostStatus.ConnectFail);
                  knownStates.put(hostSpec, HostStatus.ConnectFail);
                  if (!hostIter.hasNext()) {
                     throw new PSQLException(GT.tr("The connection attempt failed."), PSQLState.CONNECTION_UNABLE_TO_CONNECT, var24);
                  }

                  log(Level.FINE, "IOException occurred while connecting to {0}", var24, hostSpec);
               } catch (SQLException var25) {
                  this.closeStream(newStream);
                  GlobalHostStatusTracker.reportHostStatus(hostSpec, HostStatus.ConnectFail);
                  knownStates.put(hostSpec, HostStatus.ConnectFail);
                  if (!hostIter.hasNext()) {
                     throw var25;
                  }

                  log(Level.FINE, "SQLException occurred while connecting to {0}", var25, hostSpec);
               }
            }
         }

         throw new PSQLException(GT.tr("Could not find a server with specified targetServerType: {0}", targetServerType), PSQLState.CONNECTION_UNABLE_TO_CONNECT);
      }
   }

   private List<ConnectionFactoryImpl.StartupParam> getParametersForStartup(String user, String database, Properties info) {
      List<ConnectionFactoryImpl.StartupParam> paramList = new ArrayList();
      paramList.add(new ConnectionFactoryImpl.StartupParam("user", user));
      paramList.add(new ConnectionFactoryImpl.StartupParam("database", database));
      paramList.add(new ConnectionFactoryImpl.StartupParam("client_encoding", "UTF8"));
      paramList.add(new ConnectionFactoryImpl.StartupParam("DateStyle", "ISO"));
      paramList.add(new ConnectionFactoryImpl.StartupParam("TimeZone", createPostgresTimeZone()));
      Version assumeVersion = ServerVersion.from(PGProperty.ASSUME_MIN_SERVER_VERSION.getOrDefault(info));
      String replication;
      if (assumeVersion.getVersionNum() >= ServerVersion.v9_0.getVersionNum()) {
         paramList.add(new ConnectionFactoryImpl.StartupParam("extra_float_digits", "3"));
         replication = PGProperty.APPLICATION_NAME.getOrDefault(info);
         if (replication != null) {
            paramList.add(new ConnectionFactoryImpl.StartupParam("application_name", replication));
         }
      } else {
         paramList.add(new ConnectionFactoryImpl.StartupParam("extra_float_digits", "2"));
      }

      replication = PGProperty.REPLICATION.getOrDefault(info);
      if (replication != null && assumeVersion.getVersionNum() >= ServerVersion.v9_4.getVersionNum()) {
         paramList.add(new ConnectionFactoryImpl.StartupParam("replication", replication));
      }

      String currentSchema = PGProperty.CURRENT_SCHEMA.getOrDefault(info);
      if (currentSchema != null) {
         paramList.add(new ConnectionFactoryImpl.StartupParam("search_path", currentSchema));
      }

      String options = PGProperty.OPTIONS.getOrDefault(info);
      if (options != null) {
         paramList.add(new ConnectionFactoryImpl.StartupParam("options", options));
      }

      return paramList;
   }

   private static void log(Level level, String msg, Throwable thrown, Object... params) {
      if (LOGGER.isLoggable(level)) {
         LogRecord rec = new LogRecord(level, msg);
         rec.setLoggerName(LOGGER.getName());
         rec.setParameters(params);
         rec.setThrown(thrown);
         LOGGER.log(rec);
      }
   }

   private static String createPostgresTimeZone() {
      String tz = TimeZone.getDefault().getID();
      if (tz.length() > 3 && tz.startsWith("GMT")) {
         char sign = tz.charAt(3);
         String start;
         switch(sign) {
         case '+':
            start = "GMT-";
            break;
         case '-':
            start = "GMT+";
            break;
         default:
            return tz;
         }

         return start + tz.substring(4);
      } else {
         return tz;
      }
   }

   private PGStream enableGSSEncrypted(PGStream pgStream, GSSEncMode gssEncMode, String host, Properties info, int connectTimeout) throws IOException, PSQLException {
      if (gssEncMode == GSSEncMode.DISABLE) {
         return pgStream;
      } else if (gssEncMode == GSSEncMode.ALLOW) {
         return pgStream;
      } else {
         String user = PGProperty.USER.getOrDefault(info);
         if (user == null) {
            throw new PSQLException("GSSAPI encryption required but was impossible user is null", PSQLState.CONNECTION_REJECTED);
         } else {
            LOGGER.log(Level.FINEST, " FE=> GSSENCRequest");
            int gssTimeout = PGProperty.SSL_RESPONSE_TIMEOUT.getInt(info);
            int currentTimeout = pgStream.getNetworkTimeout();
            if (currentTimeout > 0 && currentTimeout < gssTimeout) {
               gssTimeout = currentTimeout;
            }

            pgStream.setNetworkTimeout(gssTimeout);
            pgStream.sendInteger4(8);
            pgStream.sendInteger2(1234);
            pgStream.sendInteger2(5680);
            pgStream.flush();
            int beresp = pgStream.receiveChar();
            pgStream.setNetworkTimeout(currentTimeout);
            switch(beresp) {
            case 69:
               LOGGER.log(Level.FINEST, " <=BE GSSEncrypted Error");
               if (gssEncMode.requireEncryption()) {
                  throw new PSQLException(GT.tr("The server does not support GSS Encoding."), PSQLState.CONNECTION_REJECTED);
               }

               pgStream.close();
               return new PGStream(pgStream.getSocketFactory(), pgStream.getHostSpec(), connectTimeout);
            case 71:
               LOGGER.log(Level.FINEST, " <=BE GSSEncryptedOk");

               try {
                  AuthenticationPluginManager.withPassword(AuthenticationRequestType.GSS, info, (password) -> {
                     MakeGSS.authenticate(true, pgStream, host, user, password, PGProperty.JAAS_APPLICATION_NAME.getOrDefault(info), PGProperty.KERBEROS_SERVER_NAME.getOrDefault(info), false, PGProperty.JAAS_LOGIN.getBoolean(info), PGProperty.LOG_SERVER_ERROR_DETAIL.getBoolean(info));
                     return Void.TYPE;
                  });
                  return pgStream;
               } catch (PSQLException var11) {
                  if (gssEncMode == GSSEncMode.PREFER) {
                     return new PGStream(pgStream, connectTimeout);
                  }
               }
            default:
               throw new PSQLException(GT.tr("An error occurred while setting up the GSS Encoded connection."), PSQLState.PROTOCOL_VIOLATION);
            case 78:
               LOGGER.log(Level.FINEST, " <=BE GSSEncrypted Refused");
               if (gssEncMode.requireEncryption()) {
                  throw new PSQLException(GT.tr("The server does not support GSS Encryption."), PSQLState.CONNECTION_REJECTED);
               } else {
                  return pgStream;
               }
            }
         }
      }
   }

   private PGStream enableSSL(PGStream pgStream, SslMode sslMode, Properties info, int connectTimeout) throws IOException, PSQLException {
      if (sslMode == SslMode.DISABLE) {
         return pgStream;
      } else if (sslMode == SslMode.ALLOW) {
         return pgStream;
      } else {
         LOGGER.log(Level.FINEST, " FE=> SSLRequest");
         int sslTimeout = PGProperty.SSL_RESPONSE_TIMEOUT.getInt(info);
         int currentTimeout = pgStream.getNetworkTimeout();
         if (currentTimeout > 0 && currentTimeout < sslTimeout) {
            sslTimeout = currentTimeout;
         }

         pgStream.setNetworkTimeout(sslTimeout);
         pgStream.sendInteger4(8);
         pgStream.sendInteger2(1234);
         pgStream.sendInteger2(5679);
         pgStream.flush();
         int beresp = pgStream.receiveChar();
         pgStream.setNetworkTimeout(currentTimeout);
         switch(beresp) {
         case 69:
            LOGGER.log(Level.FINEST, " <=BE SSLError");
            if (sslMode.requireEncryption()) {
               throw new PSQLException(GT.tr("The server does not support SSL."), PSQLState.CONNECTION_REJECTED);
            }

            return new PGStream(pgStream, connectTimeout);
         case 78:
            LOGGER.log(Level.FINEST, " <=BE SSLRefused");
            if (sslMode.requireEncryption()) {
               throw new PSQLException(GT.tr("The server does not support SSL."), PSQLState.CONNECTION_REJECTED);
            }

            return pgStream;
         case 83:
            LOGGER.log(Level.FINEST, " <=BE SSLOk");
            MakeSSL.convert(pgStream, info);
            return pgStream;
         default:
            throw new PSQLException(GT.tr("An error occurred while setting up the SSL connection."), PSQLState.PROTOCOL_VIOLATION);
         }
      }
   }

   private void sendStartupPacket(PGStream pgStream, List<ConnectionFactoryImpl.StartupParam> params) throws IOException {
      if (LOGGER.isLoggable(Level.FINEST)) {
         StringBuilder details = new StringBuilder();

         for(int i = 0; i < params.size(); ++i) {
            if (i != 0) {
               details.append(", ");
            }

            details.append(((ConnectionFactoryImpl.StartupParam)params.get(i)).toString());
         }

         LOGGER.log(Level.FINEST, " FE=> StartupPacket({0})", details);
      }

      int length = 8;
      byte[][] encodedParams = new byte[params.size() * 2][];

      for(int i = 0; i < params.size(); ++i) {
         encodedParams[i * 2] = ((ConnectionFactoryImpl.StartupParam)params.get(i)).getEncodedKey();
         encodedParams[i * 2 + 1] = ((ConnectionFactoryImpl.StartupParam)params.get(i)).getEncodedValue();
         length += encodedParams[i * 2].length + 1 + encodedParams[i * 2 + 1].length + 1;
      }

      ++length;
      pgStream.sendInteger4(length);
      pgStream.sendInteger2(3);
      pgStream.sendInteger2(0);
      byte[][] var11 = encodedParams;
      int var6 = encodedParams.length;

      for(int var7 = 0; var7 < var6; ++var7) {
         byte[] encodedParam = var11[var7];
         pgStream.send(encodedParam);
         pgStream.sendChar(0);
      }

      pgStream.sendChar(0);
      pgStream.flush();
   }

   private void doAuthentication(PGStream pgStream, String host, String user, Properties info) throws IOException, SQLException {
      ISSPIClient sspiClient = null;
      ScramAuthenticator scramAuthenticator = null;

      try {
         while(true) {
            int beresp = pgStream.receiveChar();
            switch(beresp) {
            case 69:
               int elen = pgStream.receiveInteger4();
               ServerErrorMessage errorMsg = new ServerErrorMessage(pgStream.receiveErrorString(elen - 4));
               LOGGER.log(Level.FINEST, " <=BE ErrorMessage({0})", errorMsg);
               throw new PSQLException(errorMsg, PGProperty.LOG_SERVER_ERROR_DETAIL.getBoolean(info));
            case 82:
               int msgLen = pgStream.receiveInteger4();
               int areq = pgStream.receiveInteger4();
               switch(areq) {
               case 0:
                  LOGGER.log(Level.FINEST, " <=BE AuthenticationOk");
                  return;
               case 1:
               case 2:
               case 4:
               case 6:
               default:
                  LOGGER.log(Level.FINEST, " <=BE AuthenticationReq (unsupported type {0})", areq);
                  throw new PSQLException(GT.tr("The authentication type {0} is not supported. Check that you have configured the pg_hba.conf file to include the client''s IP address or subnet, and that it is using an authentication scheme supported by the driver.", areq), PSQLState.CONNECTION_REJECTED);
               case 3:
                  LOGGER.log(Level.FINEST, "<=BE AuthenticationReqPassword");
                  LOGGER.log(Level.FINEST, " FE=> Password(password=<not shown>)");
                  AuthenticationPluginManager.withEncodedPassword(AuthenticationRequestType.CLEARTEXT_PASSWORD, info, (encodedPassword) -> {
                     pgStream.sendChar(112);
                     pgStream.sendInteger4(4 + encodedPassword.length + 1);
                     pgStream.send(encodedPassword);
                     return Void.TYPE;
                  });
                  pgStream.sendChar(0);
                  pgStream.flush();
                  continue;
               case 5:
                  byte[] md5Salt = pgStream.receive(4);
                  if (LOGGER.isLoggable(Level.FINEST)) {
                     LOGGER.log(Level.FINEST, " <=BE AuthenticationReqMD5(salt={0})", Utils.toHexString(md5Salt));
                  }

                  byte[] digest = (byte[])AuthenticationPluginManager.withEncodedPassword(AuthenticationRequestType.MD5_PASSWORD, info, (encodedPassword) -> {
                     return MD5Digest.encode(user.getBytes(StandardCharsets.UTF_8), encodedPassword, md5Salt);
                  });
                  if (LOGGER.isLoggable(Level.FINEST)) {
                     LOGGER.log(Level.FINEST, " FE=> Password(md5digest={0})", new String(digest, StandardCharsets.US_ASCII));
                  }

                  try {
                     pgStream.sendChar(112);
                     pgStream.sendInteger4(4 + digest.length + 1);
                     pgStream.send(digest);
                  } finally {
                     Arrays.fill(digest, (byte)0);
                  }

                  pgStream.sendChar(0);
                  pgStream.flush();
                  continue;
               case 7:
               case 9:
                  String gsslib = PGProperty.GSS_LIB.getOrDefault(info);
                  boolean usespnego = PGProperty.USE_SPNEGO.getBoolean(info);
                  boolean useSSPI = false;
                  if ("gssapi".equals(gsslib)) {
                     LOGGER.log(Level.FINE, "Using JSSE GSSAPI, param gsslib=gssapi");
                  } else if (areq == 7 && !"sspi".equals(gsslib)) {
                     LOGGER.log(Level.FINE, "Using JSSE GSSAPI, gssapi requested by server and gsslib=sspi not forced");
                  } else {
                     sspiClient = this.createSSPI(pgStream, PGProperty.SSPI_SERVICE_CLASS.getOrDefault(info), areq == 9 || areq == 7 && usespnego);
                     useSSPI = sspiClient.isSSPISupported();
                     LOGGER.log(Level.FINE, "SSPI support detected: {0}", useSSPI);
                     if (!useSSPI) {
                        sspiClient = null;
                        if ("sspi".equals(gsslib)) {
                           throw new PSQLException("SSPI forced with gsslib=sspi, but SSPI not available; set loglevel=2 for details", PSQLState.CONNECTION_UNABLE_TO_CONNECT);
                        }
                     }

                     if (LOGGER.isLoggable(Level.FINE)) {
                        LOGGER.log(Level.FINE, "Using SSPI: {0}, gsslib={1} and SSPI support detected", new Object[]{useSSPI, gsslib});
                     }
                  }

                  if (useSSPI) {
                     ((ISSPIClient)Nullness.castNonNull(sspiClient)).startSSPI();
                  } else {
                     AuthenticationPluginManager.withPassword(AuthenticationRequestType.GSS, info, (password) -> {
                        MakeGSS.authenticate(false, pgStream, host, user, password, PGProperty.JAAS_APPLICATION_NAME.getOrDefault(info), PGProperty.KERBEROS_SERVER_NAME.getOrDefault(info), usespnego, PGProperty.JAAS_LOGIN.getBoolean(info), PGProperty.LOG_SERVER_ERROR_DETAIL.getBoolean(info));
                        return Void.TYPE;
                     });
                  }
                  continue;
               case 8:
                  ((ISSPIClient)Nullness.castNonNull(sspiClient)).continueSSPI(msgLen - 8);
                  continue;
               case 10:
                  LOGGER.log(Level.FINEST, " <=BE AuthenticationSASL");
                  scramAuthenticator = (ScramAuthenticator)AuthenticationPluginManager.withPassword(AuthenticationRequestType.SASL, info, (password) -> {
                     if (password == null) {
                        throw new PSQLException(GT.tr("The server requested SCRAM-based authentication, but no password was provided."), PSQLState.CONNECTION_REJECTED);
                     } else if (password.length == 0) {
                        throw new PSQLException(GT.tr("The server requested SCRAM-based authentication, but the password is an empty string."), PSQLState.CONNECTION_REJECTED);
                     } else {
                        return new ScramAuthenticator(user, String.valueOf(password), pgStream);
                     }
                  });
                  scramAuthenticator.processServerMechanismsAndInit();
                  scramAuthenticator.sendScramClientFirstMessage();
                  continue;
               case 11:
                  ((ScramAuthenticator)Nullness.castNonNull(scramAuthenticator)).processServerFirstMessage(msgLen - 4 - 4);
                  continue;
               case 12:
                  ((ScramAuthenticator)Nullness.castNonNull(scramAuthenticator)).verifyServerSignature(msgLen - 4 - 4);
                  continue;
               }
            default:
               throw new PSQLException(GT.tr("Protocol error.  Session setup failed."), PSQLState.PROTOCOL_VIOLATION);
            }
         }
      } finally {
         if (sspiClient != null) {
            try {
               sspiClient.dispose();
            } catch (RuntimeException var24) {
               LOGGER.log(Level.FINE, "Unexpected error during SSPI context disposal", var24);
            }
         }

      }
   }

   private void runInitialQueries(QueryExecutor queryExecutor, Properties info) throws SQLException {
      String assumeMinServerVersion = PGProperty.ASSUME_MIN_SERVER_VERSION.getOrDefault(info);
      if (Utils.parseServerVersionStr(assumeMinServerVersion) < ServerVersion.v9_0.getVersionNum()) {
         int dbVersion = queryExecutor.getServerVersionNum();
         if (PGProperty.GROUP_STARTUP_PARAMETERS.getBoolean(info) && dbVersion >= ServerVersion.v9_0.getVersionNum()) {
            SetupQueryRunner.run(queryExecutor, "BEGIN", false);
         }

         if (dbVersion >= ServerVersion.v9_0.getVersionNum()) {
            SetupQueryRunner.run(queryExecutor, "SET extra_float_digits = 3", false);
         }

         String appName = PGProperty.APPLICATION_NAME.getOrDefault(info);
         if (appName != null && dbVersion >= ServerVersion.v9_0.getVersionNum()) {
            StringBuilder sql = new StringBuilder();
            sql.append("SET application_name = '");
            Utils.escapeLiteral(sql, appName, queryExecutor.getStandardConformingStrings());
            sql.append("'");
            SetupQueryRunner.run(queryExecutor, sql.toString(), false);
         }

         if (PGProperty.GROUP_STARTUP_PARAMETERS.getBoolean(info) && dbVersion >= ServerVersion.v9_0.getVersionNum()) {
            SetupQueryRunner.run(queryExecutor, "COMMIT", false);
         }

      }
   }

   private boolean isPrimary(QueryExecutor queryExecutor) throws SQLException, IOException {
      String inHotStandby = queryExecutor.getParameterStatus("in_hot_standby");
      if ("on".equalsIgnoreCase(inHotStandby)) {
         return false;
      } else {
         Tuple results = SetupQueryRunner.run(queryExecutor, "show transaction_read_only", true);
         Tuple nonNullResults = (Tuple)Nullness.castNonNull(results);
         String queriedTransactionReadonly = queryExecutor.getEncoding().decode((byte[])Nullness.castNonNull(nonNullResults.get(0)));
         return "off".equalsIgnoreCase(queriedTransactionReadonly);
      }
   }

   private static class StartupParam {
      private final String key;
      private final String value;

      StartupParam(String key, String value) {
         this.key = key;
         this.value = value;
      }

      public String toString() {
         return this.key + "=" + this.value;
      }

      public byte[] getEncodedKey() {
         return this.key.getBytes(StandardCharsets.UTF_8);
      }

      public byte[] getEncodedValue() {
         return this.value.getBytes(StandardCharsets.UTF_8);
      }
   }
}
