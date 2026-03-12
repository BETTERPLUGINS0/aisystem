package fr.xephi.authme.libs.org.mariadb.jdbc.client.column;

import fr.xephi.authme.libs.org.mariadb.jdbc.Configuration;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.ColumnDecoder;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.DataType;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.ReadableByteBuf;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.util.MutableInt;
import fr.xephi.authme.libs.org.mariadb.jdbc.message.server.ColumnDefinitionPacket;
import fr.xephi.authme.libs.org.mariadb.jdbc.plugin.codec.LocalDateTimeCodec;
import fr.xephi.authme.libs.org.mariadb.jdbc.plugin.codec.TimeCodec;
import java.sql.Date;
import java.sql.SQLDataException;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

public class TimestampColumn extends ColumnDefinitionPacket implements ColumnDecoder {
   public TimestampColumn(ReadableByteBuf buf, int charset, long length, DataType dataType, byte decimals, int flags, int[] stringPos, String extTypeName, String extTypeFormat) {
      super(buf, charset, length, dataType, decimals, flags, stringPos, extTypeName, extTypeFormat, false);
   }

   protected TimestampColumn(TimestampColumn prev) {
      super(prev, true);
   }

   public TimestampColumn useAliasAsName() {
      return new TimestampColumn(this);
   }

   public String defaultClassname(Configuration conf) {
      return Timestamp.class.getName();
   }

   public int getColumnType(Configuration conf) {
      return 93;
   }

   public String getColumnTypeName(Configuration conf) {
      return this.dataType.name();
   }

   public Object getDefaultText(Configuration conf, ReadableByteBuf buf, MutableInt length) throws SQLDataException {
      return this.decodeTimestampText(buf, length, (Calendar)null);
   }

   public Object getDefaultBinary(Configuration conf, ReadableByteBuf buf, MutableInt length) throws SQLDataException {
      return this.decodeTimestampBinary(buf, length, (Calendar)null);
   }

   public boolean decodeBooleanText(ReadableByteBuf buf, MutableInt length) throws SQLDataException {
      buf.skip(length.get());
      throw new SQLDataException(String.format("Data type %s cannot be decoded as Boolean", this.dataType));
   }

   public boolean decodeBooleanBinary(ReadableByteBuf buf, MutableInt length) throws SQLDataException {
      buf.skip(length.get());
      throw new SQLDataException(String.format("Data type %s cannot be decoded as Boolean", this.dataType));
   }

   public byte decodeByteText(ReadableByteBuf buf, MutableInt length) throws SQLDataException {
      buf.skip(length.get());
      throw new SQLDataException(String.format("Data type %s cannot be decoded as Byte", this.dataType));
   }

   public byte decodeByteBinary(ReadableByteBuf buf, MutableInt length) throws SQLDataException {
      buf.skip(length.get());
      throw new SQLDataException(String.format("Data type %s cannot be decoded as Byte", this.dataType));
   }

   public String decodeStringText(ReadableByteBuf buf, MutableInt length, Calendar cal) throws SQLDataException {
      return buf.readString(length.get());
   }

   public String decodeStringBinary(ReadableByteBuf buf, MutableInt length, Calendar cal) throws SQLDataException {
      if (length.get() == 0) {
         StringBuilder zeroValue = new StringBuilder("0000-00-00 00:00:00");
         if (this.getDecimals() > 0) {
            zeroValue.append(".");

            for(int i = 0; i < this.getDecimals(); ++i) {
               zeroValue.append("0");
            }
         }

         return zeroValue.toString();
      } else {
         int year = buf.readUnsignedShort();
         int month = buf.readByte();
         int day = buf.readByte();
         int hour = 0;
         int minutes = 0;
         int seconds = 0;
         long microseconds = 0L;
         if (length.get() > 4) {
            hour = buf.readByte();
            minutes = buf.readByte();
            seconds = buf.readByte();
            if (length.get() > 7) {
               microseconds = buf.readUnsignedInt();
            }
         }

         if (year == 0 && month == 0 && day == 0) {
            return "0000-00-00 00:00:00";
         } else {
            LocalDateTime dateTime = LocalDateTime.of(year, month, day, hour, minutes, seconds).plusNanos(microseconds * 1000L);
            StringBuilder microSecPattern = new StringBuilder();
            if (this.getDecimals() > 0 || microseconds > 0L) {
               int decimal = this.getDecimals() & 255;
               if (decimal == 0) {
                  decimal = 6;
               }

               microSecPattern.append(".");

               for(int i = 0; i < decimal; ++i) {
                  microSecPattern.append("S");
               }
            }

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss" + microSecPattern);
            return dateTime.toLocalDate().toString() + ' ' + dateTime.toLocalTime().format(formatter);
         }
      }
   }

