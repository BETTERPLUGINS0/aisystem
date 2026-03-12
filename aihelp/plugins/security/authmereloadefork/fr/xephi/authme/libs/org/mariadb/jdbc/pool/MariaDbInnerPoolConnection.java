package fr.xephi.authme.libs.org.mariadb.jdbc.pool;

import fr.xephi.authme.libs.org.mariadb.jdbc.Connection;
import fr.xephi.authme.libs.org.mariadb.jdbc.MariaDbPoolConnection;
import java.util.concurrent.atomic.AtomicLong;

public class MariaDbInnerPoolConnection extends MariaDbPoolConnection {
   private final AtomicLong lastUsed = new AtomicLong(System.nanoTime());

   public MariaDbInnerPoolConnection(Connection connection) {
      super(connection);
   }

   public AtomicLong getLastUsed() {
      return this.lastUsed;
   }

   public void lastUsedToNow() {
      this.lastUsed.set(System.nanoTime());
   }

   public void ensureValidation() {
      this.lastUsed.set(0L);
   }
}
