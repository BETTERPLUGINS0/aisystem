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

public class ShortCodec implements Codec<Short> {
   public static final ShortCodec INSTANCE = new ShortCodec();
   private static final EnumSet<DataType> COMPATIBLE_TYPES;

   public boolean canDecode(ColumnDecoder column, Class<?> type) {
      return COMPATIBLE_TYPES.contains(column.getType()) && (type.isPrimitive() && type == Short.TYPE || type.isAssignableFrom(Short.class));
   }

   public boolean canEncode(Object value) {
      return value instanceof Short;
   }

   public String className() {
      return Short.class.getName();
   }

   public Short decodeText(ReadableByteBuf buf, MutableInt length, ColumnDecoder column, Calendar cal, Context context) throws SQLDataException {
      return column.decodeShortText(buf, length);
   }

   public Short decodeBinary(ReadableByteBuf buf, MutableInt length, ColumnDecoder column, Calendar cal, Context context) throws SQLDataException {
      return column.decodeShortBinary(buf, length);
   }

   public void encodeText(Writer encoder, Context context, Object value, Calendar cal, Long maxLen) throws IOException {
      encoder.writeAscii(value.toString());
   }

   public int getApproximateTextProtocolLength(Object value, Long length) {
      return value.toString().length();
   }

   public void encodeBinary(Writer encoder, Context context, Object value, Calendar cal, Long maxLength) throws IOException {
      encoder.writeShort((Short)value);
   }

   public int getBinaryEncodeType() {
      return DataType.SMALLINT.get();
   }

   static {
      COMPATIBLE_TYPES = EnumSet.of(DataType.FLOAT, DataType.DOUBLE, DataType.OLDDECIMAL, DataType.VARCHAR, DataType.DECIMAL, DataType.ENUM, DataType.VARSTRING, DataType.STRING, DataType.TINYINT, DataType.SMALLINT, DataType.MEDIUMINT, DataType.INTEGER, DataType.BIGINT, DataType.BIT, DataType.YEAR, DataType.BLOB, DataType.TINYBLOB, DataType.MEDIUMBLOB, DataType.LONGBLOB);
   }
}
