package github.nighter.smartspawner.libs.mariadb.client.impl;

import github.nighter.smartspawner.libs.mariadb.Configuration;
import github.nighter.smartspawner.libs.mariadb.HostAddress;
import github.nighter.smartspawner.libs.mariadb.client.SocketHelper;
import github.nighter.smartspawner.libs.mariadb.client.socket.impl.SocketHandlerFunction;
import github.nighter.smartspawner.libs.mariadb.client.socket.impl.SocketUtility;
import github.nighter.smartspawner.libs.mariadb.export.SslMode;
import github.nighter.smartspawner.libs.mariadb.plugin.Credential;
import github.nighter.smartspawner.libs.mariadb.plugin.CredentialPlugin;
import github.nighter.smartspawner.libs.mariadb.util.ConfigurableSocketFactory;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.sql.SQLException;
import java.sql.SQLNonTransientConnectionException;
import java.sql.SQLTimeoutException;
import java.util.Arrays;
import java.util.List;
import javax.net.SocketFactory;
import javax.net.ssl.SSLSocket;

public final class ConnectionHelper {
   private static final SocketHandlerFunction socketHandler;

   public static Socket createSocket(Configuration conf, HostAddress hostAddress) throws IOException, SQLException {
      return socketHandler.apply(conf, hostAddress);
   }

   public static Socket standardSocket(Configuration conf, HostAddress hostAddress) throws IOException, SQLException {
      String socketFactoryName = conf.socketFactory();
      SocketFactory socketFactory;
      if (socketFactoryName != null) {
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
      } else {
         socketFactory = SocketFactory.getDefault();
         return socketFactory.createSocket();
      }
   }

   public static Socket connectSocket(Configuration conf, HostAddress hostAddress) throws SQLException {
      try {
         Socket socket = createSocket(conf, hostAddress);
         SocketHelper.setSocketOption(conf, socket);
         if (!socket.isConnected()) {
            InetSocketAddress sockAddr = hostAddress.pipe == null && hostAddress.localSocket == null ? new InetSocketAddress(hostAddress.host, hostAddress.port) : null;
            socket.connect(sockAddr, conf.connectTimeout());
         }

         return socket;
      } catch (SocketTimeoutException var4) {
         throw new SQLTimeoutException(String.format("Socket timeout when connecting to %s. %s", hostAddress, var4.getMessage()), "08000", var4);
      } catch (IOException var5) {
         throw new SQLNonTransientConnectionException(String.format("Socket fail to connect to %s. %s", hostAddress, var5.getMessage()), "08000", var5);
      }
   }

   public static long initializeClientCapabilities(Configuration configuration, long serverCapabilities, HostAddress hostAddress) {
      long capabilities = initializeBaseCapabilities();
      capabilities = applyOptionalCapabilities(capabilities, configuration);
      capabilities = applyTechnicalCapabilities(capabilities, configuration);
      capabilities = applyConnectionCapabilities(capabilities, configuration, hostAddress);
      return capabilities & serverCapabilities;
   }

   private static long initializeBaseCapabilities() {
      return 12493568L;
   }

   private static long applyOptionalCapabilities(long capabilities, Configuration configuration) {
      if (getBooleanProperty(configuration, "enableBulkUnitResult", true)) {
         capabilities |= 137438953472L;
      }

      if (getBooleanProperty(configuration, "disableSessionTracking", false)) {
         capabilities &= -8388609L;
      }

      if (shouldEnableMetadataCache(configuration)) {
         capabilities |= 68719476736L;
      }

      if (getBooleanProperty(configuration, "interactiveClient", false)) {
         capabilities |= 1024L;
      }

      if (configuration.useBulkStmts() || configuration.useBulkStmtsForInserts()) {
         capabilities |= 17179869184L;
      }

      if (!configuration.useAffectedRows()) {
         capabilities |= 2L;
      }

      if (configuration.allowMultiQueries() || configuration.rewriteBatchedStatements()) {
         capabilities |= 65536L;
      }

      if (configuration.allowLocalInfile()) {
         capabilities |= 128L;
      }

      return capabilities;
   }

   private static long applyTechnicalCapabilities(long capabilities, Configuration configuration) {
      if (getBooleanProperty(configuration, "extendedTypeInfo", true)) {
         capabilities |= 34359738368L;
      }

      if (getBooleanProperty(configuration, "deprecateEof", true)) {
         capabilities |= 16777216L;
      }

      if (configuration.useCompression()) {
         capabilities |= 32L;
      }

      return capabilities;
   }

   private static long applyConnectionCapabilities(long capabilities, Configuration configuration, HostAddress hostAddress) {
      if (shouldConnectWithDb(configuration, hostAddress)) {
         capabilities |= 8L;
      }

      if (shouldEnableSsl(configuration, hostAddress)) {
         capabilities |= 2048L;
      }

      if (!configuration.disconnectOnExpiredPasswords()) {
         capabilities |= 4194304L;
      }

      return capabilities;
   }

   private static boolean getBooleanProperty(Configuration configuration, String propertyName, boolean defaultValue) {
      return Boolean.parseBoolean(configuration.nonMappedOptions().getProperty(propertyName, String.valueOf(defaultValue)));
   }

   private static boolean shouldEnableMetadataCache(Configuration configuration) {
      return configuration.useServerPrepStmts() && getBooleanProperty(configuration, "enableSkipMeta", true);
   }

   private static boolean shouldConnectWithDb(Configuration configuration, HostAddress hostAddress) {
      return configuration.database() != null && (!configuration.createDatabaseIfNotExist() || configuration.createDatabaseIfNotExist() && hostAddress != null && !hostAddress.primary);
   }

   private static boolean shouldEnableSsl(Configuration configuration, HostAddress hostAddress) {
      SslMode sslMode = hostAddress.sslMode == null ? configuration.sslMode() : hostAddress.sslMode;
      return sslMode != SslMode.DISABLE;
   }

   public static Credential loadCredential(CredentialPlugin credentialPlugin, Configuration configuration, HostAddress hostAddress) throws SQLException {
      return credentialPlugin != null ? (Credential)credentialPlugin.initialize(configuration, configuration.user(), hostAddress).get() : new Credential(configuration.user(), configuration.password());
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
