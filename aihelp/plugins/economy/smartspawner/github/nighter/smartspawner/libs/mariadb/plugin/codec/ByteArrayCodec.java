package github.nighter.smartspawner.libs.mariadb.plugin.codec;

import github.nighter.smartspawner.libs.mariadb.client.ColumnDecoder;
import github.nighter.smartspawner.libs.mariadb.client.Context;
import github.nighter.smartspawner.libs.mariadb.client.DataType;
import github.nighter.smartspawner.libs.mariadb.client.ReadableByteBuf;
import github.nighter.smartspawner.libs.mariadb.client.socket.Writer;
import github.nighter.smartspawner.libs.mariadb.client.util.MutableInt;
import github.nighter.smartspawner.libs.mariadb.plugin.Codec;
import java.io.IOException;
import java.sql.SQLDataException;
import java.util.Calendar;
import java.util.EnumSet;

public class ByteArrayCodec implements Codec<byte[]> {
   public static final byte[] BINARY_PREFIX = new byte[]{95, 98, 105, 110, 97, 114, 121, 32, 39};
   public static final ByteArrayCodec INSTANCE = new ByteArrayCodec();
   private static final EnumSet<DataType> COMPATIBLE_TYPES;

   public String className() {
      return "byte[]";
   }

   public boolean canDecode(ColumnDecoder column, Class<?> type) {
      return COMPATIBLE_TYPES.contains(column.getType()) && (type.isPrimitive() && type == Byte.TYPE && type.isArray() || type.isAssignableFrom(byte[].class));
   }

   public boolean canEncode(Object value) {
      return value instanceof byte[];
   }

   public byte[] decodeText(ReadableByteBuf buf, MutableInt length, ColumnDecoder column, Calendar cal, Context context) throws SQLDataException {
      return this.getBytes(buf, length, column);
   }

   private byte[] getBytes(ReadableByteBuf buf, MutableInt length, ColumnDecoder column) throws SQLDataException {
      switch(column.getType()) {
      case BIT:
      case BLOB:
      case TINYBLOB:
      case MEDIUMBLOB:
      case LONGBLOB:
      case STRING:
      case VARSTRING:
      case VARCHAR:
      case GEOMETRY:
         byte[] arr = new byte[length.get()];
         buf.readBytes(arr);
         return arr;
      default:
         buf.skip(length.get());
         throw new SQLDataException(String.format("Data type %s cannot be decoded as byte[]", column.getType()));
      }
   }

   public byte[] decodeBinary(ReadableByteBuf buf, MutableInt length, ColumnDecoder column, Calendar cal, Context context) throws SQLDataException {
      return this.getBytes(buf, length, column);
   }

   public void encodeText(Writer encoder, Context context, Object value, Calendar cal, Long maxLength) throws IOException {
      int length = ((byte[])value).length;
      encoder.writeBytes(BINARY_PREFIX);
      encoder.writeBytesEscaped((byte[])value, maxLength == null ? length : Math.min(length, maxLength.intValue()), (context.getServerStatus() & 512) != 0);
      encoder.writeByte(39);
   }

   public int getApproximateTextProtocolLength(Object value, Long length) {
      return ((byte[])value).length + 10;
   }

   public void encodeBinary(Writer encoder, Context context, Object value, Calendar cal, Long maxLength) throws IOException {
      int length = ((byte[])value).length;
      if (maxLength != null) {
         length = Math.min(length, maxLength.intValue());
      }

      encoder.writeLength((long)length);
      encoder.writeBytes((byte[])value, 0, length);
   }

   public int getBinaryEncodeType() {
      return DataType.BLOB.get();
   }

   static {
      COMPATIBLE_TYPES = EnumSet.of(DataType.BLOB, DataType.TINYBLOB, DataType.MEDIUMBLOB, DataType.LONGBLOB, DataType.BIT, DataType.GEOMETRY, DataType.VARSTRING, DataType.VARCHAR, DataType.STRING);
   }
}
