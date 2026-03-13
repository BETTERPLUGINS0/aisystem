package github.nighter.smartspawner.libs.mariadb.client.column;

import github.nighter.smartspawner.libs.mariadb.Configuration;
import github.nighter.smartspawner.libs.mariadb.client.ColumnDecoder;
import github.nighter.smartspawner.libs.mariadb.client.Context;
import github.nighter.smartspawner.libs.mariadb.client.DataType;
import github.nighter.smartspawner.libs.mariadb.client.ReadableByteBuf;
import github.nighter.smartspawner.libs.mariadb.client.util.MutableInt;
import github.nighter.smartspawner.libs.mariadb.message.server.ColumnDefinitionPacket;
import github.nighter.smartspawner.libs.mariadb.plugin.codec.LocalDateTimeCodec;
import java.sql.Date;
import java.sql.SQLDataException;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class TimestampColumn extends ColumnDefinitionPacket implements ColumnDecoder {
   private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
   private static final DecimalFormat oldDecimalFormat;
   private static final int[] POWERS_OF_10;

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

   public Object getDefaultText(ReadableByteBuf buf, MutableInt length, Context context) throws SQLDataException {
      return this.decodeTimestampText(buf, length, (Calendar)null, context);
   }

   public Object getDefaultBinary(ReadableByteBuf buf, MutableInt length, Context context) throws SQLDataException {
      return this.decodeTimestampBinary(buf, length, (Calendar)null, context);
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

   public String decodeStringText(ReadableByteBuf buf, MutableInt length, Calendar providedCal, Context context) throws SQLDataException {
      if (length.get() == 0) {
         return this.buildZeroDate();
      } else {
         int initialPos = buf.pos();
         int initialLength = length.get();

         try {
            LocalDateTime ldt = this.parseText(buf, length);
            if (ldt == null) {
               return initialLength > 0 ? this.buildZeroDate() : null;
            } else {
               LocalDateTime modifiedLdt = localDateTimeToZoneDateTime(ldt, providedCal, context).toLocalDateTime();
               String timestampWithoutMicro = dateTimeFormatter.format(modifiedLdt);
               if (context.getConf().oldModeNoPrecisionTimestamp()) {
                  return timestampWithoutMicro + oldDecimalFormat.format((double)modifiedLdt.getNano() / 1.0E9D);
               } else {
                  return this.decimals == 0 ? timestampWithoutMicro : timestampWithoutMicro + "." + String.format(Locale.US, "%0" + this.decimals + "d", modifiedLdt.getNano() / 1000);
               }
            }
         } catch (DateTimeException var10) {
            buf.pos(initialPos);
            return buf.readString(length.get());
         }
      }
   }

   private String buildZeroDate() {
      StringBuilder zeroValue = new StringBuilder("0000-00-00 00:00:00");
      if (this.decimals > 0) {
         zeroValue.append(".");

         for(int i = 0; i < this.decimals; ++i) {
            zeroValue.append("0");
         }
      }

      return zeroValue.toString();
   }

   public String decodeStringBinary(ReadableByteBuf buf, MutableInt length, Calendar providedCal, Context context) throws SQLDataException {
      if (length.get() == 0) {
         return this.buildZeroDate();
      } else {
         int initialPos = buf.pos();
         int initialLength = length.get();

         try {
            LocalDateTime ldt = this.parseBinary(buf, length);
            if (ldt == null) {
               return initialLength > 0 ? this.buildZeroDate() : null;
            } else {
               LocalDateTime modifiedLdt = localDateTimeToZoneDateTime(ldt, providedCal, context).toLocalDateTime();
               String timestampWithoutMicro = dateTimeFormatter.format(modifiedLdt);
               if (context.getConf().oldModeNoPrecisionTimestamp()) {
                  return timestampWithoutMicro + oldDecimalFormat.format((double)modifiedLdt.getNano() / 1.0E9D);
               } else {
                  return this.decimals == 0 ? timestampWithoutMicro : timestampWithoutMicro + "." + String.format(Locale.US, "%0" + this.decimals + "d", modifiedLdt.getNano() / 1000);
               }
            }
         } catch (DateTimeException var17) {
            buf.pos(initialPos);
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

            StringBuilder sb = new StringBuilder();
            this.fill(year, 4, sb);
            sb.append("-");
            this.fill(month, 2, sb);
            sb.append("-");
            this.fill(dayOfMonth, 2, sb);
            sb.append(" ");
            this.fill(hour, 2, sb);
            sb.append(":");
            this.fill(minutes, 2, sb);
            sb.append(":");
            this.fill(seconds, 2, sb);
            if (this.getDecimals() == 0) {
               return sb.toString();
            } else {
               sb.append(".");
               this.fill((int)(microseconds / (long)POWERS_OF_10[6 - this.getDecimals()]), this.getDecimals(), sb);
               return sb.toString();
            }
         }
      }
   }

   private void fill(int val, int size, StringBuilder sb) {
      String valSt = String.valueOf(val);
      int zeroToAdd = size - valSt.length();

      for(int i = 0; i < zeroToAdd; ++i) {
         sb.append('0');
      }

      sb.append(valSt);
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

   public Date decodeDateText(ReadableByteBuf buf, MutableInt length, Calendar calParam, Context context) throws SQLDataException {
      LocalDateTime ldt = this.parseText(buf, length);
      return ldt == null ? null : new Date(localDateTimeToInstant(ldt, calParam, context) + (long)(ldt.getNano() / 1000000));
   }

   public Date decodeDateBinary(ReadableByteBuf buf, MutableInt length, Calendar calParam, Context context) throws SQLDataException {
      LocalDateTime ldt = this.parseBinary(buf, length);
      return ldt == null ? null : new Date(localDateTimeToInstant(ldt, calParam, context) + (long)(ldt.getNano() / 1000000));
   }

   public Time decodeTimeText(ReadableByteBuf buf, MutableInt length, Calendar calParam, Context context) throws SQLDataException {
      LocalDateTime ldt = this.parseText(buf, length);
      return ldt == null ? null : new Time(localDateTimeToInstant(ldt.withYear(1970).withMonth(1).withDayOfMonth(1), calParam, context) + (long)(ldt.getNano() / 1000000));
   }

   public Time decodeTimeBinary(ReadableByteBuf buf, MutableInt length, Calendar calParam, Context context) throws SQLDataException {
      LocalDateTime ldt = this.parseBinary(buf, length);
      return ldt == null ? null : new Time(localDateTimeToInstant(ldt.withYear(1970).withMonth(1).withDayOfMonth(1), calParam, context) + (long)(ldt.getNano() / 1000000));
   }

   public Timestamp decodeTimestampText(ReadableByteBuf buf, MutableInt length, Calendar calParam, Context context) throws SQLDataException {
      int[] parts = LocalDateTimeCodec.parseTextTimestamp(buf, length);
      if (LocalDateTimeCodec.isZeroTimestamp(parts)) {
         length.set(-1);
         return null;
      } else {
         Timestamp timestamp;
         try {
            LocalDateTime ldt = LocalDateTime.of(parts[0], parts[1], parts[2], parts[3], parts[4], parts[5]).plusNanos((long)parts[6]);
            timestamp = new Timestamp(localDateTimeToInstant(ldt, calParam, context));
            timestamp.setNanos(ldt.getNano());
            return timestamp;
         } catch (DateTimeException var12) {
            Calendar cal = calParam == null ? Calendar.getInstance() : calParam;
            synchronized(cal) {
               cal.setLenient(true);
               cal.clear();
               cal.set(1, parts[0]);
               cal.set(2, parts[1] - 1);
               cal.set(5, parts[2]);
               cal.set(11, parts[3]);
               cal.set(12, parts[4]);
               cal.set(13, parts[5]);
               cal.set(14, parts[6] / 1000000);
               timestamp = new Timestamp(cal.getTime().getTime());
            }

            timestamp.setNanos(parts[6]);
            return timestamp;
         }
      }
   }

   public Timestamp decodeTimestampBinary(ReadableByteBuf buf, MutableInt length, Calendar calParam, Context context) throws SQLDataException {
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

         Timestamp timestamp;
         try {
            LocalDateTime ldt = LocalDateTime.of(year, month, dayOfMonth, hour, minutes, seconds).plusNanos(microseconds * 1000L);
            if (ldt == null) {
               return null;
            } else {
               timestamp = new Timestamp(localDateTimeToInstant(ldt, calParam, context));
               timestamp.setNanos(ldt.getNano());
               return timestamp;
            }
         } catch (DateTimeException var19) {
            Calendar cal = calParam == null ? Calendar.getInstance() : calParam;
            synchronized(cal) {
               cal.setLenient(true);
               cal.clear();
               cal.set(1, year);
               cal.set(2, month - 1);
               cal.set(5, dayOfMonth);
               cal.set(11, hour);
               cal.set(12, minutes);
               cal.set(13, seconds);
               cal.set(14, (int)(microseconds / 1000000L));
               timestamp = new Timestamp(cal.getTime().getTime());
            }

            timestamp.setNanos((int)(microseconds * 1000L));
            return timestamp;
         }
      }
   }

   private LocalDateTime parseText(ReadableByteBuf buf, MutableInt length) {
      int[] parts = LocalDateTimeCodec.parseTextTimestamp(buf, length);
      if (LocalDateTimeCodec.isZeroTimestamp(parts)) {
         length.set(-1);
         return null;
      } else {
         return LocalDateTime.of(parts[0], parts[1], parts[2], parts[3], parts[4], parts[5]).plusNanos((long)parts[6]);
      }
   }

   private LocalDateTime parseBinary(ReadableByteBuf buf, MutableInt length) {
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

         return LocalDateTime.of(year, month, dayOfMonth, hour, minutes, seconds).plusNanos(microseconds * 1000L);
      }
   }

   public static long localDateTimeToInstant(LocalDateTime ldt, Calendar calParam, Context context) {
      if (calParam == null) {
         Calendar cal = context.getDefaultCalendar();
         cal.set(ldt.getYear(), ldt.getMonthValue() - 1, ldt.getDayOfMonth(), ldt.getHour(), ldt.getMinute(), ldt.getSecond());
         cal.set(14, 0);
         return cal.getTimeInMillis();
      } else {
         synchronized(calParam) {
            calParam.clear();
            calParam.set(ldt.getYear(), ldt.getMonthValue() - 1, ldt.getDayOfMonth(), ldt.getHour(), ldt.getMinute(), ldt.getSecond());
            return calParam.getTimeInMillis();
         }
      }
   }

   public static ZonedDateTime localDateTimeToZoneDateTime(LocalDateTime ldt, Calendar calParam, Context context) {
      if (calParam == null) {
         return context.getConf().preserveInstants() ? ldt.atZone(context.getConnectionTimeZone().toZoneId()).withZoneSameInstant(TimeZone.getDefault().toZoneId()) : ldt.atZone(TimeZone.getDefault().toZoneId());
      } else {
         return ldt.atZone(calParam.getTimeZone().toZoneId());
      }
   }

   static {
      oldDecimalFormat = new DecimalFormat(".0#####", DecimalFormatSymbols.getInstance(Locale.US));
      POWERS_OF_10 = new int[]{1, 10, 100, 1000, 10000, 100000, 1000000};
   }
}
