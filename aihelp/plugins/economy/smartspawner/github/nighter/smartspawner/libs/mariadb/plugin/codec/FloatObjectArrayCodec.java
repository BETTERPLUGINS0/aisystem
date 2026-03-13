package github.nighter.smartspawner.libs.mariadb.plugin.codec;

import github.nighter.smartspawner.libs.mariadb.client.ColumnDecoder;
import github.nighter.smartspawner.libs.mariadb.client.Context;
import github.nighter.smartspawner.libs.mariadb.client.DataType;
import github.nighter.smartspawner.libs.mariadb.client.ReadableByteBuf;
import github.nighter.smartspawner.libs.mariadb.client.socket.Writer;
import github.nighter.smartspawner.libs.mariadb.client.util.MutableInt;
import github.nighter.smartspawner.libs.mariadb.plugin.Codec;
import java.io.IOException;
import java.lang.reflect.Array;
import java.sql.SQLDataException;
import java.util.Calendar;
import java.util.EnumSet;

public class FloatObjectArrayCodec implements Codec<Float[]> {
   public static final FloatObjectArrayCodec INSTANCE = new FloatObjectArrayCodec();
   private static Class<?> floatArrayClass = Array.newInstance(Float.class, 0).getClass();
   private static final EnumSet<DataType> COMPATIBLE_TYPES;
   static final int BYTES_IN_FLOAT = 4;

   public String className() {
      return float[].class.getName();
   }

   public boolean canDecode(ColumnDecoder column, Class<?> type) {
      return COMPATIBLE_TYPES.contains(column.getType()) && !type.isPrimitive() && type == floatArrayClass && type.isArray();
   }

   public boolean canEncode(Object value) {
      return value instanceof Float[];
   }

   public Float[] decodeText(ReadableByteBuf buf, MutableInt length, ColumnDecoder column, Calendar cal, Context context) throws SQLDataException {
      return toFloatArray(this.getBytes(buf, length, column));
   }

   public Float[] decodeBinary(ReadableByteBuf buf, MutableInt length, ColumnDecoder column, Calendar cal, Context context) throws SQLDataException {
      return toFloatArray(this.getBytes(buf, length, column));
   }

   public static byte[] toByteArray(Float[] floatArray) {
      byte[] buf = new byte[floatArray.length * 4];
      int pos = 0;
      Float[] var3 = floatArray;
      int var4 = floatArray.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         Float f = var3[var5];
         int value = Float.floatToIntBits(f);
         buf[pos] = (byte)value;
         buf[pos + 1] = (byte)(value >> 8);
         buf[pos + 2] = (byte)(value >> 16);
         buf[pos + 3] = (byte)(value >> 24);
         pos += 4;
      }

      return buf;
   }

   public static Float[] toFloatArray(byte[] byteArray) {
      int len = (int)Math.ceil((double)byteArray.length / 4.0D);
      Float[] res = new Float[len];

      int value;
      for(int pos = 0; pos < len; res[pos++] = Float.intBitsToFloat(value)) {
         if (pos + 1 <= len) {
            value = (byteArray[pos * 4] & 255) + ((byteArray[pos * 4 + 1] & 255) << 8) + ((byteArray[pos * 4 + 2] & 255) << 16) + ((byteArray[pos * 4 + 3] & 255) << 24);
         } else {
            value = byteArray[pos * 4] & 255;
            if (pos + 1 < byteArray.length) {
               value += (byteArray[pos * 4 + 1] & 255) << 8;
            }

            if (pos + 2 < byteArray.length) {
               value += (byteArray[pos * 4 + 2] & 255) << 16;
            }
         }
      }

      return res;
   }

   private byte[] getBytes(ReadableByteBuf buf, MutableInt length, ColumnDecoder column) throws SQLDataException {
      switch(column.getType()) {
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
         throw new SQLDataException(String.format("Data type %s cannot be decoded as float[]", column.getType()));
      }
   }

   public void encodeText(Writer encoder, Context context, Object value, Calendar cal, Long maxLen) throws IOException {
      byte[] encoded = toByteArray((Float[])value);
      encoder.writeBytes(ByteArrayCodec.BINARY_PREFIX);
      encoder.writeBytesEscaped(encoded, encoded.length, (context.getServerStatus() & 512) != 0);
      encoder.writeByte(39);
   }

   public int getApproximateTextProtocolLength(Object value, Long length) {
      return ((Float[])value).length * 4 + 10;
   }

   public void encodeBinary(Writer encoder, Context context, Object value, Calendar cal, Long maxLength) throws IOException {
      byte[] arr = toByteArray((Float[])value);
      encoder.writeLength((long)arr.length);
      encoder.writeBytes(arr);
   }

   public int getBinaryEncodeType() {
      return DataType.BLOB.get();
   }

   static {
      COMPATIBLE_TYPES = EnumSet.of(DataType.BLOB, DataType.TINYBLOB, DataType.MEDIUMBLOB, DataType.LONGBLOB, DataType.VARSTRING, DataType.VARCHAR, DataType.STRING);
   }
}
