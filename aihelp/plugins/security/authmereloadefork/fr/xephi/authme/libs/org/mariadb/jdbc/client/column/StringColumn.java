package fr.xephi.authme.libs.org.mariadb.jdbc.client.column;

import fr.xephi.authme.libs.org.mariadb.jdbc.Configuration;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.ColumnDecoder;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.DataType;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.ReadableByteBuf;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.util.MutableInt;
import fr.xephi.authme.libs.org.mariadb.jdbc.message.server.ColumnDefinitionPacket;
import fr.xephi.authme.libs.org.mariadb.jdbc.plugin.codec.LocalDateTimeCodec;
import fr.xephi.authme.libs.org.mariadb.jdbc.plugin.codec.LocalTimeCodec;
import fr.xephi.authme.libs.org.mariadb.jdbc.util.CharsetEncodingLength;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.sql.Date;
import java.sql.SQLDataException;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.DateTimeException;
import java.util.Calendar;

public class StringColumn extends ColumnDefinitionPacket implements ColumnDecoder {
   public StringColumn(ReadableByteBuf buf, int charset, long length, DataType dataType, byte decimals, int flags, int[] stringPos, String extTypeName, String extTypeFormat) {
      super(buf, charset, length, dataType, decimals, flags, stringPos, extTypeName, extTypeFormat, false);
   }

   protected StringColumn(StringColumn prev) {
      super(prev, true);
   }

   public StringColumn useAliasAsName() {
      return new StringColumn(this);
   }

   public String defaultClassname(Configuration conf) {
      return this.isBinary() ? "byte[]" : String.class.getName();
   }

   public int getColumnType(Configuration conf) {
      if (this.dataType == DataType.NULL) {
         return 0;
      } else if (this.dataType == DataType.STRING) {
         return this.isBinary() ? -3 : 1;
      } else if (this.columnLength > 0L && this.getDisplaySize() <= 16777215) {
         return this.isBinary() ? -3 : 12;
      } else {
         return this.isBinary() ? -4 : -1;
      }
   }

   public String getColumnTypeName(Configuration conf) {
      switch(this.dataType) {
      case STRING:
         if (this.isBinary()) {
            return "BINARY";
         }

         return "CHAR";
      case VARSTRING:
      case VARCHAR:
         if (this.isBinary()) {
            return "VARBINARY";
         } else if (this.columnLength < 0L) {
            return "LONGTEXT";
         } else if (this.getDisplaySize() <= 65532) {
            return "VARCHAR";
         } else if (this.getDisplaySize() <= 65535) {
            return "TEXT";
         } else {
            if (this.getDisplaySize() <= 16777215) {
               return "MEDIUMTEXT";
            }

            return "LONGTEXT";
         }
      default:
         return this.dataType.name();
      }
   }

   public int getPrecision() {
      Integer maxWidth = (Integer)CharsetEncodingLength.maxCharlen.get(this.charset);
      return maxWidth == null ? (int)this.columnLength / 4 : (int)(this.columnLength / (long)maxWidth);
   }

   public Object getDefaultText(Configuration conf, ReadableByteBuf buf, MutableInt length) throws SQLDataException {
      if (this.isBinary()) {
         byte[] arr = new byte[length.get()];
         buf.readBytes(arr);
         return arr;
      } else {
         return buf.readString(length.get());
      }
   }

   public Object getDefaultBinary(Configuration conf, ReadableByteBuf buf, MutableInt length) throws SQLDataException {
      if (this.isBinary()) {
         byte[] arr = new byte[length.get()];
         buf.readBytes(arr);
         return arr;
      } else {
         return buf.readString(length.get());
      }
   }

   public boolean decodeBooleanText(ReadableByteBuf buf, MutableInt length) throws SQLDataException {
      return !"0".equals(buf.readAscii(length.get()));
   }

   public boolean decodeBooleanBinary(ReadableByteBuf buf, MutableInt length) throws SQLDataException {
      return !"0".equals(buf.readAscii(length.get()));
   }

