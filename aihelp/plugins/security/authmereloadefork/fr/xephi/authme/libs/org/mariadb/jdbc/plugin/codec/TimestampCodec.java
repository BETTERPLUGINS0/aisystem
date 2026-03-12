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
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
      return value instanceof Timestamp;
   }

   public Timestamp decodeText(ReadableByteBuf buf, MutableInt length, ColumnDecoder column, Calendar cal) throws SQLDataException {
      return column.decodeTimestampText(buf, length, cal);
   }

   public Timestamp decodeBinary(ReadableByteBuf buf, MutableInt length, ColumnDecoder column, Calendar cal) throws SQLDataException {
      return column.decodeTimestampBinary(buf, length, cal);
   }

   public void encodeText(Writer encoder, Context context, Object val, Calendar providedCal, Long maxLen) throws IOException {
      Timestamp ts = (Timestamp)val;
      Calendar cal = providedCal == null ? Calendar.getInstance() : providedCal;
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      sdf.setTimeZone(cal.getTimeZone());
      String dateString = sdf.format(ts);
      encoder.writeByte(39);
      encoder.writeAscii(dateString);
      int microseconds = ts.getNanos() / 1000;
      if (microseconds > 0) {
         if (microseconds % 1000 == 0) {
            encoder.writeAscii("." + Integer.toString(microseconds / 1000 + 1000).substring(1));
         } else {
            encoder.writeAscii("." + Integer.toString(microseconds + 1000000).substring(1));
         }
      }

      encoder.writeByte(39);
   }

   public void encodeBinary(Writer encoder, Object value, Calendar providedCal, Long maxLength) throws IOException {
      Timestamp ts = (Timestamp)value;
      if (providedCal == null) {
         Calendar cal = Calendar.getInstance();
         cal.clear();
         cal.setTimeInMillis(ts.getTime());
         if (ts.getNanos() == 0) {
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
            encoder.writeInt(ts.getNanos() / 1000);
         }
      } else {
         synchronized(providedCal) {
            providedCal.clear();
            providedCal.setTimeInMillis(ts.getTime());
            if (ts.getNanos() == 0) {
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
               encoder.writeInt(ts.getNanos() / 1000);
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
