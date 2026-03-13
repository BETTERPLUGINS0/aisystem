package github.nighter.smartspawner.libs.mariadb.plugin.codec;

import github.nighter.smartspawner.libs.mariadb.client.ColumnDecoder;
import github.nighter.smartspawner.libs.mariadb.client.Context;
import github.nighter.smartspawner.libs.mariadb.client.DataType;
import github.nighter.smartspawner.libs.mariadb.client.ReadableByteBuf;
import github.nighter.smartspawner.libs.mariadb.client.socket.Writer;
import github.nighter.smartspawner.libs.mariadb.client.util.MutableInt;
import github.nighter.smartspawner.libs.mariadb.plugin.Codec;
import github.nighter.smartspawner.libs.mariadb.type.Geometry;
import github.nighter.smartspawner.libs.mariadb.type.GeometryCollection;
import github.nighter.smartspawner.libs.mariadb.type.LineString;
import github.nighter.smartspawner.libs.mariadb.type.MultiLineString;
import github.nighter.smartspawner.libs.mariadb.type.MultiPoint;
import github.nighter.smartspawner.libs.mariadb.type.MultiPolygon;
import github.nighter.smartspawner.libs.mariadb.type.Point;
import github.nighter.smartspawner.libs.mariadb.type.Polygon;
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

   public GeometryCollection decodeText(ReadableByteBuf buf, MutableInt length, ColumnDecoder column, Calendar cal, Context context) throws SQLDataException {
      return this.decodeBinary(buf, length, column, cal, context);
   }

   public GeometryCollection decodeBinary(ReadableByteBuf buf, MutableInt length, ColumnDecoder column, Calendar cal, Context context) throws SQLDataException {
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

   public int getApproximateTextProtocolLength(Object value, Long length) {
      return -1;
   }

   public void encodeBinary(Writer encoder, Context context, Object value, Calendar cal, Long maxLength) throws IOException {
      GeometryCollection geometryCollection = (GeometryCollection)value;
      int length = 13;
      Geometry[] var8 = geometryCollection.getGeometries();
      int var9 = var8.length;

      int var10;
      Geometry geo;
      int var14;
      int var18;
      for(var10 = 0; var10 < var9; ++var10) {
         geo = var8[var10];
         if (geo instanceof Point) {
            length += 21;
         } else if (geo instanceof LineString) {
            length += 9 + ((LineString)geo).getPoints().length * 16;
         } else {
            int var13;
            LineString[] var25;
            LineString ls;
            if (geo instanceof Polygon) {
               length += 9;
               var25 = ((Polygon)geo).getLines();
               var13 = var25.length;

               for(var14 = 0; var14 < var13; ++var14) {
                  ls = var25[var14];
                  length += 4 + ls.getPoints().length * 16;
               }
            } else if (geo instanceof MultiPoint) {
               length += 9 + ((MultiPoint)geo).getPoints().length * 21;
            } else if (geo instanceof MultiLineString) {
               length += 9;
               var25 = ((MultiLineString)geo).getLines();
               var13 = var25.length;

               for(var14 = 0; var14 < var13; ++var14) {
                  ls = var25[var14];
                  length += 9 + ls.getPoints().length * 16;
               }
            } else if (geo instanceof MultiPolygon) {
               length += 9;
               Polygon[] var12 = ((MultiPolygon)geo).getPolygons();
               var13 = var12.length;

               for(var14 = 0; var14 < var13; ++var14) {
                  Polygon poly = var12[var14];
                  length += 9;
                  LineString[] var16 = poly.getLines();
                  int var17 = var16.length;

                  for(var18 = 0; var18 < var17; ++var18) {
                     LineString ls = var16[var18];
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
      var8 = geometryCollection.getGeometries();
      var9 = var8.length;

      for(var10 = 0; var10 < var9; ++var10) {
         geo = var8[var10];
         if (geo instanceof Point) {
            Point pt = (Point)geo;
            encoder.writeByte(1);
            encoder.writeInt(1);
            encoder.writeDouble(pt.getX());
            encoder.writeDouble(pt.getY());
         } else {
            Point[] var34;
            int var36;
            Point pt;
            if (geo instanceof LineString) {
               LineString ls = (LineString)geo;
               encoder.writeByte(1);
               encoder.writeInt(2);
               encoder.writeInt(ls.getPoints().length);
               var34 = ls.getPoints();
               var14 = var34.length;

               for(var36 = 0; var36 < var14; ++var36) {
                  pt = var34[var36];
                  encoder.writeDouble(pt.getX());
                  encoder.writeDouble(pt.getY());
               }
            } else {
               LineString[] var32;
               LineString ls;
               Point[] var41;
               int var42;
               Point pt;
               if (geo instanceof Polygon) {
                  Polygon poly = (Polygon)geo;
                  encoder.writeByte(1);
                  encoder.writeInt(3);
                  encoder.writeInt(poly.getLines().length);
                  var32 = poly.getLines();
                  var14 = var32.length;

                  for(var36 = 0; var36 < var14; ++var36) {
                     ls = var32[var36];
                     encoder.writeInt(ls.getPoints().length);
                     var41 = ls.getPoints();
                     var18 = var41.length;

                     for(var42 = 0; var42 < var18; ++var42) {
                        pt = var41[var42];
                        encoder.writeDouble(pt.getX());
                        encoder.writeDouble(pt.getY());
                     }
                  }
               } else if (geo instanceof MultiPoint) {
                  MultiPoint mp = (MultiPoint)geo;
                  encoder.writeByte(1);
                  encoder.writeInt(4);
                  encoder.writeInt(mp.getPoints().length);
                  var34 = mp.getPoints();
                  var14 = var34.length;

                  for(var36 = 0; var36 < var14; ++var36) {
                     pt = var34[var36];
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
                  var32 = mlines.getLines();
                  var14 = var32.length;

                  for(var36 = 0; var36 < var14; ++var36) {
                     ls = var32[var36];
                     encoder.writeByte(1);
                     encoder.writeInt(2);
                     encoder.writeInt(ls.getPoints().length);
                     var41 = ls.getPoints();
                     var18 = var41.length;

                     for(var42 = 0; var42 < var18; ++var42) {
                        pt = var41[var42];
                        encoder.writeDouble(pt.getX());
                        encoder.writeDouble(pt.getY());
                     }
                  }
               } else if (geo instanceof MultiPolygon) {
                  MultiPolygon multiPolygon = (MultiPolygon)geo;
                  encoder.writeByte(1);
                  encoder.writeInt(6);
                  encoder.writeInt(multiPolygon.getPolygons().length);
                  Polygon[] var30 = multiPolygon.getPolygons();
                  var14 = var30.length;

                  for(var36 = 0; var36 < var14; ++var36) {
                     Polygon poly = var30[var36];
                     encoder.writeByte(1);
                     encoder.writeInt(3);
                     encoder.writeInt(poly.getLines().length);
                     LineString[] var40 = poly.getLines();
                     var18 = var40.length;

                     for(var42 = 0; var42 < var18; ++var42) {
                        LineString ls = var40[var42];
                        encoder.writeInt(ls.getPoints().length);
                        Point[] var21 = ls.getPoints();
                        int var22 = var21.length;

                        for(int var23 = 0; var23 < var22; ++var23) {
                           Point pt = var21[var23];
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
