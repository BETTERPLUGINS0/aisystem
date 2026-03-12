package fr.xephi.authme.libs.org.mariadb.jdbc.plugin.codec;

import fr.xephi.authme.libs.org.mariadb.jdbc.client.ColumnDecoder;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.Context;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.DataType;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.ReadableByteBuf;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.socket.Writer;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.util.MutableInt;
import fr.xephi.authme.libs.org.mariadb.jdbc.plugin.Codec;
import java.io.IOException;
import java.util.BitSet;
import java.util.Calendar;

public class BitSetCodec implements Codec<BitSet> {
   public static final BitSetCodec INSTANCE = new BitSetCodec();

   public static BitSet parseBit(ReadableByteBuf buf, MutableInt length) {
      byte[] arr = new byte[length.get()];
      buf.readBytes(arr);
      revertOrder(arr);
      return BitSet.valueOf(arr);
   }

   public static void revertOrder(byte[] array) {
      int i = 0;

      for(int j = array.length - 1; j > i; ++i) {
         byte tmp = array[j];
         array[j] = array[i];
         array[i] = tmp;
         --j;
      }

   }

   public String className() {
      return BitSet.class.getName();
   }

   public boolean canDecode(ColumnDecoder column, Class<?> type) {
      return column.getType() == DataType.BIT && type.isAssignableFrom(BitSet.class);
   }

   public BitSet decodeText(ReadableByteBuf buf, MutableInt length, ColumnDecoder column, Calendar cal) {
      return parseBit(buf, length);
   }

   public BitSet decodeBinary(ReadableByteBuf buf, MutableInt length, ColumnDecoder column, Calendar cal) {
      return parseBit(buf, length);
   }

   public boolean canEncode(Object value) {
      return value instanceof BitSet;
   }

   public void encodeText(Writer encoder, Context context, Object value, Calendar cal, Long length) throws IOException {
      byte[] bytes = ((BitSet)value).toByteArray();
      revertOrder(bytes);
      StringBuilder sb = new StringBuilder(bytes.length * 8 + 3);
      sb.append("b'");

      for(int i = 0; i < 8 * bytes.length; ++i) {
         sb.append((char)((bytes[i / 8] << i % 8 & 128) == 0 ? '0' : '1'));
      }

      sb.append("'");
      encoder.writeAscii(sb.toString());
   }

   public void encodeBinary(Writer encoder, Object value, Calendar cal, Long maxLength) throws IOException {
      byte[] bytes = ((BitSet)value).toByteArray();
      revertOrder(bytes);
      encoder.writeLength((long)bytes.length);
      encoder.writeBytes(bytes);
   }

   public int getBinaryEncodeType() {
      return DataType.BLOB.get();
   }
}
