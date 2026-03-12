package fr.xephi.authme.libs.org.mariadb.jdbc.plugin.codec;

import fr.xephi.authme.libs.org.mariadb.jdbc.client.ColumnDecoder;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.Context;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.DataType;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.ReadableByteBuf;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.socket.Writer;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.util.MutableInt;
import fr.xephi.authme.libs.org.mariadb.jdbc.plugin.Codec;
import fr.xephi.authme.libs.org.mariadb.jdbc.type.Geometry;
import fr.xephi.authme.libs.org.mariadb.jdbc.type.GeometryCollection;
import fr.xephi.authme.libs.org.mariadb.jdbc.type.LineString;
import fr.xephi.authme.libs.org.mariadb.jdbc.type.MultiLineString;
import fr.xephi.authme.libs.org.mariadb.jdbc.type.MultiPoint;
import fr.xephi.authme.libs.org.mariadb.jdbc.type.MultiPolygon;
import fr.xephi.authme.libs.org.mariadb.jdbc.type.Point;
import fr.xephi.authme.libs.org.mariadb.jdbc.type.Polygon;
import java.io.IOException;
import java.sql.SQLDataException;
import java.util.Calendar;

public class GeometryCollectionCodec implements Codec<GeometryCollection> {
   public static final GeometryCollectionCodec INSTANCE = new GeometryCollectionCodec();

   public String className() {
      return GeometryCollection.class.getName();
   }

   public boolean canDecode(ColumnDecoder column, Class<?> type) {
      return column.getType() == DataType.GEOMETRY && type.isAssignableFrom(GeometryCollection.class);
   }

   public boolean canEncode(Object value) {
      return value instanceof GeometryCollection;
   }

   public GeometryCollection decodeText(ReadableByteBuf buf, MutableInt length, ColumnDecoder column, Calendar cal) throws SQLDataException {
      return this.decodeBinary(buf, length, column, cal);
   }

   public GeometryCollection decodeBinary(ReadableByteBuf buf, MutableInt length, ColumnDecoder column, Calendar cal) throws SQLDataException {
      if (column.getType() == DataType.GEOMETRY) {
         buf.skip(4);
         Geometry geo = Geometry.getGeometry(buf, length.get() - 4, column);
         if (geo instanceof GeometryCollection) {
            return (GeometryCollection)geo;
         } else {
            throw new SQLDataException(String.format("Geometric type %s cannot be decoded as GeometryCollection", geo.getClass().getName()));
         }
      } else {
         buf.skip(length.get());
         throw new SQLDataException(String.format("Data type %s cannot be decoded as GeometryCollection", column.getType()));
      }
   }

   public void encodeText(Writer encoder, Context context, Object value, Calendar cal, Long maxLength) throws IOException {
      encoder.writeBytes(("ST_GeomCollFromText('" + value.toString() + "')").getBytes());
   }

