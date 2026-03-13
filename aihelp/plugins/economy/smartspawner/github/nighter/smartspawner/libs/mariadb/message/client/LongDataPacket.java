package github.nighter.smartspawner.libs.mariadb.message.client;

import github.nighter.smartspawner.libs.mariadb.client.Context;
import github.nighter.smartspawner.libs.mariadb.client.socket.Writer;
import github.nighter.smartspawner.libs.mariadb.client.util.Parameter;
import github.nighter.smartspawner.libs.mariadb.message.ClientMessage;
import java.io.IOException;
import java.sql.SQLException;

public final class LongDataPacket implements ClientMessage {
   private final int statementId;
   private final Parameter parameter;
   private final int index;

   public LongDataPacket(int statementId, Parameter parameter, int index) {
      this.statementId = statementId;
      this.parameter = parameter;
      this.index = index;
   }

   public int encode(Writer writer, Context context) throws IOException, SQLException {
      writer.initPacket();
      writer.writeByte(24);
      writer.writeInt(this.statementId);
      writer.writeShort((short)this.index);
      this.parameter.encodeLongData(writer);
      writer.flush();
      return 0;
   }
}
