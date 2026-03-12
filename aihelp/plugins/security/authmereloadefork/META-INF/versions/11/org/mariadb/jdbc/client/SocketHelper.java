package fr.xephi.authme.libs.org.mariadb.jdbc.client;

import fr.xephi.authme.libs.org.mariadb.jdbc.Configuration;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import jdk.net.ExtendedSocketOptions;

public class SocketHelper {
   public static void setSocketOption(Configuration conf, Socket socket) throws IOException {
      socket.setTcpNoDelay(true);
      socket.setSoTimeout(conf.socketTimeout());
      if (conf.tcpKeepAlive()) {
         socket.setKeepAlive(true);
      }

      if (conf.tcpAbortiveClose()) {
         socket.setSoLinger(true, 0);
      }

      if (conf.tcpKeepIdle() > 0) {
         socket.setOption(ExtendedSocketOptions.TCP_KEEPIDLE, conf.tcpKeepIdle());
      }

      if (conf.tcpKeepCount() > 0) {
         socket.setOption(ExtendedSocketOptions.TCP_KEEPCOUNT, conf.tcpKeepCount());
      }

      if (conf.tcpKeepInterval() > 0) {
         socket.setOption(ExtendedSocketOptions.TCP_KEEPINTERVAL, conf.tcpKeepInterval());
      }

      if (conf.localSocketAddress() != null) {
         InetSocketAddress localAddress = new InetSocketAddress(conf.localSocketAddress(), 0);
         socket.bind(localAddress);
      }

   }
}
