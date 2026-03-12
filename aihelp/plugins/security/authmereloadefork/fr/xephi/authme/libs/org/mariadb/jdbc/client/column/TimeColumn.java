package fr.xephi.authme.libs.org.mariadb.jdbc.client.column;

import fr.xephi.authme.libs.org.mariadb.jdbc.Configuration;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.ColumnDecoder;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.DataType;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.ReadableByteBuf;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.util.MutableInt;
import fr.xephi.authme.libs.org.mariadb.jdbc.message.server.ColumnDefinitionPacket;
import fr.xephi.authme.libs.org.mariadb.jdbc.plugin.codec.LocalTimeCodec;
import java.sql.Date;
import java.sql.SQLDataException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;

public class TimeColumn extends ColumnDefinitionPacket implements ColumnDecoder {
   public TimeColumn(ReadableByteBuf buf, int charset, long length, DataType dataType, byte decimals, int flags, int[] stringPos, String extTypeName, String extTypeFormat) {
      super(buf, charset, length, dataType, decimals, flags, stringPos, extTypeName, extTypeFormat, false);
   }

   protected TimeColumn(TimeColumn prev) {
      super(prev, true);
   }

   public TimeColumn useAliasAsName() {
      return new TimeColumn(this);
   }

   public String defaultClassname(Configuration conf) {
      return Time.class.getName();
   }

   public int getColumnType(Configuration conf) {
      return 92;
   }

   public String getColumnTypeName(Configuration conf) {
      return "TIME";
   }

   public Object getDefaultText(Configuration conf, ReadableByteBuf buf, MutableInt length) throws SQLDataException {
      Calendar c = Calendar.getInstance();
      int offset = c.getTimeZone().getOffset(0L);
      int[] parts = LocalTimeCodec.parseTime(buf, length, this);
      long timeInMillis = ((long)parts[1] * 3600000L + (long)parts[2] * 60000L + (long)parts[3] * 1000L + (long)(parts[4] / 1000000)) * (long)parts[0] - (long)offset;
      return new Time(timeInMillis);
   }

   public Object getDefaultBinary(Configuration conf, ReadableByteBuf buf, MutableInt length) throws SQLDataException {
      boolean negate = false;
      Calendar cal = Calendar.getInstance();
      long dayOfMonth = 0L;
      int hour = 0;
      int minutes = 0;
      int seconds = 0;
      long microseconds = 0L;
      if (length.get() > 0) {
         negate = buf.readByte() == 1;
         dayOfMonth = buf.readUnsignedInt();
         hour = buf.readByte();
         minutes = buf.readByte();
         seconds = buf.readByte();
         if (length.get() > 8) {
            microseconds = buf.readUnsignedInt();
         }
      }

      int offset = cal.getTimeZone().getOffset(0L);
      long timeInMillis = ((24L * dayOfMonth + (long)hour) * 3600000L + (long)(minutes * '\uea60') + (long)(seconds * 1000) + microseconds / 1000L) * (long)(negate ? -1 : 1) - (long)offset;
      return new Time(timeInMillis);
   }

   public byte decodeByteText(ReadableByteBuf buf, MutableInt length) throws SQLDataException {
      buf.skip(length.get());
      throw new SQLDataException(String.format("Data type %s cannot be decoded as Byte", this.dataType));
   }

   public byte decodeByteBinary(ReadableByteBuf buf, MutableInt length) throws SQLDataException {
      buf.skip(length.get());
      throw new SQLDataException(String.format("Data type %s cannot be decoded as Byte", this.dataType));
   }

   public boolean decodeBooleanText(ReadableByteBuf buf, MutableInt length) throws SQLDataException {
      buf.skip(length.get());
      throw new SQLDataException(String.format("Data type %s cannot be decoded as Boolean", this.dataType));
   }

   public boolean decodeBooleanBinary(ReadableByteBuf buf, MutableInt length) throws SQLDataException {
      buf.skip(length.get());
      throw new SQLDataException(String.format("Data type %s cannot be decoded as Boolean", this.dataType));
   }

   public String decodeStringText(ReadableByteBuf buf, MutableInt length, Calendar cal) throws SQLDataException {
      return buf.readString(length.get());
   }

