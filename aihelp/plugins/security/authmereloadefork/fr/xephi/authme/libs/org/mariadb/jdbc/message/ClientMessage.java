package fr.xephi.authme.libs.org.mariadb.jdbc.message;

import fr.xephi.authme.libs.org.mariadb.jdbc.BasePreparedStatement;
import fr.xephi.authme.libs.org.mariadb.jdbc.Statement;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.ColumnDecoder;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.Completion;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.Context;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.ReadableByteBuf;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.impl.StandardReadableByteBuf;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.result.CompleteResult;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.result.StreamingResult;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.result.UpdatableResult;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.socket.Reader;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.socket.Writer;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.util.Parameters;
import fr.xephi.authme.libs.org.mariadb.jdbc.export.ExceptionFactory;
import fr.xephi.authme.libs.org.mariadb.jdbc.message.server.ErrorPacket;
import fr.xephi.authme.libs.org.mariadb.jdbc.message.server.OkPacket;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Pattern;

public interface ClientMessage {
   static boolean validateLocalFileName(String sql, Parameters parameters, String fileName, Context context) {
      String reg = "^(\\s*/\\*([^*]|\\*[^/])*\\*/)*\\s*LOAD\\s+(DATA|XML)\\s+((LOW_PRIORITY|CONCURRENT)\\s+)?LOCAL\\s+INFILE\\s+'" + Pattern.quote(fileName.replace("\\", "\\\\")) + "'";
      Pattern pattern = Pattern.compile(reg, 2);
      if (pattern.matcher(sql).find()) {
         return true;
      } else {
         if (parameters != null) {
            pattern = Pattern.compile("^(\\s*/\\*([^*]|\\*[^/])*\\*/)*\\s*LOAD\\s+(DATA|XML)\\s+((LOW_PRIORITY|CONCURRENT)\\s+)?LOCAL\\s+INFILE\\s+\\?", 2);
            if (pattern.matcher(sql).find() && parameters.size() > 0) {
               String paramString = parameters.get(0).bestEffortStringValue(context);
               if (paramString != null) {
                  return paramString.toLowerCase().equals("'" + fileName.replace("\\", "\\\\").toLowerCase() + "'");
               }

               return true;
            }
         }

         return false;
      }
   }

   int encode(Writer var1, Context var2) throws IOException, SQLException;

   default int batchUpdateLength() {
      return 0;
   }

   default String description() {
      return null;
   }

   default boolean binaryProtocol() {
      return false;
   }

   default boolean canSkipMeta() {
      return false;
   }

   default Completion readPacket(Statement stmt, int fetchSize, long maxRows, int resultSetConcurrency, int resultSetType, boolean closeOnCompletion, Reader reader, Writer writer, Context context, ExceptionFactory exceptionFactory, ReentrantLock lock, boolean traceEnable, ClientMessage message) throws IOException, SQLException {
      ReadableByteBuf buf = reader.readReusablePacket(traceEnable);
      int len;
      switch(buf.getByte()) {
      case -5:
         buf.skip(1);
         SQLException exception = null;
         reader.getSequence().set((byte)1);
         InputStream is = this.getLocalInfileInputStream();
         if (is == null) {
            String fileName = buf.readStringNullEnd();
            if (!message.validateLocalFileName(fileName, context)) {
               exception = exceptionFactory.withSql(this.description()).create(String.format("LOAD DATA LOCAL INFILE asked for file '%s' that doesn't correspond to initial query %s. Possible malicious proxy changing server answer ! Command interrupted", fileName, this.description()), "HY000");
            } else {
               try {
                  is = new FileInputStream(fileName);
               } catch (FileNotFoundException var25) {
                  exception = exceptionFactory.withSql(this.description()).create("Could not send file : " + var25.getMessage(), "HY000", var25);
               }
            }
         }

         if (is != null) {
            try {
               byte[] fileBuf = new byte[8192];

               while((len = ((InputStream)is).read(fileBuf)) > 0) {
                  writer.writeBytes(fileBuf, 0, len);
                  writer.flush();
               }
            } finally {
               ((InputStream)is).close();
            }
         }

         writer.writeEmptyPacket();
         Completion completion = this.readPacket(stmt, fetchSize, maxRows, resultSetConcurrency, resultSetType, closeOnCompletion, reader, writer, context, exceptionFactory, lock, traceEnable, message);
         if (exception != null) {
            throw exception;
         }

         return completion;
      case -1:
         ErrorPacket errorPacket = new ErrorPacket(buf, context);
         throw exceptionFactory.withSql(this.description()).create(errorPacket.getMessage(), errorPacket.getSqlState(), errorPacket.getErrorCode());
      case 0:
         return new OkPacket(buf, context);
      default:
         len = buf.readIntLengthEncodedNotNull();
         ColumnDecoder[] ci;
         int i;
         if (context.canSkipMeta() && this.canSkipMeta()) {
            if (buf.readByte() == 0) {
               ci = ((BasePreparedStatement)stmt).getMeta();
            } else {
               ci = new ColumnDecoder[len];

               for(i = 0; i < len; ++i) {
                  ci[i] = (ColumnDecoder)context.getColumnDecoderFunction().apply(new StandardReadableByteBuf(reader.readPacket(traceEnable)));
               }

               ((BasePreparedStatement)stmt).updateMeta(ci);
            }
         } else {
            ci = new ColumnDecoder[len];

            for(i = 0; i < len; ++i) {
               ci[i] = (ColumnDecoder)context.getColumnDecoderFunction().apply(new StandardReadableByteBuf(reader.readPacket(traceEnable)));
            }
         }

         if (!context.isEofDeprecated()) {
            reader.skipPacket();
         }

         if (resultSetConcurrency == 1008) {
            return new UpdatableResult(stmt, this.binaryProtocol(), maxRows, ci, reader, context, resultSetType, closeOnCompletion, traceEnable);
         } else if (fetchSize != 0) {
            if ((context.getServerStatus() & 8) > 0) {
               context.setServerStatus(context.getServerStatus() - 8);
            }

            return new StreamingResult(stmt, this.binaryProtocol(), maxRows, ci, reader, context, fetchSize, lock, resultSetType, closeOnCompletion, traceEnable);
         } else {
            return new CompleteResult(stmt, this.binaryProtocol(), maxRows, ci, reader, context, resultSetType, closeOnCompletion, traceEnable);
         }
      }
   }

   default InputStream getLocalInfileInputStream() {
      return null;
   }

   default boolean validateLocalFileName(String fileName, Context context) {
      return false;
   }
}
