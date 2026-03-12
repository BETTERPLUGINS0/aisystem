package fr.xephi.authme.libs.org.mariadb.jdbc.plugin.codec;

import fr.xephi.authme.libs.org.mariadb.jdbc.client.ColumnDecoder;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.Context;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.DataType;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.ReadableByteBuf;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.socket.Writer;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.util.MutableInt;
import fr.xephi.authme.libs.org.mariadb.jdbc.plugin.Codec;
import fr.xephi.authme.libs.org.mariadb.jdbc.type.Geometry;
import fr.xephi.authme.libs.org.mariadb.jdbc.type.LineString;
import fr.xephi.authme.libs.org.mariadb.jdbc.type.MultiPolygon;
import fr.xephi.authme.libs.org.mariadb.jdbc.type.Point;
import fr.xephi.authme.libs.org.mariadb.jdbc.type.Polygon;
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

   public MultiPolygon decodeText(ReadableByteBuf buf, MutableInt length, ColumnDecoder column, Calendar cal) throws SQLDataException {
      return this.decodeBinary(buf, length, column, cal);
   }

   public MultiPolygon decodeBinary(ReadableByteBuf buf, MutableInt length, ColumnDecoder column, Calendar cal) throws SQLDataException {
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

   public void encodeBinary(Writer encoder, Object value, Calendar cal, Long maxLength) throws IOException {
      MultiPolygon mariadbMultiPolygon = (MultiPolygon)value;
      int length = 13;
      Polygon[] var7 = mariadbMultiPolygon.getPolygons();
      int var8 = var7.length;

      int var9;
      Polygon poly;
      LineString[] var11;
      int var12;
      int var13;
      LineString ls;
      for(var9 = 0; var9 < var8; ++var9) {
         poly = var7[var9];
         length += 9;
         var11 = poly.getLines();
         var12 = var11.length;

         for(var13 = 0; var13 < var12; ++var13) {
            ls = var11[var13];
            length += 4 + ls.getPoints().length * 16;
         }
      }

      encoder.writeLength((long)length);
      encoder.writeInt(0);
      encoder.writeByte(1);
      encoder.writeInt(6);
      encoder.writeInt(mariadbMultiPolygon.getPolygons().length);
      var7 = mariadbMultiPolygon.getPolygons();
      var8 = var7.length;

      for(var9 = 0; var9 < var8; ++var9) {
         poly = var7[var9];
         encoder.writeByte(1);
         encoder.writeInt(3);
         encoder.writeInt(poly.getLines().length);
         var11 = poly.getLines();
         var12 = var11.length;

         for(var13 = 0; var13 < var12; ++var13) {
            ls = var11[var13];
            encoder.writeInt(ls.getPoints().length);
            Point[] var15 = ls.getPoints();
            int var16 = var15.length;

            for(int var17 = 0; var17 < var16; ++var17) {
               Point pt = var15[var17];
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
