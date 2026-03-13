package github.nighter.smartspawner.libs.mariadb.plugin.codec;

import github.nighter.smartspawner.libs.mariadb.client.ColumnDecoder;
import github.nighter.smartspawner.libs.mariadb.client.Context;
import github.nighter.smartspawner.libs.mariadb.client.DataType;
import github.nighter.smartspawner.libs.mariadb.client.ReadableByteBuf;
import github.nighter.smartspawner.libs.mariadb.client.socket.Writer;
import github.nighter.smartspawner.libs.mariadb.client.util.MutableInt;
import github.nighter.smartspawner.libs.mariadb.plugin.Codec;
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

   public String decodeText(ReadableByteBuf buf, MutableInt length, ColumnDecoder column, Calendar cal, Context context) throws SQLDataException {
      return column.decodeStringText(buf, length, cal, context);
   }

   public String decodeBinary(ReadableByteBuf buf, MutableInt length, ColumnDecoder column, Calendar cal, Context context) throws SQLDataException {
      return column.decodeStringBinary(buf, length, cal, context);
   }

   public void encodeText(Writer encoder, Context context, Object value, Calendar cal, Long maxLen) throws IOException {
      encoder.writeByte(39);
      String str = value.toString();
      encoder.writeStringEscaped(maxLen == null ? str : str.substring(0, maxLen.intValue()), (context.getServerStatus() & 512) != 0);
      encoder.writeByte(39);
   }

   public int getApproximateTextProtocolLength(Object value, Long maxLen) {
      int len = value.toString().length();
      return maxLen == null ? len + 2 : Math.min(len, maxLen.intValue()) + 2;
   }

   public void encodeBinary(Writer writer, Context context, Object value, Calendar cal, Long maxLength) throws IOException {
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
