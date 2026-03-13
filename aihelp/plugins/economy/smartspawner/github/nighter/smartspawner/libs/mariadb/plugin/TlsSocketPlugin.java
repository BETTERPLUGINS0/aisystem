package github.nighter.smartspawner.libs.mariadb.plugin;

import github.nighter.smartspawner.libs.mariadb.Configuration;
import github.nighter.smartspawner.libs.mariadb.HostAddress;
import github.nighter.smartspawner.libs.mariadb.export.ExceptionFactory;
import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

public interface TlsSocketPlugin {
   String type();

   TrustManager[] getTrustManager(Configuration var1, ExceptionFactory var2, HostAddress var3) throws SQLException;

   KeyManager[] getKeyManager(Configuration var1, ExceptionFactory var2) throws SQLException;

   default SSLSocket createSocket(Socket socket, SSLSocketFactory sslSocketFactory) throws IOException {
      return (SSLSocket)sslSocketFactory.createSocket(socket, socket.getInetAddress() == null ? null : socket.getInetAddress().getHostAddress(), socket.getPort(), true);
   }

   void verify(String var1, SSLSession var2, long var3) throws SSLException;
}
