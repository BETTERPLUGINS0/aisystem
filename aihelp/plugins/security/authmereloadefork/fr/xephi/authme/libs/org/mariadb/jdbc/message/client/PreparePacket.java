package fr.xephi.authme.libs.org.mariadb.jdbc.message.client;

import fr.xephi.authme.libs.org.mariadb.jdbc.BasePreparedStatement;
import fr.xephi.authme.libs.org.mariadb.jdbc.ServerPreparedStatement;
import fr.xephi.authme.libs.org.mariadb.jdbc.Statement;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.Completion;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.Context;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.ReadableByteBuf;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.socket.Reader;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.socket.Writer;
import fr.xephi.authme.libs.org.mariadb.jdbc.export.ExceptionFactory;
import fr.xephi.authme.libs.org.mariadb.jdbc.export.Prepare;
import fr.xephi.authme.libs.org.mariadb.jdbc.message.ClientMessage;
import fr.xephi.authme.libs.org.mariadb.jdbc.message.server.CachedPrepareResultPacket;
import fr.xephi.authme.libs.org.mariadb.jdbc.message.server.ErrorPacket;
import fr.xephi.authme.libs.org.mariadb.jdbc.message.server.PrepareResultPacket;
import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.locks.ReentrantLock;

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

   public Completion readPacket(Statement stmt, int fetchSize, long maxRows, int resultSetConcurrency, int resultSetType, boolean closeOnCompletion, Reader reader, Writer writer, Context context, ExceptionFactory exceptionFactory, ReentrantLock lock, boolean traceEnable, ClientMessage message) throws IOException, SQLException {
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
