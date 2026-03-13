package github.nighter.smartspawner.libs.mariadb.message.server;

import github.nighter.smartspawner.libs.mariadb.client.Context;
import github.nighter.smartspawner.libs.mariadb.client.ReadableByteBuf;
import github.nighter.smartspawner.libs.mariadb.message.ServerMessage;
import github.nighter.smartspawner.libs.mariadb.util.log.Logger;
import github.nighter.smartspawner.libs.mariadb.util.log.Loggers;

public final class ErrorPacket implements ServerMessage {
   private static final Logger logger = Loggers.getLogger(ErrorPacket.class);
   private final short errorCode;
   private final String message;
   private final String sqlState;

   public ErrorPacket(ReadableByteBuf buf, Context context) {
      buf.skip();
      this.errorCode = buf.readShort();
      byte next = buf.getByte(buf.pos());
      if (next == 35) {
         buf.skip();
         this.sqlState = buf.readAscii(5);
         this.message = buf.readStringEof();
      } else {
         this.message = buf.readStringEof();
         this.sqlState = "HY000";
      }

      if (logger.isWarnEnabled()) {
         logger.warn("Error: {}-{}: {}", this.errorCode, this.sqlState, this.message);
      }

      if (context != null) {
         int serverStatus = context.getServerStatus();
         serverStatus |= 1;
         context.setServerStatus(serverStatus);
      }

   }

   public short getErrorCode() {
      return this.errorCode;
   }

   public String getMessage() {
      return this.message;
   }

   public String getSqlState() {
      return this.sqlState;
   }
}
