package github.nighter.smartspawner.libs.mariadb.plugin.codec;

import github.nighter.smartspawner.libs.mariadb.MariaDbBlob;
import github.nighter.smartspawner.libs.mariadb.client.ColumnDecoder;
import github.nighter.smartspawner.libs.mariadb.client.Context;
import github.nighter.smartspawner.libs.mariadb.client.DataType;
import github.nighter.smartspawner.libs.mariadb.client.ReadableByteBuf;
import github.nighter.smartspawner.libs.mariadb.client.socket.Writer;
import github.nighter.smartspawner.libs.mariadb.client.util.MutableInt;
import github.nighter.smartspawner.libs.mariadb.plugin.Codec;
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

   public Blob decodeText(ReadableByteBuf buf, MutableInt length, ColumnDecoder column, Calendar cal, Context context) throws SQLDataException {
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

   public Blob decodeBinary(ReadableByteBuf buf, MutableInt length, ColumnDecoder column, Calendar cal, Context context) throws SQLDataException {
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

   public int getApproximateTextProtocolLength(Object value, Long length) {
      return length != null ? length.intValue() + 10 : -1;
   }

   public void encodeBinary(Writer encoder, Context context, Object value, Calendar cal, Long maxLength) throws IOException, SQLException {
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
      } catch (SQLException var13) {
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
