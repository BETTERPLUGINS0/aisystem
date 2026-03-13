package github.nighter.smartspawner.libs.mariadb.plugin.codec;

import github.nighter.smartspawner.libs.mariadb.client.ColumnDecoder;
import github.nighter.smartspawner.libs.mariadb.client.Context;
import github.nighter.smartspawner.libs.mariadb.client.DataType;
import github.nighter.smartspawner.libs.mariadb.client.ReadableByteBuf;
import github.nighter.smartspawner.libs.mariadb.client.socket.Writer;
import github.nighter.smartspawner.libs.mariadb.client.util.MutableInt;
import github.nighter.smartspawner.libs.mariadb.plugin.Codec;
import github.nighter.smartspawner.libs.mariadb.type.Geometry;
import github.nighter.smartspawner.libs.mariadb.type.LineString;
import github.nighter.smartspawner.libs.mariadb.type.MultiLineString;
import github.nighter.smartspawner.libs.mariadb.type.Point;
import java.io.IOException;
import java.sql.SQLDataException;
import java.util.Calendar;

public class MultiLinestringCodec implements Codec<MultiLineString> {
   public static final MultiLinestringCodec INSTANCE = new MultiLinestringCodec();

   public String className() {
      return MultiLineString.class.getName();
   }

   public boolean canDecode(ColumnDecoder column, Class<?> type) {
      return column.getType() == DataType.GEOMETRY && type.isAssignableFrom(MultiLineString.class);
   }

   public boolean canEncode(Object value) {
      return value instanceof MultiLineString;
   }

   public MultiLineString decodeText(ReadableByteBuf buf, MutableInt length, ColumnDecoder column, Calendar cal, Context context) throws SQLDataException {
      return this.decodeBinary(buf, length, column, cal, context);
   }

   public MultiLineString decodeBinary(ReadableByteBuf buf, MutableInt length, ColumnDecoder column, Calendar cal, Context context) throws SQLDataException {
      if (column.getType() == DataType.GEOMETRY) {
         buf.skip(4);
         Geometry geo = Geometry.getGeometry(buf, length.get() - 4, column);
         if (geo instanceof MultiLineString) {
            return (MultiLineString)geo;
         } else {
            throw new SQLDataException(String.format("Geometric type %s cannot be decoded as MultiLineString", geo.getClass().getName()));
         }
      } else {
         buf.skip(length.get());
         throw new SQLDataException(String.format("Data type %s cannot be decoded as MultiLineString", column.getType()));
      }
   }

   public void encodeText(Writer encoder, Context context, Object value, Calendar cal, Long maxLength) throws IOException {
      encoder.writeBytes(("ST_MLineFromText('" + value.toString() + "')").getBytes());
   }

   public int getApproximateTextProtocolLength(Object value, Long length) {
      return -1;
   }

   public void encodeBinary(Writer encoder, Context context, Object value, Calendar cal, Long maxLength) throws IOException {
      MultiLineString mp = (MultiLineString)value;
      int length = 13;
      LineString[] var8 = mp.getLines();
      int var9 = var8.length;

      int var10;
      LineString ls;
      for(var10 = 0; var10 < var9; ++var10) {
         ls = var8[var10];
         length += 9 + ls.getPoints().length * 16;
      }

      encoder.writeLength((long)length);
      encoder.writeInt(0);
      encoder.writeByte(1);
      encoder.writeInt(5);
      encoder.writeInt(mp.getLines().length);
      var8 = mp.getLines();
      var9 = var8.length;

      for(var10 = 0; var10 < var9; ++var10) {
         ls = var8[var10];
         encoder.writeByte(1);
         encoder.writeInt(2);
         encoder.writeInt(ls.getPoints().length);
         Point[] var12 = ls.getPoints();
         int var13 = var12.length;

         for(int var14 = 0; var14 < var13; ++var14) {
            Point pt = var12[var14];
            encoder.writeDouble(pt.getX());
            encoder.writeDouble(pt.getY());
         }
      }

   }

   public int getBinaryEncodeType() {
      return DataType.BLOB.get();
   }
}
