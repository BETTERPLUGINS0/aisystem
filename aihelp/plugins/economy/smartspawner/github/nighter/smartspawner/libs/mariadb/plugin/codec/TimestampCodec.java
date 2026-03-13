package github.nighter.smartspawner.libs.mariadb.plugin.codec;

import github.nighter.smartspawner.libs.mariadb.client.ColumnDecoder;
import github.nighter.smartspawner.libs.mariadb.client.Context;
import github.nighter.smartspawner.libs.mariadb.client.DataType;
import github.nighter.smartspawner.libs.mariadb.client.ReadableByteBuf;
import github.nighter.smartspawner.libs.mariadb.client.socket.Writer;
import github.nighter.smartspawner.libs.mariadb.client.util.MutableInt;
import github.nighter.smartspawner.libs.mariadb.plugin.Codec;
import java.io.IOException;
import java.sql.SQLDataException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.EnumSet;

public class TimestampCodec implements Codec<Timestamp> {
   public static final TimestampCodec INSTANCE = new TimestampCodec();
   private static final EnumSet<DataType> COMPATIBLE_TYPES;

   public String className() {
      return Timestamp.class.getName();
   }

   public boolean canDecode(ColumnDecoder column, Class<?> type) {
      return COMPATIBLE_TYPES.contains(column.getType()) && type.isAssignableFrom(Timestamp.class);
   }

   public boolean canEncode(Object value) {
      return value instanceof Timestamp || Date.class.equals(value.getClass());
   }

   public Timestamp decodeText(ReadableByteBuf buf, MutableInt length, ColumnDecoder column, Calendar cal, Context context) throws SQLDataException {
      return column.decodeTimestampText(buf, length, cal, context);
   }

   public Timestamp decodeBinary(ReadableByteBuf buf, MutableInt length, ColumnDecoder column, Calendar cal, Context context) throws SQLDataException {
      return column.decodeTimestampBinary(buf, length, cal, context);
   }

   public void encodeText(Writer encoder, Context context, Object val, Calendar providedCal, Long maxLen) throws IOException {
      Calendar cal = providedCal == null ? context.getDefaultCalendar() : providedCal;
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      sdf.setTimeZone(cal.getTimeZone());
      String dateString = sdf.format(val);
      encoder.writeByte(39);
      encoder.writeAscii(dateString);
      int microseconds = 0;
      if (val instanceof Timestamp) {
         microseconds = ((Timestamp)val).getNanos() / 1000;
      } else if (val instanceof Date) {
         microseconds = (int)(((Date)val).getTime() % 1000L * 1000L);
      }

      if (microseconds > 0) {
         if (microseconds % 1000 == 0) {
            encoder.writeAscii("." + Integer.toString(microseconds / 1000 + 1000).substring(1));
         } else {
            encoder.writeAscii("." + Integer.toString(microseconds + 1000000).substring(1));
         }
      }

      encoder.writeByte(39);
   }

   public int getApproximateTextProtocolLength(Object value, Long length) {
      return ((Timestamp)value).getNanos() > 0 ? 28 : 21;
   }

   public void encodeBinary(Writer encoder, Context context, Object value, Calendar providedCal, Long maxLength) throws IOException {
      int microseconds = 0;
      long timeInMillis = 0L;
      if (value instanceof Timestamp) {
         Timestamp ts = (Timestamp)value;
         microseconds = ts.getNanos() / 1000;
         timeInMillis = ts.getTime();
      } else if (value instanceof Date) {
         Date dt = (Date)value;
         timeInMillis = dt.getTime();
         microseconds = (int)(timeInMillis % 1000L * 1000L);
      }

      if (providedCal == null) {
         Calendar cal = context.getDefaultCalendar();
         cal.clear();
         cal.setTimeInMillis(timeInMillis);
         if (microseconds == 0) {
            encoder.writeByte(7);
            encoder.writeShort((short)cal.get(1));
            encoder.writeByte(cal.get(2) + 1);
            encoder.writeByte(cal.get(5));
            encoder.writeByte(cal.get(11));
            encoder.writeByte(cal.get(12));
            encoder.writeByte(cal.get(13));
         } else {
            encoder.writeByte(11);
            encoder.writeShort((short)cal.get(1));
            encoder.writeByte(cal.get(2) + 1);
            encoder.writeByte(cal.get(5));
            encoder.writeByte(cal.get(11));
            encoder.writeByte(cal.get(12));
            encoder.writeByte(cal.get(13));
            encoder.writeInt(microseconds);
         }
      } else {
         synchronized(providedCal) {
            providedCal.clear();
            providedCal.setTimeInMillis(timeInMillis);
            if (microseconds == 0) {
               encoder.writeByte(7);
               encoder.writeShort((short)providedCal.get(1));
               encoder.writeByte(providedCal.get(2) + 1);
               encoder.writeByte(providedCal.get(5));
               encoder.writeByte(providedCal.get(11));
               encoder.writeByte(providedCal.get(12));
               encoder.writeByte(providedCal.get(13));
            } else {
               encoder.writeByte(11);
               encoder.writeShort((short)providedCal.get(1));
               encoder.writeByte(providedCal.get(2) + 1);
               encoder.writeByte(providedCal.get(5));
               encoder.writeByte(providedCal.get(11));
               encoder.writeByte(providedCal.get(12));
               encoder.writeByte(providedCal.get(13));
               encoder.writeInt(microseconds);
            }
         }
      }

   }

   public int getBinaryEncodeType() {
      return DataType.DATETIME.get();
   }

   static {
      COMPATIBLE_TYPES = EnumSet.of(DataType.DATE, DataType.NEWDATE, DataType.DATETIME, DataType.TIMESTAMP, DataType.YEAR, DataType.VARSTRING, DataType.VARCHAR, DataType.STRING, DataType.TIME, DataType.BLOB, DataType.TINYBLOB, DataType.MEDIUMBLOB, DataType.LONGBLOB);
   }
}
