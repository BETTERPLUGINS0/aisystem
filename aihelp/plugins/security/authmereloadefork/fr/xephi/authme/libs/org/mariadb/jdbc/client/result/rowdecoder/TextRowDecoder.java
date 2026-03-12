package fr.xephi.authme.libs.org.mariadb.jdbc.client.result.rowdecoder;

import fr.xephi.authme.libs.org.mariadb.jdbc.Configuration;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.ColumnDecoder;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.impl.StandardReadableByteBuf;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.util.MutableInt;
import fr.xephi.authme.libs.org.mariadb.jdbc.plugin.Codec;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;

public class TextRowDecoder implements RowDecoder {
   public <T> T decode(Codec<T> codec, Calendar cal, StandardReadableByteBuf rowBuf, MutableInt fieldLength, ColumnDecoder[] metadataList, MutableInt fieldIndex) throws SQLException {
      return codec.decodeText(rowBuf, fieldLength, metadataList[fieldIndex.get()], cal);
   }

   public Object defaultDecode(Configuration conf, ColumnDecoder[] metadataList, MutableInt fieldIndex, StandardReadableByteBuf rowBuf, MutableInt fieldLength) throws SQLException {
      return metadataList[fieldIndex.get()].getDefaultText(conf, rowBuf, fieldLength);
   }

   public String decodeString(ColumnDecoder[] metadataList, MutableInt fieldIndex, StandardReadableByteBuf rowBuf, MutableInt fieldLength) throws SQLException {
      return metadataList[fieldIndex.get()].decodeStringText(rowBuf, fieldLength, (Calendar)null);
   }

   public byte decodeByte(ColumnDecoder[] metadataList, MutableInt fieldIndex, StandardReadableByteBuf rowBuf, MutableInt fieldLength) throws SQLException {
      return metadataList[fieldIndex.get()].decodeByteText(rowBuf, fieldLength);
   }

   public boolean decodeBoolean(ColumnDecoder[] metadataList, MutableInt fieldIndex, StandardReadableByteBuf rowBuf, MutableInt fieldLength) throws SQLException {
      return metadataList[fieldIndex.get()].decodeBooleanText(rowBuf, fieldLength);
   }

   public Date decodeDate(ColumnDecoder[] metadataList, MutableInt fieldIndex, StandardReadableByteBuf rowBuf, MutableInt fieldLength, Calendar cal) throws SQLException {
      return metadataList[fieldIndex.get()].decodeDateText(rowBuf, fieldLength, cal);
   }

   public Time decodeTime(ColumnDecoder[] metadataList, MutableInt fieldIndex, StandardReadableByteBuf rowBuf, MutableInt fieldLength, Calendar cal) throws SQLException {
      return metadataList[fieldIndex.get()].decodeTimeText(rowBuf, fieldLength, cal);
   }

   public Timestamp decodeTimestamp(ColumnDecoder[] metadataList, MutableInt fieldIndex, StandardReadableByteBuf rowBuf, MutableInt fieldLength, Calendar cal) throws SQLException {
      return metadataList[fieldIndex.get()].decodeTimestampText(rowBuf, fieldLength, cal);
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
