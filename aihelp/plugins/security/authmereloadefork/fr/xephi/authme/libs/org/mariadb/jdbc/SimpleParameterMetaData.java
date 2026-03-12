package fr.xephi.authme.libs.org.mariadb.jdbc;

import fr.xephi.authme.libs.org.mariadb.jdbc.export.ExceptionFactory;
import java.sql.SQLException;

public class SimpleParameterMetaData implements java.sql.ParameterMetaData {
   private final int paramCount;
   private final ExceptionFactory exceptionFactory;

   protected SimpleParameterMetaData(ExceptionFactory exceptionFactory, int paramCount) {
      this.exceptionFactory = exceptionFactory;
      this.paramCount = paramCount;
   }

   public int getParameterCount() {
      return this.paramCount;
   }

   private void checkIndex(int index) throws SQLException {
      if (index < 1 || index > this.paramCount) {
         throw this.exceptionFactory.create(String.format("Wrong index position. Is %s but must be in 1-%s range", index, this.paramCount));
      }
   }

   public int isNullable(int idx) throws SQLException {
      this.checkIndex(idx);
      return 1;
   }

   public boolean isSigned(int idx) throws SQLException {
      this.checkIndex(idx);
      return true;
   }

   public int getPrecision(int idx) throws SQLException {
      this.checkIndex(idx);
      throw this.exceptionFactory.create("Unknown parameter metadata precision");
   }

   public int getScale(int idx) throws SQLException {
      this.checkIndex(idx);
      throw this.exceptionFactory.create("Unknown parameter metadata scale");
   }

   public int getParameterType(int idx) throws SQLException {
      this.checkIndex(idx);
      throw this.exceptionFactory.create("Getting parameter type metadata is not supported", "0A000", -1);
   }

   public String getParameterTypeName(int idx) throws SQLException {
      this.checkIndex(idx);
      throw this.exceptionFactory.create("Unknown parameter metadata type name");
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
