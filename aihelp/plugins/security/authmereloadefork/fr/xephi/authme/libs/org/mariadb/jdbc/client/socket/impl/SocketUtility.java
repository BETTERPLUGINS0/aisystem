package fr.xephi.authme.libs.org.mariadb.jdbc.client.socket.impl;

import com.sun.jna.Platform;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.impl.ConnectionHelper;
import java.io.IOException;

public class SocketUtility {
   public static SocketHandlerFunction getSocketHandler() {
      Platform.getOSType();
      return (conf, hostAddress) -> {
         if (conf.pipe() != null) {
            return new NamedPipeSocket(hostAddress != null ? hostAddress.host : null, conf.pipe());
         } else if (conf.localSocket() != null) {
            try {
               return new UnixDomainSocket(conf.localSocket());
            } catch (RuntimeException var3) {
               throw new IOException(var3.getMessage(), var3.getCause());
            }
         } else {
            return ConnectionHelper.standardSocket(conf, hostAddress);
         }
      };
   }
}
