package fr.xephi.authme.libs.org.mariadb.jdbc.plugin.codec;

import fr.xephi.authme.libs.org.mariadb.jdbc.client.ColumnDecoder;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.Context;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.DataType;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.ReadableByteBuf;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.socket.Writer;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.util.MutableInt;
import fr.xephi.authme.libs.org.mariadb.jdbc.plugin.Codec;
import fr.xephi.authme.libs.org.mariadb.jdbc.type.Geometry;
import fr.xephi.authme.libs.org.mariadb.jdbc.type.MultiPoint;
import fr.xephi.authme.libs.org.mariadb.jdbc.type.Point;
import java.io.IOException;
import java.sql.SQLDataException;
import java.util.Calendar;

public class MultiPointCodec implements Codec<MultiPoint> {
   public static final MultiPointCodec INSTANCE = new MultiPointCodec();

   public String className() {
      return MultiPoint.class.getName();
   }

   public boolean canDecode(ColumnDecoder column, Class<?> type) {
      return column.getType() == DataType.GEOMETRY && type.isAssignableFrom(MultiPoint.class);
   }

   public boolean canEncode(Object value) {
      return value instanceof MultiPoint;
   }

   public MultiPoint decodeText(ReadableByteBuf buf, MutableInt length, ColumnDecoder column, Calendar cal) throws SQLDataException {
      return this.decodeBinary(buf, length, column, cal);
   }

   public MultiPoint decodeBinary(ReadableByteBuf buf, MutableInt length, ColumnDecoder column, Calendar cal) throws SQLDataException {
      if (column.getType() == DataType.GEOMETRY) {
         buf.skip(4);
         Geometry geo = Geometry.getGeometry(buf, length.get() - 4, column);
         if (geo instanceof MultiPoint) {
            return (MultiPoint)geo;
         } else {
            throw new SQLDataException(String.format("Geometric type %s cannot be decoded as MultiPoint", geo.getClass().getName()));
         }
      } else {
         buf.skip(length.get());
         throw new SQLDataException(String.format("Data type %s cannot be decoded as MultiPoint", column.getType()));
      }
   }

   public void encodeText(Writer encoder, Context context, Object value, Calendar cal, Long maxLength) throws IOException {
      encoder.writeBytes(("ST_MPointFromText('" + value.toString() + "')").getBytes());
   }

   public void encodeBinary(Writer encoder, Object value, Calendar cal, Long maxLength) throws IOException {
      MultiPoint mp = (MultiPoint)value;
      encoder.writeLength(13L + (long)mp.getPoints().length * 21L);
      encoder.writeInt(0);
      encoder.writeByte(1);
      encoder.writeInt(4);
      encoder.writeInt(mp.getPoints().length);
      Point[] var6 = mp.getPoints();
      int var7 = var6.length;

      for(int var8 = 0; var8 < var7; ++var8) {
         Point pt = var6[var8];
         encoder.writeByte(1);
         encoder.writeInt(1);
         encoder.writeDouble(pt.getX());
         encoder.writeDouble(pt.getY());
      }

   }

   public int getBinaryEncodeType() {
      return DataType.BLOB.get();
   }
}
