package fr.xephi.authme.libs.org.mariadb.jdbc.plugin.codec;

import fr.xephi.authme.libs.org.mariadb.jdbc.client.ColumnDecoder;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.Context;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.DataType;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.ReadableByteBuf;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.socket.Writer;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.util.MutableInt;
import fr.xephi.authme.libs.org.mariadb.jdbc.plugin.Codec;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.SQLDataException;
import java.util.Calendar;
import java.util.EnumSet;

public class StringCodec implements Codec<String> {
   public static final StringCodec INSTANCE = new StringCodec();
   private static final EnumSet<DataType> COMPATIBLE_TYPES;

   public String className() {
      return String.class.getName();
   }

   public boolean canDecode(ColumnDecoder column, Class<?> type) {
      return COMPATIBLE_TYPES.contains(column.getType()) && type.isAssignableFrom(String.class);
   }

   public boolean canEncode(Object value) {
      return value instanceof String;
   }

   public String decodeText(ReadableByteBuf buf, MutableInt length, ColumnDecoder column, Calendar cal) throws SQLDataException {
      return column.decodeStringText(buf, length, cal);
   }

   public String decodeBinary(ReadableByteBuf buf, MutableInt length, ColumnDecoder column, Calendar cal) throws SQLDataException {
      return column.decodeStringBinary(buf, length, cal);
   }

   public void encodeText(Writer encoder, Context context, Object value, Calendar cal, Long maxLen) throws IOException {
      encoder.writeByte(39);
      encoder.writeStringEscaped(maxLen == null ? value.toString() : value.toString().substring(0, maxLen.intValue()), (context.getServerStatus() & 512) != 0);
      encoder.writeByte(39);
   }

   public void encodeBinary(Writer writer, Object value, Calendar cal, Long maxLength) throws IOException {
      byte[] b = value.toString().getBytes(StandardCharsets.UTF_8);
      int len = maxLength != null ? Math.min(maxLength.intValue(), b.length) : b.length;
      writer.writeLength((long)len);
      writer.writeBytes(b, 0, len);
   }

   public int getBinaryEncodeType() {
      return DataType.VARSTRING.get();
   }

   static {
      COMPATIBLE_TYPES = EnumSet.of(DataType.BIT, DataType.OLDDECIMAL, DataType.TINYINT, DataType.SMALLINT, DataType.INTEGER, DataType.FLOAT, DataType.DOUBLE, DataType.TIMESTAMP, DataType.BIGINT, DataType.MEDIUMINT, DataType.DATE, DataType.TIME, DataType.DATETIME, DataType.YEAR, DataType.NEWDATE, DataType.JSON, DataType.DECIMAL, DataType.ENUM, DataType.SET, DataType.VARCHAR, DataType.VARSTRING, DataType.STRING, DataType.BLOB, DataType.TINYBLOB, DataType.MEDIUMBLOB, DataType.LONGBLOB);
   }
}
