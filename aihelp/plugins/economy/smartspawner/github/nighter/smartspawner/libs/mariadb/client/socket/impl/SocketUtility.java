package github.nighter.smartspawner.libs.mariadb.client.socket.impl;

import com.sun.jna.Platform;
import github.nighter.smartspawner.libs.mariadb.client.impl.ConnectionHelper;
import java.io.IOException;

public class SocketUtility {
   public static SocketHandlerFunction getSocketHandler() {
      Platform.getOSType();
      return (conf, hostAddress) -> {
         if (hostAddress.pipe != null) {
            return new NamedPipeSocket(hostAddress.host, hostAddress.pipe);
         } else if (hostAddress.localSocket != null) {
            try {
               return new UnixDomainSocket(hostAddress.localSocket);
            } catch (RuntimeException var3) {
               throw new IOException(var3.getMessage(), var3.getCause());
            }
         } else {
            return ConnectionHelper.standardSocket(conf, hostAddress);
         }
      };
   }
}
