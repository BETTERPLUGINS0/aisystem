package github.nighter.smartspawner.libs.mariadb.client.column;

import github.nighter.smartspawner.libs.mariadb.Configuration;
import github.nighter.smartspawner.libs.mariadb.client.ColumnDecoder;
import github.nighter.smartspawner.libs.mariadb.client.Context;
import github.nighter.smartspawner.libs.mariadb.client.DataType;
import github.nighter.smartspawner.libs.mariadb.client.ReadableByteBuf;
import github.nighter.smartspawner.libs.mariadb.client.util.MutableInt;
import github.nighter.smartspawner.libs.mariadb.message.server.ColumnDefinitionPacket;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.sql.SQLDataException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;

public class FloatColumn extends ColumnDefinitionPacket implements ColumnDecoder {
   public FloatColumn(ReadableByteBuf buf, int charset, long length, DataType dataType, byte decimals, int flags, int[] stringPos, String extTypeName, String extTypeFormat) {
      super(buf, charset, length, dataType, decimals, flags, stringPos, extTypeName, extTypeFormat, false);
   }

   protected FloatColumn(FloatColumn prev) {
      super(prev, true);
   }

   public FloatColumn useAliasAsName() {
      return new FloatColumn(this);
   }

   public String defaultClassname(Configuration conf) {
      return Float.class.getName();
   }

   public int getColumnType(Configuration conf) {
      return 7;
   }

   public String getColumnTypeName(Configuration conf) {
      return this.isSigned() ? "FLOAT" : "FLOAT UNSIGNED";
   }

   public Object getDefaultText(ReadableByteBuf buf, MutableInt length, Context context) throws SQLDataException {
      return Float.parseFloat(buf.readAscii(length.get()));
   }

   public Object getDefaultBinary(ReadableByteBuf buf, MutableInt length, Context context) throws SQLDataException {
      return buf.readFloat();
   }

   public boolean decodeBooleanText(ReadableByteBuf buf, MutableInt length) throws SQLDataException {
      String s = buf.readAscii(length.get());
      return !"0".equals(s);
   }

   public boolean decodeBooleanBinary(ReadableByteBuf buf, MutableInt length) throws SQLDataException {
      return (int)buf.readFloat() != 0;
   }

   public byte decodeByteText(ReadableByteBuf buf, MutableInt length) throws SQLDataException {
      String str = buf.readString(length.get());

      long result;
      try {
         result = (long)(new BigDecimal(str)).setScale(0, RoundingMode.DOWN).byteValueExact();
      } catch (ArithmeticException | NumberFormatException var7) {
         throw new SQLDataException(String.format("value '%s' (%s) cannot be decoded as Byte", str, this.dataType));
      }

      if ((long)((byte)((int)result)) == result && (result >= 0L || this.isSigned())) {
         return (byte)((int)result);
      } else {
         throw new SQLDataException("byte overflow");
      }
   }

   public byte decodeByteBinary(ReadableByteBuf buf, MutableInt length) throws SQLDataException {
      long result = (long)buf.readFloat();
      if ((long)((byte)((int)result)) != result) {
         throw new SQLDataException("byte overflow");
      } else {
         return (byte)((int)result);
      }
   }

   public String decodeStringText(ReadableByteBuf buf, MutableInt length, Calendar cal, Context context) throws SQLDataException {
      return buf.readString(length.get());
   }

   public String decodeStringBinary(ReadableByteBuf buf, MutableInt length, Calendar cal, Context context) throws SQLDataException {
      return String.valueOf(buf.readFloat());
   }

   public short decodeShortText(ReadableByteBuf buf, MutableInt length) throws SQLDataException {
      String str = buf.readString(length.get());

      long result;
      try {
         result = (new BigDecimal(str)).setScale(0, RoundingMode.DOWN).longValueExact();
      } catch (ArithmeticException | NumberFormatException var7) {
         throw new SQLDataException(String.format("value '%s' cannot be decoded as Short", str));
      }

      if ((long)((short)((int)result)) == result && (result >= 0L || this.isSigned())) {
         return (short)((int)result);
      } else {
         throw new SQLDataException("Short overflow");
      }
   }

