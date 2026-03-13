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

   public Boolean decodeText(ReadableByteBuf buffer, MutableInt length, ColumnDecoder column, Calendar cal, Context context) throws SQLDataException {
      return column.decodeBooleanText(buffer, length);
   }

   public Boolean decodeBinary(ReadableByteBuf buffer, MutableInt length, ColumnDecoder column, Calendar cal, Context context) throws SQLDataException {
      return column.decodeBooleanBinary(buffer, length);
   }

   public void encodeText(Writer encoder, Context context, Object value, Calendar cal, Long maxLength) throws IOException {
      encoder.writeAscii((Boolean)value ? "1" : "0");
   }

   public int getApproximateTextProtocolLength(Object value, Long length) {
      return 1;
   }

   public void encodeBinary(Writer encoder, Context context, Object value, Calendar cal, Long maxLength) throws IOException {
      encoder.writeByte((Boolean)value ? 1 : 0);
   }

   public int getBinaryEncodeType() {
      return DataType.TINYINT.get();
   }

   static {
      COMPATIBLE_TYPES = EnumSet.of(DataType.VARCHAR, DataType.VARSTRING, DataType.STRING, DataType.BIGINT, DataType.INTEGER, DataType.MEDIUMINT, DataType.SMALLINT, DataType.YEAR, DataType.TINYINT, DataType.DECIMAL, DataType.OLDDECIMAL, DataType.FLOAT, DataType.DOUBLE, DataType.BIT, DataType.BLOB, DataType.TINYBLOB, DataType.MEDIUMBLOB, DataType.LONGBLOB);
   }
}
