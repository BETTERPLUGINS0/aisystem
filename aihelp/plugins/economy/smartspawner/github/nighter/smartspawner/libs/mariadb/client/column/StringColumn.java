package github.nighter.smartspawner.libs.mariadb.client.column;

import github.nighter.smartspawner.libs.mariadb.Configuration;
import github.nighter.smartspawner.libs.mariadb.client.ColumnDecoder;
import github.nighter.smartspawner.libs.mariadb.client.Context;
import github.nighter.smartspawner.libs.mariadb.client.DataType;
import github.nighter.smartspawner.libs.mariadb.client.ReadableByteBuf;
import github.nighter.smartspawner.libs.mariadb.client.util.MutableInt;
import github.nighter.smartspawner.libs.mariadb.message.server.ColumnDefinitionPacket;
import github.nighter.smartspawner.libs.mariadb.plugin.codec.LocalDateTimeCodec;
import github.nighter.smartspawner.libs.mariadb.plugin.codec.LocalTimeCodec;
import github.nighter.smartspawner.libs.mariadb.util.CharsetEncodingLength;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.sql.Date;
import java.sql.SQLDataException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;

public class StringColumn extends ColumnDefinitionPacket implements ColumnDecoder {
   private static final int NULL_LENGTH = -1;

   public StringColumn(ReadableByteBuf buf, int charset, long length, DataType dataType, byte decimals, int flags, int[] stringPos, String extTypeName, String extTypeFormat) {
      super(buf, charset, length, dataType, decimals, flags, stringPos, extTypeName, extTypeFormat, false);
   }

   protected StringColumn(StringColumn prev) {
      super(prev, true);
   }

   public int getDisplaySize() {
      if (this.charset != 63) {
         Integer maxWidth = (Integer)CharsetEncodingLength.maxCharlen.get(this.charset);
         return maxWidth != null ? (int)(this.columnLength / (long)maxWidth) : (int)(this.columnLength / 4L);
      } else {
         return (int)this.columnLength;
      }
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

   public Object getDefaultText(ReadableByteBuf buf, MutableInt length, Context context) throws SQLDataException {
      if (this.isBinary()) {
         byte[] arr = new byte[length.get()];
         buf.readBytes(arr);
         return arr;
      } else {
         return buf.readString(length.get());
      }
   }

   public Object getDefaultBinary(ReadableByteBuf buf, MutableInt length, Context context) throws SQLDataException {
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

   public String decodeStringText(ReadableByteBuf buf, MutableInt length, Calendar cal, Context context) throws SQLDataException {
      return buf.readString(length.get());
   }

   public String decodeStringBinary(ReadableByteBuf buf, MutableInt length, Calendar cal, Context context) throws SQLDataException {
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

   public Date decodeDateText(ReadableByteBuf buf, MutableInt length, Calendar cal, Context context) throws SQLDataException {
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
            } catch (NumberFormatException var13) {
               throw new SQLDataException(String.format("value '%s' (%s) cannot be decoded as Date", val, this.dataType));
            }
         }
      }
   }

   public Date decodeDateBinary(ReadableByteBuf buf, MutableInt length, Calendar cal, Context context) throws SQLDataException {
      return this.decodeDateText(buf, length, cal, context);
   }

   public Time decodeTimeText(ReadableByteBuf buf, MutableInt length, Calendar cal, Context context) throws SQLDataException {
      Calendar c = cal == null ? Calendar.getInstance() : cal;
      int offset = c.getTimeZone().getOffset(0L);
      int[] parts = LocalTimeCodec.parseTime(buf, length, this);
      long timeInMillis = ((long)parts[1] * 3600000L + (long)parts[2] * 60000L + (long)parts[3] * 1000L + (long)(parts[4] / 1000000)) * (long)parts[0] - (long)offset;
      return new Time(timeInMillis);
   }

   public Time decodeTimeBinary(ReadableByteBuf buf, MutableInt length, Calendar calParam, Context context) throws SQLDataException {
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

   public Timestamp decodeTimestampText(ReadableByteBuf buf, MutableInt length, Calendar calParam, Context context) throws SQLDataException {
      try {
         int[] parts = LocalDateTimeCodec.parseTextTimestamp(buf, length);
         if (LocalDateTimeCodec.isZeroTimestamp(parts)) {
            length.set(-1);
            return null;
         } else {
            return this.createTimestamp(parts, calParam);
         }
      } catch (IllegalArgumentException var6) {
         throw new SQLDataException(String.format("value '%s' (%s) cannot be decoded as Timestamp", buf.readString(length.get()), this.dataType));
      }
   }

   public Timestamp decodeTimestampBinary(ReadableByteBuf buf, MutableInt length, Calendar calParam, Context context) throws SQLDataException {
      return this.decodeTimestampText(buf, length, calParam, context);
   }

   private Timestamp createTimestamp(int[] parts, Calendar calParam) {
      Calendar calendar = calParam != null ? calParam : Calendar.getInstance();
      Timestamp timestamp;
      if (calParam != null) {
         synchronized(calParam) {
            timestamp = this.createTimestampWithCalendar(parts, calendar);
         }
      } else {
         timestamp = this.createTimestampWithCalendar(parts, calendar);
      }

      timestamp.setNanos(parts[6]);
      return timestamp;
   }

   private Timestamp createTimestampWithCalendar(int[] parts, Calendar calendar) {
      calendar.clear();
      calendar.set(parts[0], parts[1] - 1, parts[2], parts[3], parts[4], parts[5]);
      return new Timestamp(calendar.getTime().getTime());
   }
}
