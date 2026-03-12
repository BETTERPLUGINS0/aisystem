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

public class SignedBigIntColumn extends ColumnDefinitionPacket implements ColumnDecoder {
   public SignedBigIntColumn(ReadableByteBuf buf, int charset, long length, DataType dataType, byte decimals, int flags, int[] stringPos, String extTypeName, String extTypeFormat) {
      super(buf, charset, length, dataType, decimals, flags, stringPos, extTypeName, extTypeFormat, false);
   }

   protected SignedBigIntColumn(SignedBigIntColumn prev) {
      super(prev, true);
   }

   public SignedBigIntColumn useAliasAsName() {
      return new SignedBigIntColumn(this);
   }

   public String defaultClassname(Configuration conf) {
      return Long.class.getName();
   }

   public int getColumnType(Configuration conf) {
      return -5;
   }

   public String getColumnTypeName(Configuration conf) {
      return "BIGINT";
   }

   public Object getDefaultText(Configuration conf, ReadableByteBuf buf, MutableInt length) throws SQLDataException {
      return buf.atoll(length.get());
   }

   public Object getDefaultBinary(Configuration conf, ReadableByteBuf buf, MutableInt length) throws SQLDataException {
      return buf.readLong();
   }

   public boolean decodeBooleanText(ReadableByteBuf buf, MutableInt length) throws SQLDataException {
      String s = buf.readAscii(length.get());
      return !"0".equals(s);
   }

   public boolean decodeBooleanBinary(ReadableByteBuf buf, MutableInt length) throws SQLDataException {
      return buf.readLong() != 0L;
   }

   public byte decodeByteText(ReadableByteBuf buf, MutableInt length) throws SQLDataException {
      long result = buf.atoll(length.get());
      if ((long)((byte)((int)result)) != result) {
         throw new SQLDataException("byte overflow");
      } else {
         return (byte)((int)result);
      }
   }

   public byte decodeByteBinary(ReadableByteBuf buf, MutableInt length) throws SQLDataException {
      long result = buf.readLong();
      if ((long)((byte)((int)result)) != result) {
         throw new SQLDataException("byte overflow");
      } else {
         return (byte)((int)result);
      }
   }

   public String decodeStringText(ReadableByteBuf buf, MutableInt length, Calendar cal) throws SQLDataException {
      return buf.readString(length.get());
   }

   public String decodeStringBinary(ReadableByteBuf buf, MutableInt length, Calendar cal) throws SQLDataException {
      return BigInteger.valueOf(buf.readLong()).toString();
   }

   public short decodeShortText(ReadableByteBuf buf, MutableInt length) throws SQLDataException {
      long result = buf.atoll(length.get());
      if ((long)((short)((int)result)) != result) {
         throw new SQLDataException("Short overflow");
      } else {
         return (short)((int)result);
      }
   }

   public short decodeShortBinary(ReadableByteBuf buf, MutableInt length) throws SQLDataException {
      long result = buf.readLong();
      if ((long)((short)((int)result)) != result) {
         throw new SQLDataException("Short overflow");
      } else {
         return (short)((int)result);
      }
   }

   public int decodeIntText(ReadableByteBuf buf, MutableInt length) throws SQLDataException {
      long result = buf.atoll(length.get());
      int res = (int)result;
      if ((long)res != result) {
         throw new SQLDataException("integer overflow");
      } else {
         return res;
      }
   }

   public int decodeIntBinary(ReadableByteBuf buf, MutableInt length) throws SQLDataException {
      long result = buf.readLong();
      int res = (int)result;
      if ((long)res != result) {
         throw new SQLDataException("integer overflow");
      } else {
         return res;
      }
   }

   public long decodeLongText(ReadableByteBuf buf, MutableInt length) throws SQLDataException {
      return buf.atoll(length.get());
   }

   public long decodeLongBinary(ReadableByteBuf buf, MutableInt length) throws SQLDataException {
      return buf.readLong();
   }

   public float decodeFloatText(ReadableByteBuf buf, MutableInt length) throws SQLDataException {
      return Float.parseFloat(buf.readAscii(length.get()));
   }

   public float decodeFloatBinary(ReadableByteBuf buf, MutableInt length) throws SQLDataException {
      return (float)buf.readLong();
   }

   public double decodeDoubleText(ReadableByteBuf buf, MutableInt length) throws SQLDataException {
      return Double.parseDouble(buf.readAscii(length.get()));
   }

   public double decodeDoubleBinary(ReadableByteBuf buf, MutableInt length) throws SQLDataException {
      return (double)buf.readLong();
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
