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

   public Integer decodeText(ReadableByteBuf buffer, MutableInt length, ColumnDecoder column, Calendar cal) throws SQLDataException {
      return column.decodeIntText(buffer, length);
   }

   public Integer decodeBinary(ReadableByteBuf buffer, MutableInt length, ColumnDecoder column, Calendar cal) throws SQLDataException {
      return column.decodeIntBinary(buffer, length);
   }

   public void encodeText(Writer encoder, Context context, Object value, Calendar cal, Long maxLen) throws IOException {
      encoder.writeAscii(value.toString());
   }

   public void encodeBinary(Writer encoder, Object value, Calendar cal, Long maxLength) throws IOException {
      encoder.writeInt((Integer)value);
   }

   public int getBinaryEncodeType() {
      return DataType.INTEGER.get();
   }

   static {
      COMPATIBLE_TYPES = EnumSet.of(DataType.FLOAT, DataType.DOUBLE, DataType.OLDDECIMAL, DataType.VARCHAR, DataType.DECIMAL, DataType.ENUM, DataType.VARSTRING, DataType.STRING, DataType.TINYINT, DataType.SMALLINT, DataType.MEDIUMINT, DataType.INTEGER, DataType.BIGINT, DataType.BIT, DataType.YEAR, DataType.BLOB, DataType.TINYBLOB, DataType.MEDIUMBLOB, DataType.LONGBLOB);
   }
}
