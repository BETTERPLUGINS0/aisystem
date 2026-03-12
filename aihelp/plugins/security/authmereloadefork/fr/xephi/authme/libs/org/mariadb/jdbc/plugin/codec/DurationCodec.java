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
import java.time.Duration;
import java.util.Calendar;
import java.util.EnumSet;

public class DurationCodec implements Codec<Duration> {
   public static final DurationCodec INSTANCE = new DurationCodec();
   private static final EnumSet<DataType> COMPATIBLE_TYPES;

   public String className() {
      return Duration.class.getName();
   }

   public boolean canDecode(ColumnDecoder column, Class<?> type) {
      return COMPATIBLE_TYPES.contains(column.getType()) && type.isAssignableFrom(Duration.class);
   }

   public boolean canEncode(Object value) {
      return value instanceof Duration;
   }

   public Duration decodeText(ReadableByteBuf buf, MutableInt length, ColumnDecoder column, Calendar cal) throws SQLDataException {
      int[] parts;
      switch(column.getType()) {
      case TIMESTAMP:
      case DATETIME:
         parts = LocalDateTimeCodec.parseTimestamp(buf.readAscii(length.get()));
         if (parts == null) {
            length.set(-1);
            return null;
         }

         return Duration.ZERO.plusDays((long)(parts[2] - 1)).plusHours((long)parts[3]).plusMinutes((long)parts[4]).plusSeconds((long)parts[5]).plusNanos((long)parts[6]);
      case BLOB:
      case TINYBLOB:
      case MEDIUMBLOB:
      case LONGBLOB:
         if (column.isBinary()) {
            buf.skip(length.get());
            throw new SQLDataException(String.format("Data type %s cannot be decoded as Duration", column.getType()));
         }
      case TIME:
      case VARCHAR:
      case VARSTRING:
      case STRING:
         parts = LocalTimeCodec.parseTime(buf, length, column);
         Duration d = Duration.ZERO.plusHours((long)parts[1]).plusMinutes((long)parts[2]).plusSeconds((long)parts[3]).plusNanos((long)parts[4]);
         if (parts[0] == -1) {
            return d.negated();
         }

         return d;
      default:
         buf.skip(length.get());
         throw new SQLDataException(String.format("Data type %s cannot be decoded as Duration", column.getType()));
      }
   }

   public Duration decodeBinary(ReadableByteBuf buf, MutableInt length, ColumnDecoder column, Calendar cal) throws SQLDataException {
      long days = 0L;
      int hours = 0;
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
            days = (long)buf.readByte();
            if (length.get() > 4) {
               hours = buf.readByte();
               minutes = buf.readByte();
               seconds = buf.readByte();
               if (length.get() > 7) {
                  microseconds = buf.readUnsignedInt();
               }
            }

            if (year == 0 && month == 0 && days == 0L && hours == 0 && minutes == 0 && seconds == 0) {
               length.set(-1);
               return null;
            }

            return Duration.ZERO.plusDays(days - 1L).plusHours((long)hours).plusMinutes((long)minutes).plusSeconds((long)seconds).plusNanos(microseconds * 1000L);
         }
      case BLOB:
      case TINYBLOB:
      case MEDIUMBLOB:
      case LONGBLOB:
      default:
         buf.skip(length.get());
         throw new SQLDataException(String.format("Data type %s cannot be decoded as Duration", column.getType()));
      case TIME:
         boolean negate = false;
         if (length.get() > 0) {
            negate = buf.readUnsignedByte() == 1;
            if (length.get() > 4) {
               days = buf.readUnsignedInt();
               if (length.get() > 7) {
                  hours = buf.readByte();
                  minutes = buf.readByte();
                  seconds = buf.readByte();
                  if (length.get() > 8) {
                     microseconds = (long)buf.readInt();
                  }
               }
            }
         }

         Duration duration = Duration.ZERO.plusDays(days).plusHours((long)hours).plusMinutes((long)minutes).plusSeconds((long)seconds).plusNanos(microseconds * 1000L);
         if (negate) {
            return duration.negated();
         }

         return duration;
      case VARCHAR:
      case VARSTRING:
      case STRING:
         int[] parts = LocalTimeCodec.parseTime(buf, length, column);
         Duration d = Duration.ZERO.plusHours((long)parts[1]).plusMinutes((long)parts[2]).plusSeconds((long)parts[3]).plusNanos((long)parts[4]);
         return parts[0] == -1 ? d.negated() : d;
      }
   }

   public void encodeText(Writer encoder, Context context, Object val, Calendar cal, Long maxLen) throws IOException {
      long s = ((Duration)val).getSeconds();
      long microSecond = (long)(((Duration)val).getNano() / 1000);
      encoder.writeByte(39);
      if (microSecond != 0L) {
         encoder.writeAscii(String.format("%d:%02d:%02d.%06d", s / 3600L, s % 3600L / 60L, s % 60L, microSecond));
      } else {
         encoder.writeAscii(String.format("%d:%02d:%02d", s / 3600L, s % 3600L / 60L, s % 60L));
      }

      encoder.writeByte(39);
   }

   public void encodeBinary(Writer encoder, Object val, Calendar cal, Long maxLength) throws IOException {
      int nano = ((Duration)val).getNano();
      if (nano > 0) {
         encoder.writeByte(12);
         this.encodeDuration(encoder, (Duration)val);
         encoder.writeInt(nano / 1000);
      } else {
         encoder.writeByte(8);
         this.encodeDuration(encoder, (Duration)val);
      }

   }

   private void encodeDuration(Writer encoder, Duration value) throws IOException {
      encoder.writeByte((byte)(value.isNegative() ? 1 : 0));
      encoder.writeInt((int)value.toDays());
      encoder.writeByte((byte)((int)(value.toHours() - 24L * value.toDays())));
      encoder.writeByte((byte)((int)(value.toMinutes() - 60L * value.toHours())));
      encoder.writeByte((byte)((int)(value.getSeconds() - 60L * value.toMinutes())));
   }

   public int getBinaryEncodeType() {
      return DataType.TIME.get();
   }

   static {
      COMPATIBLE_TYPES = EnumSet.of(DataType.TIME, DataType.DATETIME, DataType.TIMESTAMP, DataType.VARSTRING, DataType.VARCHAR, DataType.STRING, DataType.BLOB, DataType.TINYBLOB, DataType.MEDIUMBLOB, DataType.LONGBLOB);
   }
}
