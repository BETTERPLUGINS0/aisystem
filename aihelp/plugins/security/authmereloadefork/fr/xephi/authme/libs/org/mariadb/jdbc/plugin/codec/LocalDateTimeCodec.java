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
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Calendar;
import java.util.EnumSet;

public class LocalDateTimeCodec implements Codec<LocalDateTime> {
   public static final LocalDateTimeCodec INSTANCE = new LocalDateTimeCodec();
   public static final DateTimeFormatter TIMESTAMP_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");
   public static final DateTimeFormatter TIMESTAMP_FORMAT_NO_FRACTIONAL = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
   public static final DateTimeFormatter MARIADB_LOCAL_DATE_TIME;
   private static final EnumSet<DataType> COMPATIBLE_TYPES;

   public static int[] parseTimestamp(String raw) throws DateTimeException {
      int nanoLen = -1;
      int[] timestampsPart = new int[]{0, 0, 0, 0, 0, 0, 0};
      int partIdx = 0;

      int begin;
      for(begin = 0; begin < raw.length(); ++begin) {
         char b = raw.charAt(begin);
         if (b != '-' && b != ' ' && b != ':') {
            if (b == '.') {
               ++partIdx;
               nanoLen = 0;
            } else {
               if (nanoLen >= 0) {
                  ++nanoLen;
               }

               timestampsPart[partIdx] = timestampsPart[partIdx] * 10 + b - 48;
            }
         } else {
            ++partIdx;
         }
      }

      if (partIdx < 2) {
         throw new DateTimeException("Wrong timestamp format");
      } else {
         if (timestampsPart[0] == 0 && timestampsPart[1] == 0 && timestampsPart[2] == 0) {
            if (timestampsPart[3] == 0 && timestampsPart[4] == 0 && timestampsPart[5] == 0 && timestampsPart[6] == 0) {
               return null;
            }

            timestampsPart[1] = 1;
            timestampsPart[2] = 1;
         }

         if (nanoLen >= 0) {
            for(begin = 0; begin < 6 - nanoLen; ++begin) {
               timestampsPart[6] *= 10;
            }

            timestampsPart[6] *= 1000;
         }

         return timestampsPart;
      }
   }

   public String className() {
      return LocalDateTime.class.getName();
   }

   public boolean canDecode(ColumnDecoder column, Class<?> type) {
      return COMPATIBLE_TYPES.contains(column.getType()) && type.isAssignableFrom(LocalDateTime.class);
   }

   public boolean canEncode(Object value) {
      return value instanceof LocalDateTime;
   }

   public LocalDateTime decodeText(ReadableByteBuf buf, MutableInt length, ColumnDecoder column, Calendar cal) throws SQLDataException {
      int[] parts;
      switch(column.getType()) {
      case BLOB:
      case TINYBLOB:
      case MEDIUMBLOB:
      case LONGBLOB:
         if (column.isBinary()) {
            buf.skip(length.get());
            throw new SQLDataException(String.format("Data type %s cannot be decoded as LocalDateTime", column.getType()));
         }
      case STRING:
      case VARCHAR:
      case VARSTRING:
         String val = buf.readString(length.get());

         try {
            parts = parseTimestamp(val);
            if (parts == null) {
               length.set(-1);
               return null;
            }

            return LocalDateTime.of(parts[0], parts[1], parts[2], parts[3], parts[4], parts[5]).plusNanos((long)parts[6]);
         } catch (DateTimeException var8) {
            throw new SQLDataException(String.format("value '%s' (%s) cannot be decoded as LocalDateTime", val, column.getType()));
         }
      case DATE:
         parts = LocalDateCodec.parseDate(buf, length);
         if (parts == null) {
            length.set(-1);
            return null;
         }

         return LocalDateTime.of(parts[0], parts[1], parts[2], 0, 0, 0);
      case DATETIME:
      case TIMESTAMP:
         parts = parseTimestamp(buf.readAscii(length.get()));
         if (parts == null) {
            length.set(-1);
            return null;
         }

         return LocalDateTime.of(parts[0], parts[1], parts[2], parts[3], parts[4], parts[5]).plusNanos((long)parts[6]);
      case TIME:
         parts = LocalTimeCodec.parseTime(buf, length, column);
         if (parts[0] == -1) {
            return LocalDateTime.of(1970, 1, 1, 0, 0).minusHours((long)(parts[1] % 24)).minusMinutes((long)parts[2]).minusSeconds((long)parts[3]).minusNanos((long)parts[4]);
         }

         return LocalDateTime.of(1970, 1, 1, parts[1] % 24, parts[2], parts[3]).plusNanos((long)parts[4]);
      case YEAR:
         int year = Integer.parseInt(buf.readAscii(length.get()));
         if (column.getColumnLength() <= 2L) {
            year += year >= 70 ? 1900 : 2000;
         }

         return LocalDateTime.of(year, 1, 1, 0, 0);
      default:
         buf.skip(length.get());
         throw new SQLDataException(String.format("Data type %s cannot be decoded as LocalDateTime", column.getType()));
      }
   }

