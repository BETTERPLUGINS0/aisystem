package fr.xephi.authme.libs.org.mariadb.jdbc.message.server;

import fr.xephi.authme.libs.org.mariadb.jdbc.ServerPreparedStatement;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.Client;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.ColumnDecoder;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.Completion;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.Context;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.ReadableByteBuf;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.impl.StandardReadableByteBuf;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.socket.Reader;
import fr.xephi.authme.libs.org.mariadb.jdbc.export.Prepare;
import fr.xephi.authme.libs.org.mariadb.jdbc.util.log.Logger;
import fr.xephi.authme.libs.org.mariadb.jdbc.util.log.Loggers;
import java.io.IOException;
import java.sql.SQLException;

public class PrepareResultPacket implements Completion, Prepare {
   static final ColumnDecoder CONSTANT_PARAMETER;
   private static final Logger logger = Loggers.getLogger(PrepareResultPacket.class);
   private final ColumnDecoder[] parameters;
   protected int statementId;
   private ColumnDecoder[] columns;

   public PrepareResultPacket(ReadableByteBuf buffer, Reader reader, Context context) throws IOException {
      boolean trace = logger.isTraceEnabled();
      buffer.readByte();
      this.statementId = buffer.readInt();
      int numColumns = buffer.readUnsignedShort();
      int numParams = buffer.readUnsignedShort();
      this.parameters = new ColumnDecoder[numParams];
      this.columns = new ColumnDecoder[numColumns];
      int i;
      if (numParams > 0) {
         for(i = 0; i < numParams; ++i) {
            this.parameters[i] = CONSTANT_PARAMETER;
            reader.skipPacket();
         }

         if (!context.isEofDeprecated()) {
            reader.skipPacket();
         }
      }

      if (numColumns > 0) {
         for(i = 0; i < numColumns; ++i) {
            this.columns[i] = (ColumnDecoder)context.getColumnDecoderFunction().apply(new StandardReadableByteBuf(reader.readPacket(trace)));
         }

         if (!context.isEofDeprecated()) {
            reader.skipPacket();
         }
      }

   }

   public void close(Client con) throws SQLException {
      con.closePrepare(this);
   }

   public void decrementUse(Client con, ServerPreparedStatement preparedStatement) throws SQLException {
      this.close(con);
   }

   public int getStatementId() {
      return this.statementId;
   }

   public ColumnDecoder[] getParameters() {
      return this.parameters;
   }

   public ColumnDecoder[] getColumns() {
      return this.columns;
   }

   public void setColumns(ColumnDecoder[] columns) {
      this.columns = columns;
   }

   static {
      byte[] bytes = new byte[]{3, 100, 101, 102, 0, 0, 0, 1, 63, 0, 0, 12, 63, 0, 0, 0, 0, 0, 6, -128, 0, 0, 0, 0};
      CONSTANT_PARAMETER = ColumnDecoder.decode(new StandardReadableByteBuf(bytes, bytes.length));
   }
}
