package github.nighter.smartspawner.libs.mariadb.type;

import github.nighter.smartspawner.libs.mariadb.client.Column;
import github.nighter.smartspawner.libs.mariadb.client.ReadableByteBuf;
import java.sql.SQLDataException;

public interface Geometry {
   static Point parsePoint(boolean littleEndian, ReadableByteBuf buf) {
      double x = littleEndian ? buf.readDouble() : buf.readDoubleBE();
      double y = littleEndian ? buf.readDouble() : buf.readDoubleBE();
      return new Point(x, y);
   }

   static Geometry getGeometry(ReadableByteBuf buf, int length, Column column) throws SQLDataException {
      if (length == 0) {
         return null;
      } else {
         boolean littleEndian = buf.readByte() == 1;
         int dataType = littleEndian ? buf.readInt() : buf.readIntBE();
         int i;
         int i;
         int i;
         int i;
         int i;
         switch(dataType) {
         case 1:
            return parsePoint(littleEndian, buf);
         case 2:
            int pointNumber = littleEndian ? buf.readInt() : buf.readIntBE();
            Point[] points = new Point[pointNumber];

            for(i = 0; i < pointNumber; ++i) {
               points[i] = parsePoint(littleEndian, buf);
            }

            return new LineString(points, true);
         case 3:
            i = littleEndian ? buf.readInt() : buf.readIntBE();
            LineString[] lines = new LineString[i];

            for(i = 0; i < i; ++i) {
               int pointNb = littleEndian ? buf.readInt() : buf.readIntBE();
               Point[] lsPoints = new Point[pointNb];

               for(int j = 0; j < pointNb; ++j) {
                  lsPoints[j] = parsePoint(littleEndian, buf);
               }

               lines[i] = new LineString(lsPoints, false);
            }

            return new Polygon(lines);
         case 4:
            i = littleEndian ? buf.readInt() : buf.readIntBE();
            Point[] pointArr = new Point[i];

            for(i = 0; i < i; ++i) {
               pointArr[i] = (Point)getGeometry(buf, length, column);
            }

            return new MultiPoint(pointArr);
         case 5:
            i = littleEndian ? buf.readInt() : buf.readIntBE();
            LineString[] multiLines = new LineString[i];

            for(i = 0; i < i; ++i) {
               multiLines[i] = (LineString)getGeometry(buf, length, column);
            }

            return new MultiLineString(multiLines);
         case 6:
            i = littleEndian ? buf.readInt() : buf.readIntBE();
            Polygon[] multiPolygons = new Polygon[i];

            for(i = 0; i < i; ++i) {
               multiPolygons[i] = (Polygon)getGeometry(buf, length, column);
            }

            return new MultiPolygon(multiPolygons);
         case 7:
            i = littleEndian ? buf.readInt() : buf.readIntBE();
            Geometry[] multiGeos = new Geometry[i];

            for(int i = 0; i < i; ++i) {
               multiGeos[i] = getGeometry(buf, length, column);
            }

            return new GeometryCollection(multiGeos);
         default:
            buf.skip(length - 5);
            throw new SQLDataException(String.format("Data type %s cannot be decoded as Geometry", column.getType()));
         }
      }
   }
}
