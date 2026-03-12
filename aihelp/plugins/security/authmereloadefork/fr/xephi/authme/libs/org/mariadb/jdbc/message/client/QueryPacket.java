package fr.xephi.authme.libs.org.mariadb.jdbc.message.client;

import fr.xephi.authme.libs.org.mariadb.jdbc.client.Context;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.socket.Writer;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.util.Parameters;
import fr.xephi.authme.libs.org.mariadb.jdbc.message.ClientMessage;
import java.io.IOException;
import java.io.InputStream;

public final class QueryPacket implements RedoableClientMessage {
   private final String sql;
   private final InputStream localInfileInputStream;

   public QueryPacket(String sql) {
      this.sql = sql;
      this.localInfileInputStream = null;
   }

   public QueryPacket(String sql, InputStream localInfileInputStream) {
      this.sql = sql;
      this.localInfileInputStream = localInfileInputStream;
   }

   public int batchUpdateLength() {
      return 1;
   }

   public int encode(Writer writer, Context context) throws IOException {
      writer.initPacket();
      writer.writeByte(3);
      writer.writeString(this.sql);
      writer.flush();
      return 1;
   }

   public boolean isCommit() {
      return "COMMIT".equalsIgnoreCase(this.sql);
   }

   public boolean validateLocalFileName(String fileName, Context context) {
      return ClientMessage.validateLocalFileName(this.sql, (Parameters)null, fileName, context);
   }

   public InputStream getLocalInfileInputStream() {
      return this.localInfileInputStream;
   }

   public String description() {
      return this.sql;
   }
}
