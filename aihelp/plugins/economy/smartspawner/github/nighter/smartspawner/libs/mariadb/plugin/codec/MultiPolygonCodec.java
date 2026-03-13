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
import github.nighter.smartspawner.libs.mariadb.type.MultiPolygon;
import github.nighter.smartspawner.libs.mariadb.type.Point;
import github.nighter.smartspawner.libs.mariadb.type.Polygon;
import java.io.IOException;
import java.sql.SQLDataException;
import java.util.Calendar;

public class MultiPolygonCodec implements Codec<MultiPolygon> {
   public static final MultiPolygonCodec INSTANCE = new MultiPolygonCodec();

   public String className() {
      return MultiPolygon.class.getName();
   }

   public boolean canDecode(ColumnDecoder column, Class<?> type) {
      return column.getType() == DataType.GEOMETRY && type.isAssignableFrom(MultiPolygon.class);
   }

   public boolean canEncode(Object value) {
      return value instanceof MultiPolygon;
   }

   public MultiPolygon decodeText(ReadableByteBuf buf, MutableInt length, ColumnDecoder column, Calendar cal, Context context) throws SQLDataException {
      return this.decodeBinary(buf, length, column, cal, context);
   }

   public MultiPolygon decodeBinary(ReadableByteBuf buf, MutableInt length, ColumnDecoder column, Calendar cal, Context context) throws SQLDataException {
      if (column.getType() == DataType.GEOMETRY) {
         buf.skip(4);
         Geometry geo = Geometry.getGeometry(buf, length.get() - 4, column);
         if (geo instanceof MultiPolygon) {
            return (MultiPolygon)geo;
         } else {
            throw new SQLDataException(String.format("Geometric type %s cannot be decoded as MultiPolygon", geo.getClass().getName()));
         }
      } else {
         buf.skip(length.get());
         throw new SQLDataException(String.format("Data type %s cannot be decoded as MultiPolygon", column.getType()));
      }
   }

   public void encodeText(Writer encoder, Context context, Object value, Calendar cal, Long maxLength) throws IOException {
      encoder.writeBytes(("ST_MPolyFromText('" + value.toString() + "')").getBytes());
   }

   public int getApproximateTextProtocolLength(Object value, Long length) {
      return -1;
   }

   public void encodeBinary(Writer encoder, Context context, Object value, Calendar cal, Long maxLength) throws IOException {
      MultiPolygon mariadbMultiPolygon = (MultiPolygon)value;
      int length = 13;
      Polygon[] var8 = mariadbMultiPolygon.getPolygons();
      int var9 = var8.length;

      int var10;
      Polygon poly;
      LineString[] var12;
      int var13;
      int var14;
      LineString ls;
      for(var10 = 0; var10 < var9; ++var10) {
         poly = var8[var10];
         length += 9;
         var12 = poly.getLines();
         var13 = var12.length;

         for(var14 = 0; var14 < var13; ++var14) {
            ls = var12[var14];
            length += 4 + ls.getPoints().length * 16;
         }
      }

      encoder.writeLength((long)length);
      encoder.writeInt(0);
      encoder.writeByte(1);
      encoder.writeInt(6);
      encoder.writeInt(mariadbMultiPolygon.getPolygons().length);
      var8 = mariadbMultiPolygon.getPolygons();
      var9 = var8.length;

      for(var10 = 0; var10 < var9; ++var10) {
         poly = var8[var10];
         encoder.writeByte(1);
         encoder.writeInt(3);
         encoder.writeInt(poly.getLines().length);
         var12 = poly.getLines();
         var13 = var12.length;

         for(var14 = 0; var14 < var13; ++var14) {
            ls = var12[var14];
            encoder.writeInt(ls.getPoints().length);
            Point[] var16 = ls.getPoints();
            int var17 = var16.length;

            for(int var18 = 0; var18 < var17; ++var18) {
               Point pt = var16[var18];
               encoder.writeDouble(pt.getX());
               encoder.writeDouble(pt.getY());
            }
         }
      }

   }

   public int getBinaryEncodeType() {
      return DataType.BLOB.get();
   }
}