   public short decodeShortText(ReadableByteBuf buf, MutableInt length) throws SQLDataException {
      buf.skip(length.get());
      throw new SQLDataException(String.format("Data type %s cannot be decoded as Short", this.dataType));
   }

   public short decodeShortBinary(ReadableByteBuf buf, MutableInt length) throws SQLDataException {
      buf.skip(length.get());
      throw new SQLDataException(String.format("Data type %s cannot be decoded as Short", this.dataType));
   }

   public int decodeIntText(ReadableByteBuf buf, MutableInt length) throws SQLDataException {
      buf.skip(length.get());
      throw new SQLDataException(String.format("Data type %s cannot be decoded as Integer", this.dataType));
   }

   public int decodeIntBinary(ReadableByteBuf buf, MutableInt length) throws SQLDataException {
      buf.skip(length.get());
      throw new SQLDataException(String.format("Data type %s cannot be decoded as Integer", this.dataType));
   }

   public long decodeLongText(ReadableByteBuf buf, MutableInt length) throws SQLDataException {
      buf.skip(length.get());
      throw new SQLDataException(String.format("Data type %s cannot be decoded as Long", this.dataType));
   }

   public long decodeLongBinary(ReadableByteBuf buf, MutableInt length) throws SQLDataException {
      buf.skip(length.get());
      throw new SQLDataException(String.format("Data type %s cannot be decoded as Long", this.dataType));
   }

   public float decodeFloatText(ReadableByteBuf buf, MutableInt length) throws SQLDataException {
      buf.skip(length.get());
      throw new SQLDataException(String.format("Data type %s cannot be decoded as Float", this.dataType));
   }

   public float decodeFloatBinary(ReadableByteBuf buf, MutableInt length) throws SQLDataException {
      buf.skip(length.get());
      throw new SQLDataException(String.format("Data type %s cannot be decoded as Float", this.dataType));
   }

   public double decodeDoubleText(ReadableByteBuf buf, MutableInt length) throws SQLDataException {
      buf.skip(length.get());
      throw new SQLDataException(String.format("Data type %s cannot be decoded as Double", this.dataType));
   }

   public double decodeDoubleBinary(ReadableByteBuf buf, MutableInt length) throws SQLDataException {
      buf.skip(length.get());
      throw new SQLDataException(String.format("Data type %s cannot be decoded as Double", this.dataType));
   }

   public Date decodeDateText(ReadableByteBuf buf, MutableInt length, Calendar cal) throws SQLDataException {
      int pos = buf.pos();
      int nanoBegin = -1;
      int[] timestampsPart = new int[]{0, 0, 0, 0, 0, 0, 0};
      int partIdx = 0;

      int begin;
      for(begin = 0; begin < length.get(); ++begin) {
         byte b = buf.readByte();
         if (b != 45 && b != 32 && b != 58) {
            if (b == 46) {
               ++partIdx;
               nanoBegin = begin;
            } else {
               if (b < 48 || b > 57) {
                  buf.pos(pos);
                  throw new SQLDataException(String.format("value '%s' (%s) cannot be decoded as Timestamp", buf.readString(length.get()), this.dataType));
               }

               timestampsPart[partIdx] = timestampsPart[partIdx] * 10 + b - 48;
            }
         } else {
            ++partIdx;
         }
      }

      if (timestampsPart[0] == 0 && timestampsPart[1] == 0 && timestampsPart[2] == 0 && timestampsPart[3] == 0 && timestampsPart[4] == 0 && timestampsPart[5] == 0 && timestampsPart[6] == 0) {
         length.set(-1);
         return null;
      } else {
         if (nanoBegin > 0) {
            for(begin = 0; begin < 6 - (length.get() - nanoBegin - 1); ++begin) {
               timestampsPart[6] *= 10;
            }
         }

         Timestamp timestamp;
         if (cal == null) {
            Calendar c = Calendar.getInstance();
            c.set(timestampsPart[0], timestampsPart[1] - 1, timestampsPart[2], timestampsPart[3], timestampsPart[4], timestampsPart[5]);
            timestamp = new Timestamp(c.getTime().getTime());
            timestamp.setNanos(timestampsPart[6] * 1000);
         } else {
            synchronized(cal) {
               cal.clear();
               cal.set(timestampsPart[0], timestampsPart[1] - 1, timestampsPart[2], timestampsPart[3], timestampsPart[4], timestampsPart[5]);
               timestamp = new Timestamp(cal.getTime().getTime());
               timestamp.setNanos(timestampsPart[6] * 1000);
            }
         }

         String st = timestamp.toString();
         return Date.valueOf(st.substring(0, 10));
      }
   }

