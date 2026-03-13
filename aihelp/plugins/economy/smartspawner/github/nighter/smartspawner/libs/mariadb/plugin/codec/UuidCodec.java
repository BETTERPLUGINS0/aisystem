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

   public UUID decodeText(ReadableByteBuf buf, MutableInt length, ColumnDecoder column, Calendar cal, Context context) throws SQLDataException {
      return UUID.fromString(column.decodeStringText(buf, length, cal, context));
   }

   public UUID decodeBinary(ReadableByteBuf buf, MutableInt length, ColumnDecoder column, Calendar cal, Context context) throws SQLDataException {
      return UUID.fromString(column.decodeStringBinary(buf, length, cal, context));
   }

   public void encodeText(Writer encoder, Context context, Object value, Calendar cal, Long maxLen) throws IOException {
      encoder.writeByte(39);
      encoder.writeAscii(value.toString());
      encoder.writeByte(39);
   }

   public int getApproximateTextProtocolLength(Object value, Long length) {
      return 38;
   }

   public void encodeBinary(Writer writer, Context context, Object value, Calendar cal, Long maxLength) throws IOException {
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
