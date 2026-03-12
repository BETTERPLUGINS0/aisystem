package fr.xephi.authme.libs.org.mariadb.jdbc.client.impl;

import fr.xephi.authme.libs.org.mariadb.jdbc.Configuration;
import fr.xephi.authme.libs.org.mariadb.jdbc.HostAddress;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.Context;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.ReadableByteBuf;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.SocketHelper;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.socket.Reader;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.socket.Writer;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.socket.impl.SocketHandlerFunction;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.socket.impl.SocketUtility;
import fr.xephi.authme.libs.org.mariadb.jdbc.export.SslMode;
import fr.xephi.authme.libs.org.mariadb.jdbc.message.client.SslRequestPacket;
import fr.xephi.authme.libs.org.mariadb.jdbc.message.server.AuthSwitchPacket;
import fr.xephi.authme.libs.org.mariadb.jdbc.message.server.ErrorPacket;
import fr.xephi.authme.libs.org.mariadb.jdbc.message.server.OkPacket;
import fr.xephi.authme.libs.org.mariadb.jdbc.plugin.AuthenticationPlugin;
import fr.xephi.authme.libs.org.mariadb.jdbc.plugin.Credential;
import fr.xephi.authme.libs.org.mariadb.jdbc.plugin.CredentialPlugin;
import fr.xephi.authme.libs.org.mariadb.jdbc.plugin.TlsSocketPlugin;
import fr.xephi.authme.libs.org.mariadb.jdbc.plugin.authentication.AuthenticationPluginLoader;
import fr.xephi.authme.libs.org.mariadb.jdbc.plugin.tls.TlsSocketPluginLoader;
import fr.xephi.authme.libs.org.mariadb.jdbc.util.ConfigurableSocketFactory;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.sql.SQLException;
import java.sql.SQLNonTransientConnectionException;
import java.util.Arrays;
import java.util.List;
import javax.net.SocketFactory;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

public final class ConnectionHelper {
   private static final SocketHandlerFunction socketHandler;

   public static Socket createSocket(Configuration conf, HostAddress hostAddress) throws IOException, SQLException {
      return socketHandler.apply(conf, hostAddress);
   }

   public static Socket standardSocket(Configuration conf, HostAddress hostAddress) throws IOException, SQLException {
      String socketFactoryName = conf.socketFactory();
      SocketFactory socketFactory;
      if (socketFactoryName != null) {
         if (hostAddress == null) {
            throw new SQLException("hostname must be set to connect socket");
         } else {
            try {
               Class<SocketFactory> socketFactoryClass = Class.forName(socketFactoryName, false, ConnectionHelper.class.getClassLoader());
               if (!SocketFactory.class.isAssignableFrom(socketFactoryClass)) {
                  throw new IOException("Wrong Socket factory implementation '" + conf.socketFactory() + "'");
               } else {
                  Constructor<? extends SocketFactory> constructor = socketFactoryClass.getConstructor();
                  socketFactory = (SocketFactory)constructor.newInstance();
                  if (socketFactory instanceof ConfigurableSocketFactory) {
                     ((ConfigurableSocketFactory)socketFactory).setConfiguration(conf, hostAddress.host);
                  }

                  return socketFactory.createSocket();
               }
            } catch (Exception var6) {
               throw new IOException("Socket factory failed to initialized with option \"socketFactory\" set to \"" + conf.socketFactory() + "\"", var6);
            }
         }
      } else {
         socketFactory = SocketFactory.getDefault();
         return socketFactory.createSocket();
      }
   }

   public static Socket connectSocket(Configuration conf, HostAddress hostAddress) throws SQLException {
      try {
         if (conf.pipe() == null && conf.localSocket() == null && hostAddress == null) {
            throw new SQLException("hostname must be set to connect socket if not using local socket or pipe");
         } else {
            Socket socket = createSocket(conf, hostAddress);
            SocketHelper.setSocketOption(conf, socket);
            if (!socket.isConnected()) {
               InetSocketAddress sockAddr = conf.pipe() == null && conf.localSocket() == null ? new InetSocketAddress(hostAddress.host, hostAddress.port) : null;
               socket.connect(sockAddr, conf.connectTimeout());
            }

            return socket;
         }
      } catch (IOException var4) {
         throw new SQLNonTransientConnectionException(String.format("Socket fail to connect to host:%s. %s", hostAddress == null ? conf.localSocket() : hostAddress, var4.getMessage()), "08000", var4);
      }
   }

   public static long initializeClientCapabilities(Configuration configuration, long serverCapabilities, HostAddress hostAddress) {
      long capabilities = 12493568L;
      if (configuration.useServerPrepStmts() && Boolean.parseBoolean(configuration.nonMappedOptions().getProperty("enableSkipMeta", "true"))) {
         capabilities |= 68719476736L;
      }

      if (Boolean.parseBoolean(configuration.nonMappedOptions().getProperty("interactiveClient", "false"))) {
         capabilities |= 1024L;
      }

      if (configuration.useBulkStmts() || configuration.useBulkStmtsForInserts()) {
         capabilities |= 17179869184L;
      }

      if (!configuration.useAffectedRows()) {
         capabilities |= 2L;
      }

      if (configuration.allowMultiQueries()) {
         capabilities |= 65536L;
      }

      if (configuration.allowLocalInfile()) {
         capabilities |= 128L;
      }

      boolean extendedTypeInfo = Boolean.parseBoolean(configuration.nonMappedOptions().getProperty("extendedTypeInfo", "true"));
      if (extendedTypeInfo) {
         capabilities |= 34359738368L;
      }

      boolean deprecateEof = Boolean.parseBoolean(configuration.nonMappedOptions().getProperty("deprecateEof", "true"));
      if (deprecateEof) {
         capabilities |= 16777216L;
      }

      if (configuration.useCompression()) {
         capabilities |= 32L;
      }

      if (configuration.database() != null && (!configuration.createDatabaseIfNotExist() || configuration.createDatabaseIfNotExist() && hostAddress != null && !hostAddress.primary)) {
         capabilities |= 8L;
      }

      if (configuration.sslMode() != SslMode.DISABLE) {
         capabilities |= 2048L;
      }

      return capabilities & serverCapabilities;
   }

