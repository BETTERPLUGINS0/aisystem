package fr.xephi.authme.libs.org.mariadb.jdbc.message.client;

import fr.xephi.authme.libs.org.mariadb.jdbc.BasePreparedStatement;
import fr.xephi.authme.libs.org.mariadb.jdbc.ServerPreparedStatement;
import fr.xephi.authme.libs.org.mariadb.jdbc.Statement;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.Completion;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.Context;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.ReadableByteBuf;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.socket.Reader;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.socket.Writer;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.util.Parameter;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.util.Parameters;
import fr.xephi.authme.libs.org.mariadb.jdbc.export.ExceptionFactory;
import fr.xephi.authme.libs.org.mariadb.jdbc.export.Prepare;
import fr.xephi.authme.libs.org.mariadb.jdbc.message.ClientMessage;
import fr.xephi.authme.libs.org.mariadb.jdbc.message.server.CachedPrepareResultPacket;
import fr.xephi.authme.libs.org.mariadb.jdbc.message.server.ErrorPacket;
import fr.xephi.authme.libs.org.mariadb.jdbc.message.server.PrepareResultPacket;
import fr.xephi.authme.libs.org.mariadb.jdbc.plugin.codec.ByteArrayCodec;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.concurrent.locks.ReentrantLock;

public final class PrepareExecutePacket implements RedoableWithPrepareClientMessage {
   private final String sql;
   private final ServerPreparedStatement prep;
   private final InputStream localInfileInputStream;
   private PrepareResultPacket prepareResult;
   private Parameters parameters;

   public PrepareExecutePacket(String sql, Parameters parameters, ServerPreparedStatement prep, InputStream localInfileInputStream) {
      this.sql = sql;
      this.parameters = parameters;
      this.prep = prep;
      this.localInfileInputStream = localInfileInputStream;
      this.prepareResult = null;
   }

   public int encode(Writer writer, Context context, Prepare newPrepareResult) throws IOException, SQLException {
      int statementId = -1;
      if (newPrepareResult == null) {
         writer.initPacket();
         writer.writeByte(22);
         writer.writeString(this.sql);
         writer.flushPipeline();
      } else {
         statementId = newPrepareResult.getStatementId();
      }

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
               p.encodeBinary(writer);
            }
         }
      }

      writer.flush();
      return newPrepareResult == null ? 2 : 1;
   }

   public Completion readPacket(Statement stmt, int fetchSize, long maxRows, int resultSetConcurrency, int resultSetType, boolean closeOnCompletion, Reader reader, Writer writer, Context context, ExceptionFactory exceptionFactory, ReentrantLock lock, boolean traceEnable, ClientMessage message) throws IOException, SQLException {
      if (this.prepareResult == null) {
         ReadableByteBuf buf = reader.readReusablePacket(traceEnable);
         if (buf.getUnsignedByte() == 255) {
            ErrorPacket errorPacket = new ErrorPacket(buf, context);
            throw exceptionFactory.withSql(this.description()).create(errorPacket.getMessage(), errorPacket.getSqlState(), errorPacket.getErrorCode());
         } else if (context.getConf().useServerPrepStmts() && context.getConf().cachePrepStmts() && this.sql.length() < 8192) {
            PrepareResultPacket prepare = new CachedPrepareResultPacket(buf, reader, context);
            PrepareResultPacket previousCached = (PrepareResultPacket)context.getPrepareCache().put(this.sql, prepare, stmt instanceof ServerPreparedStatement ? (ServerPreparedStatement)stmt : null);
            if (stmt != null) {
               ((BasePreparedStatement)stmt).setPrepareResult((Prepare)(previousCached != null ? previousCached : prepare));
            }

            this.prepareResult = (PrepareResultPacket)(previousCached != null ? previousCached : prepare);
            return this.prepareResult;
         } else {
            PrepareResultPacket prepareResult = new PrepareResultPacket(buf, reader, context);
            if (stmt != null) {
               ((BasePreparedStatement)stmt).setPrepareResult(prepareResult);
            }

            this.prepareResult = prepareResult;
            return prepareResult;
         }
      } else {
         return RedoableWithPrepareClientMessage.super.readPacket(stmt, fetchSize, maxRows, resultSetConcurrency, resultSetType, closeOnCompletion, reader, writer, context, exceptionFactory, lock, traceEnable, message);
      }
   }

   public void saveParameters() {
      this.parameters = this.parameters.clone();
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

   public boolean canSkipMeta() {
      return true;
   }

   public String description() {
      return "PREPARE + EXECUTE " + this.sql;
   }

   public int batchUpdateLength() {
      return 1;
   }

   public String getCommand() {
      return this.sql;
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

   public boolean validateLocalFileName(String fileName, Context context) {
      return ClientMessage.validateLocalFileName(this.sql, this.parameters, fileName, context);
   }

   public void setPrepareResult(PrepareResultPacket prepareResult) {
      this.prepareResult = prepareResult;
   }
}
