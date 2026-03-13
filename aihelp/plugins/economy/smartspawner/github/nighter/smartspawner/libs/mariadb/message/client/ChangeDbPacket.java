package github.nighter.smartspawner.libs.mariadb.message.client;

import github.nighter.smartspawner.libs.mariadb.client.Context;
import github.nighter.smartspawner.libs.mariadb.client.socket.Writer;
import java.io.IOException;

public final class ChangeDbPacket implements RedoableClientMessage {
   private final String database;

   public ChangeDbPacket(String database) {
      this.database = database;
   }

   public int encode(Writer writer, Context context) throws IOException {
      writer.initPacket();
      writer.writeByte(2);
      writer.writeString(this.database);
      writer.flush();
      return 1;
   }
}
