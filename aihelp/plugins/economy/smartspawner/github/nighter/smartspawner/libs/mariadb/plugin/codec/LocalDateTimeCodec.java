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
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Calendar;
import java.util.EnumSet;

public class LocalDateTimeCodec implements Codec<LocalDateTime> {
   public static final LocalDateTimeCodec INSTANCE = new LocalDateTimeCodec();
   public static final DateTimeFormatter TIMESTAMP_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");
   public static final DateTimeFormatter TIMESTAMP_FORMAT_NO_FRACTIONAL = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
   public static final DateTimeFormatter MARIADB_LOCAL_DATE_TIME;
   private static final EnumSet<DataType> COMPATIBLE_TYPES;

   public static int[] parseTextTimestamp(ReadableByteBuf buf, MutableInt length) {
      int pos = buf.pos();
      int nanoBegin = -1;
      int[] parts = new int[7];
      int partIdx = 0;

      for(int begin = 0; begin < length.get(); ++begin) {
         byte b = buf.readByte();
         if (isDelimiter(b)) {
            ++partIdx;
            if (b == 46) {
               nanoBegin = begin;
            }
         } else {
            if (!isDigit(b)) {
               buf.pos(pos);
               throw new IllegalArgumentException("Invalid character in timestamp");
            }

            parts[partIdx] = parts[partIdx] * 10 + (b - 48);
         }
      }

      if (nanoBegin > 0) {
         adjustNanoPrecision(parts, length.get() - nanoBegin - 1);
      }

      if (partIdx < 2) {
         buf.pos(pos);
         throw new IllegalArgumentException("Wrong timestamp format");
      } else {
         return parts;
      }
   }

   private static boolean isDelimiter(byte b) {
      return b == 45 || b == 32 || b == 58 || b == 46;
   }

   private static boolean isDigit(byte b) {
      return b >= 48 && b <= 57;
   }

   private static void adjustNanoPrecision(int[] parts, int nanoLength) {
      for(int i = 0; i < 9 - nanoLength; ++i) {
         parts[6] *= 10;
      }

   }

   public static boolean isZeroTimestamp(int[] parts) {
      int[] var1 = parts;
      int var2 = parts.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         int part = var1[var3];
         if (part != 0) {
            return false;
         }
      }

      return true;
   }

   public String className() {
      return LocalDateTime.class.getName();
   }

   public boolean canDecode(ColumnDecoder column, Class<?> type) {
      return COMPATIBLE_TYPES.contains(column.getType()) && type.isAssignableFrom(LocalDateTime.class);
   }

   public boolean canEncode(Object value) {
      return value instanceof LocalDateTime;
   }

   public LocalDateTime decodeText(ReadableByteBuf buf, MutableInt length, ColumnDecoder column, Calendar cal, Context context) throws SQLDataException {
      ZonedDateTime zdt = ZonedDateTimeCodec.INSTANCE.decodeText(buf, length, column, cal, context);
      return zdt == null ? null : zdt.toLocalDateTime();
   }

   public LocalDateTime decodeBinary(ReadableByteBuf buf, MutableInt length, ColumnDecoder column, Calendar cal, Context context) throws SQLDataException {
      ZonedDateTime zdt = ZonedDateTimeCodec.INSTANCE.decodeBinary(buf, length, column, cal, context);
      return zdt == null ? null : zdt.toLocalDateTime();
   }

   public void encodeText(Writer encoder, Context context, Object value, Calendar cal, Long maxLen) throws IOException {
      LocalDateTime val = (LocalDateTime)value;
      encoder.writeByte(39);
      encoder.writeAscii(val.format(val.getNano() != 0 ? TIMESTAMP_FORMAT : TIMESTAMP_FORMAT_NO_FRACTIONAL));
      encoder.writeByte(39);
   }

   public int getApproximateTextProtocolLength(Object value, Long length) {
      return ((LocalDateTime)value).getNano() > 0 ? 28 : 21;
   }

   public void encodeBinary(Writer encoder, Context context, Object value, Calendar cal, Long maxLength) throws IOException {
      LocalDateTime val = (LocalDateTime)value;
      int nano = val.getNano();
      if (nano > 0) {
         encoder.writeByte(11);
         encoder.writeShort((short)val.getYear());
         encoder.writeByte(val.getMonthValue());
         encoder.writeByte(val.getDayOfMonth());
         encoder.writeByte(val.getHour());
         encoder.writeByte(val.getMinute());
         encoder.writeByte(val.getSecond());
         encoder.writeInt(nano / 1000);
      } else {
         encoder.writeByte(7);
         encoder.writeShort((short)val.getYear());
         encoder.writeByte(val.getMonthValue());
         encoder.writeByte(val.getDayOfMonth());
         encoder.writeByte(val.getHour());
         encoder.writeByte(val.getMinute());
         encoder.writeByte(val.getSecond());
      }

   }

   public int getBinaryEncodeType() {
      return DataType.DATETIME.get();
   }

   static {
      COMPATIBLE_TYPES = EnumSet.of(DataType.DATETIME, DataType.TIMESTAMP, DataType.VARSTRING, DataType.VARCHAR, DataType.STRING, DataType.TIME, DataType.YEAR, DataType.DATE, DataType.BLOB, DataType.TINYBLOB, DataType.MEDIUMBLOB, DataType.LONGBLOB);
      MARIADB_LOCAL_DATE_TIME = (new DateTimeFormatterBuilder()).parseCaseInsensitive().append(DateTimeFormatter.ISO_LOCAL_DATE).appendLiteral(' ').append(DateTimeFormatter.ISO_LOCAL_TIME).toFormatter();
   }
}