   public byte decodeByteText(ReadableByteBuf buf, MutableInt length) throws SQLDataException {
      if (this.isBinary()) {
         byte b = buf.readByte();
         if (length.get() > 1) {
            buf.skip(length.get() - 1);
         }

         return b;
      } else {
         String str = buf.readString(length.get());

         long result;
         try {
            result = (new BigDecimal(str)).setScale(0, RoundingMode.DOWN).longValue();
         } catch (NumberFormatException var7) {
            throw new SQLDataException(String.format("value '%s' (%s) cannot be decoded as Byte", str, this.dataType));
         }

         if ((long)((byte)((int)result)) == result && (result >= 0L || this.isSigned())) {
            return (byte)((int)result);
         } else {
            throw new SQLDataException("byte overflow");
         }
      }
   }

   public byte decodeByteBinary(ReadableByteBuf buf, MutableInt length) throws SQLDataException {
      return this.decodeByteText(buf, length);
   }

   public String decodeStringText(ReadableByteBuf buf, MutableInt length, Calendar cal) throws SQLDataException {
      return buf.readString(length.get());
   }

   public String decodeStringBinary(ReadableByteBuf buf, MutableInt length, Calendar cal) throws SQLDataException {
      return buf.readString(length.get());
   }

   public short decodeShortText(ReadableByteBuf buf, MutableInt length) throws SQLDataException {
      String str = buf.readString(length.get());

      try {
         return (new BigDecimal(str)).setScale(0, RoundingMode.DOWN).shortValueExact();
      } catch (ArithmeticException | NumberFormatException var5) {
         throw new SQLDataException(String.format("value '%s' cannot be decoded as Short", str));
      }
   }

   public short decodeShortBinary(ReadableByteBuf buf, MutableInt length) throws SQLDataException {
      return this.decodeShortText(buf, length);
   }

   public int decodeIntText(ReadableByteBuf buf, MutableInt length) throws SQLDataException {
      String str = buf.readString(length.get());

      try {
         return (new BigDecimal(str)).setScale(0, RoundingMode.DOWN).intValueExact();
      } catch (ArithmeticException | NumberFormatException var5) {
         throw new SQLDataException(String.format("value '%s' cannot be decoded as Integer", str));
      }
   }

   public int decodeIntBinary(ReadableByteBuf buf, MutableInt length) throws SQLDataException {
      return this.decodeIntText(buf, length);
   }

   public long decodeLongText(ReadableByteBuf buf, MutableInt length) throws SQLDataException {
      String str = buf.readString(length.get());

      try {
         return (new BigInteger(str)).longValueExact();
      } catch (NumberFormatException var5) {
         throw new SQLDataException(String.format("value '%s' cannot be decoded as Long", str));
      }
   }

   public long decodeLongBinary(ReadableByteBuf buf, MutableInt length) throws SQLDataException {
      return this.decodeLongText(buf, length);
   }

   public float decodeFloatText(ReadableByteBuf buf, MutableInt length) throws SQLDataException {
      String val = buf.readString(length.get());

      try {
         return Float.parseFloat(val);
      } catch (NumberFormatException var5) {
         throw new SQLDataException(String.format("value '%s' cannot be decoded as Float", val));
      }
   }

   public float decodeFloatBinary(ReadableByteBuf buf, MutableInt length) throws SQLDataException {
      return this.decodeFloatText(buf, length);
   }

   public double decodeDoubleText(ReadableByteBuf buf, MutableInt length) throws SQLDataException {
      String str2 = buf.readString(length.get());

      try {
         return Double.parseDouble(str2);
      } catch (NumberFormatException var5) {
         throw new SQLDataException(String.format("value '%s' cannot be decoded as Double", str2));
      }
   }

   public double decodeDoubleBinary(ReadableByteBuf buf, MutableInt length) throws SQLDataException {
      return this.decodeDoubleText(buf, length);
   }