   public LocalDateTime decodeBinary(ReadableByteBuf buf, MutableInt length, ColumnDecoder column, Calendar cal) throws SQLDataException {
      int year = 1970;
      int month = 1;
      long dayOfMonth = 1L;
      int hour = 0;
      int minutes = 0;
      int seconds = 0;
      long microseconds = 0L;
      switch(column.getType()) {
      case BLOB:
      case TINYBLOB:
      case MEDIUMBLOB:
      case LONGBLOB:
         if (column.isBinary()) {
            buf.skip(length.get());
            throw new SQLDataException(String.format("Data type %s cannot be decoded as LocalDateTime", column.getType()));
         }
      case STRING:
      case VARCHAR:
      case VARSTRING:
         String val = buf.readString(length.get());

         try {
            int[] parts = parseTimestamp(val);
            if (parts == null) {
               length.set(-1);
               return null;
            }

            return LocalDateTime.of(parts[0], parts[1], parts[2], parts[3], parts[4], parts[5]).plusNanos((long)parts[6]);
         } catch (DateTimeException var16) {
            throw new SQLDataException(String.format("value '%s' (%s) cannot be decoded as LocalDateTime", val, column.getType()));
         }
      case DATE:
      case DATETIME:
      case TIMESTAMP:
         if (length.get() == 0) {
            length.set(-1);
            return null;
         }

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
         break;
      case TIME:
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
               return LocalDateTime.of(1970, 1, 1, 0, 0).minusDays((long)day).minusHours((long)hour).minusMinutes((long)minutes).minusSeconds((long)seconds).minusNanos(microseconds * 1000L);
            }
         }
         break;
      case YEAR:
         year = buf.readUnsignedShort();
         if (column.getColumnLength() <= 2L) {
            year += year >= 70 ? 1900 : 2000;
         }
         break;
      default:
         buf.skip(length.get());
         throw new SQLDataException(String.format("Data type %s cannot be decoded as LocalDateTime", column.getType()));
      }

      return LocalDateTime.of(year, month, (int)dayOfMonth, hour, minutes, seconds).plusNanos(microseconds * 1000L);
   }

   public void encodeText(Writer encoder, Context context, Object value, Calendar cal, Long maxLen) throws IOException {
      LocalDateTime val = (LocalDateTime)value;
      encoder.writeByte(39);
      encoder.writeAscii(val.format(val.getNano() != 0 ? TIMESTAMP_FORMAT : TIMESTAMP_FORMAT_NO_FRACTIONAL));
      encoder.writeByte(39);
   }

   public void encodeBinary(Writer encoder, Object value, Calendar cal, Long maxLength) throws IOException {
      LocalDateTime val = (LocalDateTime)value;
      int nano = val.getNano();
      if (nano > 0) {
         encoder.writeByte(11);
         encoder.writeShort((short)val.getYear());
         encoder.writeByte(val.getMonthValue());
         encoder.writeByte(val.getDayOfMonth());
         encoder.writeByte(val.getHour());
         encoder.writeByte(val.getMinute());
         encoder.writeByte(val.getSecond());
         encoder.writeInt(nano / 1000);
      } else {
         encoder.writeByte(7);
         encoder.writeShort((short)val.getYear());
         encoder.writeByte(val.getMonthValue());
         encoder.writeByte(val.getDayOfMonth());
         encoder.writeByte(val.getHour());
         encoder.writeByte(val.getMinute());
         encoder.writeByte(val.getSecond());
      }

   }

   public int getBinaryEncodeType() {
      return DataType.DATETIME.get();
   }

   static {
      COMPATIBLE_TYPES = EnumSet.of(DataType.DATETIME, DataType.TIMESTAMP, DataType.VARSTRING, DataType.VARCHAR, DataType.STRING, DataType.TIME, DataType.YEAR, DataType.DATE, DataType.BLOB, DataType.TINYBLOB, DataType.MEDIUMBLOB, DataType.LONGBLOB);
      MARIADB_LOCAL_DATE_TIME = (new DateTimeFormatterBuilder()).parseCaseInsensitive().append(DateTimeFormatter.ISO_LOCAL_DATE).appendLiteral(' ').append(DateTimeFormatter.ISO_LOCAL_TIME).toFormatter();
   }
}