   public String decodeStringBinary(ReadableByteBuf buf, MutableInt length, Calendar cal) throws SQLDataException {
      long tDays = 0L;
      int tHours = 0;
      int tMinutes = 0;
      int tSeconds = 0;
      long tMicroseconds = 0L;
      int i;
      if (length.get() == 0) {
         StringBuilder zeroValue = new StringBuilder("00:00:00");
         if (this.getDecimals() > 0) {
            zeroValue.append(".");

            for(i = 0; i < this.getDecimals(); ++i) {
               zeroValue.append("0");
            }
         }

         return zeroValue.toString();
      } else {
         boolean negate = buf.readByte() == 1;
         if (length.get() > 4) {
            tDays = buf.readUnsignedInt();
            if (length.get() > 7) {
               tHours = buf.readByte();
               tMinutes = buf.readByte();
               tSeconds = buf.readByte();
               if (length.get() > 8) {
                  tMicroseconds = (long)buf.readInt();
               }
            }
         }

         i = (int)(tDays * 24L + (long)tHours);
         String stTime = (negate ? "-" : "") + (i < 10 ? "0" : "") + i + ":" + (tMinutes < 10 ? "0" : "") + tMinutes + ":" + (tSeconds < 10 ? "0" : "") + tSeconds;
         StringBuilder stMicro;
         if (this.getDecimals() == 0) {
            if (tMicroseconds == 0L) {
               return stTime;
            } else {
               stMicro = new StringBuilder(String.valueOf(tMicroseconds));

               while(stMicro.length() < 6) {
                  stMicro.insert(0, "0");
               }

               return stTime + "." + stMicro;
            }
         } else {
            stMicro = new StringBuilder(String.valueOf(tMicroseconds));

            while(stMicro.length() < this.getDecimals()) {
               stMicro.insert(0, "0");
            }

            return stTime + "." + stMicro;
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
      buf.skip(length.get());
      throw new SQLDataException(String.format("Data type %s cannot be decoded as Date", this.dataType));
   }

   public Date decodeDateBinary(ReadableByteBuf buf, MutableInt length, Calendar cal) throws SQLDataException {
      buf.skip(length.get());
      throw new SQLDataException(String.format("Data type %s cannot be decoded as Date", this.dataType));
   }

   public Time decodeTimeText(ReadableByteBuf buf, MutableInt length, Calendar cal) throws SQLDataException {
      Calendar c = cal == null ? Calendar.getInstance() : cal;
      int offset = c.getTimeZone().getOffset(0L);
      int[] parts = LocalTimeCodec.parseTime(buf, length, this);
      long timeInMillis = ((long)parts[1] * 3600000L + (long)parts[2] * 60000L + (long)parts[3] * 1000L + (long)(parts[4] / 1000000)) * (long)parts[0] - (long)offset;
      return new Time(timeInMillis);
   }

   public Time decodeTimeBinary(ReadableByteBuf buf, MutableInt length, Calendar calParam) throws SQLDataException {
      Calendar cal = calParam == null ? Calendar.getInstance() : calParam;
      long dayOfMonth = 0L;
      int hour = 0;
      int minutes = 0;
      int seconds = 0;
      long microseconds = 0L;
      boolean negate = false;
      if (length.get() > 0) {
         negate = buf.readByte() == 1;
         dayOfMonth = buf.readUnsignedInt();
         hour = buf.readByte();
         minutes = buf.readByte();
         seconds = buf.readByte();
         if (length.get() > 8) {
            microseconds = buf.readUnsignedInt();
         }
      }

      int offset = cal.getTimeZone().getOffset(0L);
      long timeInMillis = ((24L * dayOfMonth + (long)hour) * 3600000L + (long)(minutes * '\uea60') + (long)(seconds * 1000) + microseconds / 1000L) * (long)(negate ? -1 : 1) - (long)offset;
      return new Time(timeInMillis);
   }

   public Timestamp decodeTimestampText(ReadableByteBuf buf, MutableInt length, Calendar calParam) throws SQLDataException {
      int[] parts = LocalTimeCodec.parseTime(buf, length, this);
      Timestamp t;
      if (calParam == null) {
         Calendar cal = Calendar.getInstance();
         cal.clear();
         cal.setLenient(true);
         if (parts[0] == -1) {
            cal.set(1970, 0, 1, parts[0] * parts[1], parts[0] * parts[2], parts[0] * parts[3] - 1);
            t = new Timestamp(cal.getTimeInMillis());
            t.setNanos(1000000000 - parts[4]);
         } else {
            cal.set(1970, 0, 1, parts[1], parts[2], parts[3]);
            t = new Timestamp(cal.getTimeInMillis());
            t.setNanos(parts[4]);
         }
      } else {
         synchronized(calParam) {
            calParam.clear();
            calParam.setLenient(true);
            if (parts[0] == -1) {
               calParam.set(1970, 0, 1, parts[0] * parts[1], parts[0] * parts[2], parts[0] * parts[3] - 1);
               t = new Timestamp(calParam.getTimeInMillis());
               t.setNanos(1000000000 - parts[4]);
            } else {
               calParam.set(1970, 0, 1, parts[1], parts[2], parts[3]);
               t = new Timestamp(calParam.getTimeInMillis());
               t.setNanos(parts[4]);
            }
         }
      }

      return t;
   }

   public Timestamp decodeTimestampBinary(ReadableByteBuf buf, MutableInt length, Calendar calParam) throws SQLDataException {
      Calendar cal = calParam == null ? Calendar.getInstance() : calParam;
      long microseconds = 0L;
      boolean negate = buf.readByte() == 1;
      long dayOfMonth = buf.readUnsignedInt();
      int hour = buf.readByte();
      int minutes = buf.readByte();
      int seconds = buf.readByte();
      if (length.get() > 8) {
         microseconds = buf.readUnsignedInt();
      }

      int offset = cal.getTimeZone().getOffset(0L);
      long timeInMillis = ((24L * dayOfMonth + (long)hour) * 3600000L + (long)(minutes * '\uea60') + (long)(seconds * 1000) + microseconds / 1000L) * (long)(negate ? -1 : 1) - (long)offset;
      return new Timestamp(timeInMillis);
   }
}
