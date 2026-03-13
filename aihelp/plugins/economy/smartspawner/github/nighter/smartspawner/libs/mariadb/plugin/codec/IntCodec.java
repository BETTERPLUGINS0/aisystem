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

public class IntCodec implements Codec<Integer> {
   public static final IntCodec INSTANCE = new IntCodec();
   private static final EnumSet<DataType> COMPATIBLE_TYPES;

   public String className() {
      return Integer.class.getName();
   }

   public boolean canDecode(ColumnDecoder column, Class<?> type) {
      return COMPATIBLE_TYPES.contains(column.getType()) && (type.isPrimitive() && type == Integer.TYPE || type.isAssignableFrom(Integer.class));
   }

   public boolean canEncode(Object value) {
      return value instanceof Integer;
   }

   public Integer decodeText(ReadableByteBuf buf, MutableInt length, ColumnDecoder column, Calendar cal, Context context) throws SQLDataException {
      return column.decodeIntText(buf, length);
   }

   public Integer decodeBinary(ReadableByteBuf buf, MutableInt length, ColumnDecoder column, Calendar cal, Context context) throws SQLDataException {
      return column.decodeIntBinary(buf, length);
   }

   public void encodeText(Writer encoder, Context context, Object value, Calendar cal, Long maxLen) throws IOException {
      encoder.writeAscii(value.toString());
   }

   public int getApproximateTextProtocolLength(Object value, Long length) {
      return value.toString().length();
   }

   public void encodeBinary(Writer encoder, Context context, Object value, Calendar cal, Long maxLength) throws IOException {
      encoder.writeInt((Integer)value);
   }

   public int getBinaryEncodeType() {
      return DataType.INTEGER.get();
   }

   static {
      COMPATIBLE_TYPES = EnumSet.of(DataType.FLOAT, DataType.DOUBLE, DataType.OLDDECIMAL, DataType.VARCHAR, DataType.DECIMAL, DataType.ENUM, DataType.VARSTRING, DataType.STRING, DataType.TINYINT, DataType.SMALLINT, DataType.MEDIUMINT, DataType.INTEGER, DataType.BIGINT, DataType.BIT, DataType.YEAR, DataType.BLOB, DataType.TINYBLOB, DataType.MEDIUMBLOB, DataType.LONGBLOB);
   }
}
