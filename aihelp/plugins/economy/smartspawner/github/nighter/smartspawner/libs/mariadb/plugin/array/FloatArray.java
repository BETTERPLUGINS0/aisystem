package github.nighter.smartspawner.libs.mariadb.plugin.array;

import github.nighter.smartspawner.libs.mariadb.client.ColumnDecoder;
import github.nighter.smartspawner.libs.mariadb.client.Context;
import github.nighter.smartspawner.libs.mariadb.client.DataType;
import github.nighter.smartspawner.libs.mariadb.client.result.CompleteResult;
import java.nio.charset.StandardCharsets;
import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Arrays;
import java.util.Map;

public class FloatArray implements Array {
   private final float[] val;
   private Context context;

   public FloatArray(float[] val, Context context) {
      this.val = val;
      this.context = context;
   }

   public String getBaseTypeName() throws SQLException {
      return "float[]";
   }

   public int getBaseType() throws SQLException {
      return 6;
   }

   public Object getArray() throws SQLException {
      return this.val;
   }

   public Object getArray(Map<String, Class<?>> map) throws SQLException {
      throw new SQLFeatureNotSupportedException("getArray(Map<String, Class<?>> map) is not supported");
   }

   public Object getArray(long index, int count) throws SQLException {
      if (index >= 1L && index <= (long)this.val.length) {
         if (count >= 0 && index - 1L + (long)count <= (long)this.val.length) {
            return Arrays.copyOfRange(this.val, (int)index - 1, (int)(index - 1L) + count);
         } else {
            throw new SQLException(String.format("Count value is too big. Count is %s but cannot be > to %s", count, (long)this.val.length - (index - 1L)));
         }
      } else {
         throw new SQLException(String.format("Wrong index position. Is %s but must be in 1-%s range", index, this.val.length));
      }
   }

   public Object getArray(long index, int count, Map<String, Class<?>> map) throws SQLException {
      throw new SQLFeatureNotSupportedException("getArray(long index, int count, Map<String, Class<?>> map) is not supported");
   }

   public ResultSet getResultSet() throws SQLException {
      return this.getResultSet(1L, this.val.length);
   }

   public ResultSet getResultSet(Map<String, Class<?>> map) throws SQLException {
      throw new SQLFeatureNotSupportedException("getResultSet(Map<String, Class<?>> map) is not supported");
   }

   public ResultSet getResultSet(long index, int count) throws SQLException {
      byte[][] rows = new byte[count][];

      for(int i = 0; i < count; ++i) {
         byte[] val = Float.toString(this.val[(int)index - 1 + i]).getBytes(StandardCharsets.US_ASCII);
         rows[i] = new byte[val.length + 1];
         rows[i][0] = (byte)val.length;
         System.arraycopy(val, 0, rows[i], 1, val.length);
      }

      return new CompleteResult(new ColumnDecoder[]{ColumnDecoder.create(this.context.getDatabase(), "Array", DataType.FLOAT, 1)}, rows, this.context, 1004);
   }

   public ResultSet getResultSet(long index, int count, Map<String, Class<?>> map) throws SQLException {
      throw new SQLFeatureNotSupportedException("getResultSet(long index, int count, Map<String, Class<?>> map) is not supported");
   }

   public void free() {
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         FloatArray that = (FloatArray)o;
         return Arrays.equals(this.val, that.val);
      } else {
         return false;
      }
   }

   public int hashCode() {
      return Arrays.hashCode(this.val);
   }
}
