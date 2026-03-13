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
import java.time.LocalDate;
import java.util.Calendar;
import java.util.TimeZone;

public class DateColumn extends ColumnDefinitionPacket implements ColumnDecoder {
   public DateColumn(ReadableByteBuf buf, int charset, long length, DataType dataType, byte decimals, int flags, int[] stringPos, String extTypeName, String extTypeFormat) {
      super(buf, charset, length, dataType, decimals, flags, stringPos, extTypeName, extTypeFormat, false);
   }

   protected DateColumn(DateColumn prev) {
      super(prev, true);
   }

   public DateColumn useAliasAsName() {
      return new DateColumn(this);
   }

   public String defaultClassname(Configuration conf) {
      return Date.class.getName();
   }

   public int getColumnType(Configuration conf) {
      return 91;
   }

   public String getColumnTypeName(Configuration conf) {
      return "DATE";
   }

   public Object getDefaultText(ReadableByteBuf buf, MutableInt length, Context context) throws SQLDataException {
      return this.decodeDateText(buf, length, (Calendar)null, context);
   }

   public Object getDefaultBinary(ReadableByteBuf buf, MutableInt length, Context context) throws SQLDataException {
      return this.decodeDateBinary(buf, length, (Calendar)null, context);
   }

   public boolean decodeBooleanText(ReadableByteBuf buf, MutableInt length) throws SQLDataException {
      buf.skip(length.get());
      throw new SQLDataException(String.format("Data type %s cannot be decoded as Boolean", this.dataType));
   }

   public boolean decodeBooleanBinary(ReadableByteBuf buf, MutableInt length) throws SQLDataException {
      buf.skip(length.get());
      throw new SQLDataException(String.format("Data type %s cannot be decoded as Boolean", this.dataType));
   }

   public byte decodeByteText(ReadableByteBuf buf, MutableInt length) throws SQLDataException {
      buf.skip(length.get());
      throw new SQLDataException(String.format("Data type %s cannot be decoded as Byte", this.dataType));
   }

   public byte decodeByteBinary(ReadableByteBuf buf, MutableInt length) throws SQLDataException {
      buf.skip(length.get());
      throw new SQLDataException(String.format("Data type %s cannot be decoded as Byte", this.dataType));
   }

   public String decodeStringText(ReadableByteBuf buf, MutableInt length, Calendar cal, Context context) throws SQLDataException {
      return buf.readString(length.get());
   }

   public String decodeStringBinary(ReadableByteBuf buf, MutableInt length, Calendar cal, Context context) throws SQLDataException {
      if (length.get() == 0) {
         return "0000-00-00";
      } else {
         int dateYear = buf.readUnsignedShort();
         int dateMonth = buf.readByte();
         int dateDay = buf.readByte();
         return LocalDate.of(dateYear, dateMonth, dateDay).toString();
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

   public Date decodeDateText(ReadableByteBuf buf, MutableInt length, Calendar cal, Context context) throws SQLDataException {
      int year = (int)buf.atoull(4);
      buf.skip(1);
      int month = (int)buf.atoull(2);
      buf.skip(1);
      int dayOfMonth = (int)buf.atoull(2);
      if (year == 0 && month == 0 && dayOfMonth == 0) {
         length.set(-1);
         return null;
      } else if (cal == null) {
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
   }

   public Date decodeDateBinary(ReadableByteBuf buf, MutableInt length, Calendar cal, Context context) throws SQLDataException {
      if (length.get() == 0) {
         length.set(-1);
         return null;
      } else if (cal == null) {
         Calendar c = Calendar.getInstance();
         c.clear();
         c.set(1, buf.readShort());
         c.set(2, buf.readByte() - 1);
         c.set(5, buf.readByte());
         return new Date(c.getTimeInMillis());
      } else {
         synchronized(cal) {
            cal.clear();
            cal.set(1, buf.readShort());
            cal.set(2, buf.readByte() - 1);
            cal.set(5, buf.readByte());
            return new Date(cal.getTimeInMillis());
         }
      }
   }

   public Time decodeTimeText(ReadableByteBuf buf, MutableInt length, Calendar cal, Context context) throws SQLDataException {
      buf.skip(length.get());
      throw new SQLDataException(String.format("Data type %s cannot be decoded as Time", this.dataType));
   }

   public Time decodeTimeBinary(ReadableByteBuf buf, MutableInt length, Calendar cal, Context context) throws SQLDataException {
      buf.skip(length.get());
      throw new SQLDataException(String.format("Data type %s cannot be decoded as Time", this.dataType));
   }

   public Timestamp decodeTimestampText(ReadableByteBuf buf, MutableInt length, Calendar calParam, Context context) throws SQLDataException {
      if (calParam != null && !calParam.getTimeZone().equals(TimeZone.getDefault())) {
         String[] datePart = buf.readAscii(length.get()).split("-");
         synchronized(calParam) {
            calParam.clear();
            calParam.set(Integer.parseInt(datePart[0]), Integer.parseInt(datePart[1]) - 1, Integer.parseInt(datePart[2]));
            return new Timestamp(calParam.getTimeInMillis());
         }
      } else {
         String s = buf.readAscii(length.get());
         if ("0000-00-00".equals(s)) {
            length.set(-1);
            return null;
         } else {
            return new Timestamp(Date.valueOf(s).getTime());
         }
      }
   }

   public Timestamp decodeTimestampBinary(ReadableByteBuf buf, MutableInt length, Calendar calParam, Context context) throws SQLDataException {
      if (length.get() == 0) {
         length.set(-1);
         return null;
      } else {
         int year = buf.readUnsignedShort();
         int month = buf.readByte();
         long dayOfMonth = (long)buf.readByte();
         if (year == 0 && month == 0 && dayOfMonth == 0L) {
            length.set(-1);
            return null;
         } else {
            Timestamp timestamp;
            if (calParam == null) {
               Calendar cal = Calendar.getInstance();
               cal.clear();
               cal.set(year, month - 1, (int)dayOfMonth, 0, 0, 0);
               timestamp = new Timestamp(cal.getTimeInMillis());
            } else {
               synchronized(calParam) {
                  calParam.clear();
                  calParam.set(year, month - 1, (int)dayOfMonth, 0, 0, 0);
                  timestamp = new Timestamp(calParam.getTimeInMillis());
               }
            }

            timestamp.setNanos(0);
            return timestamp;
         }
      }
   }
}
