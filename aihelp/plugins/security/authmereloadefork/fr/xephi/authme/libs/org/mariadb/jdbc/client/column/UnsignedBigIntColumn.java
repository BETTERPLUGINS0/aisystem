package fr.xephi.authme.libs.org.mariadb.jdbc.client.column;

import fr.xephi.authme.libs.org.mariadb.jdbc.Configuration;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.ColumnDecoder;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.DataType;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.ReadableByteBuf;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.util.MutableInt;
import fr.xephi.authme.libs.org.mariadb.jdbc.message.server.ColumnDefinitionPacket;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.SQLDataException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;

public class UnsignedBigIntColumn extends ColumnDefinitionPacket implements ColumnDecoder {
   public UnsignedBigIntColumn(ReadableByteBuf buf, int charset, long length, DataType dataType, byte decimals, int flags, int[] stringPos, String extTypeName, String extTypeFormat) {
      super(buf, charset, length, dataType, decimals, flags, stringPos, extTypeName, extTypeFormat, false);
   }

   protected UnsignedBigIntColumn(UnsignedBigIntColumn prev) {
      super(prev, true);
   }

   public UnsignedBigIntColumn useAliasAsName() {
      return new UnsignedBigIntColumn(this);
   }

   public String defaultClassname(Configuration conf) {
      return BigInteger.class.getName();
   }

   public int getColumnType(Configuration conf) {
      return -5;
   }

   public String getColumnTypeName(Configuration conf) {
      return "BIGINT UNSIGNED";
   }

   public Object getDefaultText(Configuration conf, ReadableByteBuf buf, MutableInt length) throws SQLDataException {
      return new BigInteger(buf.readAscii(length.get()));
   }

   public Object getDefaultBinary(Configuration conf, ReadableByteBuf buf, MutableInt length) throws SQLDataException {
      byte[] bb = new byte[8];

      for(int i = 7; i >= 0; --i) {
         bb[i] = buf.readByte();
      }

      return new BigInteger(1, bb);
   }

   public boolean decodeBooleanText(ReadableByteBuf buf, MutableInt length) throws SQLDataException {
      String s = buf.readAscii(length.get());
      return !"0".equals(s);
   }

   public boolean decodeBooleanBinary(ReadableByteBuf buf, MutableInt length) throws SQLDataException {
      return buf.readLong() != 0L;
   }

   public byte decodeByteText(ReadableByteBuf buf, MutableInt length) throws SQLDataException {
      long result = buf.atoull(length.get());
      if ((long)((byte)((int)result)) == result && result >= 0L) {
         return (byte)((int)result);
      } else {
         throw new SQLDataException("byte overflow");
      }
   }

   public byte decodeByteBinary(ReadableByteBuf buf, MutableInt length) throws SQLDataException {
      byte[] bb = new byte[8];

      for(int i = 7; i >= 0; --i) {
         bb[i] = buf.readByte();
      }

      BigInteger val = new BigInteger(1, bb);

      try {
         return val.byteValueExact();
      } catch (ArithmeticException | NumberFormatException var6) {
         throw new SQLDataException(String.format("value '%s' (%s) cannot be decoded as Byte", val, this.dataType));
      }
   }

   public String decodeStringText(ReadableByteBuf buf, MutableInt length, Calendar cal) throws SQLDataException {
      return buf.readString(length.get());
   }

   public String decodeStringBinary(ReadableByteBuf buf, MutableInt length, Calendar cal) throws SQLDataException {
      byte[] bb = new byte[8];

      for(int i = 7; i >= 0; --i) {
         bb[i] = buf.readByte();
      }

      return (new BigInteger(1, bb)).toString();
   }

   public short decodeShortText(ReadableByteBuf buf, MutableInt length) throws SQLDataException {
      long result = buf.atoull(length.get());
      if ((long)((short)((int)result)) == result && result >= 0L) {
         return (short)((int)result);
      } else {
         throw new SQLDataException("Short overflow");
      }
   }

   public short decodeShortBinary(ReadableByteBuf buf, MutableInt length) throws SQLDataException {
      long result = buf.readLong();
      if ((long)((short)((int)result)) == result && result >= 0L) {
         return (short)((int)result);
      } else {
         throw new SQLDataException("Short overflow");
      }
   }

   public int decodeIntText(ReadableByteBuf buf, MutableInt length) throws SQLDataException {
      long result = buf.atoull(length.get());
      int res = (int)result;
      if ((long)res == result && result >= 0L) {
         return res;
      } else {
         throw new SQLDataException("integer overflow");
      }
   }

   public int decodeIntBinary(ReadableByteBuf buf, MutableInt length) throws SQLDataException {
      byte[] bb = new byte[8];

      for(int i = 7; i >= 0; --i) {
         bb[i] = buf.readByte();
      }

      BigInteger val = new BigInteger(1, bb);

      try {
         return val.intValueExact();
      } catch (ArithmeticException var6) {
         throw new SQLDataException(String.format("value '%s' cannot be decoded as Integer", val));
      }
   }

   public long decodeLongText(ReadableByteBuf buf, MutableInt length) throws SQLDataException {
      if (length.get() < 10) {
         return buf.atoull(length.get());
      } else {
         BigInteger val = new BigInteger(buf.readAscii(length.get()));

         try {
            return val.longValueExact();
         } catch (ArithmeticException var5) {
            throw new SQLDataException(String.format("value '%s' cannot be decoded as Long", val));
         }
      }
   }

   public long decodeLongBinary(ReadableByteBuf buf, MutableInt length) throws SQLDataException {
      if ((buf.getByte(buf.pos() + 7) & 128) == 0) {
         return buf.readLong();
      } else {
         byte[] bb = new byte[8];

         for(int i = 7; i >= 0; --i) {
            bb[i] = buf.readByte();
         }

         BigInteger val = new BigInteger(1, bb);

         try {
            return val.longValueExact();
         } catch (ArithmeticException var6) {
            throw new SQLDataException(String.format("value '%s' cannot be decoded as Long", val));
         }
      }
   }

   public float decodeFloatText(ReadableByteBuf buf, MutableInt length) throws SQLDataException {
      return Float.parseFloat(buf.readAscii(length.get()));
   }

   public float decodeFloatBinary(ReadableByteBuf buf, MutableInt length) throws SQLDataException {
      byte[] bb = new byte[8];

      for(int i = 7; i >= 0; --i) {
         bb[i] = buf.readByte();
      }

      return (new BigInteger(1, bb)).floatValue();
   }

   public double decodeDoubleText(ReadableByteBuf buf, MutableInt length) throws SQLDataException {
      return Double.parseDouble(buf.readAscii(length.get()));
   }

   public double decodeDoubleBinary(ReadableByteBuf buf, MutableInt length) throws SQLDataException {
      byte[] bb = new byte[8];

      for(int i = 7; i >= 0; --i) {
         bb[i] = buf.readByte();
      }

      return (new BigInteger(1, bb)).doubleValue();
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
