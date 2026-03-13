package github.nighter.smartspawner.libs.mariadb.message.client;

import github.nighter.smartspawner.libs.mariadb.client.Context;
import github.nighter.smartspawner.libs.mariadb.client.socket.Writer;
import github.nighter.smartspawner.libs.mariadb.message.ClientMessage;
import java.io.IOException;

public final class PingPacket implements ClientMessage {
   public static final PingPacket INSTANCE = new PingPacket();

   public int encode(Writer writer, Context context) throws IOException {
      writer.initPacket();
      writer.writeByte(14);
      writer.flush();
      return 1;
   }
}
