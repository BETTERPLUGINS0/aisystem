package fr.xephi.authme.libs.org.mariadb.jdbc.plugin.codec;

import fr.xephi.authme.libs.org.mariadb.jdbc.client.ColumnDecoder;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.Context;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.DataType;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.ReadableByteBuf;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.socket.Writer;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.util.MutableInt;
import fr.xephi.authme.libs.org.mariadb.jdbc.plugin.Codec;
import java.io.IOException;
import java.sql.SQLDataException;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.EnumSet;

public class TimeCodec implements Codec<Time> {
   public static final TimeCodec INSTANCE = new TimeCodec();
   public static final LocalDate EPOCH_DATE = LocalDate.of(1970, 1, 1);
   private static final EnumSet<DataType> COMPATIBLE_TYPES;

   public String className() {
      return Time.class.getName();
   }

   public boolean canDecode(ColumnDecoder column, Class<?> type) {
      return COMPATIBLE_TYPES.contains(column.getType()) && type.isAssignableFrom(Time.class) && !type.equals(Date.class);
   }

   public boolean canEncode(Object value) {
      return value instanceof Time;
   }

   public Time decodeText(ReadableByteBuf buf, MutableInt length, ColumnDecoder column, Calendar cal) throws SQLDataException {
      return column.decodeTimeText(buf, length, cal);
   }

   public Time decodeBinary(ReadableByteBuf buf, MutableInt length, ColumnDecoder column, Calendar calParam) throws SQLDataException {
      return column.decodeTimeBinary(buf, length, calParam);
   }

   public void encodeText(Writer encoder, Context context, Object val, Calendar providedCal, Long maxLen) throws IOException {
      Calendar cal = providedCal == null ? Calendar.getInstance() : providedCal;
      SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.SSS");
      sdf.setTimeZone(cal.getTimeZone());
      String dateString = sdf.format(val);
      encoder.writeByte(39);
      encoder.writeAscii(dateString);
      encoder.writeByte(39);
   }

   public void encodeBinary(Writer encoder, Object value, Calendar providedCal, Long maxLength) throws IOException {
      if (providedCal == null) {
         Calendar cal = Calendar.getInstance();
         cal.clear();
         cal.setTime((Time)value);
         cal.set(5, 1);
         if (cal.get(14) > 0) {
            encoder.writeByte(12);
            encoder.writeByte(0);
            encoder.writeInt(0);
            encoder.writeByte((byte)cal.get(11));
            encoder.writeByte((byte)cal.get(12));
            encoder.writeByte((byte)cal.get(13));
            encoder.writeInt(cal.get(14) * 1000);
         } else {
            encoder.writeByte(8);
            encoder.writeByte(0);
            encoder.writeInt(0);
            encoder.writeByte((byte)cal.get(11));
            encoder.writeByte((byte)cal.get(12));
            encoder.writeByte((byte)cal.get(13));
         }
      } else {
         synchronized(providedCal) {
            providedCal.clear();
            providedCal.setTime((Time)value);
            providedCal.set(5, 1);
            if (providedCal.get(14) > 0) {
               encoder.writeByte(12);
               encoder.writeByte(0);
               encoder.writeInt(0);
               encoder.writeByte((byte)providedCal.get(11));
               encoder.writeByte((byte)providedCal.get(12));
               encoder.writeByte((byte)providedCal.get(13));
               encoder.writeInt(providedCal.get(14) * 1000);
            } else {
               encoder.writeByte(8);
               encoder.writeByte(0);
               encoder.writeInt(0);
               encoder.writeByte((byte)providedCal.get(11));
               encoder.writeByte((byte)providedCal.get(12));
               encoder.writeByte((byte)providedCal.get(13));
            }
         }
      }

   }

   public int getBinaryEncodeType() {
      return DataType.TIME.get();
   }

   static {
      COMPATIBLE_TYPES = EnumSet.of(DataType.TIME, DataType.DATETIME, DataType.TIMESTAMP, DataType.VARSTRING, DataType.VARCHAR, DataType.STRING, DataType.BLOB, DataType.TINYBLOB, DataType.MEDIUMBLOB, DataType.LONGBLOB);
   }
}
