package github.nighter.smartspawner.libs.mariadb.client;

import github.nighter.smartspawner.libs.mariadb.Configuration;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

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

      if (conf.localSocketAddress() != null) {
         InetSocketAddress localAddress = new InetSocketAddress(conf.localSocketAddress(), 0);
         socket.bind(localAddress);
      }

   }
}
