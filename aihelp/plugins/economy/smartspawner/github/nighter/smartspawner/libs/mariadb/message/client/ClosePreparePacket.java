package github.nighter.smartspawner.libs.mariadb.message.client;

import github.nighter.smartspawner.libs.mariadb.client.Context;
import github.nighter.smartspawner.libs.mariadb.client.socket.Writer;
import github.nighter.smartspawner.libs.mariadb.message.ClientMessage;
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
