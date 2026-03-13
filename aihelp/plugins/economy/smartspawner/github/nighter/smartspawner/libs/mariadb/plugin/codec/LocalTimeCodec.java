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
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeParseException;
import java.util.Calendar;
import java.util.EnumSet;
import java.util.TimeZone;

public class LocalTimeCodec implements Codec<LocalTime> {
   public static final LocalTimeCodec INSTANCE = new LocalTimeCodec();
   private static final EnumSet<DataType> COMPATIBLE_TYPES;

   public static int[] parseTime(ReadableByteBuf buf, MutableInt length, ColumnDecoder column) throws SQLDataException {
      int initialPos = buf.pos();
      int[] parts = new int[5];
      parts[0] = 1;
      int idx = 1;
      int partLength = 0;
      int i = 0;
      if (length.get() > 0 && buf.getByte() == 45) {
         buf.skip();
         ++i;
         parts[0] = -1;
      }

      String val;
      for(; i < length.get(); ++i) {
         byte b = buf.readByte();
         if (b != 58 && b != 46) {
            if (b < 48 || b > 57) {
               buf.pos(initialPos);
               val = buf.readString(length.get());
               throw new SQLDataException(String.format("%s value '%s' cannot be decoded as Time", column.getType(), val));
            }

            ++partLength;
            parts[idx] = parts[idx] * 10 + (b - 48);
         } else {
            ++idx;
            partLength = 0;
         }
      }

      if (idx < 2) {
         buf.pos(initialPos);
         val = buf.readString(length.get());
         throw new SQLDataException(String.format("%s value '%s' cannot be decoded as Time", column.getType(), val));
      } else {
         if (idx == 4) {
            for(i = 0; i < 9 - partLength; ++i) {
               parts[4] *= 10;
            }
         }

         return parts;
      }
   }

   public String className() {
      return LocalTime.class.getName();
   }

   public boolean canDecode(ColumnDecoder column, Class<?> type) {
      return COMPATIBLE_TYPES.contains(column.getType()) && type.isAssignableFrom(LocalTime.class);
   }

   public boolean canEncode(Object value) {
      return value instanceof LocalTime;
   }

   public LocalTime decodeText(ReadableByteBuf buf, MutableInt length, ColumnDecoder column, Calendar cal, Context context) throws SQLDataException {
      int[] parts;
      switch(column.getType()) {
      case TIMESTAMP:
      case DATETIME:
         parts = LocalDateTimeCodec.parseTextTimestamp(buf, length);
         if (LocalDateTimeCodec.isZeroTimestamp(parts)) {
            length.set(-1);
            return null;
         }

         return LocalTime.of(parts[3], parts[4], parts[5], parts[6]);
      case TIME:
         parts = parseTime(buf, length, column);
         parts[1] %= 24;
         if (parts[0] == -1) {
            long seconds = 86400L - ((long)(parts[1] * 3600) + (long)parts[2] * 60L + (long)parts[3]);
            return LocalTime.ofNanoOfDay(seconds * 1000000000L - (long)parts[4]);
         }

         return LocalTime.of(parts[1] % 24, parts[2], parts[3], parts[4]);
      case BLOB:
      case TINYBLOB:
      case MEDIUMBLOB:
      case LONGBLOB:
         if (column.isBinary()) {
            buf.skip(length.get());
            throw new SQLDataException(String.format("Data type %s cannot be decoded as LocalTime", column.getType()));
         }
      case VARSTRING:
      case VARCHAR:
      case STRING:
         String val = buf.readString(length.get());

         try {
            if (val.contains(" ")) {
               ZoneId tz = cal != null ? cal.getTimeZone().toZoneId() : TimeZone.getDefault().toZoneId();
               return LocalDateTime.parse(val, LocalDateTimeCodec.MARIADB_LOCAL_DATE_TIME.withZone(tz)).toLocalTime();
            }

            return LocalTime.parse(val);
         } catch (DateTimeParseException var9) {
            throw new SQLDataException(String.format("value '%s' (%s) cannot be decoded as LocalTime", val, column.getType()));
         }
      default:
         buf.skip(length.get());
         throw new SQLDataException(String.format("Data type %s cannot be decoded as LocalTime", column.getType()));
      }
   }

