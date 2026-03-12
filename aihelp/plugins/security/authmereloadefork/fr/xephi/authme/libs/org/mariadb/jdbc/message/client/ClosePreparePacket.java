package fr.xephi.authme.libs.org.mariadb.jdbc.message.client;

import fr.xephi.authme.libs.org.mariadb.jdbc.client.Context;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.socket.Writer;
import fr.xephi.authme.libs.org.mariadb.jdbc.message.ClientMessage;
import java.io.IOException;

public final class ClosePreparePacket implements ClientMessage {
   private final int statementId;

   public ClosePreparePacket(int statementId) {
      this.statementId = statementId;
   }

   public int encode(Writer writer, Context context) throws IOException {
      assert this.statementId != 0;

      writer.initPacket();
      writer.writeByte(25);
      writer.writeInt(this.statementId);
      writer.flush();
      return 0;
   }
}
