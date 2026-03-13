package github.nighter.smartspawner.libs.mariadb.message.client;

import github.nighter.smartspawner.libs.mariadb.client.Context;
import github.nighter.smartspawner.libs.mariadb.client.socket.Writer;
import github.nighter.smartspawner.libs.mariadb.export.Prepare;
import github.nighter.smartspawner.libs.mariadb.message.ClientMessage;
import java.io.IOException;
import java.sql.SQLException;

public interface RedoableClientMessage extends ClientMessage {
   default void saveParameters() {
   }

   default void ensureReplayable(Context context) throws IOException, SQLException {
   }

   default int encode(Writer writer, Context context, Prepare newPrepareResult) throws IOException, SQLException {
      return this.encode(writer, context);
   }

   default int reEncode(Writer writer, Context context, Prepare newPrepareResult) throws IOException, SQLException {
      return this.encode(writer, context, newPrepareResult);
   }
}
