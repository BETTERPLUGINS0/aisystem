package fr.xephi.authme.libs.org.mariadb.jdbc.message.client;

import fr.xephi.authme.libs.org.mariadb.jdbc.client.Context;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.socket.Writer;
import fr.xephi.authme.libs.org.mariadb.jdbc.message.ClientMessage;
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
