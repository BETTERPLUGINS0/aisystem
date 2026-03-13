package github.nighter.smartspawner.libs.mariadb.message.client;

import github.nighter.smartspawner.libs.mariadb.client.Context;
import github.nighter.smartspawner.libs.mariadb.client.socket.Writer;
import github.nighter.smartspawner.libs.mariadb.message.ClientMessage;
import java.io.IOException;

public final class ResetPacket implements ClientMessage {
   public static final ResetPacket INSTANCE = new ResetPacket();

   public int encode(Writer writer, Context context) throws IOException {
      writer.initPacket();
      writer.writeByte(31);
      writer.flush();
      return 1;
   }
}
