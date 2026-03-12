package fr.xephi.authme.libs.org.mariadb.jdbc.plugin.codec;

import fr.xephi.authme.libs.org.mariadb.jdbc.client.ColumnDecoder;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.Context;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.DataType;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.ReadableByteBuf;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.socket.Writer;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.util.MutableInt;
import fr.xephi.authme.libs.org.mariadb.jdbc.plugin.Codec;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLDataException;
import java.util.Calendar;
import java.util.EnumSet;

public class StreamCodec implements Codec<InputStream> {
   public static final StreamCodec INSTANCE = new StreamCodec();
   private static final EnumSet<DataType> COMPATIBLE_TYPES;

   public String className() {
      return InputStream.class.getName();
   }

   public boolean canDecode(ColumnDecoder column, Class<?> type) {
      return COMPATIBLE_TYPES.contains(column.getType()) && type.isAssignableFrom(InputStream.class);
   }

   public InputStream decodeText(ReadableByteBuf buf, MutableInt length, ColumnDecoder column, Calendar cal) throws SQLDataException {
      switch(column.getType()) {
      case STRING:
      case VARCHAR:
      case VARSTRING:
      case BLOB:
      case TINYBLOB:
      case MEDIUMBLOB:
      case LONGBLOB:
         ByteArrayInputStream is = new ByteArrayInputStream(buf.buf(), buf.pos(), length.get());
         buf.skip(length.get());
         return is;
      default:
         buf.skip(length.get());
         throw new SQLDataException(String.format("Data type %s cannot be decoded as Stream", column.getType()));
      }
   }

   public InputStream decodeBinary(ReadableByteBuf buf, MutableInt length, ColumnDecoder column, Calendar cal) throws SQLDataException {
      switch(column.getType()) {
      case STRING:
      case VARCHAR:
      case VARSTRING:
      case BLOB:
      case TINYBLOB:
      case MEDIUMBLOB:
      case LONGBLOB:
         ByteArrayInputStream is = new ByteArrayInputStream(buf.buf(), buf.pos(), length.get());
         buf.skip(length.get());
         return is;
      default:
         buf.skip(length.get());
         throw new SQLDataException(String.format("Data type %s cannot be decoded as Stream", column.getType()));
      }
   }

   public boolean canEncode(Object value) {
      return value instanceof InputStream;
   }

   public void encodeText(Writer encoder, Context context, Object value, Calendar cal, Long maxLen) throws IOException {
      encoder.writeBytes(ByteArrayCodec.BINARY_PREFIX);
      byte[] array = new byte[4096];
      InputStream stream = (InputStream)value;
      int len;
      if (maxLen == null) {
         while((len = stream.read(array)) > 0) {
            encoder.writeBytesEscaped(array, len, (context.getServerStatus() & 512) != 0);
         }
      } else {
         while((len = stream.read(array)) > 0 && maxLen > 0L) {
            encoder.writeBytesEscaped(array, Math.min(len, maxLen.intValue()), (context.getServerStatus() & 512) != 0);
            maxLen = maxLen - (long)len;
         }
      }

      encoder.writeByte(39);
   }

   public void encodeBinary(Writer encoder, Object value, Calendar cal, Long maxLength) throws IOException {
      byte[] blobBytes = new byte[4096];
      int pos = 0;
      byte[] array = new byte[4096];
      InputStream stream = (InputStream)value;
      int len;
      if (maxLength == null) {
         while((len = stream.read(array)) > 0) {
            if (blobBytes.length - pos < len) {
               byte[] newBlobBytes = new byte[blobBytes.length + 65536];
               System.arraycopy(blobBytes, 0, newBlobBytes, 0, blobBytes.length);
               blobBytes = newBlobBytes;
            }

            System.arraycopy(array, 0, blobBytes, pos, len);
            pos += len;
         }
      } else {
         for(long remainingLen = maxLength; (len = stream.read(array)) > 0 && remainingLen > 0L; remainingLen -= (long)len) {
            len = Math.min((int)remainingLen, len);
            if (blobBytes.length - pos < len) {
               byte[] newBlobBytes = new byte[blobBytes.length + 65536];
               System.arraycopy(blobBytes, 0, newBlobBytes, 0, blobBytes.length);
               blobBytes = newBlobBytes;
            }

            System.arraycopy(array, 0, blobBytes, pos, len);
            pos += len;
         }
      }

      encoder.writeLength((long)pos);
      encoder.writeBytes(blobBytes, 0, pos);
   }

   public void encodeLongData(Writer encoder, InputStream value, Long maxLength) throws IOException {
      byte[] array = new byte[4096];
      int len;
      if (maxLength == null) {
         while((len = value.read(array)) > 0) {
            encoder.writeBytes(array, 0, len);
         }
      } else {
         for(long maxLen = maxLength; (len = value.read(array)) > 0 && maxLen > 0L; maxLen -= (long)len) {
            encoder.writeBytes(array, 0, Math.min(len, (int)maxLen));
         }
      }

   }

   public byte[] encodeData(InputStream value, Long maxLength) throws IOException {
      ByteArrayOutputStream bb = new ByteArrayOutputStream();
      byte[] array = new byte[4096];
      int len;
      if (maxLength == null) {
         while((len = value.read(array)) > 0) {
            bb.write(array, 0, len);
         }
      } else {
         for(long maxLen = maxLength; (len = value.read(array)) > 0 && maxLen > 0L; maxLen -= (long)len) {
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
      COMPATIBLE_TYPES = EnumSet.of(DataType.VARCHAR, DataType.VARSTRING, DataType.STRING, DataType.BLOB, DataType.TINYBLOB, DataType.MEDIUMBLOB, DataType.LONGBLOB);
   }
}
