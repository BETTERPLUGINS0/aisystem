package github.nighter.smartspawner.libs.mariadb.export;

import java.io.IOException;

public class MaxAllowedPacketException extends IOException {
   private static final long serialVersionUID = 5669184960442818475L;
   private final boolean mustReconnect;

   public MaxAllowedPacketException(String message, boolean mustReconnect) {
      super(message);
      this.mustReconnect = mustReconnect;
   }

   public boolean isMustReconnect() {
      return this.mustReconnect;
   }
}
