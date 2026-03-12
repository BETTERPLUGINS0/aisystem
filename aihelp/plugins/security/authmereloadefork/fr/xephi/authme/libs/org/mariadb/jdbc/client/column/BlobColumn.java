package fr.xephi.authme.libs.org.mariadb.jdbc.client.column;

import fr.xephi.authme.libs.org.mariadb.jdbc.Configuration;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.ColumnDecoder;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.DataType;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.ReadableByteBuf;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.util.MutableInt;
import fr.xephi.authme.libs.org.mariadb.jdbc.util.CharsetEncodingLength;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Blob;
import java.sql.Date;
import java.sql.SQLDataException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Locale;

public class BlobColumn extends StringColumn implements ColumnDecoder {
   public BlobColumn(ReadableByteBuf buf, int charset, long length, DataType dataType, byte decimals, int flags, int[] stringPos, String extTypeName, String extTypeFormat) {
      super(buf, charset, length, dataType, decimals, flags, stringPos, extTypeName, extTypeFormat);
   }

   protected BlobColumn(BlobColumn prev) {
      super(prev);
   }

   public BlobColumn useAliasAsName() {
      return new BlobColumn(this);
   }

   public String defaultClassname(Configuration conf) {
      return this.isBinary() ? Blob.class.getName() : String.class.getName();
   }

   public int getColumnType(Configuration conf) {
      if (this.columnLength > 0L && this.getDisplaySize() <= 16777215) {
         if (this.dataType != DataType.TINYBLOB && this.dataType != DataType.BLOB) {
            return this.isBinary() ? -4 : -1;
         } else {
            return this.isBinary() ? -3 : 12;
         }
      } else {
         return this.isBinary() ? -4 : -1;
      }
   }

   public String getColumnTypeName(Configuration conf) {
      if (this.extTypeFormat != null) {
         return this.extTypeFormat.toUpperCase(Locale.ROOT);
      } else if (this.isBinary()) {
         if (this.columnLength < 0L) {
            return "LONGBLOB";
         } else if (this.columnLength <= 255L) {
            return "TINYBLOB";
         } else if (this.columnLength <= 65535L) {
            return "BLOB";
         } else {
            return this.columnLength <= 16777215L ? "MEDIUMBLOB" : "LONGBLOB";
         }
      } else if (this.columnLength < 0L) {
         return "LONGTEXT";
      } else if (this.getDisplaySize() <= 65532) {
         return "VARCHAR";
      } else if (this.getDisplaySize() <= 65535) {
         return "TEXT";
      } else {
         return this.getDisplaySize() <= 16777215 ? "MEDIUMTEXT" : "LONGTEXT";
      }
   }

   public int getPrecision() {
      if (!this.isBinary()) {
         Integer maxWidth2 = (Integer)CharsetEncodingLength.maxCharlen.get(this.charset);
         return maxWidth2 != null ? (int)(this.columnLength / (long)maxWidth2) : (int)this.columnLength / 4;
      } else {
         return (int)this.columnLength;
      }
   }

   public Object getDefaultText(Configuration conf, ReadableByteBuf buf, MutableInt length) throws SQLDataException {
      return this.isBinary() ? buf.readBlob(length.get()) : buf.readString(length.get());
   }

   public Object getDefaultBinary(Configuration conf, ReadableByteBuf buf, MutableInt length) throws SQLDataException {
      return this.getDefaultText(conf, buf, length);
   }

   public boolean decodeBooleanText(ReadableByteBuf buf, MutableInt length) throws SQLDataException {
      if (this.isBinary()) {
         buf.skip(length.get());
         throw new SQLDataException(String.format("Data type %s cannot be decoded as Boolean", this.dataType));
      } else {
         String s = buf.readAscii(length.get());
         return !"0".equals(s);
      }
   }

   public boolean decodeBooleanBinary(ReadableByteBuf buf, MutableInt length) throws SQLDataException {
      return this.decodeBooleanText(buf, length);
   }

