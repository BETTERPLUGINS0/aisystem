package fr.xephi.authme.libs.org.mariadb.jdbc.message.server.util;

import fr.xephi.authme.libs.org.mariadb.jdbc.client.ServerVersion;
import fr.xephi.authme.libs.org.mariadb.jdbc.util.Version;

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
