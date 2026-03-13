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

public class BinaryRowDecoder implements RowDecoder {
   public <T> T decode(Codec<T> codec, Calendar cal, StandardReadableByteBuf rowBuf, MutableInt fieldLength, ColumnDecoder[] metadataList, MutableInt fieldIndex, Context context) throws SQLException {
      return codec.decodeBinary(rowBuf, fieldLength, metadataList[fieldIndex.get()], cal, context);
   }

   public Object defaultDecode(ColumnDecoder[] metadataList, MutableInt fieldIndex, StandardReadableByteBuf rowBuf, MutableInt fieldLength, Context context) throws SQLException {
      return metadataList[fieldIndex.get()].getDefaultBinary(rowBuf, fieldLength, context);
   }

   public String decodeString(ColumnDecoder[] metadataList, MutableInt fieldIndex, StandardReadableByteBuf rowBuf, MutableInt fieldLength, Context context) throws SQLException {
      return metadataList[fieldIndex.get()].decodeStringBinary(rowBuf, fieldLength, (Calendar)null, context);
   }

   public byte decodeByte(ColumnDecoder[] metadataList, MutableInt fieldIndex, StandardReadableByteBuf rowBuf, MutableInt fieldLength) throws SQLException {
      return metadataList[fieldIndex.get()].decodeByteBinary(rowBuf, fieldLength);
   }

   public boolean decodeBoolean(ColumnDecoder[] metadataList, MutableInt fieldIndex, StandardReadableByteBuf rowBuf, MutableInt fieldLength) throws SQLException {
      return metadataList[fieldIndex.get()].decodeBooleanBinary(rowBuf, fieldLength);
   }

   public Date decodeDate(ColumnDecoder[] metadataList, MutableInt fieldIndex, StandardReadableByteBuf rowBuf, MutableInt fieldLength, Calendar cal, Context context) throws SQLException {
      return metadataList[fieldIndex.get()].decodeDateBinary(rowBuf, fieldLength, cal, context);
   }

   public Time decodeTime(ColumnDecoder[] metadataList, MutableInt fieldIndex, StandardReadableByteBuf rowBuf, MutableInt fieldLength, Calendar cal, Context context) throws SQLException {
      return metadataList[fieldIndex.get()].decodeTimeBinary(rowBuf, fieldLength, cal, context);
   }

   public Timestamp decodeTimestamp(ColumnDecoder[] metadataList, MutableInt fieldIndex, StandardReadableByteBuf rowBuf, MutableInt fieldLength, Calendar cal, Context context) throws SQLException {
      return metadataList[fieldIndex.get()].decodeTimestampBinary(rowBuf, fieldLength, cal, context);
   }

   public short decodeShort(ColumnDecoder[] metadataList, MutableInt fieldIndex, StandardReadableByteBuf rowBuf, MutableInt fieldLength) throws SQLException {
      return metadataList[fieldIndex.get()].decodeShortBinary(rowBuf, fieldLength);
   }

   public int decodeInt(ColumnDecoder[] metadataList, MutableInt fieldIndex, StandardReadableByteBuf rowBuf, MutableInt fieldLength) throws SQLException {
      return metadataList[fieldIndex.get()].decodeIntBinary(rowBuf, fieldLength);
   }

   public long decodeLong(ColumnDecoder[] metadataList, MutableInt fieldIndex, StandardReadableByteBuf rowBuf, MutableInt fieldLength) throws SQLException {
      return metadataList[fieldIndex.get()].decodeLongBinary(rowBuf, fieldLength);
   }

   public float decodeFloat(ColumnDecoder[] metadataList, MutableInt fieldIndex, StandardReadableByteBuf rowBuf, MutableInt fieldLength) throws SQLException {
      return metadataList[fieldIndex.get()].decodeFloatBinary(rowBuf, fieldLength);
   }

   public double decodeDouble(ColumnDecoder[] metadataList, MutableInt fieldIndex, StandardReadableByteBuf rowBuf, MutableInt fieldLength) throws SQLException {
      return metadataList[fieldIndex.get()].decodeDoubleBinary(rowBuf, fieldLength);
   }

   public boolean wasNull(byte[] nullBitmap, MutableInt fieldIndex, MutableInt fieldLength) {
      return (nullBitmap[(fieldIndex.get() + 2) / 8] & 1 << (fieldIndex.get() + 2) % 8) > 0 || fieldLength.get() == -1;
   }

   public int setPosition(int newIndex, MutableInt fieldIndex, int maxIndex, StandardReadableByteBuf rowBuf, byte[] nullBitmap, ColumnDecoder[] metadataList) {
      if (fieldIndex.get() >= newIndex) {
         fieldIndex.set(0);
         rowBuf.pos(1);
         rowBuf.readBytes(nullBitmap);
      } else {
         fieldIndex.incrementAndGet();
         if (fieldIndex.get() == 0) {
            rowBuf.pos(1);
            rowBuf.readBytes(nullBitmap);
         }
      }

      for(; fieldIndex.get() < newIndex; fieldIndex.incrementAndGet()) {
         if ((nullBitmap[(fieldIndex.get() + 2) / 8] & 1 << (fieldIndex.get() + 2) % 8) == 0) {
            switch(metadataList[fieldIndex.get()].getType()) {
            case BIGINT:
            case DOUBLE:
               rowBuf.skip(8);
               break;
            case INTEGER:
            case MEDIUMINT:
            case FLOAT:
               rowBuf.skip(4);
               break;
            case SMALLINT:
            case YEAR:
               rowBuf.skip(2);
               break;
            case TINYINT:
               rowBuf.skip(1);
               break;
            default:
               rowBuf.skipLengthEncoded();
            }
         }
      }

      if ((nullBitmap[(fieldIndex.get() + 2) / 8] & 1 << (fieldIndex.get() + 2) % 8) > 0) {
         return -1;
      } else {
         switch(metadataList[fieldIndex.get()].getType()) {
         case BIGINT:
         case DOUBLE:
            return 8;
         case INTEGER:
         case MEDIUMINT:
         case FLOAT:
            return 4;
         case SMALLINT:
         case YEAR:
            return 2;
         case TINYINT:
            return 1;
         default:
            byte len = rowBuf.readByte();
            switch(len) {
            case -4:
               return rowBuf.readUnsignedShort();
            case -3:
               return rowBuf.readUnsignedMedium();
            case -2:
               return (int)rowBuf.readLong();
            default:
               return len & 255;
            }
         }
      }
   }
}
