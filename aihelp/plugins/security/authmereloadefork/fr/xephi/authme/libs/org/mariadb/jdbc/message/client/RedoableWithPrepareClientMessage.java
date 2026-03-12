package fr.xephi.authme.libs.org.mariadb.jdbc.message.client;

import fr.xephi.authme.libs.org.mariadb.jdbc.ServerPreparedStatement;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.Client;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.Context;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.socket.Writer;
import fr.xephi.authme.libs.org.mariadb.jdbc.export.Prepare;
import fr.xephi.authme.libs.org.mariadb.jdbc.message.server.PrepareResultPacket;
import java.io.IOException;
import java.sql.SQLException;

public interface RedoableWithPrepareClientMessage extends RedoableClientMessage {
   String getCommand();

   ServerPreparedStatement prep();

   default int encode(Writer writer, Context context) throws IOException, SQLException {
      return this.encode(writer, context, (Prepare)null);
   }

   int encode(Writer var1, Context var2, Prepare var3) throws IOException, SQLException;

   default int reEncode(Writer writer, Context context, Prepare newPrepareResult) throws IOException, SQLException {
      return this.encode(writer, context, newPrepareResult);
   }

   void setPrepareResult(PrepareResultPacket var1);

   default void rePrepare(Client client) throws SQLException {
      PreparePacket preparePacket = new PreparePacket(this.getCommand());
      this.setPrepareResult((PrepareResultPacket)client.execute(preparePacket, this.prep(), 0, 0L, 1007, 1003, false, true).get(0));
   }
}
