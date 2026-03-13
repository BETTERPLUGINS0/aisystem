package github.nighter.smartspawner.libs.mariadb.message.server;

import github.nighter.smartspawner.libs.mariadb.BasePreparedStatement;
import github.nighter.smartspawner.libs.mariadb.client.Client;
import github.nighter.smartspawner.libs.mariadb.client.ColumnDecoder;
import github.nighter.smartspawner.libs.mariadb.client.Completion;
import github.nighter.smartspawner.libs.mariadb.client.Context;
import github.nighter.smartspawner.libs.mariadb.client.ReadableByteBuf;
import github.nighter.smartspawner.libs.mariadb.client.impl.StandardReadableByteBuf;
import github.nighter.smartspawner.libs.mariadb.client.socket.Reader;
import github.nighter.smartspawner.libs.mariadb.export.Prepare;
import github.nighter.smartspawner.libs.mariadb.util.log.Logger;
import github.nighter.smartspawner.libs.mariadb.util.log.Loggers;
import java.io.IOException;
import java.sql.SQLException;

public class PrepareResultPacket implements Completion, Prepare {
   private static final byte[] CONSTANT_PARAMETER_BYTES = new byte[]{3, 100, 101, 102, 0, 0, 0, 1, 63, 0, 0, 12, 63, 0, 0, 0, 0, 0, 6, -128, 0, 0, 0, 0};
   static final ColumnDecoder CONSTANT_PARAMETER;
   private static final Logger logger;
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
            reader.readReusablePacket();
         }

         if (!context.isEofDeprecated()) {
            reader.readReusablePacket();
         }
      }

      if (numColumns > 0) {
         for(i = 0; i < numColumns; ++i) {
            this.columns[i] = (ColumnDecoder)context.getColumnDecoderFunction().apply(new StandardReadableByteBuf(reader.readPacket(trace)));
         }

         if (!context.isEofDeprecated()) {
            reader.readReusablePacket();
         }
      }

   }

   public void close(Client con) throws SQLException {
      con.closePrepare(this);
   }

   public void decrementUse(Client con, BasePreparedStatement preparedStatement) throws SQLException {
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
      CONSTANT_PARAMETER = ColumnDecoder.decode(new StandardReadableByteBuf(CONSTANT_PARAMETER_BYTES, CONSTANT_PARAMETER_BYTES.length));
      logger = Loggers.getLogger(PrepareResultPacket.class);
   }
}
