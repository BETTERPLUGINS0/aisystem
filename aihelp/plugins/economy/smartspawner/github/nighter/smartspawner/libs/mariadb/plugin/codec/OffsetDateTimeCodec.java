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
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.EnumSet;

public class OffsetDateTimeCodec implements Codec<OffsetDateTime> {
   public static final OffsetDateTimeCodec INSTANCE = new OffsetDateTimeCodec();
   private static final EnumSet<DataType> COMPATIBLE_TYPES;

   public String className() {
      return OffsetDateTime.class.getName();
   }

   public boolean canDecode(ColumnDecoder column, Class<?> type) {
      return COMPATIBLE_TYPES.contains(column.getType()) && type.isAssignableFrom(OffsetDateTime.class);
   }

   public boolean canEncode(Object value) {
      return value instanceof OffsetDateTime;
   }

   public OffsetDateTime decodeText(ReadableByteBuf buf, MutableInt length, ColumnDecoder column, Calendar calParam, Context context) throws SQLDataException {
      switch(column.getType()) {
      case DATETIME:
      case TIMESTAMP:
         ZonedDateTime zdt = ZonedDateTimeCodec.INSTANCE.decodeText(buf, length, column, calParam, context);
         if (zdt == null) {
            return null;
         }

         return zdt.toOffsetDateTime();
      case STRING:
      case VARCHAR:
      case VARSTRING:
         try {
            int[] parts = LocalDateTimeCodec.parseTextTimestamp(buf, length);
            if (LocalDateTimeCodec.isZeroTimestamp(parts)) {
               length.set(-1);
               return null;
            }

            return TimestampColumn.localDateTimeToZoneDateTime(LocalDateTime.of(parts[0], parts[1], parts[2], parts[3], parts[4], parts[5]).plusNanos((long)parts[6]), calParam, context).toOffsetDateTime();
         } catch (Throwable var11) {
            String val = buf.readString(length.get());

            try {
               return OffsetDateTime.parse(val);
            } catch (Throwable var10) {
               throw new SQLDataException(String.format("value '%s' (%s) cannot be decoded as OffsetDateTime", val, column.getType()));
            }
         }
      default:
         buf.skip(length.get());
         throw new SQLDataException(String.format("value of type %s cannot be decoded as OffsetDateTime", column.getType()));
      }
   }

   public OffsetDateTime decodeBinary(ReadableByteBuf buf, MutableInt length, ColumnDecoder column, Calendar calParam, Context context) throws SQLDataException {
      switch(column.getType()) {
      case DATETIME:
      case TIMESTAMP:
         ZonedDateTime zdt = ZonedDateTimeCodec.INSTANCE.decodeBinary(buf, length, column, calParam, context);
         if (zdt == null) {
            return null;
         }

         return zdt.toOffsetDateTime();
      case STRING:
      case VARCHAR:
      case VARSTRING:
         try {
            int[] parts = LocalDateTimeCodec.parseTextTimestamp(buf, length);
            if (LocalDateTimeCodec.isZeroTimestamp(parts)) {
               length.set(-1);
               return null;
            }

            return TimestampColumn.localDateTimeToZoneDateTime(LocalDateTime.of(parts[0], parts[1], parts[2], parts[3], parts[4], parts[5]).plusNanos((long)parts[6]), calParam, context).toOffsetDateTime();
         } catch (Throwable var11) {
            String val = buf.readString(length.get());

            try {
               return OffsetDateTime.parse(val);
            } catch (Throwable var10) {
               throw new SQLDataException(String.format("value '%s' (%s) cannot be decoded as OffsetDateTime", val, column.getType()));
            }
         }
      default:
         buf.skip(length.get());
         throw new SQLDataException(String.format("value of type %s cannot be decoded as OffsetDateTime", column.getType()));
      }
   }

   public void encodeText(Writer encoder, Context context, Object val, Calendar calParam, Long maxLen) throws IOException {
      OffsetDateTime zdt = (OffsetDateTime)val;
      Calendar cal = calParam == null ? context.getDefaultCalendar() : calParam;
      encoder.writeByte(39);
      encoder.writeAscii(zdt.atZoneSameInstant(cal.getTimeZone().toZoneId()).format(zdt.getNano() != 0 ? LocalDateTimeCodec.TIMESTAMP_FORMAT : LocalDateTimeCodec.TIMESTAMP_FORMAT_NO_FRACTIONAL));
      encoder.writeByte(39);
   }

   public int getApproximateTextProtocolLength(Object value, Long length) {
      return ((OffsetDateTime)value).getNano() > 0 ? 28 : 21;
   }

   public void encodeBinary(Writer encoder, Context context, Object value, Calendar calParam, Long maxLength) throws IOException {
      OffsetDateTime zdt = (OffsetDateTime)value;
      Calendar cal = calParam == null ? context.getDefaultCalendar() : calParam;
      ZonedDateTime convertedZdt = zdt.atZoneSameInstant(cal.getTimeZone().toZoneId());
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
