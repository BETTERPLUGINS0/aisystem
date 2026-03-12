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

public class BooleanCodec implements Codec<Boolean> {
   public static final BooleanCodec INSTANCE = new BooleanCodec();
   private static final EnumSet<DataType> COMPATIBLE_TYPES;

   public String className() {
      return Boolean.class.getName();
   }

   public boolean canDecode(ColumnDecoder column, Class<?> type) {
      return COMPATIBLE_TYPES.contains(column.getType()) && (type.isPrimitive() && type == Boolean.TYPE || type.isAssignableFrom(Boolean.class));
   }

   public boolean canEncode(Object value) {
      return value instanceof Boolean;
   }

   public Boolean decodeText(ReadableByteBuf buffer, MutableInt length, ColumnDecoder column, Calendar cal) throws SQLDataException {
      return column.decodeBooleanText(buffer, length);
   }

   public Boolean decodeBinary(ReadableByteBuf buffer, MutableInt length, ColumnDecoder column, Calendar cal) throws SQLDataException {
      return column.decodeBooleanBinary(buffer, length);
   }

   public void encodeText(Writer encoder, Context context, Object value, Calendar cal, Long maxLength) throws IOException {
      encoder.writeAscii((Boolean)value ? "1" : "0");
   }

   public void encodeBinary(Writer encoder, Object value, Calendar cal, Long maxLength) throws IOException {
      encoder.writeByte((Boolean)value ? 1 : 0);
   }

   public int getBinaryEncodeType() {
      return DataType.TINYINT.get();
   }

   static {
      COMPATIBLE_TYPES = EnumSet.of(DataType.VARCHAR, DataType.VARSTRING, DataType.STRING, DataType.BIGINT, DataType.INTEGER, DataType.MEDIUMINT, DataType.SMALLINT, DataType.YEAR, DataType.TINYINT, DataType.DECIMAL, DataType.OLDDECIMAL, DataType.FLOAT, DataType.DOUBLE, DataType.BIT, DataType.BLOB, DataType.TINYBLOB, DataType.MEDIUMBLOB, DataType.LONGBLOB);
   }
}
