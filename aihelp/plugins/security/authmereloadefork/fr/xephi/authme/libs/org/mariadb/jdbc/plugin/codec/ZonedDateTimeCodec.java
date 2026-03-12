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
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.EnumSet;

public class ZonedDateTimeCodec implements Codec<ZonedDateTime> {
   public static final ZonedDateTimeCodec INSTANCE = new ZonedDateTimeCodec();
   private static final EnumSet<DataType> COMPATIBLE_TYPES;

   public String className() {
      return ZonedDateTime.class.getName();
   }

   public boolean canDecode(ColumnDecoder column, Class<?> type) {
      return COMPATIBLE_TYPES.contains(column.getType()) && type.isAssignableFrom(ZonedDateTime.class);
   }

   public boolean canEncode(Object value) {
      return value instanceof ZonedDateTime;
   }

   public ZonedDateTime decodeText(ReadableByteBuf buf, MutableInt length, ColumnDecoder column, Calendar calParam) throws SQLDataException {
      LocalDateTime localDateTime = LocalDateTimeCodec.INSTANCE.decodeText(buf, length, column, calParam);
      if (localDateTime == null) {
         return null;
      } else {
         Calendar cal = calParam == null ? Calendar.getInstance() : calParam;
         return localDateTime.atZone(cal.getTimeZone().toZoneId());
      }
   }

   public ZonedDateTime decodeBinary(ReadableByteBuf buf, MutableInt length, ColumnDecoder column, Calendar calParam) throws SQLDataException {
      LocalDateTime localDateTime = LocalDateTimeCodec.INSTANCE.decodeBinary(buf, length, column, calParam);
      if (localDateTime == null) {
         return null;
      } else {
         Calendar cal = calParam == null ? Calendar.getInstance() : calParam;
         return localDateTime.atZone(cal.getTimeZone().toZoneId());
      }
   }

   public void encodeText(Writer encoder, Context context, Object val, Calendar calParam, Long maxLen) throws IOException {
      ZonedDateTime zdt = (ZonedDateTime)val;
      Calendar cal = calParam == null ? Calendar.getInstance() : calParam;
      encoder.writeByte(39);
      encoder.writeAscii(zdt.withZoneSameInstant(cal.getTimeZone().toZoneId()).format(zdt.getNano() != 0 ? LocalDateTimeCodec.TIMESTAMP_FORMAT : LocalDateTimeCodec.TIMESTAMP_FORMAT_NO_FRACTIONAL));
      encoder.writeByte(39);
   }

   public void encodeBinary(Writer encoder, Object value, Calendar calParam, Long maxLength) throws IOException {
      ZonedDateTime zdt = (ZonedDateTime)value;
      Calendar cal = calParam == null ? Calendar.getInstance() : calParam;
      ZonedDateTime convertedZdt = zdt.withZoneSameInstant(cal.getTimeZone().toZoneId());
      int nano = convertedZdt.getNano();
      if (nano > 0) {
         encoder.writeByte(11);
         encoder.writeShort((short)convertedZdt.getYear());
         encoder.writeByte(convertedZdt.getMonthValue());
         encoder.writeByte(convertedZdt.getDayOfMonth());
         encoder.writeByte(convertedZdt.getHour());
         encoder.writeByte(convertedZdt.getMinute());
         encoder.writeByte(convertedZdt.getSecond());
         encoder.writeInt(nano / 1000);
      } else {
         encoder.writeByte(7);
         encoder.writeShort((short)convertedZdt.getYear());
         encoder.writeByte(convertedZdt.getMonthValue());
         encoder.writeByte(convertedZdt.getDayOfMonth());
         encoder.writeByte(convertedZdt.getHour());
         encoder.writeByte(convertedZdt.getMinute());
         encoder.writeByte(convertedZdt.getSecond());
      }

   }

   public int getBinaryEncodeType() {
      return DataType.DATETIME.get();
   }

   static {
      COMPATIBLE_TYPES = EnumSet.of(DataType.DATETIME, DataType.DATE, DataType.YEAR, DataType.TIMESTAMP, DataType.VARSTRING, DataType.VARCHAR, DataType.STRING, DataType.TIME, DataType.BLOB, DataType.TINYBLOB, DataType.MEDIUMBLOB, DataType.LONGBLOB);
   }
}
