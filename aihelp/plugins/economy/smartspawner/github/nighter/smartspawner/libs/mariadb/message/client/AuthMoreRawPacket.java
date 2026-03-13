package github.nighter.smartspawner.libs.mariadb.message.client;

import github.nighter.smartspawner.libs.mariadb.client.Context;
import github.nighter.smartspawner.libs.mariadb.client.socket.Writer;
import github.nighter.smartspawner.libs.mariadb.message.ClientMessage;
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
