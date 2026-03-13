package github.nighter.smartspawner.libs.mariadb.message.server.util;

import github.nighter.smartspawner.libs.mariadb.client.ServerVersion;
import github.nighter.smartspawner.libs.mariadb.util.Version;

public final class ServerVersionUtility extends Version implements ServerVersion {
   private final boolean mariaDBServer;

   public ServerVersionUtility(String serverVersion, boolean mariaDBServer) {
      super(serverVersion);
      this.mariaDBServer = mariaDBServer;
   }

   public boolean isMariaDBServer() {
      return this.mariaDBServer;
   }
}
