package fr.xephi.authme.libs.org.mariadb.jdbc.plugin.codec;

import fr.xephi.authme.libs.org.mariadb.jdbc.client.ColumnDecoder;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.Context;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.DataType;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.ReadableByteBuf;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.socket.Writer;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.util.MutableInt;
import fr.xephi.authme.libs.org.mariadb.jdbc.plugin.Codec;
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

   public Long decodeText(ReadableByteBuf buffer, MutableInt length, ColumnDecoder column, Calendar cal) throws SQLDataException {
      return column.decodeLongText(buffer, length);
   }

   public Long decodeBinary(ReadableByteBuf buffer, MutableInt length, ColumnDecoder column, Calendar cal) throws SQLDataException {
      return column.decodeLongBinary(buffer, length);
   }

   public void encodeText(Writer encoder, Context context, Object value, Calendar cal, Long maxLen) throws IOException {
      encoder.writeAscii(value.toString());
   }

   public void encodeBinary(Writer encoder, Object value, Calendar cal, Long maxLength) throws IOException {
      encoder.writeLong((Long)value);
   }

   public int getBinaryEncodeType() {
      return DataType.BIGINT.get();
   }

   static {
      COMPATIBLE_TYPES = EnumSet.of(DataType.FLOAT, DataType.DOUBLE, DataType.OLDDECIMAL, DataType.VARCHAR, DataType.DECIMAL, DataType.ENUM, DataType.VARSTRING, DataType.STRING, DataType.TINYINT, DataType.SMALLINT, DataType.MEDIUMINT, DataType.INTEGER, DataType.BIGINT, DataType.BIT, DataType.YEAR, DataType.BLOB, DataType.TINYBLOB, DataType.MEDIUMBLOB, DataType.LONGBLOB);
   }
}
