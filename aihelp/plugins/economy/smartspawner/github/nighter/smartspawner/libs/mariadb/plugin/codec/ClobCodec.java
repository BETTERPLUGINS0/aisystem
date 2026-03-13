package github.nighter.smartspawner.libs.mariadb.plugin.codec;

import github.nighter.smartspawner.libs.mariadb.MariaDbClob;
import github.nighter.smartspawner.libs.mariadb.client.ColumnDecoder;
import github.nighter.smartspawner.libs.mariadb.client.Context;
import github.nighter.smartspawner.libs.mariadb.client.DataType;
import github.nighter.smartspawner.libs.mariadb.client.ReadableByteBuf;
import github.nighter.smartspawner.libs.mariadb.client.socket.Writer;
import github.nighter.smartspawner.libs.mariadb.client.util.MutableInt;
import github.nighter.smartspawner.libs.mariadb.plugin.Codec;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.sql.Clob;
import java.sql.NClob;
import java.sql.SQLDataException;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.EnumSet;

public class ClobCodec implements Codec<Clob> {
   public static final ClobCodec INSTANCE = new ClobCodec();
   private static final EnumSet<DataType> COMPATIBLE_TYPES;

   public String className() {
      return Clob.class.getName();
   }

   public boolean canDecode(ColumnDecoder column, Class<?> type) {
      return COMPATIBLE_TYPES.contains(column.getType()) && (type.isAssignableFrom(Clob.class) || type.isAssignableFrom(NClob.class));
   }

   public boolean canEncode(Object value) {
      return value instanceof Clob;
   }

   public Clob decodeText(ReadableByteBuf buf, MutableInt length, ColumnDecoder column, Calendar cal, Context context) throws SQLDataException {
      return this.getClob(buf, length, column);
   }

   private Clob getClob(ReadableByteBuf buf, MutableInt length, ColumnDecoder column) throws SQLDataException {
      switch(column.getType()) {
      case BLOB:
      case TINYBLOB:
      case MEDIUMBLOB:
      case LONGBLOB:
         if (column.isBinary()) {
            buf.skip(length.get());
            throw new SQLDataException(String.format("Data type %s cannot be decoded as Clob", column.getType()));
         }
      case STRING:
      case VARCHAR:
      case VARSTRING:
         Clob clob = new MariaDbClob(buf.buf(), buf.pos(), length.get());
         buf.skip(length.get());
         return clob;
      default:
         buf.skip(length.get());
         throw new SQLDataException(String.format("Data type %s cannot be decoded as Clob", column.getType()));
      }
   }

   public Clob decodeBinary(ReadableByteBuf buf, MutableInt length, ColumnDecoder column, Calendar cal, Context context) throws SQLDataException {
      return this.getClob(buf, length, column);
   }

   public void encodeText(Writer encoder, Context context, Object value, Calendar cal, Long maxLength) throws IOException, SQLException {
      Reader reader = ((Clob)value).getCharacterStream();
      char[] buf = new char[4096];
      long remainingLen = maxLength == null ? Long.MAX_VALUE : maxLength;
      encoder.writeByte(39);

      int len;
      while(remainingLen > 0L && (len = reader.read(buf)) >= 0) {
         byte[] data = (new String(buf, 0, (int)Math.min((long)len, remainingLen))).getBytes(StandardCharsets.UTF_8);
         encoder.writeBytesEscaped(data, data.length, (context.getServerStatus() & 512) != 0);
         remainingLen -= (long)len;
      }

      encoder.writeByte(39);
   }

   public int getApproximateTextProtocolLength(Object value, Long length) {
      return length != null ? length.intValue() + 10 : -1;
   }

   public void encodeBinary(Writer encoder, Context context, Object value, Calendar cal, Long maxLength) throws IOException, SQLException {
      Reader reader = ((Clob)value).getCharacterStream();
      byte[] clobBytes = new byte[4096];
      int pos = 0;
      char[] buf = new char[4096];

      int len;
      for(long remainingLen = maxLength == null ? Long.MAX_VALUE : maxLength; remainingLen > 0L && (len = reader.read(buf)) > 0; remainingLen -= (long)len) {
         byte[] data = (new String(buf, 0, (int)Math.min((long)len, remainingLen))).getBytes(StandardCharsets.UTF_8);
         if (clobBytes.length - pos < data.length) {
            byte[] newBlobBytes = new byte[clobBytes.length + 65536];
            System.arraycopy(clobBytes, 0, newBlobBytes, 0, pos);
            clobBytes = newBlobBytes;
         }

         System.arraycopy(data, 0, clobBytes, pos, data.length);
         pos += data.length;
      }

      encoder.writeLength((long)pos);
      encoder.writeBytes(clobBytes, 0, pos);
   }

   public void encodeLongData(Writer encoder, Clob value, Long maxLength) throws IOException, SQLException {
      Reader reader = value.getCharacterStream();
      char[] buf = new char[4096];

      int len;
      for(long remainingLen = maxLength == null ? Long.MAX_VALUE : maxLength; remainingLen > 0L && (len = reader.read(buf)) > 0; remainingLen -= (long)len) {
         byte[] data = (new String(buf, 0, (int)Math.min((long)len, remainingLen))).getBytes(StandardCharsets.UTF_8);
         encoder.writeBytes(data, 0, data.length);
      }

   }

   public byte[] encodeData(Clob value, Long maxLength) throws IOException, SQLException {
      ByteArrayOutputStream bb = new ByteArrayOutputStream();
      Reader reader = value.getCharacterStream();
      char[] buf = new char[4096];

      int len;
      for(long remainingLen = maxLength == null ? Long.MAX_VALUE : maxLength; remainingLen > 0L && (len = reader.read(buf)) > 0; remainingLen -= (long)len) {
         byte[] data = (new String(buf, 0, (int)Math.min((long)len, remainingLen))).getBytes(StandardCharsets.UTF_8);
         bb.write(data, 0, data.length);
      }

      return bb.toByteArray();
   }

   public boolean canEncodeLongData() {
      return true;
   }

   public int getBinaryEncodeType() {
      return DataType.VARSTRING.get();
   }

   static {
      COMPATIBLE_TYPES = EnumSet.of(DataType.VARCHAR, DataType.VARSTRING, DataType.STRING, DataType.BLOB, DataType.TINYBLOB, DataType.MEDIUMBLOB, DataType.LONGBLOB);
   }
}
