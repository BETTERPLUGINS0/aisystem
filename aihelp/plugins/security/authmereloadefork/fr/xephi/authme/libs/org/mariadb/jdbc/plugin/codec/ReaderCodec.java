package fr.xephi.authme.libs.org.mariadb.jdbc.plugin.codec;

import fr.xephi.authme.libs.org.mariadb.jdbc.client.ColumnDecoder;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.Context;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.DataType;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.ReadableByteBuf;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.socket.Writer;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.util.MutableInt;
import fr.xephi.authme.libs.org.mariadb.jdbc.plugin.Codec;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.sql.SQLDataException;
import java.util.Calendar;
import java.util.EnumSet;

public class ReaderCodec implements Codec<Reader> {
   public static final ReaderCodec INSTANCE = new ReaderCodec();
   private static final EnumSet<DataType> COMPATIBLE_TYPES;

   public boolean canDecode(ColumnDecoder column, Class<?> type) {
      return COMPATIBLE_TYPES.contains(column.getType()) && type.isAssignableFrom(Reader.class);
   }

   public String className() {
      return Reader.class.getName();
   }

   public Reader decodeText(ReadableByteBuf buf, MutableInt length, ColumnDecoder column, Calendar cal) throws SQLDataException {
      switch(column.getType()) {
      case BLOB:
      case TINYBLOB:
      case MEDIUMBLOB:
      case LONGBLOB:
         if (column.isBinary()) {
            buf.skip(length.get());
            throw new SQLDataException(String.format("Data type %s cannot be decoded as Reader", column.getType()));
         }
      case STRING:
      case VARCHAR:
      case VARSTRING:
         return new StringReader(buf.readString(length.get()));
      default:
         buf.skip(length.get());
         throw new SQLDataException(String.format("Data type %s cannot be decoded as Reader", column.getType()));
      }
   }

   public Reader decodeBinary(ReadableByteBuf buf, MutableInt length, ColumnDecoder column, Calendar cal) throws SQLDataException {
      return this.decodeText(buf, length, column, cal);
   }

   public boolean canEncode(Object value) {
      return value instanceof Reader;
   }

   public void encodeText(Writer encoder, Context context, Object val, Calendar cal, Long maxLen) throws IOException {
      Reader reader = (Reader)val;
      encoder.writeByte(39);
      char[] buf = new char[4096];
      int len;
      byte[] data;
      if (maxLen == null) {
         while((len = reader.read(buf)) >= 0) {
            data = (new String(buf, 0, len)).getBytes(StandardCharsets.UTF_8);
            encoder.writeBytesEscaped(data, data.length, (context.getServerStatus() & 512) != 0);
         }
      } else {
         while((len = reader.read(buf)) >= 0) {
            data = (new String(buf, 0, Math.min(len, maxLen.intValue()))).getBytes(StandardCharsets.UTF_8);
            maxLen = maxLen - (long)len;
            encoder.writeBytesEscaped(data, data.length, (context.getServerStatus() & 512) != 0);
         }
      }

      encoder.writeByte(39);
   }

   public void encodeBinary(Writer encoder, Object val, Calendar cal, Long maxLength) throws IOException {
      byte[] clobBytes = new byte[4096];
      int pos = 0;
      char[] buf = new char[4096];
      Reader reader = (Reader)val;

      int len;
      for(long maxLen = maxLength != null ? maxLength : Long.MAX_VALUE; maxLen > 0L && (len = reader.read(buf)) > 0; maxLen -= (long)len) {
         byte[] data = (new String(buf, 0, (int)Math.min((long)len, maxLen))).getBytes(StandardCharsets.UTF_8);
         if (clobBytes.length - pos < data.length) {
            byte[] newBlobBytes = new byte[clobBytes.length + 65536];
            System.arraycopy(clobBytes, 0, newBlobBytes, 0, pos);
            clobBytes = newBlobBytes;
         }

         System.arraycopy(data, 0, clobBytes, pos, data.length);
         pos += data.length;
      }

      encoder.writeLength((long)pos);
      encoder.writeBytes(clobBytes, 0, pos);
   }

   public void encodeLongData(Writer encoder, Reader reader, Long maxLength) throws IOException {
      char[] buf = new char[4096];

      int len;
      for(long maxLen = maxLength != null ? maxLength : Long.MAX_VALUE; maxLen > 0L && (len = reader.read(buf)) >= 0; maxLen -= (long)len) {
         byte[] data = (new String(buf, 0, (int)Math.min((long)len, maxLen))).getBytes(StandardCharsets.UTF_8);
         encoder.writeBytes(data, 0, data.length);
      }

   }

   public byte[] encodeData(Reader reader, Long maxLength) throws IOException {
      ByteArrayOutputStream bb = new ByteArrayOutputStream();
      char[] buf = new char[4096];

      int len;
      for(long maxLen = maxLength != null ? maxLength : Long.MAX_VALUE; maxLen > 0L && (len = reader.read(buf)) >= 0; maxLen -= (long)len) {
         byte[] data = (new String(buf, 0, (int)Math.min((long)len, maxLen))).getBytes(StandardCharsets.UTF_8);
         bb.write(data, 0, data.length);
      }

      return bb.toByteArray();
   }

   public int getBinaryEncodeType() {
      return DataType.VARSTRING.get();
   }

   public boolean canEncodeLongData() {
      return true;
   }

   static {
      COMPATIBLE_TYPES = EnumSet.of(DataType.STRING, DataType.VARCHAR, DataType.VARSTRING, DataType.BLOB, DataType.TINYBLOB, DataType.MEDIUMBLOB, DataType.LONGBLOB);
   }
}
