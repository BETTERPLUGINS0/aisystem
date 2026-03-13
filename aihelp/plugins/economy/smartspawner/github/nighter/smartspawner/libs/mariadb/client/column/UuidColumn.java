package github.nighter.smartspawner.libs.mariadb.client.column;

import github.nighter.smartspawner.libs.mariadb.Configuration;
import github.nighter.smartspawner.libs.mariadb.client.ColumnDecoder;
import github.nighter.smartspawner.libs.mariadb.client.Context;
import github.nighter.smartspawner.libs.mariadb.client.DataType;
import github.nighter.smartspawner.libs.mariadb.client.ReadableByteBuf;
import github.nighter.smartspawner.libs.mariadb.client.util.MutableInt;
import github.nighter.smartspawner.libs.mariadb.message.server.ColumnDefinitionPacket;
import github.nighter.smartspawner.libs.mariadb.util.CharsetEncodingLength;
import java.sql.Date;
import java.sql.SQLDataException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.UUID;

public class UuidColumn extends ColumnDefinitionPacket implements ColumnDecoder {
   public UuidColumn(ReadableByteBuf buf, int charset, long length, DataType dataType, byte decimals, int flags, int[] stringPos, String extTypeName, String extTypeFormat) {
      super(buf, charset, length, dataType, decimals, flags, stringPos, extTypeName, extTypeFormat, false);
   }

   protected UuidColumn(UuidColumn prev) {
      super(prev, true);
   }

   public UuidColumn useAliasAsName() {
      return new UuidColumn(this);
   }

   public String defaultClassname(Configuration conf) {
      return conf.uuidAsString() ? String.class.getName() : UUID.class.getName();
   }

   public int getColumnType(Configuration conf) {
      return conf.uuidAsString() ? 1 : 1111;
   }

   public String getColumnTypeName(Configuration conf) {
      return "uuid";
   }

   public int getPrecision() {
      Integer maxWidth = (Integer)CharsetEncodingLength.maxCharlen.get(this.charset);
      return maxWidth == null ? (int)this.columnLength : (int)(this.columnLength / (long)maxWidth);
   }

   public Object getDefaultText(ReadableByteBuf buf, MutableInt length, Context context) throws SQLDataException {
      return context.getConf().uuidAsString() ? buf.readString(length.get()) : UUID.fromString(buf.readAscii(length.get()));
   }

   public Object getDefaultBinary(ReadableByteBuf buf, MutableInt length, Context context) throws SQLDataException {
      return context.getConf().uuidAsString() ? buf.readString(length.get()) : UUID.fromString(buf.readAscii(length.get()));
   }

   public boolean decodeBooleanText(ReadableByteBuf buf, MutableInt length) throws SQLDataException {
      buf.skip(length.get());
      throw new SQLDataException("Data type UUID cannot be decoded as Boolean");
   }

   public boolean decodeBooleanBinary(ReadableByteBuf buf, MutableInt length) throws SQLDataException {
      buf.skip(length.get());
      throw new SQLDataException("Data type UUID cannot be decoded as Boolean");
   }

   public byte decodeByteText(ReadableByteBuf buf, MutableInt length) throws SQLDataException {
      buf.skip(length.get());
      throw new SQLDataException("Data type UUID cannot be decoded as byte");
   }

   public byte decodeByteBinary(ReadableByteBuf buf, MutableInt length) throws SQLDataException {
      buf.skip(length.get());
      throw new SQLDataException("Data type UUID cannot be decoded as byte");
   }

   public String decodeStringText(ReadableByteBuf buf, MutableInt length, Calendar cal, Context context) throws SQLDataException {
      return buf.readString(length.get());
   }

   public String decodeStringBinary(ReadableByteBuf buf, MutableInt length, Calendar cal, Context context) throws SQLDataException {
      return buf.readString(length.get());
   }

   public short decodeShortText(ReadableByteBuf buf, MutableInt length) throws SQLDataException {
      buf.skip(length.get());
      throw new SQLDataException("Data type UUID cannot be decoded as Short");
   }

   public short decodeShortBinary(ReadableByteBuf buf, MutableInt length) throws SQLDataException {
      buf.skip(length.get());
      throw new SQLDataException("Data type UUID cannot be decoded as Short");
   }

   public int decodeIntText(ReadableByteBuf buf, MutableInt length) throws SQLDataException {
      buf.skip(length.get());
      throw new SQLDataException("Data type UUID cannot be decoded as Integer");
   }

   public int decodeIntBinary(ReadableByteBuf buf, MutableInt length) throws SQLDataException {
      buf.skip(length.get());
      throw new SQLDataException("Data type UUID cannot be decoded as Integer");
   }

   public long decodeLongText(ReadableByteBuf buf, MutableInt length) throws SQLDataException {
      buf.skip(length.get());
      throw new SQLDataException("Data type UUID cannot be decoded as Long");
   }

   public long decodeLongBinary(ReadableByteBuf buf, MutableInt length) throws SQLDataException {
      buf.skip(length.get());
      throw new SQLDataException("Data type UUID cannot be decoded as Long");
   }

   public float decodeFloatText(ReadableByteBuf buf, MutableInt length) throws SQLDataException {
      buf.skip(length.get());
      throw new SQLDataException("Data type UUID cannot be decoded as Float");
   }

   public float decodeFloatBinary(ReadableByteBuf buf, MutableInt length) throws SQLDataException {
      buf.skip(length.get());
      throw new SQLDataException("Data type UUID cannot be decoded as Float");
   }

   public double decodeDoubleText(ReadableByteBuf buf, MutableInt length) throws SQLDataException {
      buf.skip(length.get());
      throw new SQLDataException("Data type UUID cannot be decoded as Double");
   }

   public double decodeDoubleBinary(ReadableByteBuf buf, MutableInt length) throws SQLDataException {
      buf.skip(length.get());
      throw new SQLDataException("Data type UUID cannot be decoded as Double");
   }

   public Date decodeDateText(ReadableByteBuf buf, MutableInt length, Calendar cal, Context context) throws SQLDataException {
      buf.skip(length.get());
      throw new SQLDataException("Data type UUID cannot be decoded as Date");
   }

   public Date decodeDateBinary(ReadableByteBuf buf, MutableInt length, Calendar cal, Context context) throws SQLDataException {
      buf.skip(length.get());
      throw new SQLDataException("Data type UUID cannot be decoded as Date");
   }

   public Time decodeTimeText(ReadableByteBuf buf, MutableInt length, Calendar cal, Context context) throws SQLDataException {
      buf.skip(length.get());
      throw new SQLDataException("Data type UUID cannot be decoded as Time");
   }

   public Time decodeTimeBinary(ReadableByteBuf buf, MutableInt length, Calendar calParam, Context context) throws SQLDataException {
      buf.skip(length.get());
      throw new SQLDataException("Data type UUID cannot be decoded as Time");
   }

   public Timestamp decodeTimestampText(ReadableByteBuf buf, MutableInt length, Calendar calParam, Context context) throws SQLDataException {
      buf.skip(length.get());
      throw new SQLDataException("Data type UUID cannot be decoded as Timestamp");
   }

   public Timestamp decodeTimestampBinary(ReadableByteBuf buf, MutableInt length, Calendar calParam, Context context) throws SQLDataException {
      buf.skip(length.get());
      throw new SQLDataException("Data type UUID cannot be decoded as Timestamp");
   }
}
