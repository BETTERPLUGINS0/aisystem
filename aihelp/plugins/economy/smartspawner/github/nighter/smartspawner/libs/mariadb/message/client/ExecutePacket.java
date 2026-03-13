package github.nighter.smartspawner.libs.mariadb.message.client;

import github.nighter.smartspawner.libs.mariadb.ServerPreparedStatement;
import github.nighter.smartspawner.libs.mariadb.client.Context;
import github.nighter.smartspawner.libs.mariadb.client.socket.Writer;
import github.nighter.smartspawner.libs.mariadb.client.util.Parameter;
import github.nighter.smartspawner.libs.mariadb.client.util.Parameters;
import github.nighter.smartspawner.libs.mariadb.export.Prepare;
import github.nighter.smartspawner.libs.mariadb.message.ClientMessage;
import github.nighter.smartspawner.libs.mariadb.message.server.PrepareResultPacket;
import github.nighter.smartspawner.libs.mariadb.plugin.codec.ByteArrayCodec;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;

public final class ExecutePacket implements RedoableWithPrepareClientMessage {
   private final String command;
   private final ServerPreparedStatement prep;
   private final InputStream localInfileInputStream;
   private Prepare prepareResult;
   private Parameters parameters;

   public ExecutePacket(Prepare prepareResult, Parameters parameters, String command, ServerPreparedStatement prep, InputStream localInfileInputStream) {
      this.parameters = parameters;
      this.prepareResult = prepareResult;
      this.command = command;
      this.prep = prep;
      this.localInfileInputStream = localInfileInputStream;
   }

   public void saveParameters() {
      this.parameters = this.parameters.clone();
   }

   public void ensureReplayable(Context context) throws IOException, SQLException {
      int parameterCount = this.parameters.size();

      for(int i = 0; i < parameterCount; ++i) {
         Parameter p = this.parameters.get(i);
         if (!p.isNull() && p.canEncodeLongData()) {
            this.parameters.set(i, new github.nighter.smartspawner.libs.mariadb.codec.Parameter(ByteArrayCodec.INSTANCE, p.encodeData()));
         }
      }

   }

   public int encode(Writer writer, Context context, Prepare newPrepareResult) throws IOException, SQLException {
      int statementId = newPrepareResult != null && newPrepareResult.getStatementId() != -1 ? newPrepareResult.getStatementId() : (this.prepareResult != null ? this.prepareResult.getStatementId() : -1);
      int parameterCount = this.parameters.size();

      int nullCount;
      for(nullCount = 0; nullCount < parameterCount; ++nullCount) {
         Parameter p = this.parameters.get(nullCount);
         if (!p.isNull() && p.canEncodeLongData()) {
            (new LongDataPacket(statementId, p, nullCount)).encode(writer, context);
         }
      }

      writer.initPacket();
      writer.writeByte(23);
      writer.writeInt(statementId);
      writer.writeByte(0);
      writer.writeInt(1);
      if (parameterCount > 0) {
         nullCount = (parameterCount + 7) / 8;
         byte[] nullBitsBuffer = new byte[nullCount];
         int initialPos = writer.pos();
         writer.pos(initialPos + nullCount);
         writer.writeByte(1);

         int i;
         Parameter p;
         for(i = 0; i < parameterCount; ++i) {
            p = this.parameters.get(i);
            writer.writeByte(p.getBinaryEncodeType());
            writer.writeByte(0);
            if (p.isNull()) {
               nullBitsBuffer[i / 8] |= (byte)(1 << i % 8);
            }
         }

         writer.writeBytesAtPos(nullBitsBuffer, initialPos);

         for(i = 0; i < parameterCount; ++i) {
            p = this.parameters.get(i);
            if (!p.isNull() && !p.canEncodeLongData()) {
               p.encodeBinary(writer, context);
            }
         }
      }

      writer.flush();
      return 1;
   }

   public boolean canSkipMeta() {
      return true;
   }

   public int batchUpdateLength() {
      return 1;
   }

   public String getCommand() {
      return this.command;
   }

   public InputStream getLocalInfileInputStream() {
      return this.localInfileInputStream;
   }

   public ServerPreparedStatement prep() {
      return this.prep;
   }

   public boolean binaryProtocol() {
      return true;
   }

   public String description() {
      return "EXECUTE " + this.command;
   }

   public boolean validateLocalFileName(String fileName, Context context) {
      return ClientMessage.validateLocalFileName(this.command, this.parameters, fileName, context);
   }

   public void setPrepareResult(PrepareResultPacket prepareResult) {
      this.prepareResult = prepareResult;
   }
}
