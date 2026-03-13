package github.nighter.smartspawner.libs.mariadb.message.client;

import github.nighter.smartspawner.libs.mariadb.client.Context;
import github.nighter.smartspawner.libs.mariadb.client.socket.Writer;
import github.nighter.smartspawner.libs.mariadb.message.ClientMessage;
import java.io.IOException;

public final class QuitPacket implements ClientMessage {
   public static final QuitPacket INSTANCE = new QuitPacket();

   public int encode(Writer writer, Context context) throws IOException {
      writer.initPacket();
      writer.writeByte(1);
      writer.flush();
      return 0;
   }
}