   public Date decodeDateBinary(ReadableByteBuf buf, MutableInt length, Calendar calParam) throws SQLDataException {
      if (length.get() == 0) {
         length.set(-1);
         return null;
      } else {
         int year = buf.readUnsignedShort();
         int month = buf.readByte();
         int dayOfMonth = buf.readByte();
         int hour = 0;
         int minutes = 0;
         int seconds = 0;
         long microseconds = 0L;
         if (length.get() > 4) {
            hour = buf.readByte();
            minutes = buf.readByte();
            seconds = buf.readByte();
            if (length.get() > 7) {
               microseconds = buf.readUnsignedInt();
            }
         }

         if (year == 0 && month == 0 && dayOfMonth == 0 && hour == 0 && minutes == 0 && seconds == 0 && microseconds == 0L) {
            length.set(-1);
            return null;
         } else {
            Timestamp timestamp;
            if (calParam == null) {
               Calendar cal = Calendar.getInstance();
               cal.clear();
               cal.set(year, month - 1, dayOfMonth, hour, minutes, seconds);
               timestamp = new Timestamp(cal.getTimeInMillis());
            } else {
               synchronized(calParam) {
                  calParam.clear();
                  calParam.set(year, month - 1, dayOfMonth, hour, minutes, seconds);
                  timestamp = new Timestamp(calParam.getTimeInMillis());
               }
            }

            timestamp.setNanos((int)(microseconds * 1000L));
            String st = timestamp.toString();
            return Date.valueOf(st.substring(0, 10));
         }
      }
   }

   public Time decodeTimeText(ReadableByteBuf buf, MutableInt length, Calendar cal) throws SQLDataException {
      LocalDateTime lt = LocalDateTimeCodec.INSTANCE.decodeText(buf, length, this, cal);
      if (lt == null) {
         return null;
      } else {
         Calendar cc = cal == null ? Calendar.getInstance() : cal;
         ZonedDateTime d = TimeCodec.EPOCH_DATE.atTime(lt.toLocalTime()).atZone(cc.getTimeZone().toZoneId());
         return new Time(d.toEpochSecond() * 1000L + (long)(d.getNano() / 1000000));
      }
   }

   public Time decodeTimeBinary(ReadableByteBuf buf, MutableInt length, Calendar calParam) throws SQLDataException {
      if (length.get() == 0) {
         length.set(-1);
         return null;
      } else {
         int year = buf.readUnsignedShort();
         int month = buf.readByte();
         int dayOfMonth = buf.readByte();
         int hour = 0;
         int minutes = 0;
         int seconds = 0;
         long microseconds = 0L;
         if (length.get() > 4) {
            hour = buf.readByte();
            minutes = buf.readByte();
            seconds = buf.readByte();
            if (length.get() > 7) {
               microseconds = buf.readUnsignedInt();
            }
         }

         if (year == 0 && month == 0 && dayOfMonth == 0 && hour == 0 && minutes == 0 && seconds == 0) {
            length.set(-1);
            return null;
         } else if (calParam == null) {
            Calendar cal = Calendar.getInstance();
            cal.clear();
            cal.set(1970, 0, 1, hour, minutes, seconds);
            return new Time(cal.getTimeInMillis() + microseconds / 1000L);
         } else {
            synchronized(calParam) {
               calParam.clear();
               calParam.set(1970, 0, 1, hour, minutes, seconds);
               return new Time(calParam.getTimeInMillis() + microseconds / 1000L);
            }
         }
      }
   }

