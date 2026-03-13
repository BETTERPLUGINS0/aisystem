package github.nighter.smartspawner.libs.mariadb;

import github.nighter.smartspawner.libs.mariadb.client.ColumnDecoder;
import github.nighter.smartspawner.libs.mariadb.client.DataType;
import github.nighter.smartspawner.libs.mariadb.export.ExceptionFactory;
import java.sql.SQLException;

public class ParameterMetaData implements java.sql.ParameterMetaData {
   private final ColumnDecoder[] params;
   private final ExceptionFactory exceptionFactory;

   protected ParameterMetaData(ExceptionFactory exceptionFactory, ColumnDecoder[] params) {
      this.params = params;
      this.exceptionFactory = exceptionFactory;
   }

   public int getParameterCount() {
      return this.params.length;
   }

   private void checkIndex(int index) throws SQLException {
      if (index < 1 || index > this.params.length) {
         throw new SQLException(String.format("Wrong index position. Is %s but must be in 1-%s range", index, this.params.length));
      }
   }

   public int isNullable(int idx) throws SQLException {
      this.checkIndex(idx);
      return 1;
   }

   public boolean isSigned(int idx) throws SQLException {
      this.checkIndex(idx);
      return this.params[idx - 1].isSigned();
   }

   public int getPrecision(int idx) throws SQLException {
      this.checkIndex(idx);
      return this.params[idx - 1].getPrecision();
   }

   public int getScale(int idx) throws SQLException {
      this.checkIndex(idx);
      return this.params[idx - 1].getDecimals();
   }

   public int getParameterType(int idx) throws SQLException {
      this.checkIndex(idx);
      throw this.exceptionFactory.create("Getting parameter type metadata are not supported", "0A000", -1);
   }

   public String getParameterTypeName(int idx) throws SQLException {
      this.checkIndex(idx);
      DataType type = this.params[idx - 1].getType();
      return type == null ? null : type.name();
   }

   public String getParameterClassName(int idx) throws SQLException {
      this.checkIndex(idx);
      throw this.exceptionFactory.create("Unknown parameter metadata class name", "0A000");
   }

   public int getParameterMode(int idx) throws SQLException {
      this.checkIndex(idx);
      return 1;
   }

   public <T> T unwrap(Class<T> iface) throws SQLException {
      if (this.isWrapperFor(iface)) {
         return iface.cast(this);
      } else {
         throw new SQLException("The receiver is not a wrapper for " + iface.getName());
      }
   }

   public boolean isWrapperFor(Class<?> iface) {
      return iface.isInstance(this);
   }
}
