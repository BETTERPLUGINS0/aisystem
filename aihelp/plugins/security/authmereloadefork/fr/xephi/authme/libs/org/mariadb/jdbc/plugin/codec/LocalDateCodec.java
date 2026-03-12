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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.EnumSet;

public class LocalDateCodec implements Codec<LocalDate> {
   public static final LocalDateCodec INSTANCE = new LocalDateCodec();
   private static final EnumSet<DataType> COMPATIBLE_TYPES;

   public static int[] parseDate(ReadableByteBuf buf, MutableInt length) {
      int[] datePart = new int[]{0, 0, 0};
      int partIdx = 0;
      int var4 = 0;

      while(var4++ < length.get()) {
         byte b = buf.readByte();
         if (b == 45) {
            ++partIdx;
         } else {
            datePart[partIdx] = datePart[partIdx] * 10 + b - 48;
         }
      }

      if (datePart[0] == 0 && datePart[1] == 0 && datePart[2] == 0) {
         length.set(-1);
         return null;
      } else {
         return datePart;
      }
   }

   public String className() {
      return LocalDate.class.getName();
   }

   public boolean canDecode(ColumnDecoder column, Class<?> type) {
      return COMPATIBLE_TYPES.contains(column.getType()) && type.isAssignableFrom(LocalDate.class);
   }

   public boolean canEncode(Object value) {
      return value instanceof LocalDate;
   }

   public LocalDate decodeText(ReadableByteBuf buf, MutableInt length, ColumnDecoder column, Calendar cal) throws SQLDataException {
      int[] parts;
      switch(column.getType()) {
      case YEAR:
         short y = (short)((int)buf.atoull(length.get()));
         if (length.get() == 2 && column.getColumnLength() == 2L) {
            if (y <= 69) {
               y = (short)(y + 2000);
            } else {
               y = (short)(y + 1900);
            }
         }

         return LocalDate.of(y, 1, 1);
      case NEWDATE:
      case DATE:
         parts = parseDate(buf, length);
         break;
      case TIMESTAMP:
      case DATETIME:
         parts = LocalDateTimeCodec.parseTimestamp(buf.readAscii(length.get()));
         break;
      case BLOB:
      case TINYBLOB:
      case MEDIUMBLOB:
      case LONGBLOB:
         if (column.isBinary()) {
            buf.skip(length.get());
            throw new SQLDataException(String.format("Data type %s cannot be decoded as Date", column.getType()));
         }
      case VARSTRING:
      case VARCHAR:
      case STRING:
         String val = buf.readString(length.get());
         String[] stDatePart = val.split("[- ]");
         if (stDatePart.length < 3) {
            throw new SQLDataException(String.format("value '%s' (%s) cannot be decoded as Date", val, column.getType()));
         }

         try {
            int year = Integer.parseInt(stDatePart[0]);
            int month = Integer.parseInt(stDatePart[1]);
            int dayOfMonth = Integer.parseInt(stDatePart[2]);
            if (year == 0 && month == 0 && dayOfMonth == 0) {
               length.set(-1);
               return null;
            }

            return LocalDate.of(year, month, dayOfMonth);
         } catch (NumberFormatException var12) {
            throw new SQLDataException(String.format("value '%s' (%s) cannot be decoded as Date", val, column.getType()));
         }
      default:
         buf.skip(length.get());
         throw new SQLDataException(String.format("Data type %s cannot be decoded as Date", column.getType()));
      }

      if (parts == null) {
         length.set(-1);
         return null;
      } else {
         return LocalDate.of(parts[0], parts[1], parts[2]);
      }
   }

   public LocalDate decodeBinary(ReadableByteBuf buf, MutableInt length, ColumnDecoder column, Calendar cal) throws SQLDataException {
      int month = 1;
      int dayOfMonth = 1;
      int year;
      switch(column.getType()) {
      case YEAR:
      case DATE:
         if (length.get() == 0) {
            length.set(-1);
            return null;
         } else {
            year = buf.readUnsignedShort();
            if (column.getColumnLength() == 2L) {
               if (year <= 69) {
                  year += 2000;
               } else {
                  year += 1900;
               }
            }

            if (length.get() >= 4) {
               month = buf.readByte();
               dayOfMonth = buf.readByte();
            }

            if (year == 0 && month == 0 && dayOfMonth == 0) {
               length.set(-1);
               return null;
            }

            return LocalDate.of(year, month, dayOfMonth);
         }
      case NEWDATE:
      default:
         buf.skip(length.get());
         throw new SQLDataException(String.format("Data type %s cannot be decoded as Date", column.getType()));
      case TIMESTAMP:
      case DATETIME:
         if (length.get() == 0) {
            length.set(-1);
            return null;
         } else {
            year = buf.readUnsignedShort();
            month = buf.readByte();
            dayOfMonth = buf.readByte();
            if (length.get() > 4) {
               buf.skip(length.get() - 4);
            }

            if (year == 0 && month == 0 && dayOfMonth == 0) {
               length.set(-1);
               return null;
            }

            return LocalDate.of(year, month, dayOfMonth);
         }
      case BLOB:
      case TINYBLOB:
      case MEDIUMBLOB:
      case LONGBLOB:
         if (column.isBinary()) {
            buf.skip(length.get());
            throw new SQLDataException(String.format("Data type %s cannot be decoded as Date", column.getType()));
         }
      case VARSTRING:
      case VARCHAR:
      case STRING:
         String val = buf.readString(length.get());
         String[] stDatePart = val.split("[- ]");
         if (stDatePart.length < 3) {
            throw new SQLDataException(String.format("value '%s' (%s) cannot be decoded as Date", val, column.getType()));
         } else {
            try {
               year = Integer.parseInt(stDatePart[0]);
               int month = Integer.parseInt(stDatePart[1]);
               int dayOfMonth = Integer.parseInt(stDatePart[2]);
               if (year == 0 && month == 0 && dayOfMonth == 0) {
                  length.set(-1);
                  return null;
               } else {
                  return LocalDate.of(year, month, dayOfMonth);
               }
            } catch (NumberFormatException var11) {
               throw new SQLDataException(String.format("value '%s' (%s) cannot be decoded as Date", val, column.getType()));
            }
         }
      }
   }

   public void encodeText(Writer encoder, Context context, Object val, Calendar cal, Long maxLen) throws IOException {
      encoder.writeByte(39);
      encoder.writeAscii(((LocalDate)val).format(DateTimeFormatter.ISO_LOCAL_DATE));
      encoder.writeByte(39);
   }

   public void encodeBinary(Writer encoder, Object value, Calendar providedCal, Long maxLength) throws IOException {
      LocalDate val = (LocalDate)value;
      encoder.writeByte(7);
      encoder.writeShort((short)val.getYear());
      encoder.writeByte(val.getMonthValue());
      encoder.writeByte(val.getDayOfMonth());
      encoder.writeBytes(new byte[]{0, 0, 0});
   }

   public int getBinaryEncodeType() {
      return DataType.DATE.get();
   }

   static {
      COMPATIBLE_TYPES = EnumSet.of(DataType.DATE, DataType.NEWDATE, DataType.DATETIME, DataType.TIMESTAMP, DataType.YEAR, DataType.VARSTRING, DataType.VARCHAR, DataType.STRING, DataType.BLOB, DataType.TINYBLOB, DataType.MEDIUMBLOB, DataType.LONGBLOB);
   }
}
