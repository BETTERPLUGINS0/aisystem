package fr.xephi.authme.libs.org.mariadb.jdbc.plugin.codec;

import fr.xephi.authme.libs.org.mariadb.jdbc.client.ColumnDecoder;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.Context;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.DataType;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.ReadableByteBuf;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.socket.Writer;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.util.MutableInt;
import fr.xephi.authme.libs.org.mariadb.jdbc.plugin.Codec;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLDataException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.EnumSet;

public class DateCodec implements Codec<Date> {
   public static final DateCodec INSTANCE = new DateCodec();
   private static final EnumSet<DataType> COMPATIBLE_TYPES;

   public String className() {
      return Date.class.getName();
   }

   public boolean canDecode(ColumnDecoder column, Class<?> type) {
      return COMPATIBLE_TYPES.contains(column.getType()) && type.isAssignableFrom(Date.class);
   }

   public boolean canEncode(Object value) {
      return value instanceof Date || java.util.Date.class.equals(value.getClass());
   }

   public Date decodeText(ReadableByteBuf buf, MutableInt length, ColumnDecoder column, Calendar cal) throws SQLDataException {
      return column.decodeDateText(buf, length, cal);
   }

   public Date decodeBinary(ReadableByteBuf buf, MutableInt length, ColumnDecoder column, Calendar cal) throws SQLDataException {
      return column.decodeDateBinary(buf, length, cal);
   }

   public void encodeText(Writer encoder, Context context, Object val, Calendar providedCal, Long maxLen) throws IOException {
      Calendar cal = providedCal == null ? Calendar.getInstance() : providedCal;
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
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
         cal.setTimeInMillis(((java.util.Date)value).getTime());
         encoder.writeByte(4);
         encoder.writeShort((short)cal.get(1));
         encoder.writeByte(cal.get(2) + 1 & 255);
         encoder.writeByte(cal.get(5) & 255);
      } else {
         synchronized(providedCal) {
            providedCal.clear();
            providedCal.setTimeInMillis(((java.util.Date)value).getTime());
            encoder.writeByte(4);
            encoder.writeShort((short)providedCal.get(1));
            encoder.writeByte(providedCal.get(2) + 1 & 255);
            encoder.writeByte(providedCal.get(5) & 255);
         }
      }

   }

   public int getBinaryEncodeType() {
      return DataType.DATE.get();
   }

   static {
      COMPATIBLE_TYPES = EnumSet.of(DataType.DATE, DataType.NEWDATE, DataType.DATETIME, DataType.TIMESTAMP, DataType.YEAR, DataType.VARSTRING, DataType.VARCHAR, DataType.STRING, DataType.BLOB, DataType.TINYBLOB, DataType.MEDIUMBLOB, DataType.LONGBLOB);
   }
}