   public byte decodeByteText(ReadableByteBuf buf, MutableInt length) throws SQLDataException {
      if (!this.isBinary()) {
         String str2 = buf.readString(length.get());

         long result;
         try {
            result = (new BigDecimal(str2)).setScale(0, RoundingMode.DOWN).longValue();
         } catch (NumberFormatException var7) {
            throw new SQLDataException(String.format("value '%s' (%s) cannot be decoded as Byte", str2, this.dataType));
         }

         if ((long)((byte)((int)result)) != result) {
            throw new SQLDataException("byte overflow");
         } else {
            return (byte)((int)result);
         }
      } else if (length.get() > 0) {
         byte b = buf.readByte();
         buf.skip(length.get() - 1);
         return b;
      } else {
         throw new SQLDataException("empty String value cannot be decoded as Byte");
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
      if (this.isBinary()) {
         buf.skip(length.get());
         throw new SQLDataException(String.format("Data type %s cannot be decoded as Short", this.dataType));
      } else {
         return super.decodeShortText(buf, length);
      }
   }

   public short decodeShortBinary(ReadableByteBuf buf, MutableInt length) throws SQLDataException {
      if (this.isBinary()) {
         buf.skip(length.get());
         throw new SQLDataException(String.format("Data type %s cannot be decoded as Short", this.dataType));
      } else {
         return super.decodeShortBinary(buf, length);
      }
   }

   public int decodeIntText(ReadableByteBuf buf, MutableInt length) throws SQLDataException {
      if (this.isBinary()) {
         buf.skip(length.get());
         throw new SQLDataException(String.format("Data type %s cannot be decoded as Integer", this.dataType));
      } else {
         return super.decodeIntText(buf, length);
      }
   }

   public int decodeIntBinary(ReadableByteBuf buf, MutableInt length) throws SQLDataException {
      if (this.isBinary()) {
         buf.skip(length.get());
         throw new SQLDataException(String.format("Data type %s cannot be decoded as Integer", this.dataType));
      } else {
         return super.decodeIntBinary(buf, length);
      }
   }

   public long decodeLongText(ReadableByteBuf buf, MutableInt length) throws SQLDataException {
      if (this.isBinary()) {
         buf.skip(length.get());
         throw new SQLDataException(String.format("Data type %s cannot be decoded as Long", this.dataType));
      } else {
         return super.decodeLongText(buf, length);
      }
   }

   public long decodeLongBinary(ReadableByteBuf buf, MutableInt length) throws SQLDataException {
      if (this.isBinary()) {
         buf.skip(length.get());
         throw new SQLDataException(String.format("Data type %s cannot be decoded as Long", this.dataType));
      } else {
         return super.decodeLongBinary(buf, length);
      }
   }

   public float decodeFloatText(ReadableByteBuf buf, MutableInt length) throws SQLDataException {
      if (this.isBinary()) {
         buf.skip(length.get());
         throw new SQLDataException(String.format("Data type %s cannot be decoded as Float", this.dataType));
      } else {
         return super.decodeFloatText(buf, length);
      }
   }

   public float decodeFloatBinary(ReadableByteBuf buf, MutableInt length) throws SQLDataException {
      if (this.isBinary()) {
         buf.skip(length.get());
         throw new SQLDataException(String.format("Data type %s cannot be decoded as Float", this.dataType));
      } else {
         return super.decodeFloatText(buf, length);
      }
   }

   public double decodeDoubleText(ReadableByteBuf buf, MutableInt length) throws SQLDataException {
      if (this.isBinary()) {
         buf.skip(length.get());
         throw new SQLDataException(String.format("Data type %s cannot be decoded as Double", this.dataType));
      } else {
         return super.decodeDoubleText(buf, length);
      }
   }

   public double decodeDoubleBinary(ReadableByteBuf buf, MutableInt length) throws SQLDataException {
      if (this.isBinary()) {
         buf.skip(length.get());
         throw new SQLDataException(String.format("Data type %s cannot be decoded as Double", this.dataType));
      } else {
         return super.decodeDoubleBinary(buf, length);
      }
   }

   public Date decodeDateText(ReadableByteBuf buf, MutableInt length, Calendar cal) throws SQLDataException {
      if (this.isBinary()) {
         buf.skip(length.get());
         throw new SQLDataException(String.format("Data type %s cannot be decoded as Date", this.dataType));
      } else {
         return super.decodeDateText(buf, length, cal);
      }
   }

   public Date decodeDateBinary(ReadableByteBuf buf, MutableInt length, Calendar cal) throws SQLDataException {
      if (this.isBinary()) {
         buf.skip(length.get());
         throw new SQLDataException(String.format("Data type %s cannot be decoded as Date", this.dataType));
      } else {
         return super.decodeDateBinary(buf, length, cal);
      }
   }

   public Time decodeTimeText(ReadableByteBuf buf, MutableInt length, Calendar cal) throws SQLDataException {
      if (this.isBinary()) {
         buf.skip(length.get());
         throw new SQLDataException(String.format("Data type %s cannot be decoded as Time", this.dataType));
      } else {
         return super.decodeTimeText(buf, length, cal);
      }
   }

   public Time decodeTimeBinary(ReadableByteBuf buf, MutableInt length, Calendar cal) throws SQLDataException {
      if (this.isBinary()) {
         buf.skip(length.get());
         throw new SQLDataException(String.format("Data type %s cannot be decoded as Time", this.dataType));
      } else {
         return super.decodeTimeBinary(buf, length, cal);
      }
   }

   public Timestamp decodeTimestampText(ReadableByteBuf buf, MutableInt length, Calendar cal) throws SQLDataException {
      if (this.isBinary()) {
         buf.skip(length.get());
         throw new SQLDataException(String.format("Data type %s cannot be decoded as Timestamp", this.dataType));
      } else {
         return super.decodeTimestampText(buf, length, cal);
      }
   }

   public Timestamp decodeTimestampBinary(ReadableByteBuf buf, MutableInt length, Calendar cal) throws SQLDataException {
      if (this.isBinary()) {
         buf.skip(length.get());
         throw new SQLDataException(String.format("Data type %s cannot be decoded as Timestamp", this.dataType));
      } else {
         return super.decodeTimestampBinary(buf, length, cal);
      }
   }
}
