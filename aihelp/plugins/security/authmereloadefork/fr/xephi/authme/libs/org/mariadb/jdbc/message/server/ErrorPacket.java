package fr.xephi.authme.libs.org.mariadb.jdbc.message.server;

import fr.xephi.authme.libs.org.mariadb.jdbc.client.Context;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.ReadableByteBuf;
import fr.xephi.authme.libs.org.mariadb.jdbc.message.ServerMessage;
import fr.xephi.authme.libs.org.mariadb.jdbc.util.log.Logger;
import fr.xephi.authme.libs.org.mariadb.jdbc.util.log.Loggers;

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
