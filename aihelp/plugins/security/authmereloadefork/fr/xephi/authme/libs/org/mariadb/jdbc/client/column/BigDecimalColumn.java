package fr.xephi.authme.libs.org.mariadb.jdbc.client.column;

import fr.xephi.authme.libs.org.mariadb.jdbc.Configuration;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.ColumnDecoder;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.DataType;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.ReadableByteBuf;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.util.MutableInt;
import fr.xephi.authme.libs.org.mariadb.jdbc.message.server.ColumnDefinitionPacket;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.sql.SQLDataException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;

public class BigDecimalColumn extends ColumnDefinitionPacket implements ColumnDecoder {
   public BigDecimalColumn(ReadableByteBuf buf, int charset, long length, DataType dataType, byte decimals, int flags, int[] stringPos, String extTypeName, String extTypeFormat) {
      super(buf, charset, length, dataType, decimals, flags, stringPos, extTypeName, extTypeFormat, false);
   }

   protected BigDecimalColumn(BigDecimalColumn prev) {
      super(prev, true);
   }

   public BigDecimalColumn useAliasAsName() {
      return new BigDecimalColumn(this);
   }

   public String defaultClassname(Configuration conf) {
      return BigDecimal.class.getName();
   }

   public int getColumnType(Configuration conf) {
      return 3;
   }

   public String getColumnTypeName(Configuration conf) {
      return this.isSigned() ? "DECIMAL" : "DECIMAL UNSIGNED";
   }

   public int getPrecision() {
      return this.isSigned() ? (int)(this.columnLength - (long)(this.decimals > 0 ? 2 : 1)) : (int)(this.columnLength - (long)(this.decimals > 0 ? 1 : 0));
   }

   public Object getDefaultText(Configuration conf, ReadableByteBuf buf, MutableInt length) throws SQLDataException {
      return new BigDecimal(buf.readAscii(length.get()));
   }

   public Object getDefaultBinary(Configuration conf, ReadableByteBuf buf, MutableInt length) throws SQLDataException {
      return new BigDecimal(buf.readAscii(length.get()));
   }

   public boolean decodeBooleanText(ReadableByteBuf buf, MutableInt length) throws SQLDataException {
      return (new BigDecimal(buf.readAscii(length.get()))).intValue() != 0;
   }

   public boolean decodeBooleanBinary(ReadableByteBuf buf, MutableInt length) throws SQLDataException {
      return this.decodeBooleanText(buf, length);
   }

   public byte decodeByteText(ReadableByteBuf buf, MutableInt length) throws SQLDataException {
      String str = buf.readString(length.get());

      try {
         return (new BigDecimal(str)).setScale(0, RoundingMode.DOWN).byteValueExact();
      } catch (ArithmeticException | NumberFormatException var5) {
         throw new SQLDataException(String.format("value '%s' (%s) cannot be decoded as Byte", str, this.dataType));
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
      return this.decodeShortText(buf, length);
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
      return this.decodeIntText(buf, length);
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
      String str = buf.readString(length.get());

      try {
         return (new BigDecimal(str)).setScale(0, RoundingMode.DOWN).longValueExact();
      } catch (ArithmeticException | NumberFormatException var5) {
         throw new SQLDataException(String.format("value '%s' cannot be decoded as Long", str));
      }
   }

   public float decodeFloatText(ReadableByteBuf buf, MutableInt length) throws SQLDataException {
      return Float.parseFloat(buf.readAscii(length.get()));
   }

   public float decodeFloatBinary(ReadableByteBuf buf, MutableInt length) throws SQLDataException {
      return (new BigDecimal(buf.readAscii(length.get()))).floatValue();
   }

   public double decodeDoubleText(ReadableByteBuf buf, MutableInt length) throws SQLDataException {
      return Double.parseDouble(buf.readAscii(length.get()));
   }

   public double decodeDoubleBinary(ReadableByteBuf buf, MutableInt length) throws SQLDataException {
      return (new BigDecimal(buf.readAscii(length.get()))).doubleValue();
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
      buf.skip(length.get());
      throw new SQLDataException(String.format("Data type %s cannot be decoded as Time", this.dataType));
   }

   public Time decodeTimeBinary(ReadableByteBuf buf, MutableInt length, Calendar cal) throws SQLDataException {
      buf.skip(length.get());
      throw new SQLDataException(String.format("Data type %s cannot be decoded as Time", this.dataType));
   }

   public Timestamp decodeTimestampText(ReadableByteBuf buf, MutableInt length, Calendar cal) throws SQLDataException {
      buf.skip(length.get());
      throw new SQLDataException(String.format("Data type %s cannot be decoded as Timestamp", this.dataType));
   }

   public Timestamp decodeTimestampBinary(ReadableByteBuf buf, MutableInt length, Calendar cal) throws SQLDataException {
      buf.skip(length.get());
      throw new SQLDataException(String.format("Data type %s cannot be decoded as Timestamp", this.dataType));
   }
}
