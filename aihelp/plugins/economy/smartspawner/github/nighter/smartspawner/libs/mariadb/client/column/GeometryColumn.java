package github.nighter.smartspawner.libs.mariadb.client.column;

import github.nighter.smartspawner.libs.mariadb.Configuration;
import github.nighter.smartspawner.libs.mariadb.client.Context;
import github.nighter.smartspawner.libs.mariadb.client.DataType;
import github.nighter.smartspawner.libs.mariadb.client.ReadableByteBuf;
import github.nighter.smartspawner.libs.mariadb.client.util.MutableInt;
import github.nighter.smartspawner.libs.mariadb.type.Geometry;
import github.nighter.smartspawner.libs.mariadb.type.GeometryCollection;
import github.nighter.smartspawner.libs.mariadb.type.LineString;
import github.nighter.smartspawner.libs.mariadb.type.MultiLineString;
import github.nighter.smartspawner.libs.mariadb.type.MultiPoint;
import github.nighter.smartspawner.libs.mariadb.type.MultiPolygon;
import github.nighter.smartspawner.libs.mariadb.type.Point;
import github.nighter.smartspawner.libs.mariadb.type.Polygon;
import java.sql.SQLDataException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Locale;

public class GeometryColumn extends BlobColumn {
   public GeometryColumn(ReadableByteBuf buf, int charset, long length, DataType dataType, byte decimals, int flags, int[] stringPos, String extTypeName, String extTypeFormat) {
      super(buf, charset, length, dataType, decimals, flags, stringPos, extTypeName, extTypeFormat);
   }

   protected GeometryColumn(GeometryColumn prev) {
      super(prev);
   }

   public GeometryColumn useAliasAsName() {
      return new GeometryColumn(this);
   }

   public String defaultClassname(Configuration conf) {
      if (conf.geometryDefaultType() != null && "default".equals(conf.geometryDefaultType())) {
         if (this.extTypeName != null) {
            String var2 = this.extTypeName;
            byte var3 = -1;
            switch(var2.hashCode()) {
            case -1884598128:
               if (var2.equals("geometrycollection")) {
                  var3 = 6;
               }
               break;
            case -707417346:
               if (var2.equals("multilinestring")) {
                  var3 = 4;
               }
               break;
            case -397519558:
               if (var2.equals("polygon")) {
                  var3 = 2;
               }
               break;
            case 106845584:
               if (var2.equals("point")) {
                  var3 = 0;
               }
               break;
            case 349232609:
               if (var2.equals("multipolygon")) {
                  var3 = 5;
               }
               break;
            case 729368837:
               if (var2.equals("linestring")) {
                  var3 = 1;
               }
               break;
            case 1265163255:
               if (var2.equals("multipoint")) {
                  var3 = 3;
               }
            }

            switch(var3) {
            case 0:
               return Point.class.getName();
            case 1:
               return LineString.class.getName();
            case 2:
               return Polygon.class.getName();
            case 3:
               return MultiPoint.class.getName();
            case 4:
               return MultiLineString.class.getName();
            case 5:
               return MultiPolygon.class.getName();
            case 6:
               return GeometryCollection.class.getName();
            }
         }

         return GeometryCollection.class.getName();
      } else {
         return "byte[]";
      }
   }

   public int getColumnType(Configuration conf) {
      return -3;
   }

   public String getColumnTypeName(Configuration conf) {
      return this.extTypeName != null ? this.extTypeName.toUpperCase(Locale.ROOT) : "GEOMETRY";
   }

   public Object getDefaultText(ReadableByteBuf buf, MutableInt length, Context context) throws SQLDataException {
      if (context.getConf().geometryDefaultType() != null && "default".equals(context.getConf().geometryDefaultType())) {
         buf.skip(4);
         return Geometry.getGeometry(buf, length.get() - 4, this);
      } else {
         byte[] arr = new byte[length.get()];
         buf.readBytes(arr);
         return arr;
      }
   }

   public Object getDefaultBinary(ReadableByteBuf buf, MutableInt length, Context context) throws SQLDataException {
      return this.getDefaultText(buf, length, context);
   }

   public Timestamp decodeTimestampText(ReadableByteBuf buf, MutableInt length, Calendar cal, Context context) throws SQLDataException {
      buf.skip(length.get());
      throw new SQLDataException(String.format("Data type %s cannot be decoded as Timestamp", this.dataType));
   }

   public Timestamp decodeTimestampBinary(ReadableByteBuf buf, MutableInt length, Calendar cal, Context context) throws SQLDataException {
      buf.skip(length.get());
      throw new SQLDataException(String.format("Data type %s cannot be decoded as Timestamp", this.dataType));
   }
}
