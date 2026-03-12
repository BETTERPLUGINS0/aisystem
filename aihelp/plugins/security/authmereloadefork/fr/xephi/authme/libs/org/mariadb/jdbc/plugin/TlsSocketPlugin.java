package fr.xephi.authme.libs.org.mariadb.jdbc.plugin;

import fr.xephi.authme.libs.org.mariadb.jdbc.Configuration;
import fr.xephi.authme.libs.org.mariadb.jdbc.export.ExceptionFactory;
import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

public interface TlsSocketPlugin {
   String type();

   SSLSocketFactory getSocketFactory(Configuration var1, ExceptionFactory var2) throws SQLException;

   default SSLSocket createSocket(Socket socket, SSLSocketFactory sslSocketFactory) throws IOException {
      return (SSLSocket)sslSocketFactory.createSocket(socket, socket.getInetAddress() == null ? null : socket.getInetAddress().getHostAddress(), socket.getPort(), true);
   }

   void verify(String var1, SSLSession var2, long var3) throws SSLException;
}
