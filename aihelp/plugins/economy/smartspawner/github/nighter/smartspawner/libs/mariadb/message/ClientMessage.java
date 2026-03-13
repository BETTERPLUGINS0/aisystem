package github.nighter.smartspawner.libs.mariadb.message;

import github.nighter.smartspawner.libs.mariadb.BasePreparedStatement;
import github.nighter.smartspawner.libs.mariadb.Statement;
import github.nighter.smartspawner.libs.mariadb.client.ColumnDecoder;
import github.nighter.smartspawner.libs.mariadb.client.Completion;
import github.nighter.smartspawner.libs.mariadb.client.Context;
import github.nighter.smartspawner.libs.mariadb.client.ReadableByteBuf;
import github.nighter.smartspawner.libs.mariadb.client.impl.StandardReadableByteBuf;
import github.nighter.smartspawner.libs.mariadb.client.result.CompleteResult;
import github.nighter.smartspawner.libs.mariadb.client.result.StreamingResult;
import github.nighter.smartspawner.libs.mariadb.client.result.UpdatableResult;
import github.nighter.smartspawner.libs.mariadb.client.socket.Reader;
import github.nighter.smartspawner.libs.mariadb.client.socket.Writer;
import github.nighter.smartspawner.libs.mariadb.client.util.ClosableLock;
import github.nighter.smartspawner.libs.mariadb.client.util.Parameters;
import github.nighter.smartspawner.libs.mariadb.export.ExceptionFactory;
import github.nighter.smartspawner.libs.mariadb.message.server.ErrorPacket;
import github.nighter.smartspawner.libs.mariadb.message.server.OkPacket;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.function.Consumer;
import java.util.regex.Pattern;

public interface ClientMessage {
   Pattern LOAD_LOCAL_PATTERN_PARAM = Pattern.compile("^((\\s[--]|#).*(\\r\\n|\\r|\\n)|\\s*/\\*([^*]|\\*[^/])*\\*/|.)*\\s*LOAD\\s+(DATA|XML)\\s+((LOW_PRIORITY|CONCURRENT)\\s+)?LOCAL\\s+INFILE\\s+\\?", 2);

   static boolean validateLocalFileName(String sql, Parameters parameters, String fileName, Context context) {
      String escapedFileName = Pattern.quote(fileName.replace("\\", "\\\\"));
      String reg = "^((\\s[--]|#).*(\\r\\n|\\r|\\n)|\\s*/\\*([^*]|\\*[^/])*\\*/|.)*\\s*LOAD\\s+(DATA|XML)\\s+((LOW_PRIORITY|CONCURRENT)\\s+)?LOCAL\\s+INFILE\\s+'" + escapedFileName + "'";
      Pattern pattern = Pattern.compile(reg, 2);
      if (pattern.matcher(sql).find()) {
         return true;
      } else if (parameters != null && LOAD_LOCAL_PATTERN_PARAM.matcher(sql).find() && parameters.size() > 0) {
         String paramString = parameters.get(0).bestEffortStringValue(context);
         return paramString != null ? paramString.equalsIgnoreCase("'" + fileName.replace("\\", "\\\\") + "'") : true;
      } else {
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

   default Completion readPacket(Statement stmt, int fetchSize, long maxRows, int resultSetConcurrency, int resultSetType, boolean closeOnCompletion, Reader reader, Writer writer, Context context, ExceptionFactory exceptionFactory, ClosableLock lock, boolean traceEnable, ClientMessage message, Consumer<String> redirectFct) throws IOException, SQLException {
      ReadableByteBuf buf = reader.readReusablePacket(traceEnable);
      int len;
      switch(buf.getByte()) {
      case -5:
         buf.skip(1);
         SQLException exception = null;
         reader.getSequence().set(writer.getSequence());
         InputStream is = this.getLocalInfileInputStream();
         if (is == null) {
            String fileName = buf.readStringNullEnd();
            if (!message.validateLocalFileName(fileName, context)) {
               exception = exceptionFactory.withSql(this.description()).create(String.format("LOAD DATA LOCAL INFILE asked for file '%s' that doesn't correspond to initial query %s. Possible malicious proxy changing server answer ! Command interrupted", fileName, this.description()), "HY000");
            } else {
               try {
                  is = new FileInputStream(fileName);
               } catch (FileNotFoundException var28) {
                  exception = exceptionFactory.withSql(this.description()).create("Could not send file : " + var28.getMessage(), "HY000", var28);
               }
            }
         }

         if (is != null) {
            try {
               byte[] fileBuf = new byte[65536];

               while((len = ((InputStream)is).read(fileBuf)) > 0) {
                  writer.writeBytes(fileBuf, 0, len);
                  writer.flush();
               }
            } finally {
               ((InputStream)is).close();
            }
         }

         writer.writeEmptyPacket();
         Completion completion = this.readPacket(stmt, fetchSize, maxRows, resultSetConcurrency, resultSetType, closeOnCompletion, reader, writer, context, exceptionFactory, lock, traceEnable, message, redirectFct);
         if (exception != null) {
            throw exception;
         }

         return completion;
      case -1:
         ErrorPacket errorPacket = new ErrorPacket(buf, context);
         throw exceptionFactory.withSql(this.description()).create(errorPacket.getMessage(), errorPacket.getSqlState(), errorPacket.getErrorCode());
      case 0:
         OkPacket ok = OkPacket.parse(buf, context);
         int serverStatus = context.getServerStatus();
         if (context.getRedirectUrl() != null && (serverStatus & 9) == 0) {
            redirectFct.accept(context.getRedirectUrl());
         }

         return ok;
      default:
         len = buf.readIntLengthEncodedNotNull();
         ColumnDecoder[] ci;
         int status;
         if (context.canSkipMeta() && this.canSkipMeta()) {
            if (buf.readByte() == 0) {
               ci = ((BasePreparedStatement)stmt).getMeta();
            } else {
               ci = new ColumnDecoder[len];

               for(status = 0; status < len; ++status) {
                  ci[status] = (ColumnDecoder)context.getColumnDecoderFunction().apply(new StandardReadableByteBuf(reader.readPacket(traceEnable)));
               }

               ((BasePreparedStatement)stmt).updateMeta(ci);
            }
         } else {
            ci = new ColumnDecoder[len];

            for(status = 0; status < len; ++status) {
               ci[status] = (ColumnDecoder)context.getColumnDecoderFunction().apply(new StandardReadableByteBuf(reader.readPacket(traceEnable)));
            }
         }

         if (!context.isEofDeprecated()) {
            reader.readReusablePacket();
         }

         if (resultSetConcurrency == 1008) {
            return new UpdatableResult(stmt, this.binaryProtocol(), maxRows, ci, reader, context, resultSetType, closeOnCompletion, traceEnable);
         } else if (fetchSize != 0) {
            status = context.getServerStatus();
            if ((status & 8) != 0) {
               context.setServerStatus(status & -9);
            }

            return new StreamingResult(stmt, this.binaryProtocol(), maxRows, ci, reader, context, fetchSize, lock, resultSetType, closeOnCompletion, traceEnable);
         } else {
            return new CompleteResult(stmt, this.binaryProtocol(), maxRows, ci, reader, context, resultSetType, closeOnCompletion, traceEnable, this.mightBeBulkResult());
         }
      }
   }

   default InputStream getLocalInfileInputStream() {
      return null;
   }

   default boolean mightBeBulkResult() {
      return false;
   }

   default boolean validateLocalFileName(String fileName, Context context) {
      return false;
   }
}
