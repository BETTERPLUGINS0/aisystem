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

public class FloatCodec implements Codec<Float> {
   public static final FloatCodec INSTANCE = new FloatCodec();
   private static final EnumSet<DataType> COMPATIBLE_TYPES;

   public String className() {
      return Float.class.getName();
   }

   public boolean canDecode(ColumnDecoder column, Class<?> type) {
      return COMPATIBLE_TYPES.contains(column.getType()) && (type.isPrimitive() && type == Float.TYPE || type.isAssignableFrom(Float.class));
   }

   public boolean canEncode(Object value) {
      return value instanceof Float;
   }

   public Float decodeText(ReadableByteBuf buffer, MutableInt length, ColumnDecoder column, Calendar cal) throws SQLDataException {
      return column.decodeFloatText(buffer, length);
   }

   public Float decodeBinary(ReadableByteBuf buffer, MutableInt length, ColumnDecoder column, Calendar cal) throws SQLDataException {
      return column.decodeFloatBinary(buffer, length);
   }

   public void encodeText(Writer encoder, Context context, Object value, Calendar cal, Long maxLen) throws IOException {
      encoder.writeAscii(value.toString());
   }

   public void encodeBinary(Writer encoder, Object value, Calendar cal, Long maxLength) throws IOException {
      encoder.writeFloat((Float)value);
   }

   public int getBinaryEncodeType() {
      return DataType.FLOAT.get();
   }

   static {
      COMPATIBLE_TYPES = EnumSet.of(DataType.TINYINT, DataType.SMALLINT, DataType.MEDIUMINT, DataType.INTEGER, DataType.FLOAT, DataType.BIGINT, DataType.OLDDECIMAL, DataType.DECIMAL, DataType.YEAR, DataType.DOUBLE, DataType.VARCHAR, DataType.VARSTRING, DataType.STRING, DataType.BLOB, DataType.TINYBLOB, DataType.MEDIUMBLOB, DataType.LONGBLOB);
   }
}
