package fr.xephi.authme.libs.org.mariadb.jdbc.message.client;

import fr.xephi.authme.libs.org.mariadb.jdbc.client.Context;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.socket.Writer;
import fr.xephi.authme.libs.org.mariadb.jdbc.message.ClientMessage;
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