   public Timestamp decodeTimestampText(ReadableByteBuf buf, MutableInt length, Calendar calParam) throws SQLDataException {
      int pos = buf.pos();
      int nanoBegin = -1;
      int[] timestampsPart = new int[]{0, 0, 0, 0, 0, 0, 0};
      int partIdx = 0;

      int begin;
      for(begin = 0; begin < length.get(); ++begin) {
         byte b = buf.readByte();
         if (b != 45 && b != 32 && b != 58) {
            if (b == 46) {
               ++partIdx;
               nanoBegin = begin;
            } else {
               if (b < 48 || b > 57) {
                  buf.pos(pos);
                  throw new SQLDataException(String.format("value '%s' (%s) cannot be decoded as Timestamp", buf.readString(length.get()), this.dataType));
               }

               timestampsPart[partIdx] = timestampsPart[partIdx] * 10 + b - 48;
            }
         } else {
            ++partIdx;
         }
      }

      if (timestampsPart[0] == 0 && timestampsPart[1] == 0 && timestampsPart[2] == 0 && timestampsPart[3] == 0 && timestampsPart[4] == 0 && timestampsPart[5] == 0 && timestampsPart[6] == 0) {
         length.set(-1);
         return null;
      } else {
         if (nanoBegin > 0) {
            for(begin = 0; begin < 6 - (length.get() - nanoBegin - 1); ++begin) {
               timestampsPart[6] *= 10;
            }
         }

         Timestamp timestamp;
         if (calParam == null) {
            Calendar c = Calendar.getInstance();
            c.set(timestampsPart[0], timestampsPart[1] - 1, timestampsPart[2], timestampsPart[3], timestampsPart[4], timestampsPart[5]);
            timestamp = new Timestamp(c.getTime().getTime());
            timestamp.setNanos(timestampsPart[6] * 1000);
         } else {
            synchronized(calParam) {
               calParam.clear();
               calParam.set(timestampsPart[0], timestampsPart[1] - 1, timestampsPart[2], timestampsPart[3], timestampsPart[4], timestampsPart[5]);
               timestamp = new Timestamp(calParam.getTime().getTime());
               timestamp.setNanos(timestampsPart[6] * 1000);
            }
         }

         return timestamp;
      }
   }

   public Timestamp decodeTimestampBinary(ReadableByteBuf buf, MutableInt length, Calendar calParam) throws SQLDataException {
      if (length.get() == 0) {
         length.set(-1);
         return null;
      } else {
         int year = buf.readUnsignedShort();
         int month = buf.readByte();
         int dayOfMonth = buf.readByte();
         int hour = 0;
         int minutes = 0;
         int seconds = 0;
         long microseconds = 0L;
         if (length.get() > 4) {
            hour = buf.readByte();
            minutes = buf.readByte();
            seconds = buf.readByte();
            if (length.get() > 7) {
               microseconds = buf.readUnsignedInt();
            }
         }

         if (year == 0 && month == 0 && dayOfMonth == 0 && hour == 0 && minutes == 0 && seconds == 0 && microseconds == 0L) {
            length.set(-1);
            return null;
         } else {
            Timestamp timestamp;
            if (calParam == null) {
               Calendar cal = Calendar.getInstance();
               cal.clear();
               cal.set(year, month - 1, dayOfMonth, hour, minutes, seconds);
               timestamp = new Timestamp(cal.getTimeInMillis());
            } else {
               synchronized(calParam) {
                  calParam.clear();
                  calParam.set(year, month - 1, dayOfMonth, hour, minutes, seconds);
                  timestamp = new Timestamp(calParam.getTimeInMillis());
               }
            }

            timestamp.setNanos((int)(microseconds * 1000L));
            return timestamp;
         }
      }
   }
}
