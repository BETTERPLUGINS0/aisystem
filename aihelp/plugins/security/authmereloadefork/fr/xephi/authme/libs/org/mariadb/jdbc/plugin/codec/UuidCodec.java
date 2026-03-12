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
import java.util.UUID;

public class UuidCodec implements Codec<UUID> {
   public static final UuidCodec INSTANCE = new UuidCodec();
   private static final EnumSet<DataType> COMPATIBLE_TYPES;

   public String className() {
      return UUID.class.getName();
   }

   public boolean canDecode(ColumnDecoder column, Class<?> type) {
      return COMPATIBLE_TYPES.contains(column.getType()) && type.isAssignableFrom(UUID.class);
   }

   public boolean canEncode(Object value) {
      return value instanceof UUID;
   }

   public UUID decodeText(ReadableByteBuf buf, MutableInt length, ColumnDecoder column, Calendar cal) throws SQLDataException {
      return UUID.fromString(column.decodeStringText(buf, length, cal));
   }

   public UUID decodeBinary(ReadableByteBuf buf, MutableInt length, ColumnDecoder column, Calendar cal) throws SQLDataException {
      return UUID.fromString(column.decodeStringBinary(buf, length, cal));
   }

   public void encodeText(Writer encoder, Context context, Object value, Calendar cal, Long maxLen) throws IOException {
      encoder.writeByte(39);
      encoder.writeAscii(value.toString());
      encoder.writeByte(39);
   }

   public void encodeBinary(Writer writer, Object value, Calendar cal, Long maxLength) throws IOException {
      String valueSt = value.toString();
      writer.writeLength((long)valueSt.length());
      writer.writeAscii(valueSt);
   }

   public int getBinaryEncodeType() {
      return DataType.VARSTRING.get();
   }

   static {
      COMPATIBLE_TYPES = EnumSet.of(DataType.VARCHAR, DataType.VARSTRING, DataType.STRING);
   }
}
