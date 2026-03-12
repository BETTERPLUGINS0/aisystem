package fr.xephi.authme.libs.org.mariadb.jdbc.plugin.codec;

import fr.xephi.authme.libs.org.mariadb.jdbc.MariaDbBlob;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.ColumnDecoder;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.Context;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.DataType;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.ReadableByteBuf;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.socket.Writer;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.util.MutableInt;
import fr.xephi.authme.libs.org.mariadb.jdbc.plugin.Codec;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.SQLDataException;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.EnumSet;

public class BlobCodec implements Codec<Blob> {
   public static final BlobCodec INSTANCE = new BlobCodec();
   private static final EnumSet<DataType> COMPATIBLE_TYPES;

   public String className() {
      return Blob.class.getName();
   }

   public boolean canDecode(ColumnDecoder column, Class<?> type) {
      return COMPATIBLE_TYPES.contains(column.getType()) && type.isAssignableFrom(Blob.class);
   }

   public boolean canEncode(Object value) {
      return value instanceof Blob && !(value instanceof Clob);
   }

   public Blob decodeText(ReadableByteBuf buf, MutableInt length, ColumnDecoder column, Calendar cal) throws SQLDataException {
      switch(column.getType()) {
      case STRING:
      case VARCHAR:
      case VARSTRING:
      case BIT:
      case TINYBLOB:
      case MEDIUMBLOB:
      case LONGBLOB:
      case BLOB:
      case GEOMETRY:
         return buf.readBlob(length.get());
      default:
         buf.skip(length.get());
         throw new SQLDataException(String.format("Data type %s cannot be decoded as Blob", column.getType()));
      }
   }

   public Blob decodeBinary(ReadableByteBuf buf, MutableInt length, ColumnDecoder column, Calendar cal) throws SQLDataException {
      switch(column.getType()) {
      case STRING:
      case VARCHAR:
      case VARSTRING:
      case BIT:
      case TINYBLOB:
      case MEDIUMBLOB:
      case LONGBLOB:
      case BLOB:
      case GEOMETRY:
         buf.skip(length.get());
         return new MariaDbBlob(buf.buf(), buf.pos() - length.get(), length.get());
      default:
         buf.skip(length.get());
         throw new SQLDataException(String.format("Data type %s cannot be decoded as Blob", column.getType()));
      }
   }

   public void encodeText(Writer encoder, Context context, Object value, Calendar cal, Long maxLength) throws IOException, SQLException {
      encoder.writeBytes(ByteArrayCodec.BINARY_PREFIX);
      byte[] array = new byte[4096];
      InputStream is = ((Blob)value).getBinaryStream();
      int len;
      if (maxLength == null) {
         while((len = is.read(array)) > 0) {
            encoder.writeBytesEscaped(array, len, (context.getServerStatus() & 512) != 0);
         }
      } else {
         for(long maxLen = maxLength; maxLen > 0L && (len = is.read(array)) > 0; maxLen -= (long)len) {
            encoder.writeBytesEscaped(array, Math.min(len, (int)maxLen), (context.getServerStatus() & 512) != 0);
         }
      }

      encoder.writeByte(39);
   }

   public void encodeBinary(Writer encoder, Object value, Calendar cal, Long maxLength) throws IOException, SQLException {
      InputStream is = ((Blob)value).getBinaryStream();

      try {
         long length = ((Blob)value).length();
         if (maxLength != null) {
            length = Math.min(maxLength, length);
         }

         encoder.writeLength(length);
         byte[] array = new byte[4096];
         long remainingLen = length;

         int len;
         while((len = is.read(array)) > 0) {
            encoder.writeBytes(array, 0, Math.min((int)remainingLen, len));
            remainingLen -= (long)len;
            if (remainingLen < 0L) {
               break;
            }
         }
      } catch (SQLException var12) {
         byte[] val = this.encode(is, maxLength);
         encoder.writeLength((long)val.length);
         encoder.writeBytes(val, 0, val.length);
      }

   }

   public void encodeLongData(Writer encoder, Blob value, Long maxLength) throws IOException, SQLException {
      byte[] array = new byte[4096];
      InputStream is = value.getBinaryStream();
      int len;
      int len;
      if (maxLength == null) {
         while((len = is.read(array)) > 0) {
            encoder.writeBytes(array, 0, len);
         }
      } else {
         for(long maxLen = maxLength; maxLen > 0L && (len = is.read(array)) > 0; maxLen -= (long)len) {
            encoder.writeBytes(array, 0, Math.min(len, (int)maxLen));
         }
      }

   }

   public byte[] encodeData(Blob value, Long maxLength) throws IOException, SQLException {
      return this.encode(value.getBinaryStream(), maxLength);
   }

   private byte[] encode(InputStream is, Long maxLength) throws IOException {
      ByteArrayOutputStream bb = new ByteArrayOutputStream();
      byte[] array = new byte[4096];
      int len;
      int len;
      if (maxLength == null) {
         while((len = is.read(array)) > 0) {
            bb.write(array, 0, len);
         }
      } else {
         for(long maxLen = maxLength; maxLen > 0L && (len = is.read(array)) > 0; maxLen -= (long)len) {
            bb.write(array, 0, Math.min(len, (int)maxLen));
         }
      }

      return bb.toByteArray();
   }

   public int getBinaryEncodeType() {
      return DataType.BLOB.get();
   }

   public boolean canEncodeLongData() {
      return true;
   }

   static {
      COMPATIBLE_TYPES = EnumSet.of(DataType.BIT, DataType.BLOB, DataType.TINYBLOB, DataType.MEDIUMBLOB, DataType.LONGBLOB, DataType.STRING, DataType.VARSTRING, DataType.VARCHAR);
   }
}