   public Date decodeDateText(ReadableByteBuf buf, MutableInt length, Calendar cal) throws SQLDataException {
      String val = buf.readString(length.get());
      if ("0000-00-00".equals(val)) {
         return null;
      } else {
         String[] stDatePart = val.split("[- ]");
         if (stDatePart.length < 3) {
            throw new SQLDataException(String.format("value '%s' (%s) cannot be decoded as Date", val, this.dataType));
         } else {
            try {
               int year = Integer.parseInt(stDatePart[0]);
               int month = Integer.parseInt(stDatePart[1]);
               int dayOfMonth = Integer.parseInt(stDatePart[2]);
               if (cal == null) {
                  Calendar c = Calendar.getInstance();
                  c.clear();
                  c.set(1, year);
                  c.set(2, month - 1);
                  c.set(5, dayOfMonth);
                  return new Date(c.getTimeInMillis());
               } else {
                  synchronized(cal) {
                     cal.clear();
                     cal.set(1, year);
                     cal.set(2, month - 1);
                     cal.set(5, dayOfMonth);
                     return new Date(cal.getTimeInMillis());
                  }
               }
            } catch (NumberFormatException var12) {
               throw new SQLDataException(String.format("value '%s' (%s) cannot be decoded as Date", val, this.dataType));
            }
         }
      }
   }

   public Date decodeDateBinary(ReadableByteBuf buf, MutableInt length, Calendar cal) throws SQLDataException {
      return this.decodeDateText(buf, length, cal);
   }

   public Time decodeTimeText(ReadableByteBuf buf, MutableInt length, Calendar cal) throws SQLDataException {
      Calendar c = cal == null ? Calendar.getInstance() : cal;
      int offset = c.getTimeZone().getOffset(0L);
      int[] parts = LocalTimeCodec.parseTime(buf, length, this);
      long timeInMillis = ((long)parts[1] * 3600000L + (long)parts[2] * 60000L + (long)parts[3] * 1000L + (long)(parts[4] / 1000000)) * (long)parts[0] - (long)offset;
      return new Time(timeInMillis);
   }

   public Time decodeTimeBinary(ReadableByteBuf buf, MutableInt length, Calendar calParam) throws SQLDataException {
      int[] parts = LocalTimeCodec.parseTime(buf, length, this);
      Time t;
      if (calParam == null) {
         Calendar cal = Calendar.getInstance();
         cal.clear();
         cal.setLenient(true);
         if (parts[0] == -1) {
            cal.set(1970, 0, 1, parts[0] * parts[1], parts[0] * parts[2], parts[0] * parts[3] - 1);
            t = new Time(cal.getTimeInMillis() + (long)(1000 - parts[4]));
         } else {
            cal.set(1970, 0, 1, parts[1], parts[2], parts[3]);
            t = new Time(cal.getTimeInMillis() + (long)(parts[4] / 1000000));
         }
      } else {
         synchronized(calParam) {
            calParam.clear();
            calParam.setLenient(true);
            if (parts[0] == -1) {
               calParam.set(1970, 0, 1, parts[0] * parts[1], parts[0] * parts[2], parts[0] * parts[3] - 1);
               t = new Time(calParam.getTimeInMillis() + (long)(1000 - parts[4]));
            } else {
               calParam.set(1970, 0, 1, parts[1], parts[2], parts[3]);
               t = new Time(calParam.getTimeInMillis() + (long)(parts[4] / 1000000));
            }
         }
      }

      return t;
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
         } else {
            synchronized(calParam) {
               calParam.clear();
               calParam.set(timestampsPart[0], timestampsPart[1] - 1, timestampsPart[2], timestampsPart[3], timestampsPart[4], timestampsPart[5]);
               timestamp = new Timestamp(calParam.getTime().getTime());
            }
         }

         timestamp.setNanos(timestampsPart[6] * 1000);
         return timestamp;
      }
   }

   public Timestamp decodeTimestampBinary(ReadableByteBuf buf, MutableInt length, Calendar calParam) throws SQLDataException {
      String val = buf.readString(length.get());

      try {
         int[] parts = LocalDateTimeCodec.parseTimestamp(val);
         if (parts == null) {
            length.set(-1);
            return null;
         } else {
            int year = parts[0];
            int month = parts[1];
            int dayOfMonth = parts[2];
            int hour = parts[3];
            int minutes = parts[4];
            int seconds = parts[5];
            int microseconds = parts[6] / 1000;
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

            timestamp.setNanos(microseconds * 1000);
            return timestamp;
         }
      } catch (DateTimeException var17) {
         throw new SQLDataException(String.format("value '%s' (%s) cannot be decoded as Timestamp", val, this.dataType));
      }
   }
}
