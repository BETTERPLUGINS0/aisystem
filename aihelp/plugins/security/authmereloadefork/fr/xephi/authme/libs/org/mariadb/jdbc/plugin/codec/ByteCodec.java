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

public class ByteCodec implements Codec<Byte> {
   public static final ByteCodec INSTANCE = new ByteCodec();
   private static final EnumSet<DataType> COMPATIBLE_TYPES;

   public static long parseBit(ReadableByteBuf buf, MutableInt length) {
      if (length.get() == 1) {
         return (long)buf.readUnsignedByte();
      } else {
         long val = 0L;
         int idx = 0;

         do {
            val += (long)buf.readUnsignedByte() << 8 * length.get();
            ++idx;
         } while(idx < length.get());

         return val;
      }
   }

   public String className() {
      return Byte.class.getName();
   }

   public boolean canDecode(ColumnDecoder column, Class<?> type) {
      return COMPATIBLE_TYPES.contains(column.getType()) && (type.isPrimitive() && type == Byte.TYPE || type.isAssignableFrom(Byte.class));
   }

   public boolean canEncode(Object value) {
      return value instanceof Byte;
   }

   public Byte decodeText(ReadableByteBuf buffer, MutableInt length, ColumnDecoder column, Calendar cal) throws SQLDataException {
      return column.decodeByteText(buffer, length);
   }

   public Byte decodeBinary(ReadableByteBuf buffer, MutableInt length, ColumnDecoder column, Calendar cal) throws SQLDataException {
      return column.decodeByteBinary(buffer, length);
   }

   public void encodeText(Writer encoder, Context context, Object value, Calendar cal, Long maxLength) throws IOException {
      encoder.writeAscii(Integer.toString((Byte)value));
   }

   public void encodeBinary(Writer encoder, Object value, Calendar cal, Long maxLength) throws IOException {
      encoder.writeByte((Byte)value);
   }

   public int getBinaryEncodeType() {
      return DataType.TINYINT.get();
   }

   static {
      COMPATIBLE_TYPES = EnumSet.of(DataType.TINYINT, DataType.SMALLINT, DataType.MEDIUMINT, DataType.INTEGER, DataType.BIGINT, DataType.YEAR, DataType.BIT, DataType.FLOAT, DataType.DOUBLE, DataType.OLDDECIMAL, DataType.BLOB, DataType.TINYBLOB, DataType.MEDIUMBLOB, DataType.LONGBLOB, DataType.DECIMAL, DataType.ENUM, DataType.VARSTRING, DataType.STRING, DataType.VARCHAR);
   }
}
