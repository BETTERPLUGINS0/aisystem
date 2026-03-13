package github.nighter.smartspawner.libs.mariadb.client.column;

import github.nighter.smartspawner.libs.mariadb.Configuration;
import github.nighter.smartspawner.libs.mariadb.client.ColumnDecoder;
import github.nighter.smartspawner.libs.mariadb.client.DataType;
import github.nighter.smartspawner.libs.mariadb.client.ReadableByteBuf;
import github.nighter.smartspawner.libs.mariadb.util.CharsetEncodingLength;

public class JsonColumn extends StringColumn implements ColumnDecoder {
   public JsonColumn(ReadableByteBuf buf, int charset, long length, DataType dataType, byte decimals, int flags, int[] stringPos, String extTypeName, String extTypeFormat) {
      super(buf, charset, length, dataType, decimals, flags, stringPos, extTypeName, extTypeFormat);
   }

   protected JsonColumn(JsonColumn prev) {
      super(prev);
   }

   public int getDisplaySize() {
      if (this.charset != 63) {
         Integer maxWidth = (Integer)CharsetEncodingLength.maxCharlen.get(this.charset);
         return maxWidth != null ? (int)(this.columnLength / (long)maxWidth) : (int)(this.columnLength / 4L);
      } else {
         return (int)this.columnLength;
      }
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
