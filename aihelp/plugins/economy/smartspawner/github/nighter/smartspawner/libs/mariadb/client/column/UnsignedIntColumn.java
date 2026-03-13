package github.nighter.smartspawner.libs.mariadb.client.column;

import github.nighter.smartspawner.libs.mariadb.Configuration;
import github.nighter.smartspawner.libs.mariadb.client.ColumnDecoder;
import github.nighter.smartspawner.libs.mariadb.client.Context;
import github.nighter.smartspawner.libs.mariadb.client.DataType;
import github.nighter.smartspawner.libs.mariadb.client.ReadableByteBuf;
import github.nighter.smartspawner.libs.mariadb.client.util.MutableInt;
import github.nighter.smartspawner.libs.mariadb.message.server.ColumnDefinitionPacket;
import java.sql.Date;
import java.sql.SQLDataException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;

public class UnsignedIntColumn extends ColumnDefinitionPacket implements ColumnDecoder {
   public UnsignedIntColumn(ReadableByteBuf buf, int charset, long length, DataType dataType, byte decimals, int flags, int[] stringPos, String extTypeName, String extTypeFormat) {
      super(buf, charset, length, dataType, decimals, flags, stringPos, extTypeName, extTypeFormat, false);
   }

   protected UnsignedIntColumn(UnsignedIntColumn prev) {
      super(prev, true);
   }

   public UnsignedIntColumn useAliasAsName() {
      return new UnsignedIntColumn(this);
   }

   public String defaultClassname(Configuration conf) {
      return Long.class.getName();
   }

   public int getColumnType(Configuration conf) {
      boolean resultSetMetaDataUnsignedCompatibility = Boolean.parseBoolean(conf.nonMappedOptions().getProperty("resultSetMetaDataUnsignedCompatibility", "false"));
      return resultSetMetaDataUnsignedCompatibility ? -5 : 4;
   }

   public String getColumnTypeName(Configuration conf) {
      return "INTEGER UNSIGNED";
   }

   public Object getDefaultText(ReadableByteBuf buf, MutableInt length, Context context) throws SQLDataException {
      return buf.atoull(length.get());
   }

   public Object getDefaultBinary(ReadableByteBuf buf, MutableInt length, Context context) throws SQLDataException {
      return buf.readUnsignedInt();
   }

   public boolean decodeBooleanText(ReadableByteBuf buf, MutableInt length) throws SQLDataException {
      String s = buf.readAscii(length.get());
      return !"0".equals(s);
   }

   public boolean decodeBooleanBinary(ReadableByteBuf buf, MutableInt length) throws SQLDataException {
      return buf.readInt() != 0;
   }

   public byte decodeByteText(ReadableByteBuf buf, MutableInt length) throws SQLDataException {
      long result = buf.atoull(length.get());
      if ((long)((byte)((int)result)) == result && (result >= 0L || this.isSigned())) {
         return (byte)((int)result);
      } else {
         throw new SQLDataException("byte overflow");
      }
   }

   public byte decodeByteBinary(ReadableByteBuf buf, MutableInt length) throws SQLDataException {
      long result = buf.readUnsignedInt();
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
      return String.valueOf(buf.readUnsignedInt());
   }

   public short decodeShortText(ReadableByteBuf buf, MutableInt length) throws SQLDataException {
      long result = buf.atoll(length.get());
      if ((long)((short)((int)result)) == result && result >= 0L) {
         return (short)((int)result);
      } else {
         throw new SQLDataException("Short overflow");
      }
   }

   public short decodeShortBinary(ReadableByteBuf buf, MutableInt length) throws SQLDataException {
      long result = buf.readUnsignedInt();
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
      long result = buf.readUnsignedInt();
      int res = (int)result;
      if ((long)res != result) {
         throw new SQLDataException("integer overflow");
      } else {
         return res;
      }
   }

   public long decodeLongText(ReadableByteBuf buf, MutableInt length) throws SQLDataException {
      return buf.atoull(length.get());
   }

   public long decodeLongBinary(ReadableByteBuf buf, MutableInt length) throws SQLDataException {
      return buf.readUnsignedInt();
   }

   public float decodeFloatText(ReadableByteBuf buf, MutableInt length) throws SQLDataException {
      return Float.parseFloat(buf.readAscii(length.get()));
   }

   public float decodeFloatBinary(ReadableByteBuf buf, MutableInt length) throws SQLDataException {
      return (float)buf.readUnsignedInt();
   }

   public double decodeDoubleText(ReadableByteBuf buf, MutableInt length) throws SQLDataException {
      return Double.parseDouble(buf.readAscii(length.get()));
   }

   public double decodeDoubleBinary(ReadableByteBuf buf, MutableInt length) throws SQLDataException {
      return (double)buf.readUnsignedInt();
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
