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
import fr.xephi.authme.libs.org.mariadb.jdbc.type.Point;
import java.io.IOException;
import java.sql.SQLDataException;
import java.util.Calendar;

public class LineStringCodec implements Codec<LineString> {
   public static final LineStringCodec INSTANCE = new LineStringCodec();

   public String className() {
      return LineString.class.getName();
   }

   public boolean canDecode(ColumnDecoder column, Class<?> type) {
      return column.getType() == DataType.GEOMETRY && type.isAssignableFrom(LineString.class);
   }

   public boolean canEncode(Object value) {
      return value instanceof LineString;
   }

   public LineString decodeText(ReadableByteBuf buf, MutableInt length, ColumnDecoder column, Calendar cal) throws SQLDataException {
      return this.decodeBinary(buf, length, column, cal);
   }

   public LineString decodeBinary(ReadableByteBuf buf, MutableInt length, ColumnDecoder column, Calendar cal) throws SQLDataException {
      if (column.getType() == DataType.GEOMETRY) {
         buf.skip(4);
         Geometry geo = Geometry.getGeometry(buf, length.get() - 4, column);
         if (geo instanceof LineString) {
            return (LineString)geo;
         } else {
            throw new SQLDataException(String.format("Geometric type %s cannot be decoded as LineString", geo.getClass().getName()));
         }
      } else {
         buf.skip(length.get());
         throw new SQLDataException(String.format("Data type %s cannot be decoded as LineString", column.getType()));
      }
   }

   public void encodeText(Writer encoder, Context context, Object value, Calendar cal, Long maxLength) throws IOException {
      encoder.writeBytes(("ST_LineFromText('" + value.toString() + "')").getBytes());
   }

   public void encodeBinary(Writer encoder, Object value, Calendar cal, Long maxLength) throws IOException {
      LineString line = (LineString)value;
      encoder.writeLength(13L + (long)line.getPoints().length * 16L);
      encoder.writeInt(0);
      encoder.writeByte(1);
      encoder.writeInt(2);
      encoder.writeInt(line.getPoints().length);
      Point[] var6 = line.getPoints();
      int var7 = var6.length;

      for(int var8 = 0; var8 < var7; ++var8) {
         Point pt = var6[var8];
         encoder.writeDouble(pt.getX());
         encoder.writeDouble(pt.getY());
      }

   }

   public int getBinaryEncodeType() {
      return DataType.BLOB.get();
   }
}
