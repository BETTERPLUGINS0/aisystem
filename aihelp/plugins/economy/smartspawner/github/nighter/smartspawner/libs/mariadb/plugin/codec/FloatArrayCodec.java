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
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.sql.SQLDataException;
import java.util.Calendar;
import java.util.EnumSet;

public class FloatArrayCodec implements Codec<float[]> {
   public static final FloatArrayCodec INSTANCE = new FloatArrayCodec();
   private static Class<?> floatArrayClass;
   private static final EnumSet<DataType> COMPATIBLE_TYPES;
   static final int BYTES_IN_FLOAT = 4;

   public String className() {
      return float[].class.getName();
   }

   public boolean canDecode(ColumnDecoder column, Class<?> type) {
      return COMPATIBLE_TYPES.contains(column.getType()) && !type.isPrimitive() && type == floatArrayClass && type.isArray();
   }

   public boolean canEncode(Object value) {
      return value instanceof float[];
   }

   public float[] decodeText(ReadableByteBuf buf, MutableInt length, ColumnDecoder column, Calendar cal, Context context) throws SQLDataException {
      return toFloatArray(this.getBytes(buf, length, column));
   }

   public float[] decodeBinary(ReadableByteBuf buf, MutableInt length, ColumnDecoder column, Calendar cal, Context context) throws SQLDataException {
      return toFloatArray(this.getBytes(buf, length, column));
   }

   public static byte[] toByteArray(float[] floatArray) {
      ByteBuffer buffer = ByteBuffer.allocate(floatArray.length * 4);
      buffer.order(ByteOrder.LITTLE_ENDIAN);
      buffer.asFloatBuffer().put(floatArray);
      return buffer.array();
   }

   public static float[] toFloatArray(byte[] byteArray) {
      float[] result = new float[byteArray.length / 4];
      ByteBuffer.wrap(byteArray).order(ByteOrder.LITTLE_ENDIAN).asFloatBuffer().get(result, 0, result.length);
      return result;
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
      byte[] encoded = toByteArray((float[])value);
      encoder.writeBytes(ByteArrayCodec.BINARY_PREFIX);
      encoder.writeBytesEscaped(encoded, encoded.length, (context.getServerStatus() & 512) != 0);
      encoder.writeByte(39);
   }

   public int getApproximateTextProtocolLength(Object value, Long length) {
      return ((float[])value).length * 4 + 10;
   }

   public void encodeBinary(Writer encoder, Context context, Object value, Calendar cal, Long maxLength) throws IOException {
      byte[] arr = toByteArray((float[])value);
      encoder.writeLength((long)arr.length);
      encoder.writeBytes(arr);
   }

   public int getBinaryEncodeType() {
      return DataType.BLOB.get();
   }

   static {
      floatArrayClass = Array.newInstance(Float.TYPE, 0).getClass();
      COMPATIBLE_TYPES = EnumSet.of(DataType.BLOB, DataType.TINYBLOB, DataType.MEDIUMBLOB, DataType.LONGBLOB, DataType.VARSTRING, DataType.VARCHAR, DataType.STRING);
   }
}
