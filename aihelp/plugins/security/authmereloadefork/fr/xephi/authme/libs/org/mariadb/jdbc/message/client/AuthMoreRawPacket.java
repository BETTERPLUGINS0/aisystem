package fr.xephi.authme.libs.org.mariadb.jdbc.message.client;

import fr.xephi.authme.libs.org.mariadb.jdbc.client.Context;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.socket.Writer;
import fr.xephi.authme.libs.org.mariadb.jdbc.message.ClientMessage;
import java.io.IOException;

public final class AuthMoreRawPacket implements ClientMessage {
   private final byte[] raw;

   public AuthMoreRawPacket(byte[] raw) {
      this.raw = raw;
   }

   public int encode(Writer writer, Context context) throws IOException {
      if (this.raw.length == 0) {
         writer.writeEmptyPacket();
      } else {
         writer.writeBytes(this.raw);
         writer.flush();
      }

      return 0;
   }
}
