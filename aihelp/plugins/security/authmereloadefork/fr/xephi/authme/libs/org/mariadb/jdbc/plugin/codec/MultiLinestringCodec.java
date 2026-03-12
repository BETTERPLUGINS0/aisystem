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
import fr.xephi.authme.libs.org.mariadb.jdbc.type.MultiLineString;
import fr.xephi.authme.libs.org.mariadb.jdbc.type.Point;
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

   public MultiLineString decodeText(ReadableByteBuf buf, MutableInt length, ColumnDecoder column, Calendar cal) throws SQLDataException {
      return this.decodeBinary(buf, length, column, cal);
   }

   public MultiLineString decodeBinary(ReadableByteBuf buf, MutableInt length, ColumnDecoder column, Calendar cal) throws SQLDataException {
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

   public void encodeBinary(Writer encoder, Object value, Calendar cal, Long maxLength) throws IOException {
      MultiLineString mp = (MultiLineString)value;
      int length = 13;
      LineString[] var7 = mp.getLines();
      int var8 = var7.length;

      int var9;
      LineString ls;
      for(var9 = 0; var9 < var8; ++var9) {
         ls = var7[var9];
         length += 9 + ls.getPoints().length * 16;
      }

      encoder.writeLength((long)length);
      encoder.writeInt(0);
      encoder.writeByte(1);
      encoder.writeInt(5);
      encoder.writeInt(mp.getLines().length);
      var7 = mp.getLines();
      var8 = var7.length;

      for(var9 = 0; var9 < var8; ++var9) {
         ls = var7[var9];
         encoder.writeByte(1);
         encoder.writeInt(2);
         encoder.writeInt(ls.getPoints().length);
         Point[] var11 = ls.getPoints();
         int var12 = var11.length;

         for(int var13 = 0; var13 < var12; ++var13) {
            Point pt = var11[var13];
            encoder.writeDouble(pt.getX());
            encoder.writeDouble(pt.getY());
         }
      }

   }

   public int getBinaryEncodeType() {
      return DataType.BLOB.get();
   }
}
