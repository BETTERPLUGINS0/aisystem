package fr.xephi.authme.libs.org.mariadb.jdbc.client.column;

import fr.xephi.authme.libs.org.mariadb.jdbc.Configuration;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.ColumnDecoder;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.DataType;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.ReadableByteBuf;

public class JsonColumn extends StringColumn implements ColumnDecoder {
   public JsonColumn(ReadableByteBuf buf, int charset, long length, DataType dataType, byte decimals, int flags, int[] stringPos, String extTypeName, String extTypeFormat) {
      super(buf, charset, length, dataType, decimals, flags, stringPos, extTypeName, extTypeFormat);
   }

   protected JsonColumn(JsonColumn prev) {
      super(prev);
   }

   public JsonColumn useAliasAsName() {
      return new JsonColumn(this);
   }

   public String defaultClassname(Configuration conf) {
      return String.class.getName();
   }

   public int getColumnType(Configuration conf) {
      return -1;
   }

   public String getColumnTypeName(Configuration conf) {
      return "JSON";
   }
}
