package github.nighter.smartspawner.libs.mariadb.message.client;

import github.nighter.smartspawner.libs.mariadb.BasePreparedStatement;
import github.nighter.smartspawner.libs.mariadb.ServerPreparedStatement;
import github.nighter.smartspawner.libs.mariadb.Statement;
import github.nighter.smartspawner.libs.mariadb.client.Completion;
import github.nighter.smartspawner.libs.mariadb.client.Context;
import github.nighter.smartspawner.libs.mariadb.client.ReadableByteBuf;
import github.nighter.smartspawner.libs.mariadb.client.socket.Reader;
import github.nighter.smartspawner.libs.mariadb.client.socket.Writer;
import github.nighter.smartspawner.libs.mariadb.client.util.ClosableLock;
import github.nighter.smartspawner.libs.mariadb.export.ExceptionFactory;
import github.nighter.smartspawner.libs.mariadb.export.Prepare;
import github.nighter.smartspawner.libs.mariadb.message.ClientMessage;
import github.nighter.smartspawner.libs.mariadb.message.server.CachedPrepareResultPacket;
import github.nighter.smartspawner.libs.mariadb.message.server.ErrorPacket;
import github.nighter.smartspawner.libs.mariadb.message.server.PrepareResultPacket;
import java.io.IOException;
import java.sql.SQLException;
import java.util.function.Consumer;

public final class PreparePacket implements ClientMessage {
   private final String sql;

   public PreparePacket(String sql) {
      this.sql = sql;
   }

   public int encode(Writer writer, Context context) throws IOException {
      writer.initPacket();
      writer.writeByte(22);
      writer.writeString(this.sql);
      writer.flush();
      return 1;
   }

   public Completion readPacket(Statement stmt, int fetchSize, long maxRows, int resultSetConcurrency, int resultSetType, boolean closeOnCompletion, Reader reader, Writer writer, Context context, ExceptionFactory exceptionFactory, ClosableLock lock, boolean traceEnable, ClientMessage message, Consumer<String> redirectFct) throws IOException, SQLException {
      ReadableByteBuf buf = reader.readReusablePacket(traceEnable);
      if (buf.getUnsignedByte() == 255) {
         ErrorPacket errorPacket = new ErrorPacket(buf, context);
         throw exceptionFactory.withSql(this.description()).create(errorPacket.getMessage(), errorPacket.getSqlState(), errorPacket.getErrorCode(), true);
      } else if (context.getConf().useServerPrepStmts() && context.getConf().cachePrepStmts() && this.sql.length() < 8192) {
         PrepareResultPacket prepare = new CachedPrepareResultPacket(buf, reader, context);
         PrepareResultPacket previousCached = (PrepareResultPacket)context.putPrepareCacheCmd(this.sql, prepare, stmt instanceof ServerPreparedStatement ? (ServerPreparedStatement)stmt : null);
         if (stmt != null) {
            ((BasePreparedStatement)stmt).setPrepareResult((Prepare)(previousCached != null ? previousCached : prepare));
         }

         return (Completion)(previousCached != null ? previousCached : prepare);
      } else {
         PrepareResultPacket prepareResult = new PrepareResultPacket(buf, reader, context);
         if (stmt != null) {
            ((BasePreparedStatement)stmt).setPrepareResult(prepareResult);
         }

         return prepareResult;
      }
   }

   public String description() {
      return "PREPARE " + this.sql;
   }
}
