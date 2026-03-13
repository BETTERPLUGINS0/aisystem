package github.nighter.smartspawner.libs.mariadb.plugin.codec;

import github.nighter.smartspawner.libs.mariadb.client.ColumnDecoder;
import github.nighter.smartspawner.libs.mariadb.client.Context;
import github.nighter.smartspawner.libs.mariadb.client.DataType;
import github.nighter.smartspawner.libs.mariadb.client.ReadableByteBuf;
import github.nighter.smartspawner.libs.mariadb.client.column.TimestampColumn;
import github.nighter.smartspawner.libs.mariadb.client.socket.Writer;
import github.nighter.smartspawner.libs.mariadb.client.util.MutableInt;
import github.nighter.smartspawner.libs.mariadb.plugin.Codec;
import java.io.IOException;
import java.sql.SQLDataException;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.EnumSet;
import java.util.TimeZone;

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

   public ZonedDateTime decodeText(ReadableByteBuf buf, MutableInt length, ColumnDecoder column, Calendar calParam, Context context) throws SQLDataException {
      int[] parts;
      switch(column.getType()) {
      case BLOB:
      case TINYBLOB:
      case MEDIUMBLOB:
      case LONGBLOB:
         if (column.isBinary()) {
            buf.skip(length.get());
            throw new SQLDataException(String.format("Data type %s cannot be decoded as ZoneDateTime", column.getType()));
         }
      case STRING:
      case VARCHAR:
      case VARSTRING:
         try {
            parts = LocalDateTimeCodec.parseTextTimestamp(buf, length);
            if (LocalDateTimeCodec.isZeroTimestamp(parts)) {
               length.set(-1);
               return null;
            }

            return TimestampColumn.localDateTimeToZoneDateTime(LocalDateTime.of(parts[0], parts[1], parts[2], parts[3], parts[4], parts[5]).plusNanos((long)parts[6]), calParam, context);
         } catch (Throwable var12) {
            String val = buf.readString(length.get());
            throw new SQLDataException(String.format("value '%s' (%s) cannot be decoded as ZoneDateTime", val, column.getType()));
         }
      case DATE:
         parts = LocalDateCodec.parseDate(buf, length);
         if (parts == null) {
            length.set(-1);
            return null;
         }

         TimeZone tz = calParam == null ? TimeZone.getDefault() : calParam.getTimeZone();
         return LocalDateTime.of(parts[0], parts[1], parts[2], 0, 0, 0).atZone(tz.toZoneId());
      case DATETIME:
      case TIMESTAMP:
         parts = LocalDateTimeCodec.parseTextTimestamp(buf, length);
         if (LocalDateTimeCodec.isZeroTimestamp(parts)) {
            length.set(-1);
            return null;
         }

         LocalDateTime ldt = LocalDateTime.of(parts[0], parts[1], parts[2], parts[3], parts[4], parts[5]).plusNanos((long)parts[6]);
         return TimestampColumn.localDateTimeToZoneDateTime(ldt, calParam, context);
      case TIME:
         parts = LocalTimeCodec.parseTime(buf, length, column);
         TimeZone tzTime = calParam == null ? TimeZone.getDefault() : calParam.getTimeZone();
         if (parts[0] == -1) {
            return LocalDateTime.of(1970, 1, 1, 0, 0).minusHours((long)(parts[1] % 24)).minusMinutes((long)parts[2]).minusSeconds((long)parts[3]).minusNanos((long)parts[4]).atZone(tzTime.toZoneId());
         }

         return LocalDateTime.of(1970, 1, 1, parts[1] % 24, parts[2], parts[3]).plusNanos((long)parts[4]).atZone(tzTime.toZoneId());
      case YEAR:
         int year = Integer.parseInt(buf.readAscii(length.get()));
         if (column.getColumnLength() <= 2L) {
            year += year >= 70 ? 1900 : 2000;
         }

         TimeZone tzYear = calParam == null ? TimeZone.getDefault() : calParam.getTimeZone();
         return LocalDateTime.of(year, 1, 1, 0, 0).atZone(tzYear.toZoneId());
      default:
         buf.skip(length.get());
         throw new SQLDataException(String.format("Data type %s cannot be decoded as ZoneDateTime", column.getType()));
      }
   }

   public ZonedDateTime decodeBinary(ReadableByteBuf buf, MutableInt length, ColumnDecoder column, Calendar calParam, Context context) throws SQLDataException {
      int year = 1970;
      int month = 1;
      long dayOfMonth = 1L;
      int hour = 0;
      int minutes = 0;
      int seconds = 0;
      long microseconds = 0L;
      int year;
      byte month;
      switch(column.getType()) {
      case BLOB:
      case TINYBLOB:
      case MEDIUMBLOB:
      case LONGBLOB:
         if (column.isBinary()) {
            buf.skip(length.get());
            throw new SQLDataException(String.format("Data type %s cannot be decoded as ZoneDateTime", column.getType()));
         }
      case STRING:
      case VARCHAR:
      case VARSTRING:
         try {
            int[] parts = LocalDateTimeCodec.parseTextTimestamp(buf, length);
            if (LocalDateTimeCodec.isZeroTimestamp(parts)) {
               length.set(-1);
               return null;
            }

            return TimestampColumn.localDateTimeToZoneDateTime(LocalDateTime.of(parts[0], parts[1], parts[2], parts[3], parts[4], parts[5]).plusNanos((long)parts[6]), calParam, context);
         } catch (Throwable var19) {
            String val = buf.readString(length.get());
            throw new SQLDataException(String.format("value '%s' (%s) cannot be decoded as ZoneDateTime", val, column.getType()));
         }
      case DATE:
         if (length.get() == 0) {
            length.set(-1);
            return null;
         } else {
            year = buf.readUnsignedShort();
            month = buf.readByte();
            dayOfMonth = (long)buf.readByte();
            if (year == 0 && month == 0 && dayOfMonth == 0L) {
               length.set(-1);
               return null;
            }

            TimeZone tz = calParam == null ? TimeZone.getDefault() : calParam.getTimeZone();
            return LocalDateTime.of(year, month, (int)dayOfMonth, 0, 0, 0).atZone(tz.toZoneId());
         }
      case DATETIME:
      case TIMESTAMP:
         if (length.get() == 0) {
            length.set(-1);
            return null;
         } else {
            year = buf.readUnsignedShort();
            month = buf.readByte();
            dayOfMonth = (long)buf.readByte();
            if (length.get() > 4) {
               hour = buf.readByte();
               minutes = buf.readByte();
               seconds = buf.readByte();
               if (length.get() > 7) {
                  microseconds = buf.readUnsignedInt();
               }
            }

            if (year == 0 && month == 0 && dayOfMonth == 0L && hour == 0 && minutes == 0 && seconds == 0) {
               length.set(-1);
               return null;
            }

            LocalDateTime ldt = LocalDateTime.of(year, month, (int)dayOfMonth, hour, minutes, seconds).plusNanos(microseconds * 1000L);
            return TimestampColumn.localDateTimeToZoneDateTime(ldt, calParam, context);
         }
      case TIME:
         TimeZone tzTime = calParam == null ? TimeZone.getDefault() : calParam.getTimeZone();
         if (length.get() > 0) {
            boolean negate = buf.readByte() == 1;
            int day = buf.readInt();
            hour = buf.readByte();
            minutes = buf.readByte();
            seconds = buf.readByte();
            if (length.get() > 8) {
               microseconds = buf.readUnsignedInt();
            }

            if (negate) {
               return LocalDateTime.of(1970, 1, 1, 0, 0).minusDays((long)day).minusHours((long)hour).minusMinutes((long)minutes).minusSeconds((long)seconds).minusNanos(microseconds * 1000L).atZone(tzTime.toZoneId());
            }
         }

         return LocalDateTime.of(year, month, (int)dayOfMonth, hour, minutes, seconds).plusNanos(microseconds * 1000L).atZone(tzTime.toZoneId());
      case YEAR:
         year = buf.readUnsignedShort();
         if (column.getColumnLength() <= 2L) {
            year += year >= 70 ? 1900 : 2000;
         }

         TimeZone tzYear = calParam == null ? TimeZone.getDefault() : calParam.getTimeZone();
         return LocalDateTime.of(year, 1, 1, 0, 0).atZone(tzYear.toZoneId());
      default:
         buf.skip(length.get());
         throw new SQLDataException(String.format("Data type %s cannot be decoded as LocalDateTime", column.getType()));
      }
   }

   public void encodeText(Writer encoder, Context context, Object val, Calendar calParam, Long maxLen) throws IOException {
      ZonedDateTime zdt = (ZonedDateTime)val;
      Calendar cal = calParam == null ? context.getDefaultCalendar() : calParam;
      encoder.writeByte(39);
      encoder.writeAscii(zdt.withZoneSameInstant(cal.getTimeZone().toZoneId()).format(zdt.getNano() != 0 ? LocalDateTimeCodec.TIMESTAMP_FORMAT : LocalDateTimeCodec.TIMESTAMP_FORMAT_NO_FRACTIONAL));
      encoder.writeByte(39);
   }

   public int getApproximateTextProtocolLength(Object value, Long length) {
      return ((ZonedDateTime)value).getNano() > 0 ? 28 : 21;
   }

   public void encodeBinary(Writer encoder, Context context, Object value, Calendar calParam, Long maxLength) throws IOException {
      ZonedDateTime zdt = (ZonedDateTime)value;
      Calendar cal = calParam == null ? context.getDefaultCalendar() : calParam;
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