   public short decodeShortBinary(ReadableByteBuf buf, MutableInt length) throws SQLDataException {
      long result = (long)buf.readFloat();
      if ((long)((short)((int)result)) == result && (result >= 0L || this.isSigned())) {
         return (short)((int)result);
      } else {
         throw new SQLDataException("Short overflow");
      }
   }

   public int decodeIntText(ReadableByteBuf buf, MutableInt length) throws SQLDataException {
      String str = buf.readString(length.get());

      long result;
      try {
         result = (new BigDecimal(str)).setScale(0, RoundingMode.DOWN).longValueExact();
      } catch (ArithmeticException | NumberFormatException var7) {
         throw new SQLDataException(String.format("value '%s' cannot be decoded as Integer", str));
      }

      int res = (int)result;
      if ((long)res == result && (result >= 0L || this.isSigned())) {
         return res;
      } else {
         throw new SQLDataException("integer overflow");
      }
   }

   public int decodeIntBinary(ReadableByteBuf buf, MutableInt length) throws SQLDataException {
      long result = (long)buf.readFloat();
      int res = (int)result;
      if ((long)res != result) {
         throw new SQLDataException("integer overflow");
      } else {
         return res;
      }
   }

   public long decodeLongText(ReadableByteBuf buf, MutableInt length) throws SQLDataException {
      String str2 = buf.readAscii(length.get());

      try {
         return (new BigDecimal(str2)).setScale(0, RoundingMode.DOWN).longValueExact();
      } catch (ArithmeticException | NumberFormatException var5) {
         throw new SQLDataException(String.format("value '%s' cannot be decoded as Long", str2));
      }
   }

   public long decodeLongBinary(ReadableByteBuf buf, MutableInt length) throws SQLDataException {
      return (long)buf.readFloat();
   }

   public float decodeFloatText(ReadableByteBuf buf, MutableInt length) throws SQLDataException {
      return Float.parseFloat(buf.readAscii(length.get()));
   }

   public float decodeFloatBinary(ReadableByteBuf buf, MutableInt length) throws SQLDataException {
      return buf.readFloat();
   }

   public double decodeDoubleText(ReadableByteBuf buf, MutableInt length) throws SQLDataException {
      return Double.parseDouble(buf.readAscii(length.get()));
   }

   public double decodeDoubleBinary(ReadableByteBuf buf, MutableInt length) throws SQLDataException {
      return (double)buf.readFloat();
   }

   public Date decodeDateText(ReadableByteBuf buf, MutableInt length, Calendar cal, Context context) throws SQLDataException {
      buf.skip(length.get());
      throw new SQLDataException(String.format("Data type %s cannot be decoded as Date", this.dataType));
   }

   public Date decodeDateBinary(ReadableByteBuf buf, MutableInt length, Calendar cal, Context context) throws SQLDataException {
      buf.skip(length.get());
      throw new SQLDataException(String.format("Data type %s cannot be decoded as Date", this.dataType));
   }

   public Time decodeTimeText(ReadableByteBuf buf, MutableInt length, Calendar cal, Context context) throws SQLDataException {
      buf.skip(length.get());
      throw new SQLDataException(String.format("Data type %s cannot be decoded as Time", this.dataType));
   }

   public Time decodeTimeBinary(ReadableByteBuf buf, MutableInt length, Calendar cal, Context context) throws SQLDataException {
      buf.skip(length.get());
      throw new SQLDataException(String.format("Data type %s cannot be decoded as Time", this.dataType));
   }

   public Timestamp decodeTimestampText(ReadableByteBuf buf, MutableInt length, Calendar cal, Context context) throws SQLDataException {
      buf.skip(length.get());
      throw new SQLDataException(String.format("Data type %s cannot be decoded as Timestamp", this.dataType));
   }

   public Timestamp decodeTimestampBinary(ReadableByteBuf buf, MutableInt length, Calendar cal, Context context) throws SQLDataException {
      buf.skip(length.get());
      throw new SQLDataException(String.format("Data type %s cannot be decoded as Timestamp", this.dataType));
   }
}
