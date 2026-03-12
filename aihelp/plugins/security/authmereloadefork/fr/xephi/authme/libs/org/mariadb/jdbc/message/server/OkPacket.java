package fr.xephi.authme.libs.org.mariadb.jdbc.message.server;

import fr.xephi.authme.libs.org.mariadb.jdbc.client.Completion;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.Context;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.ReadableByteBuf;
import fr.xephi.authme.libs.org.mariadb.jdbc.util.log.Logger;
import fr.xephi.authme.libs.org.mariadb.jdbc.util.log.Loggers;

public class OkPacket implements Completion {
   private static final Logger logger = Loggers.getLogger(OkPacket.class);
   private final long affectedRows;
   private final long lastInsertId;

   public OkPacket(ReadableByteBuf buf, Context context) {
      buf.skip();
      this.affectedRows = buf.readLongLengthEncodedNotNull();
      this.lastInsertId = buf.readLongLengthEncodedNotNull();
      context.setServerStatus(buf.readUnsignedShort());
      context.setWarning(buf.readUnsignedShort());
      if (buf.readableBytes() > 0 && context.hasClientCapability(8388608L)) {
         buf.skip(buf.readIntLengthEncodedNotNull());

         label73:
         while(buf.readableBytes() > 0) {
            ReadableByteBuf sessionStateBuf = buf.readLengthBuffer();

            while(true) {
               label64:
               while(true) {
                  if (sessionStateBuf.readableBytes() <= 0) {
                     continue label73;
                  }

                  switch(sessionStateBuf.readByte()) {
                  case 0:
                     while(true) {
                        ReadableByteBuf tmpBufsv = sessionStateBuf.readLengthBuffer();
                        String variableSv = tmpBufsv.readString(tmpBufsv.readIntLengthEncodedNotNull());
                        Integer lenSv = tmpBufsv.readLength();
                        String valueSv = lenSv == null ? null : tmpBufsv.readString(lenSv);
                        logger.debug("System variable change:  {} = {}", variableSv, valueSv);
                        byte var9 = -1;
                        switch(variableSv.hashCode()) {
                        case -1872494125:
                           if (variableSv.equals("threads_Connected")) {
                              var9 = 2;
                           }
                           break;
                        case -513204708:
                           if (variableSv.equals("connection_id")) {
                              var9 = 1;
                           }
                           break;
                        case 293703695:
                           if (variableSv.equals("auto_increment_increment")) {
                              var9 = 3;
                           }
                           break;
                        case 2123110686:
                           if (variableSv.equals("character_set_client")) {
                              var9 = 0;
                           }
                        }

                        switch(var9) {
                        case 0:
                           context.setCharset(valueSv);
                           break;
                        case 1:
                           context.setThreadId(Long.parseLong(valueSv));
                           break;
                        case 2:
                           context.setTreadsConnected(Long.parseLong(valueSv));
                           break;
                        case 3:
                           context.setAutoIncrement(Long.parseLong(valueSv));
                        }

                        if (tmpBufsv.readableBytes() <= 0) {
                           continue label64;
                        }
                     }
                  case 1:
                     sessionStateBuf.readIntLengthEncodedNotNull();
                     Integer dbLen = sessionStateBuf.readLength();
                     String database = dbLen != null && dbLen != 0 ? sessionStateBuf.readString(dbLen) : null;
                     context.setDatabase(database);
                     logger.debug("Database change: is '{}'", database);
                     break;
                  default:
                     buf.skip(buf.readIntLengthEncodedNotNull());
                  }
               }
            }
         }
      }

   }

   public long getAffectedRows() {
      return this.affectedRows;
   }

   public long getLastInsertId() {
      return this.lastInsertId;
   }
}