   public static void authenticationHandler(Credential credential, Writer writer, Reader reader, Context context) throws IOException, SQLException {
      writer.permitTrace(true);
      Configuration conf = context.getConf();
      ReadableByteBuf buf = reader.readReusablePacket();

      while(true) {
         switch(buf.getByte() & 255) {
         case 0:
            new OkPacket(buf, context);
            writer.permitTrace(true);
            return;
         case 254:
            AuthSwitchPacket authSwitchPacket = AuthSwitchPacket.decode(buf);
            AuthenticationPlugin authenticationPlugin = AuthenticationPluginLoader.get(authSwitchPacket.getPlugin(), conf);
            authenticationPlugin.initialize(credential.getPassword(), authSwitchPacket.getSeed(), conf);
            buf = authenticationPlugin.process(writer, reader, context);
            break;
         case 255:
            ErrorPacket errorPacket = new ErrorPacket(buf, context);
            throw context.getExceptionFactory().create(errorPacket.getMessage(), errorPacket.getSqlState(), errorPacket.getErrorCode());
         default:
            throw context.getExceptionFactory().create("unexpected data during authentication (header=" + buf.getUnsignedByte(), "08000");
         }
      }
   }

   public static Credential loadCredential(CredentialPlugin credentialPlugin, Configuration configuration, HostAddress hostAddress) throws SQLException {
      return credentialPlugin != null ? (Credential)credentialPlugin.initialize(configuration, configuration.user(), hostAddress).get() : new Credential(configuration.user(), configuration.password());
   }

   public static SSLSocket sslWrapper(HostAddress hostAddress, Socket socket, long clientCapabilities, byte exchangeCharset, Context context, Writer writer) throws IOException, SQLException {
      Configuration conf = context.getConf();
      if (conf.sslMode() != SslMode.DISABLE) {
         if (!context.hasServerCapability(2048L)) {
            throw context.getExceptionFactory().create("Trying to connect with ssl, but ssl not enabled in the server", "08000");
         } else {
            clientCapabilities |= 2048L;
            SslRequestPacket.create(clientCapabilities, exchangeCharset).encode(writer, context);
            TlsSocketPlugin socketPlugin = TlsSocketPluginLoader.get(conf.tlsSocketType());
            SSLSocketFactory sslSocketFactory = socketPlugin.getSocketFactory(conf, context.getExceptionFactory());
            SSLSocket sslSocket = socketPlugin.createSocket(socket, sslSocketFactory);
            enabledSslProtocolSuites(sslSocket, conf);
            enabledSslCipherSuites(sslSocket, conf);
            sslSocket.setUseClientMode(true);
            sslSocket.startHandshake();
            if (conf.sslMode() == SslMode.VERIFY_FULL && hostAddress != null) {
               SSLSession session = sslSocket.getSession();

               try {
                  socketPlugin.verify(hostAddress.host, session, context.getThreadId());
               } catch (SSLException var13) {
                  throw context.getExceptionFactory().create("SSL hostname verification failed : " + var13.getMessage() + "\nThis verification can be disabled using the sslMode to VERIFY_CA but won't prevent man-in-the-middle attacks anymore", "08006");
               }
            }

            return sslSocket;
         }
      } else {
         return null;
      }
   }

   static void enabledSslProtocolSuites(SSLSocket sslSocket, Configuration conf) throws SQLException {
      if (conf.enabledSslProtocolSuites() != null) {
         List<String> possibleProtocols = Arrays.asList(sslSocket.getSupportedProtocols());
         String[] protocols = conf.enabledSslProtocolSuites().split("[,;\\s]+");
         String[] var4 = protocols;
         int var5 = protocols.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            String protocol = var4[var6];
            if (!possibleProtocols.contains(protocol)) {
               throw new SQLException("Unsupported SSL protocol '" + protocol + "'. Supported protocols : " + possibleProtocols.toString().replace("[", "").replace("]", ""));
            }
         }

         sslSocket.setEnabledProtocols(protocols);
      }

   }

   static void enabledSslCipherSuites(SSLSocket sslSocket, Configuration conf) throws SQLException {
      if (conf.enabledSslCipherSuites() != null) {
         List<String> possibleCiphers = Arrays.asList(sslSocket.getSupportedCipherSuites());
         String[] ciphers = conf.enabledSslCipherSuites().split("[,;\\s]+");
         String[] var4 = ciphers;
         int var5 = ciphers.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            String cipher = var4[var6];
            if (!possibleCiphers.contains(cipher)) {
               throw new SQLException("Unsupported SSL cipher '" + cipher + "'. Supported ciphers : " + possibleCiphers.toString().replace("[", "").replace("]", ""));
            }
         }

         sslSocket.setEnabledCipherSuites(ciphers);
      }

   }

   static {
      SocketHandlerFunction init;
      try {
         init = SocketUtility.getSocketHandler();
      } catch (Throwable var2) {
         init = ConnectionHelper::standardSocket;
      }

      socketHandler = init;
   }
}
