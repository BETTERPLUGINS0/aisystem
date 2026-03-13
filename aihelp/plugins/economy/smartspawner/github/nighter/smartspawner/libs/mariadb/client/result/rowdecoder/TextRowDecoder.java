package github.nighter.smartspawner.libs.mariadb.client.result.rowdecoder;

import github.nighter.smartspawner.libs.mariadb.client.ColumnDecoder;
import github.nighter.smartspawner.libs.mariadb.client.Context;
import github.nighter.smartspawner.libs.mariadb.client.impl.StandardReadableByteBuf;
import github.nighter.smartspawner.libs.mariadb.client.util.MutableInt;
import github.nighter.smartspawner.libs.mariadb.plugin.Codec;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;

public class TextRowDecoder implements RowDecoder {
   public <T> T decode(Codec<T> codec, Calendar cal, StandardReadableByteBuf rowBuf, MutableInt fieldLength, ColumnDecoder[] metadataList, MutableInt fieldIndex, Context context) throws SQLException {
      return codec.decodeText(rowBuf, fieldLength, metadataList[fieldIndex.get()], cal, context);
   }

   public Object defaultDecode(ColumnDecoder[] metadataList, MutableInt fieldIndex, StandardReadableByteBuf rowBuf, MutableInt fieldLength, Context context) throws SQLException {
      return metadataList[fieldIndex.get()].getDefaultText(rowBuf, fieldLength, context);
   }

   public String decodeString(ColumnDecoder[] metadataList, MutableInt fieldIndex, StandardReadableByteBuf rowBuf, MutableInt fieldLength, Context context) throws SQLException {
      return metadataList[fieldIndex.get()].decodeStringText(rowBuf, fieldLength, (Calendar)null, context);
   }

   public byte decodeByte(ColumnDecoder[] metadataList, MutableInt fieldIndex, StandardReadableByteBuf rowBuf, MutableInt fieldLength) throws SQLException {
      return metadataList[fieldIndex.get()].decodeByteText(rowBuf, fieldLength);
   }

   public boolean decodeBoolean(ColumnDecoder[] metadataList, MutableInt fieldIndex, StandardReadableByteBuf rowBuf, MutableInt fieldLength) throws SQLException {
      return metadataList[fieldIndex.get()].decodeBooleanText(rowBuf, fieldLength);
   }

   public Date decodeDate(ColumnDecoder[] metadataList, MutableInt fieldIndex, StandardReadableByteBuf rowBuf, MutableInt fieldLength, Calendar cal, Context context) throws SQLException {
      return metadataList[fieldIndex.get()].decodeDateText(rowBuf, fieldLength, cal, context);
   }

   public Time decodeTime(ColumnDecoder[] metadataList, MutableInt fieldIndex, StandardReadableByteBuf rowBuf, MutableInt fieldLength, Calendar cal, Context context) throws SQLException {
      return metadataList[fieldIndex.get()].decodeTimeText(rowBuf, fieldLength, cal, context);
   }

   public Timestamp decodeTimestamp(ColumnDecoder[] metadataList, MutableInt fieldIndex, StandardReadableByteBuf rowBuf, MutableInt fieldLength, Calendar cal, Context context) throws SQLException {
      return metadataList[fieldIndex.get()].decodeTimestampText(rowBuf, fieldLength, cal, context);
   }

   public short decodeShort(ColumnDecoder[] metadataList, MutableInt fieldIndex, StandardReadableByteBuf rowBuf, MutableInt fieldLength) throws SQLException {
      return metadataList[fieldIndex.get()].decodeShortText(rowBuf, fieldLength);
   }

   public int decodeInt(ColumnDecoder[] metadataList, MutableInt fieldIndex, StandardReadableByteBuf rowBuf, MutableInt fieldLength) throws SQLException {
      return metadataList[fieldIndex.get()].decodeIntText(rowBuf, fieldLength);
   }

   public long decodeLong(ColumnDecoder[] metadataList, MutableInt fieldIndex, StandardReadableByteBuf rowBuf, MutableInt fieldLength) throws SQLException {
      return metadataList[fieldIndex.get()].decodeLongText(rowBuf, fieldLength);
   }

   public float decodeFloat(ColumnDecoder[] metadataList, MutableInt fieldIndex, StandardReadableByteBuf rowBuf, MutableInt fieldLength) throws SQLException {
      return metadataList[fieldIndex.get()].decodeFloatText(rowBuf, fieldLength);
   }

   public double decodeDouble(ColumnDecoder[] metadataList, MutableInt fieldIndex, StandardReadableByteBuf rowBuf, MutableInt fieldLength) throws SQLException {
      return metadataList[fieldIndex.get()].decodeDoubleText(rowBuf, fieldLength);
   }

   public boolean wasNull(byte[] nullBitmap, MutableInt fieldIndex, MutableInt fieldLength) {
      return fieldLength.get() == -1;
   }

   public int setPosition(int newIndex, MutableInt fieldIndex, int maxIndex, StandardReadableByteBuf rowBuf, byte[] nullBitmap, ColumnDecoder[] metadataList) {
      if (fieldIndex.get() >= newIndex) {
         fieldIndex.set(0);
         rowBuf.pos(0);
      } else {
         fieldIndex.incrementAndGet();
      }

      while(fieldIndex.get() < newIndex) {
         rowBuf.skipLengthEncoded();
         fieldIndex.incrementAndGet();
      }

      byte len = rowBuf.buf[rowBuf.pos++];
      switch(len) {
      case -5:
         return -1;
      case -4:
         return rowBuf.readUnsignedShort();
      case -3:
         return rowBuf.readUnsignedMedium();
      case -2:
         int fieldLength = (int)rowBuf.readUnsignedInt();
         rowBuf.skip(4);
         return fieldLength;
      default:
         return len & 255;
      }
   }
}
