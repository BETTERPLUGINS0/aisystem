package fr.xephi.authme.libs.org.mariadb.jdbc.message.client;

import fr.xephi.authme.libs.org.mariadb.jdbc.client.Context;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.socket.Writer;
import fr.xephi.authme.libs.org.mariadb.jdbc.export.Prepare;
import fr.xephi.authme.libs.org.mariadb.jdbc.message.ClientMessage;
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
