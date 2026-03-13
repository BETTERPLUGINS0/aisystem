package github.nighter.smartspawner.libs.mariadb.client.result;

import github.nighter.smartspawner.libs.mariadb.Configuration;
import github.nighter.smartspawner.libs.mariadb.client.Column;
import github.nighter.smartspawner.libs.mariadb.client.ColumnDecoder;
import github.nighter.smartspawner.libs.mariadb.export.ExceptionFactory;
import github.nighter.smartspawner.libs.mariadb.util.constants.CatalogTerm;
import java.sql.SQLException;

public class ResultSetMetaData implements java.sql.ResultSetMetaData {
   private final ExceptionFactory exceptionFactory;
   private final ColumnDecoder[] fieldPackets;
   private final Configuration conf;
   private final boolean forceAlias;

   public ResultSetMetaData(ExceptionFactory exceptionFactory, ColumnDecoder[] fieldPackets, Configuration conf, boolean forceAlias) {
      this.exceptionFactory = exceptionFactory;
      this.fieldPackets = fieldPackets;
      this.conf = conf;
      this.forceAlias = forceAlias;
   }

   public int getColumnCount() {
      return this.fieldPackets.length;
   }

   public boolean isAutoIncrement(int column) throws SQLException {
      return (this.getColumn(column).getFlags() & 512) != 0;
   }

   public boolean isCaseSensitive(int column) {
      return true;
   }

   public boolean isSearchable(int column) {
      return true;
   }

   public boolean isCurrency(int column) {
      return false;
   }

   public int isNullable(int column) throws SQLException {
      return (this.getColumn(column).getFlags() & 1) == 0 ? 1 : 0;
   }

   public boolean isSigned(int column) throws SQLException {
      return this.getColumn(column).isSigned();
   }

   public int getColumnDisplaySize(int column) throws SQLException {
      return this.getColumn(column).getDisplaySize();
   }

   public String getColumnLabel(int column) throws SQLException {
      return this.getColumn(column).getColumnAlias();
   }

   public String getColumnName(int idx) throws SQLException {
      Column column = this.getColumn(idx);
      String columnName = column.getColumnName();
      return !"".equals(columnName) && !this.forceAlias ? columnName : column.getColumnAlias();
   }

   public String getCatalogName(int column) throws SQLException {
      return this.conf.useCatalogTerm() == CatalogTerm.UseSchema ? this.getColumn(column).getCatalog() : this.getColumn(column).getSchema();
   }

   public int getPrecision(int column) throws SQLException {
      return this.getColumn(column).getPrecision();
   }

   public int getScale(int index) throws SQLException {
      return this.getColumn(index).getDecimals();
   }

   public String getTableName(int column) throws SQLException {
      if (this.forceAlias) {
         return this.getColumn(column).getTableAlias();
      } else {
         return this.conf.blankTableNameMeta() ? "" : this.getColumn(column).getTable();
      }
   }

   public String getSchemaName(int column) throws SQLException {
      return this.conf.useCatalogTerm() == CatalogTerm.UseSchema ? this.getColumn(column).getSchema() : "";
   }

   public int getColumnType(int column) throws SQLException {
      return this.getColumn(column).getColumnType(this.conf);
   }

   public String getColumnTypeName(int index) throws SQLException {
      return this.getColumn(index).getColumnTypeName(this.conf);
   }

   public boolean isReadOnly(int column) throws SQLException {
      Column ci = this.getColumn(column);
      return ci.getColumnName().isEmpty();
   }

   public boolean isWritable(int column) throws SQLException {
      return !this.isReadOnly(column);
   }

   public boolean isDefinitelyWritable(int column) throws SQLException {
      return !this.isReadOnly(column);
   }

   public String getColumnClassName(int column) throws SQLException {
      return this.getColumn(column).defaultClassname(this.conf);
   }

   private ColumnDecoder getColumn(int column) throws SQLException {
      if (column >= 1 && column <= this.fieldPackets.length) {
         return this.fieldPackets[column - 1];
      } else {
         throw this.exceptionFactory.create(String.format("wrong column index %s", column));
      }
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
