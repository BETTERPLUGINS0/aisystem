package fr.xephi.authme.libs.org.postgresql.jdbc;

import fr.xephi.authme.libs.org.postgresql.core.BaseConnection;
import fr.xephi.authme.libs.org.postgresql.util.GT;
import fr.xephi.authme.libs.org.postgresql.util.PSQLException;
import fr.xephi.authme.libs.org.postgresql.util.PSQLState;
import fr.xephi.authme.libs.org.postgresql.util.internal.Nullness;
import java.sql.ParameterMetaData;
import java.sql.SQLException;
import org.checkerframework.checker.index.qual.Positive;

public class PgParameterMetaData implements ParameterMetaData {
   private final BaseConnection connection;
   private final int[] oids;

   public PgParameterMetaData(BaseConnection connection, int[] oids) {
      this.connection = connection;
      this.oids = oids;
   }

   public String getParameterClassName(@Positive int param) throws SQLException {
      this.checkParamIndex(param);
      return this.connection.getTypeInfo().getJavaClass(this.oids[param - 1]);
   }

   public int getParameterCount() {
      return this.oids.length;
   }

   public int getParameterMode(int param) throws SQLException {
      this.checkParamIndex(param);
      return 1;
   }

   public int getParameterType(int param) throws SQLException {
      this.checkParamIndex(param);
      return this.connection.getTypeInfo().getSQLType(this.oids[param - 1]);
   }

   public String getParameterTypeName(int param) throws SQLException {
      this.checkParamIndex(param);
      return (String)Nullness.castNonNull(this.connection.getTypeInfo().getPGType(this.oids[param - 1]));
   }

   public int getPrecision(int param) throws SQLException {
      this.checkParamIndex(param);
      return 0;
   }

   public int getScale(int param) throws SQLException {
      this.checkParamIndex(param);
      return 0;
   }

   public int isNullable(int param) throws SQLException {
      this.checkParamIndex(param);
      return 2;
   }

   public boolean isSigned(int param) throws SQLException {
      this.checkParamIndex(param);
      return this.connection.getTypeInfo().isSigned(this.oids[param - 1]);
   }

   private void checkParamIndex(int param) throws PSQLException {
      if (param < 1 || param > this.oids.length) {
         throw new PSQLException(GT.tr("The parameter index is out of range: {0}, number of parameters: {1}.", param, this.oids.length), PSQLState.INVALID_PARAMETER_VALUE);
      }
   }

   public boolean isWrapperFor(Class<?> iface) throws SQLException {
      return iface.isAssignableFrom(this.getClass());
   }

   public <T> T unwrap(Class<T> iface) throws SQLException {
      if (iface.isAssignableFrom(this.getClass())) {
         return iface.cast(this);
      } else {
         throw new SQLException("Cannot unwrap to " + iface.getName());
      }
   }
}
