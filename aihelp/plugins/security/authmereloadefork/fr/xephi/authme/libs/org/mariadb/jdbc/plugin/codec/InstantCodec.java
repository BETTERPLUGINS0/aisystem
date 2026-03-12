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
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.EnumSet;

public class InstantCodec implements Codec<Instant> {
   public static final InstantCodec INSTANCE = new InstantCodec();
   private static final EnumSet<DataType> COMPATIBLE_TYPES;

   public String className() {
      return Instant.class.getName();
   }

   public boolean canDecode(ColumnDecoder column, Class<?> type) {
      return COMPATIBLE_TYPES.contains(column.getType()) && type.isAssignableFrom(Instant.class);
   }

   public boolean canEncode(Object value) {
      return value instanceof Instant;
   }

   public Instant decodeText(ReadableByteBuf buf, MutableInt length, ColumnDecoder column, Calendar calParam) throws SQLDataException {
      LocalDateTime localDateTime = LocalDateTimeCodec.INSTANCE.decodeText(buf, length, column, calParam);
      return localDateTime == null ? null : localDateTime.atZone(ZoneId.systemDefault()).toInstant();
   }

   public Instant decodeBinary(ReadableByteBuf buf, MutableInt length, ColumnDecoder column, Calendar calParam) throws SQLDataException {
      LocalDateTime localDateTime = LocalDateTimeCodec.INSTANCE.decodeBinary(buf, length, column, calParam);
      return localDateTime == null ? null : localDateTime.atZone(ZoneId.systemDefault()).toInstant();
   }

   public void encodeText(Writer encoder, Context context, Object val, Calendar calParam, Long maxLen) throws IOException {
      Instant instant = (Instant)val;
      encoder.writeByte(39);
      if (calParam == null && "UTC".equals(ZoneId.systemDefault().getId())) {
         encoder.writeAscii(instant.toString().replace('T', ' '));
         encoder.pos(encoder.pos() - 1);
      } else {
         ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(instant, ZoneId.systemDefault());
         if (calParam != null) {
            zonedDateTime = zonedDateTime.withZoneSameInstant(calParam.getTimeZone().toZoneId());
         }

         encoder.writeAscii(zonedDateTime.format(instant.getNano() != 0 ? LocalDateTimeCodec.TIMESTAMP_FORMAT : LocalDateTimeCodec.TIMESTAMP_FORMAT_NO_FRACTIONAL));
      }

      encoder.writeByte(39);
   }

   public void encodeBinary(Writer encoder, Object value, Calendar calParam, Long maxLength) throws IOException {
      Instant instant = (Instant)value;
      ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(instant, ZoneId.systemDefault());
      if (calParam != null) {
         zonedDateTime = zonedDateTime.withZoneSameInstant(calParam.getTimeZone().toZoneId());
      }

      int nano = zonedDateTime.getNano();
      if (nano > 0) {
         encoder.writeByte(11);
         encoder.writeShort((short)zonedDateTime.getYear());
         encoder.writeByte(zonedDateTime.getMonth().getValue());
         encoder.writeByte(zonedDateTime.getDayOfMonth());
         encoder.writeByte(zonedDateTime.getHour());
         encoder.writeByte(zonedDateTime.getMinute());
         encoder.writeByte(zonedDateTime.getSecond());
         encoder.writeInt(nano / 1000);
      } else {
         encoder.writeByte(7);
         encoder.writeShort((short)zonedDateTime.getYear());
         encoder.writeByte(zonedDateTime.getMonthValue());
         encoder.writeByte(zonedDateTime.getDayOfMonth());
         encoder.writeByte(zonedDateTime.getHour());
         encoder.writeByte(zonedDateTime.getMinute());
         encoder.writeByte(zonedDateTime.getSecond());
      }

   }

   public int getBinaryEncodeType() {
      return DataType.DATETIME.get();
   }

   static {
      COMPATIBLE_TYPES = EnumSet.of(DataType.DATETIME, DataType.DATE, DataType.YEAR, DataType.TIMESTAMP, DataType.VARSTRING, DataType.VARCHAR, DataType.STRING, DataType.TIME, DataType.BLOB, DataType.TINYBLOB, DataType.MEDIUMBLOB, DataType.LONGBLOB);
   }
}
