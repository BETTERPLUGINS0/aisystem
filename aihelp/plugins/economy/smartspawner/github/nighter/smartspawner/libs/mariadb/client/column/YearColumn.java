package github.nighter.smartspawner.libs.mariadb.client.column;

import github.nighter.smartspawner.libs.mariadb.Configuration;
import github.nighter.smartspawner.libs.mariadb.client.Context;
import github.nighter.smartspawner.libs.mariadb.client.DataType;
import github.nighter.smartspawner.libs.mariadb.client.ReadableByteBuf;
import github.nighter.smartspawner.libs.mariadb.client.util.MutableInt;
import java.sql.Date;
import java.sql.SQLDataException;
import java.sql.Timestamp;
import java.util.Calendar;

public class YearColumn extends UnsignedSmallIntColumn {
   public YearColumn(ReadableByteBuf buf, int charset, long length, DataType dataType, byte decimals, int flags, int[] stringPos, String extTypeName, String extTypeFormat) {
      super(buf, charset, length, dataType, decimals, flags, stringPos, extTypeName, extTypeFormat);
   }

   protected YearColumn(YearColumn prev) {
      super(prev);
   }

   public YearColumn useAliasAsName() {
      return new YearColumn(this);
   }

   public String defaultClassname(Configuration conf) {
      return conf.yearIsDateType() ? Date.class.getName() : Short.class.getName();
   }

   public int getColumnType(Configuration conf) {
      return conf.yearIsDateType() ? 91 : 5;
   }

   public String getColumnTypeName(Configuration conf) {
      return "YEAR";
   }

   public Object getDefaultText(ReadableByteBuf buf, MutableInt length, Context context) throws SQLDataException {
      if (context.getConf().yearIsDateType()) {
         short y = (short)((int)buf.atoull(length.get()));
         if (this.columnLength == 2L) {
            if (y <= 69) {
               y = (short)(y + 2000);
            } else {
               y = (short)(y + 1900);
            }
         }

         return Date.valueOf(y + "-01-01");
      } else {
         return this.decodeShortText(buf, length);
      }
   }

   public Object getDefaultBinary(ReadableByteBuf buf, MutableInt length, Context context) throws SQLDataException {
      if (context.getConf().yearIsDateType()) {
         int v = buf.readShort();
         if (this.columnLength == 2L) {
            if (v <= 69) {
               v += 2000;
            } else {
               v += 1900;
            }
         }

         return Date.valueOf(v + "-01-01");
      } else {
         return this.decodeShortText(buf, length);
      }
   }

   public Date decodeDateText(ReadableByteBuf buf, MutableInt length, Calendar cal, Context context) throws SQLDataException {
      short y = (short)((int)buf.atoll(length.get()));
      if (this.columnLength == 2L) {
         if (y <= 69) {
            y = (short)(y + 2000);
         } else {
            y = (short)(y + 1900);
         }
      }

      return Date.valueOf(y + "-01-01");
   }

   public Date decodeDateBinary(ReadableByteBuf buf, MutableInt length, Calendar cal, Context context) throws SQLDataException {
      int v = buf.readShort();
      if (this.columnLength == 2L) {
         if (v <= 69) {
            v += 2000;
         } else {
            v += 1900;
         }
      }

      return Date.valueOf(v + "-01-01");
   }

   public Timestamp decodeTimestampText(ReadableByteBuf buf, MutableInt length, Calendar calParam, Context context) throws SQLDataException {
      int year = Integer.parseInt(buf.readAscii(length.get()));
      if (this.columnLength <= 2L) {
         year += year >= 70 ? 1900 : 2000;
      }

      if (calParam == null) {
         Calendar cal1 = context.getDefaultCalendar();
         cal1.clear();
         cal1.set(year, 0, 1);
         return new Timestamp(cal1.getTimeInMillis());
      } else {
         synchronized(calParam) {
            calParam.clear();
            calParam.set(year, 0, 1);
            return new Timestamp(calParam.getTimeInMillis());
         }
      }
   }

   public Timestamp decodeTimestampBinary(ReadableByteBuf buf, MutableInt length, Calendar calParam, Context context) throws SQLDataException {
      int year = buf.readUnsignedShort();
      if (this.columnLength <= 2L) {
         year += year >= 70 ? 1900 : 2000;
      }

      Timestamp timestamp;
      if (calParam == null) {
         Calendar cal = context.getDefaultCalendar();
         cal.clear();
         cal.set(year, 0, 1, 0, 0, 0);
         timestamp = new Timestamp(cal.getTimeInMillis());
      } else {
         synchronized(calParam) {
            calParam.clear();
            calParam.set(year, 0, 1, 0, 0, 0);
            timestamp = new Timestamp(calParam.getTimeInMillis());
         }
      }

      timestamp.setNanos(0);
      return timestamp;
   }
}
