package fr.xephi.authme.libs.org.mariadb.jdbc.message.client;

import fr.xephi.authme.libs.org.mariadb.jdbc.client.Context;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.socket.Writer;
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