   public LocalTime decodeBinary(ReadableByteBuf buf, MutableInt length, ColumnDecoder column, Calendar cal, Context context) throws SQLDataException {
      int hour = 0;
      int minutes = 0;
      int seconds = 0;
      long microseconds = 0L;
      switch(column.getType()) {
      case TIMESTAMP:
      case DATETIME:
         if (length.get() == 0) {
            length.set(-1);
            return null;
         } else {
            int year = buf.readUnsignedShort();
            int month = buf.readByte();
            int dayOfMonth = buf.readByte();
            if (length.get() > 4) {
               hour = buf.readByte();
               minutes = buf.readByte();
               seconds = buf.readByte();
               if (length.get() > 7) {
                  microseconds = (long)buf.readInt();
               }
            }

            if (year == 0 && month == 0 && dayOfMonth == 0 && hour == 0 && minutes == 0 && seconds == 0) {
               length.set(-1);
               return null;
            }

            return LocalTime.of(hour, minutes, seconds).plusNanos(microseconds * 1000L);
         }
      case TIME:
         if (length.get() > 0) {
            boolean negate = buf.readByte() == 1;
            if (length.get() > 4) {
               buf.skip(4);
               if (length.get() > 7) {
                  hour = buf.readByte();
                  minutes = buf.readByte();
                  seconds = buf.readByte();
                  if (length.get() > 8) {
                     microseconds = (long)buf.readInt();
                  }
               }
            }

            if (negate) {
               long nanos = (long)(86400 - (hour * 3600 + minutes * 60 + seconds));
               return LocalTime.ofNanoOfDay(nanos * 1000000000L - microseconds * 1000L);
            }
         }

         return LocalTime.of(hour % 24, minutes, seconds, (int)microseconds * 1000);
      case BLOB:
      case TINYBLOB:
      case MEDIUMBLOB:
      case LONGBLOB:
         if (column.isBinary()) {
            buf.skip(length.get());
            throw new SQLDataException(String.format("Data type %s cannot be decoded as LocalTime", column.getType()));
         }
      case VARSTRING:
      case VARCHAR:
      case STRING:
         String val = buf.readString(length.get());

         try {
            if (val.contains(" ")) {
               ZoneId tz = cal != null ? cal.getTimeZone().toZoneId() : TimeZone.getDefault().toZoneId();
               return LocalDateTime.parse(val, LocalDateTimeCodec.MARIADB_LOCAL_DATE_TIME.withZone(tz)).toLocalTime();
            }

            return LocalTime.parse(val);
         } catch (DateTimeParseException var17) {
            throw new SQLDataException(String.format("value '%s' (%s) cannot be decoded as LocalTime", val, column.getType()));
         }
      default:
         buf.skip(length.get());
         throw new SQLDataException(String.format("Data type %s cannot be decoded as LocalTime", column.getType()));
      }
   }

   public void encodeText(Writer encoder, Context context, Object value, Calendar cal, Long maxLen) throws IOException {
      LocalTime val = (LocalTime)value;
      StringBuilder dateString = new StringBuilder(15);
      dateString.append(val.getHour() < 10 ? "0" : "").append(val.getHour()).append(val.getMinute() < 10 ? ":0" : ":").append(val.getMinute()).append(val.getSecond() < 10 ? ":0" : ":").append(val.getSecond());
      int microseconds = val.getNano() / 1000;
      if (microseconds > 0) {
         dateString.append(".");
         if (microseconds % 1000 == 0) {
            dateString.append(Integer.toString(microseconds / 1000 + 1000).substring(1));
         } else {
            dateString.append(Integer.toString(microseconds + 1000000).substring(1));
         }
      }

      encoder.writeByte(39);
      encoder.writeAscii(dateString.toString());
      encoder.writeByte(39);
   }

   public int getApproximateTextProtocolLength(Object value, Long length) {
      LocalTime val = (LocalTime)value;
      int microseconds = val.getNano() / 1000;
      if (microseconds > 0) {
         return microseconds % 1000 == 0 ? 14 : 17;
      } else {
         return 10;
      }
   }

   public void encodeBinary(Writer encoder, Context context, Object value, Calendar cal, Long maxLength) throws IOException {
      LocalTime val = (LocalTime)value;
      int nano = val.getNano();
      if (nano > 0) {
         encoder.writeByte(12);
         encoder.writeByte(0);
         encoder.writeInt(0);
         encoder.writeByte((byte)val.getHour());
         encoder.writeByte((byte)val.getMinute());
         encoder.writeByte((byte)val.getSecond());
         encoder.writeInt(nano / 1000);
      } else {
         encoder.writeByte(8);
         encoder.writeByte(0);
         encoder.writeInt(0);
         encoder.writeByte((byte)val.getHour());
         encoder.writeByte((byte)val.getMinute());
         encoder.writeByte((byte)val.getSecond());
      }

   }

   public int getBinaryEncodeType() {
      return DataType.TIME.get();
   }

   static {
      COMPATIBLE_TYPES = EnumSet.of(DataType.TIME, DataType.DATETIME, DataType.TIMESTAMP, DataType.VARSTRING, DataType.VARCHAR, DataType.STRING, DataType.BLOB, DataType.TINYBLOB, DataType.MEDIUMBLOB, DataType.LONGBLOB);
   }
}
