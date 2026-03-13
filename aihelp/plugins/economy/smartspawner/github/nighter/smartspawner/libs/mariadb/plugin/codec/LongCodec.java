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

public class LongCodec implements Codec<Long> {
   public static final LongCodec INSTANCE = new LongCodec();
   private static final EnumSet<DataType> COMPATIBLE_TYPES;

   public String className() {
      return Long.class.getName();
   }

   public boolean canDecode(ColumnDecoder column, Class<?> type) {
      return COMPATIBLE_TYPES.contains(column.getType()) && (type.isPrimitive() && type == Integer.TYPE || type.isAssignableFrom(Long.class));
   }

   public boolean canEncode(Object value) {
      return value instanceof Long;
   }

   public Long decodeText(ReadableByteBuf buf, MutableInt length, ColumnDecoder column, Calendar cal, Context context) throws SQLDataException {
      return column.decodeLongText(buf, length);
   }

   public Long decodeBinary(ReadableByteBuf buf, MutableInt length, ColumnDecoder column, Calendar cal, Context context) throws SQLDataException {
      return column.decodeLongBinary(buf, length);
   }

   public void encodeText(Writer encoder, Context context, Object value, Calendar cal, Long maxLen) throws IOException {
      encoder.writeAscii(value.toString());
   }

   public int getApproximateTextProtocolLength(Object value, Long length) {
      return value.toString().length();
   }

   public void encodeBinary(Writer encoder, Context context, Object value, Calendar cal, Long maxLength) throws IOException {
      encoder.writeLong((Long)value);
   }

   public int getBinaryEncodeType() {
      return DataType.BIGINT.get();
   }

   static {
      COMPATIBLE_TYPES = EnumSet.of(DataType.FLOAT, DataType.DOUBLE, DataType.OLDDECIMAL, DataType.VARCHAR, DataType.DECIMAL, DataType.ENUM, DataType.VARSTRING, DataType.STRING, DataType.TINYINT, DataType.SMALLINT, DataType.MEDIUMINT, DataType.INTEGER, DataType.BIGINT, DataType.BIT, DataType.YEAR, DataType.BLOB, DataType.TINYBLOB, DataType.MEDIUMBLOB, DataType.LONGBLOB);
   }
}