   public void encodeBinary(Writer encoder, Object value, Calendar cal, Long maxLength) throws IOException {
      GeometryCollection geometryCollection = (GeometryCollection)value;
      int length = 13;
      Geometry[] var7 = geometryCollection.getGeometries();
      int var8 = var7.length;

      int var9;
      Geometry geo;
      int var13;
      int var17;
      for(var9 = 0; var9 < var8; ++var9) {
         geo = var7[var9];
         if (geo instanceof Point) {
            length += 21;
         } else if (geo instanceof LineString) {
            length += 9 + ((LineString)geo).getPoints().length * 16;
         } else {
            int var12;
            LineString[] var24;
            LineString ls;
            if (geo instanceof Polygon) {
               length += 9;
               var24 = ((Polygon)geo).getLines();
               var12 = var24.length;

               for(var13 = 0; var13 < var12; ++var13) {
                  ls = var24[var13];
                  length += 4 + ls.getPoints().length * 16;
               }
            } else if (geo instanceof MultiPoint) {
               length += 9 + ((MultiPoint)geo).getPoints().length * 21;
            } else if (geo instanceof MultiLineString) {
               length += 9;
               var24 = ((MultiLineString)geo).getLines();
               var12 = var24.length;

               for(var13 = 0; var13 < var12; ++var13) {
                  ls = var24[var13];
                  length += 9 + ls.getPoints().length * 16;
               }
            } else if (geo instanceof MultiPolygon) {
               length += 9;
               Polygon[] var11 = ((MultiPolygon)geo).getPolygons();
               var12 = var11.length;

               for(var13 = 0; var13 < var12; ++var13) {
                  Polygon poly = var11[var13];
                  length += 9;
                  LineString[] var15 = poly.getLines();
                  int var16 = var15.length;

                  for(var17 = 0; var17 < var16; ++var17) {
                     LineString ls = var15[var17];
                     length += 4 + ls.getPoints().length * 16;
                  }
               }
            }
         }
      }

      encoder.writeLength((long)length);
      encoder.writeInt(0);
      encoder.writeByte(1);
      encoder.writeInt(7);
      encoder.writeInt(geometryCollection.getGeometries().length);
      var7 = geometryCollection.getGeometries();
      var8 = var7.length;

      for(var9 = 0; var9 < var8; ++var9) {
         geo = var7[var9];
         if (geo instanceof Point) {
            Point pt = (Point)geo;
            encoder.writeByte(1);
            encoder.writeInt(1);
            encoder.writeDouble(pt.getX());
            encoder.writeDouble(pt.getY());
         } else {
            Point[] var33;
            int var35;
            Point pt;
            if (geo instanceof LineString) {
               LineString ls = (LineString)geo;
               encoder.writeByte(1);
               encoder.writeInt(2);
               encoder.writeInt(ls.getPoints().length);
               var33 = ls.getPoints();
               var13 = var33.length;

               for(var35 = 0; var35 < var13; ++var35) {
                  pt = var33[var35];
                  encoder.writeDouble(pt.getX());
                  encoder.writeDouble(pt.getY());
               }
            } else {
               LineString[] var31;
               LineString ls;
               Point[] var40;
               int var41;
               Point pt;
               if (geo instanceof Polygon) {
                  Polygon poly = (Polygon)geo;
                  encoder.writeByte(1);
                  encoder.writeInt(3);
                  encoder.writeInt(poly.getLines().length);
                  var31 = poly.getLines();
                  var13 = var31.length;

                  for(var35 = 0; var35 < var13; ++var35) {
                     ls = var31[var35];
                     encoder.writeInt(ls.getPoints().length);
                     var40 = ls.getPoints();
                     var17 = var40.length;

                     for(var41 = 0; var41 < var17; ++var41) {
                        pt = var40[var41];
                        encoder.writeDouble(pt.getX());
                        encoder.writeDouble(pt.getY());
                     }
                  }
               } else if (geo instanceof MultiPoint) {
                  MultiPoint mp = (MultiPoint)geo;
                  encoder.writeByte(1);
                  encoder.writeInt(4);
                  encoder.writeInt(mp.getPoints().length);
                  var33 = mp.getPoints();
                  var13 = var33.length;

                  for(var35 = 0; var35 < var13; ++var35) {
                     pt = var33[var35];
                     encoder.writeByte(1);
                     encoder.writeInt(1);
                     encoder.writeDouble(pt.getX());
                     encoder.writeDouble(pt.getY());
                  }
               } else if (geo instanceof MultiLineString) {
                  MultiLineString mlines = (MultiLineString)geo;
                  encoder.writeByte(1);
                  encoder.writeInt(5);
                  encoder.writeInt(mlines.getLines().length);
                  var31 = mlines.getLines();
                  var13 = var31.length;

                  for(var35 = 0; var35 < var13; ++var35) {
                     ls = var31[var35];
                     encoder.writeByte(1);
                     encoder.writeInt(2);
                     encoder.writeInt(ls.getPoints().length);
                     var40 = ls.getPoints();
                     var17 = var40.length;

                     for(var41 = 0; var41 < var17; ++var41) {
                        pt = var40[var41];
                        encoder.writeDouble(pt.getX());
                        encoder.writeDouble(pt.getY());
                     }
                  }
               } else if (geo instanceof MultiPolygon) {
                  MultiPolygon multiPolygon = (MultiPolygon)geo;
                  encoder.writeByte(1);
                  encoder.writeInt(6);
                  encoder.writeInt(multiPolygon.getPolygons().length);
                  Polygon[] var29 = multiPolygon.getPolygons();
                  var13 = var29.length;

                  for(var35 = 0; var35 < var13; ++var35) {
                     Polygon poly = var29[var35];
                     encoder.writeByte(1);
                     encoder.writeInt(3);
                     encoder.writeInt(poly.getLines().length);
                     LineString[] var39 = poly.getLines();
                     var17 = var39.length;

                     for(var41 = 0; var41 < var17; ++var41) {
                        LineString ls = var39[var41];
                        encoder.writeInt(ls.getPoints().length);
                        Point[] var20 = ls.getPoints();
                        int var21 = var20.length;

                        for(int var22 = 0; var22 < var21; ++var22) {
                           Point pt = var20[var22];
                           encoder.writeDouble(pt.getX());
                           encoder.writeDouble(pt.getY());
                        }
                     }
                  }
               }
            }
         }
      }

   }

   public int getBinaryEncodeType() {
      return DataType.BLOB.get();
   }
}
