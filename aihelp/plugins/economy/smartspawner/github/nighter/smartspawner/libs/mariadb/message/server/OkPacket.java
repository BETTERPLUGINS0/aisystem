package github.nighter.smartspawner.libs.mariadb.message.server;

import github.nighter.smartspawner.libs.mariadb.client.Completion;
import github.nighter.smartspawner.libs.mariadb.client.Context;
import github.nighter.smartspawner.libs.mariadb.client.ReadableByteBuf;
import github.nighter.smartspawner.libs.mariadb.util.log.Logger;
import github.nighter.smartspawner.libs.mariadb.util.log.Loggers;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class OkPacket implements Completion {
   private static final OkPacket BASIC_OK = new OkPacket(0L, 0L, (byte[])null);
   private static final Logger logger = Loggers.getLogger(OkPacket.class);
   private final long affectedRows;
   private final long lastInsertId;
   private final byte[] info;
   static final byte[] CHARACTER_SET_CLIENT;
   static final byte[] CONNECTION_ID;
   static final byte[] THREAD_CONNECTED;
   static final byte[] AUTO_INCREMENT_INCREMENT;
   static final byte[] MAXSCALE;
   static final byte[] REDIRECT_URL;
   static final byte[] TX_ISOLATION;
   static final byte[] TRANSACTION_ISOLATION;
   static final byte[] REPEATABLE_READ;
   static final byte[] READ_UNCOMMITTED;
   static final byte[] READ_COMMITTED;
   static final byte[] SERIALIZABLE;

   private OkPacket(long affectedRows, long lastInsertId, byte[] info) {
      this.affectedRows = affectedRows;
      this.lastInsertId = lastInsertId;
      this.info = info;
   }

   public static OkPacket parse(ReadableByteBuf buf, Context context) {
      buf.skip();
      long affectedRows = buf.readLongLengthEncodedNotNull();
      long lastInsertId = buf.readLongLengthEncodedNotNull();
      context.setServerStatus(buf.readUnsignedShort());
      context.setWarning(buf.readUnsignedShort());
      if (buf.readableBytes() > 0) {
         buf.skip(buf.readIntLengthEncodedNotNull());
         if (context.hasClientCapability(8388608L)) {
            label113:
            while(buf.readableBytes() > 0) {
               ReadableByteBuf sessionStateBuf = buf.readLengthBuffer();

               while(true) {
                  label104:
                  while(true) {
                     if (sessionStateBuf.readableBytes() <= 0) {
                        continue label113;
                     }

                     switch(sessionStateBuf.readByte()) {
                     case 0:
                        while(true) {
                           ReadableByteBuf tmpBufsv = sessionStateBuf.readLengthBuffer();
                           int len = tmpBufsv.readIntLengthEncodedNotNull();
                           byte[] variableBytes = new byte[len];
                           tmpBufsv.readBytes(variableBytes);
                           Integer lenSv = tmpBufsv.readLength();
                           byte[] valueBytes;
                           if (lenSv == null) {
                              valueBytes = null;
                           } else {
                              valueBytes = new byte[lenSv];
                              tmpBufsv.readBytes(valueBytes);
                           }

                           if (logger.isDebugEnabled()) {
                              logger.debug("System variable change:  {} = {}", new String(variableBytes, 0, len), valueBytes == null ? "null" : new String(valueBytes, 0, lenSv));
                           }

                           if (Arrays.equals(CHARACTER_SET_CLIENT, variableBytes)) {
                              context.setCharset(new String(valueBytes, 0, lenSv));
                           } else if (Arrays.equals(CONNECTION_ID, variableBytes)) {
                              context.setThreadId(Long.parseLong(new String(valueBytes, 0, lenSv)));
                           } else if (Arrays.equals(THREAD_CONNECTED, variableBytes)) {
                              context.setTreadsConnected(Long.parseLong(new String(valueBytes, 0, lenSv)));
                           } else if (Arrays.equals(AUTO_INCREMENT_INCREMENT, variableBytes)) {
                              context.setAutoIncrement(Long.parseLong(new String(valueBytes, 0, lenSv)));
                           } else if (Arrays.equals(MAXSCALE, variableBytes)) {
                              context.setMaxscaleVersion(new String(valueBytes, 0, lenSv));
                           } else if (Arrays.equals(REDIRECT_URL, variableBytes)) {
                              if (lenSv != null && lenSv > 0) {
                                 context.setRedirectUrl(new String(valueBytes, 0, lenSv));
                              }
                           } else if (Arrays.equals(TX_ISOLATION, variableBytes) || Arrays.equals(TRANSACTION_ISOLATION, variableBytes)) {
                              if (Arrays.equals(REPEATABLE_READ, valueBytes)) {
                                 context.setTransactionIsolationLevel(4);
                              } else if (Arrays.equals(READ_UNCOMMITTED, valueBytes)) {
                                 context.setTransactionIsolationLevel(1);
                              } else if (Arrays.equals(READ_COMMITTED, valueBytes)) {
                                 context.setTransactionIsolationLevel(2);
                              } else if (Arrays.equals(SERIALIZABLE, valueBytes)) {
                                 context.setTransactionIsolationLevel(8);
                              } else {
                                 context.setTransactionIsolationLevel((Integer)null);
                              }
                           }

                           if (tmpBufsv.readableBytes() <= 0) {
                              continue label104;
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

      return affectedRows == 0L && lastInsertId == 0L ? BASIC_OK : new OkPacket(affectedRows, lastInsertId, (byte[])null);
   }

   public static OkPacket parseWithInfo(ReadableByteBuf buf, Context context) {
      buf.skip();
      long affectedRows = buf.readLongLengthEncodedNotNull();
      long lastInsertId = buf.readLongLengthEncodedNotNull();
      context.setServerStatus(buf.readUnsignedShort());
      context.setWarning(buf.readUnsignedShort());
      byte[] info;
      if (buf.readableBytes() > 0) {
         info = new byte[buf.readIntLengthEncodedNotNull()];
         buf.readBytes(info);
         if (context.hasClientCapability(8388608L)) {
            label104:
            while(buf.readableBytes() > 0) {
               ReadableByteBuf sessionStateBuf = buf.readLengthBuffer();

               while(true) {
                  label95:
                  while(true) {
                     if (sessionStateBuf.readableBytes() <= 0) {
                        continue label104;
                     }

                     switch(sessionStateBuf.readByte()) {
                     case 0:
                        while(true) {
                           ReadableByteBuf tmpBufsv = sessionStateBuf.readLengthBuffer();
                           int len = tmpBufsv.readIntLengthEncodedNotNull();
                           byte[] variableBytes = new byte[len];
                           tmpBufsv.readBytes(variableBytes);
                           Integer lenSv = tmpBufsv.readLength();
                           byte[] valueBytes;
                           if (lenSv == null) {
                              valueBytes = null;
                           } else {
                              valueBytes = new byte[lenSv];
                              tmpBufsv.readBytes(valueBytes);
                           }

                           if (logger.isDebugEnabled()) {
                              logger.debug("System variable change:  {} = {}", new String(variableBytes, 0, len), valueBytes == null ? "null" : new String(valueBytes, 0, lenSv));
                           }

                           if (Arrays.equals(CHARACTER_SET_CLIENT, variableBytes)) {
                              context.setCharset(new String(valueBytes, 0, lenSv));
                           } else if (Arrays.equals(CONNECTION_ID, variableBytes)) {
                              context.setThreadId(Long.parseLong(new String(valueBytes, 0, lenSv)));
                           } else if (Arrays.equals(THREAD_CONNECTED, variableBytes)) {
                              context.setTreadsConnected(Long.parseLong(new String(valueBytes, 0, lenSv)));
                           } else if (Arrays.equals(AUTO_INCREMENT_INCREMENT, variableBytes)) {
                              context.setAutoIncrement(Long.parseLong(new String(valueBytes, 0, lenSv)));
                           } else if (Arrays.equals(REDIRECT_URL, variableBytes)) {
                              if (lenSv != null && lenSv > 0) {
                                 context.setRedirectUrl(new String(valueBytes, 0, lenSv));
                              }
                           } else if (Arrays.equals(TX_ISOLATION, variableBytes) || Arrays.equals(TRANSACTION_ISOLATION, variableBytes)) {
                              if (Arrays.equals(REPEATABLE_READ, valueBytes)) {
                                 context.setTransactionIsolationLevel(4);
                              } else if (Arrays.equals(READ_UNCOMMITTED, valueBytes)) {
                                 context.setTransactionIsolationLevel(1);
                              } else if (Arrays.equals(READ_COMMITTED, valueBytes)) {
                                 context.setTransactionIsolationLevel(2);
                              } else if (Arrays.equals(SERIALIZABLE, valueBytes)) {
                                 context.setTransactionIsolationLevel(8);
                              } else {
                                 context.setTransactionIsolationLevel((Integer)null);
                              }
                           }

                           if (tmpBufsv.readableBytes() <= 0) {
                              continue label95;
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
      } else {
         info = new byte[0];
      }

      return new OkPacket(affectedRows, lastInsertId, info);
   }

   public long getAffectedRows() {
      return this.affectedRows;
   }

   public long getLastInsertId() {
      return this.lastInsertId;
   }

   public byte[] getInfo() {
      return this.info;
   }

   static {
      CHARACTER_SET_CLIENT = "character_set_client".getBytes(StandardCharsets.UTF_8);
      CONNECTION_ID = "connection_id".getBytes(StandardCharsets.UTF_8);
      THREAD_CONNECTED = "threads_Connected".getBytes(StandardCharsets.UTF_8);
      AUTO_INCREMENT_INCREMENT = "auto_increment_increment".getBytes(StandardCharsets.UTF_8);
      MAXSCALE = "maxscale".getBytes(StandardCharsets.UTF_8);
      REDIRECT_URL = "redirect_url".getBytes(StandardCharsets.UTF_8);
      TX_ISOLATION = "tx_isolation".getBytes(StandardCharsets.UTF_8);
      TRANSACTION_ISOLATION = "transaction_isolation".getBytes(StandardCharsets.UTF_8);
      REPEATABLE_READ = "REPEATABLE-READ".getBytes(StandardCharsets.UTF_8);
      READ_UNCOMMITTED = "READ-UNCOMMITTED".getBytes(StandardCharsets.UTF_8);
      READ_COMMITTED = "READ-COMMITTED".getBytes(StandardCharsets.UTF_8);
      SERIALIZABLE = "SERIALIZABLE".getBytes(StandardCharsets.UTF_8);
   }
}
