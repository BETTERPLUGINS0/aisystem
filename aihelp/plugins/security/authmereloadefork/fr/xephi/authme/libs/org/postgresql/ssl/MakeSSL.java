package fr.xephi.authme.libs.org.postgresql.ssl;

import fr.xephi.authme.libs.org.postgresql.PGProperty;
import fr.xephi.authme.libs.org.postgresql.core.PGStream;
import fr.xephi.authme.libs.org.postgresql.core.SocketFactoryFactory;
import fr.xephi.authme.libs.org.postgresql.jdbc.SslMode;
import fr.xephi.authme.libs.org.postgresql.util.GT;
import fr.xephi.authme.libs.org.postgresql.util.ObjectFactory;
import fr.xephi.authme.libs.org.postgresql.util.PSQLException;
import fr.xephi.authme.libs.org.postgresql.util.PSQLState;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

public class MakeSSL extends ObjectFactory {
   private static final Logger LOGGER = Logger.getLogger(MakeSSL.class.getName());

   public static void convert(PGStream stream, Properties info) throws PSQLException, IOException {
      LOGGER.log(Level.FINE, "converting regular socket connection to ssl");
      SSLSocketFactory factory = SocketFactoryFactory.getSslSocketFactory(info);

      SSLSocket newConnection;
      try {
         newConnection = (SSLSocket)factory.createSocket(stream.getSocket(), stream.getHostSpec().getHost(), stream.getHostSpec().getPort(), true);
         int connectTimeoutSeconds = PGProperty.CONNECT_TIMEOUT.getInt(info);
         newConnection.setSoTimeout(connectTimeoutSeconds * 1000);
         newConnection.setUseClientMode(true);
         newConnection.startHandshake();
      } catch (IOException var6) {
         throw new PSQLException(GT.tr("SSL error: {0}", var6.getMessage()), PSQLState.CONNECTION_FAILURE, var6);
      }

      if (factory instanceof LibPQFactory) {
         ((LibPQFactory)factory).throwKeyManagerException();
      }

      SslMode sslMode = SslMode.of(info);
      if (sslMode.verifyPeerName()) {
         verifyPeerName(stream, info, newConnection);
      }

      int socketTimeout = PGProperty.SOCKET_TIMEOUT.getInt(info);
      newConnection.setSoTimeout(socketTimeout * 1000);
      stream.changeSocket(newConnection);
   }

   private static void verifyPeerName(PGStream stream, Properties info, SSLSocket newConnection) throws PSQLException {
      String sslhostnameverifier = PGProperty.SSL_HOSTNAME_VERIFIER.getOrDefault(info);
      Object hvn;
      if (sslhostnameverifier == null) {
         hvn = PGjdbcHostnameVerifier.INSTANCE;
         sslhostnameverifier = "PgjdbcHostnameVerifier";
      } else {
         try {
            hvn = (HostnameVerifier)instantiate(HostnameVerifier.class, sslhostnameverifier, info, false, (String)null);
         } catch (Exception var6) {
            throw new PSQLException(GT.tr("The HostnameVerifier class provided {0} could not be instantiated.", sslhostnameverifier), PSQLState.CONNECTION_FAILURE, var6);
         }
      }

      if (!((HostnameVerifier)hvn).verify(stream.getHostSpec().getHost(), newConnection.getSession())) {
         throw new PSQLException(GT.tr("The hostname {0} could not be verified by hostnameverifier {1}.", stream.getHostSpec().getHost(), sslhostnameverifier), PSQLState.CONNECTION_FAILURE);
      }
   }
}
