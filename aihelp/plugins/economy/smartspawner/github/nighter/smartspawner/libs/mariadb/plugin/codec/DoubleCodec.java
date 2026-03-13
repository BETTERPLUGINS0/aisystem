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

public class DoubleCodec implements Codec<Double> {
   public static final DoubleCodec INSTANCE = new DoubleCodec();
   private static final EnumSet<DataType> COMPATIBLE_TYPES;

   public String className() {
      return Double.class.getName();
   }

   public boolean canDecode(ColumnDecoder column, Class<?> type) {
      return COMPATIBLE_TYPES.contains(column.getType()) && (type.isPrimitive() && type == Double.TYPE || type.isAssignableFrom(Double.class));
   }

   public boolean canEncode(Object value) {
      return value instanceof Double;
   }

   public Double decodeText(ReadableByteBuf buf, MutableInt length, ColumnDecoder column, Calendar cal, Context context) throws SQLDataException {
      return column.decodeDoubleText(buf, length);
   }

   public Double decodeBinary(ReadableByteBuf buf, MutableInt length, ColumnDecoder column, Calendar cal, Context context) throws SQLDataException {
      return column.decodeDoubleBinary(buf, length);
   }

   public void encodeText(Writer encoder, Context context, Object value, Calendar cal, Long maxLen) throws IOException {
      encoder.writeAscii(value.toString());
   }

   public int getApproximateTextProtocolLength(Object value, Long length) {
      return value.toString().length();
   }

   public void encodeBinary(Writer encoder, Context context, Object value, Calendar cal, Long maxLength) throws IOException {
      encoder.writeDouble((Double)value);
   }

   public int getBinaryEncodeType() {
      return DataType.DOUBLE.get();
   }

   static {
      COMPATIBLE_TYPES = EnumSet.of(DataType.TINYINT, DataType.SMALLINT, DataType.MEDIUMINT, DataType.INTEGER, DataType.FLOAT, DataType.DOUBLE, DataType.BIGINT, DataType.YEAR, DataType.OLDDECIMAL, DataType.DECIMAL, DataType.VARCHAR, DataType.VARSTRING, DataType.STRING, DataType.BLOB, DataType.TINYBLOB, DataType.MEDIUMBLOB, DataType.LONGBLOB);
   }
}
