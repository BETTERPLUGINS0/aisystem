package fr.xephi.authme.libs.org.mariadb.jdbc.message.client;

import fr.xephi.authme.libs.org.mariadb.jdbc.client.Context;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.socket.Writer;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.util.Parameter;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.util.Parameters;
import fr.xephi.authme.libs.org.mariadb.jdbc.message.ClientMessage;
import fr.xephi.authme.libs.org.mariadb.jdbc.plugin.codec.ByteArrayCodec;
import fr.xephi.authme.libs.org.mariadb.jdbc.util.ClientParser;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;

public final class QueryWithParametersPacket implements RedoableClientMessage {
   private final String preSqlCmd;
   private final ClientParser parser;
   private final InputStream localInfileInputStream;
   private Parameters parameters;

   public QueryWithParametersPacket(String preSqlCmd, ClientParser parser, Parameters parameters, InputStream localInfileInputStream) {
      this.preSqlCmd = preSqlCmd;
      this.parser = parser;
      this.parameters = parameters;
      this.localInfileInputStream = localInfileInputStream;
   }

   public void ensureReplayable(Context context) throws IOException, SQLException {
      int parameterCount = this.parameters.size();

      for(int i = 0; i < parameterCount; ++i) {
         Parameter p = this.parameters.get(i);
         if (!p.isNull() && p.canEncodeLongData()) {
            this.parameters.set(i, new fr.xephi.authme.libs.org.mariadb.jdbc.codec.Parameter(ByteArrayCodec.INSTANCE, p.encodeData()));
         }
      }

   }

   public void saveParameters() {
      this.parameters = this.parameters.clone();
   }

   public int encode(Writer encoder, Context context) throws IOException, SQLException {
      encoder.initPacket();
      encoder.writeByte(3);
      if (this.preSqlCmd != null) {
         encoder.writeAscii(this.preSqlCmd);
      }

      if (this.parser.getParamPositions().size() == 0) {
         encoder.writeBytes(this.parser.getQuery());
      } else {
         int pos = 0;

         for(int i = 0; i < this.parser.getParamPositions().size(); ++i) {
            int paramPos = (Integer)this.parser.getParamPositions().get(i);
            encoder.writeBytes(this.parser.getQuery(), pos, paramPos - pos);
            pos = paramPos + 1;
            this.parameters.get(i).encodeText(encoder, context);
         }

         encoder.writeBytes(this.parser.getQuery(), pos, this.parser.getQuery().length - pos);
      }

      encoder.flush();
      return 1;
   }

   public int batchUpdateLength() {
      return 1;
   }

   public boolean validateLocalFileName(String fileName, Context context) {
      return ClientMessage.validateLocalFileName(this.parser.getSql(), this.parameters, fileName, context);
   }

   public InputStream getLocalInfileInputStream() {
      return this.localInfileInputStream;
   }

   public String description() {
      return this.parser.getSql();
   }
}
