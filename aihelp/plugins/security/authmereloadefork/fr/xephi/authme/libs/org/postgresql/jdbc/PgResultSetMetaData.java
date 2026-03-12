package fr.xephi.authme.libs.org.postgresql.jdbc;

import fr.xephi.authme.libs.org.postgresql.PGResultSetMetaData;
import fr.xephi.authme.libs.org.postgresql.core.BaseConnection;
import fr.xephi.authme.libs.org.postgresql.core.Field;
import fr.xephi.authme.libs.org.postgresql.core.ServerVersion;
import fr.xephi.authme.libs.org.postgresql.util.GT;
import fr.xephi.authme.libs.org.postgresql.util.Gettable;
import fr.xephi.authme.libs.org.postgresql.util.GettableHashMap;
import fr.xephi.authme.libs.org.postgresql.util.JdbcBlackHole;
import fr.xephi.authme.libs.org.postgresql.util.PSQLException;
import fr.xephi.authme.libs.org.postgresql.util.PSQLState;
import fr.xephi.authme.libs.org.postgresql.util.internal.Nullness;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import org.checkerframework.checker.nullness.qual.Nullable;

public class PgResultSetMetaData implements ResultSetMetaData, PGResultSetMetaData {
   protected final BaseConnection connection;
   protected final Field[] fields;
   private boolean fieldInfoFetched;

   public PgResultSetMetaData(BaseConnection connection, Field[] fields) {
      this.connection = connection;
      this.fields = fields;
      this.fieldInfoFetched = false;
   }

   public int getColumnCount() throws SQLException {
      return this.fields.length;
   }

   public boolean isAutoIncrement(int column) throws SQLException {
      this.fetchFieldMetaData();
      Field field = this.getField(column);
      FieldMetadata metadata = field.getMetadata();
      return metadata != null && metadata.autoIncrement;
   }

   public boolean isCaseSensitive(int column) throws SQLException {
      Field field = this.getField(column);
      return this.connection.getTypeInfo().isCaseSensitive(field.getOID());
   }

   public boolean isSearchable(int column) throws SQLException {
      return true;
   }

   public boolean isCurrency(int column) throws SQLException {
      String typeName = this.getPGType(column);
      return "cash".equals(typeName) || "money".equals(typeName);
   }

   public int isNullable(int column) throws SQLException {
      this.fetchFieldMetaData();
      Field field = this.getField(column);
      FieldMetadata metadata = field.getMetadata();
      return metadata == null ? 1 : metadata.nullable;
   }

   public boolean isSigned(int column) throws SQLException {
      Field field = this.getField(column);
      return this.connection.getTypeInfo().isSigned(field.getOID());
   }

   public int getColumnDisplaySize(int column) throws SQLException {
      Field field = this.getField(column);
      return this.connection.getTypeInfo().getDisplaySize(field.getOID(), field.getMod());
   }

   public String getColumnLabel(int column) throws SQLException {
      Field field = this.getField(column);
      return field.getColumnLabel();
   }

   public String getColumnName(int column) throws SQLException {
      return this.getColumnLabel(column);
   }

   public String getBaseColumnName(int column) throws SQLException {
      Field field = this.getField(column);
      if (field.getTableOid() == 0) {
         return "";
      } else {
         this.fetchFieldMetaData();
         FieldMetadata metadata = field.getMetadata();
         return metadata == null ? "" : metadata.columnName;
      }
   }

   public String getSchemaName(int column) throws SQLException {
      return "";
   }

   private boolean populateFieldsWithMetadata(Gettable<FieldMetadata.Key, FieldMetadata> metadata) {
      boolean allOk = true;
      Field[] var3 = this.fields;
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         Field field = var3[var5];
         if (field.getMetadata() == null) {
            FieldMetadata fieldMetadata = (FieldMetadata)metadata.get(new FieldMetadata.Key(field.getTableOid(), field.getPositionInTable()));
            if (fieldMetadata == null) {
               allOk = false;
            } else {
               field.setMetadata(fieldMetadata);
            }
         }
      }

      this.fieldInfoFetched |= allOk;
      return allOk;
   }

   private void fetchFieldMetaData() throws SQLException {
      if (!this.fieldInfoFetched) {
         if (!this.populateFieldsWithMetadata(this.connection.getFieldMetadataCache())) {
            StringBuilder sql = new StringBuilder("SELECT c.oid, a.attnum, a.attname, c.relname, n.nspname, a.attnotnull OR (t.typtype = 'd' AND t.typnotnull), ");
            if (this.connection.haveMinimumServerVersion(ServerVersion.v10)) {
               sql.append("a.attidentity != '' OR pg_catalog.pg_get_expr(d.adbin, d.adrelid) LIKE '%nextval(%' ");
            } else {
               sql.append("pg_catalog.pg_get_expr(d.adbin, d.adrelid) LIKE '%nextval(%' ");
            }

            sql.append("FROM pg_catalog.pg_class c JOIN pg_catalog.pg_namespace n ON (c.relnamespace = n.oid) JOIN pg_catalog.pg_attribute a ON (c.oid = a.attrelid) JOIN pg_catalog.pg_type t ON (a.atttypid = t.oid) LEFT JOIN pg_catalog.pg_attrdef d ON (d.adrelid = a.attrelid AND d.adnum = a.attnum) JOIN (");
            boolean hasSourceInfo = false;
            Field[] var3 = this.fields;
            int var4 = var3.length;

            for(int var5 = 0; var5 < var4; ++var5) {
               Field field = var3[var5];
               if (field.getMetadata() == null) {
                  if (hasSourceInfo) {
                     sql.append(" UNION ALL ");
                  }

                  sql.append("SELECT ");
                  sql.append(field.getTableOid());
                  if (!hasSourceInfo) {
                     sql.append(" AS oid ");
                  }

                  sql.append(", ");
                  sql.append(field.getPositionInTable());
                  if (!hasSourceInfo) {
                     sql.append(" AS attnum");
                  }

                  if (!hasSourceInfo) {
                     hasSourceInfo = true;
                  }
               }
            }

            sql.append(") vals ON (c.oid = vals.oid AND a.attnum = vals.attnum) ");
            if (!hasSourceInfo) {
               this.fieldInfoFetched = true;
            } else {
               Statement stmt = this.connection.createStatement();
               ResultSet rs = null;
               GettableHashMap md = new GettableHashMap();

               try {
                  rs = stmt.executeQuery(sql.toString());

                  while(rs.next()) {
                     int table = (int)rs.getLong(1);
                     int column = (int)rs.getLong(2);
                     String columnName = (String)Nullness.castNonNull(rs.getString(3));
                     String tableName = (String)Nullness.castNonNull(rs.getString(4));
                     String schemaName = (String)Nullness.castNonNull(rs.getString(5));
                     int nullable = rs.getBoolean(6) ? 0 : 1;
                     boolean autoIncrement = rs.getBoolean(7);
                     FieldMetadata fieldMetadata = new FieldMetadata(columnName, tableName, schemaName, nullable, autoIncrement);
                     FieldMetadata.Key key = new FieldMetadata.Key(table, column);
                     md.put(key, fieldMetadata);
                  }
               } finally {
                  JdbcBlackHole.close(rs);
                  JdbcBlackHole.close(stmt);
               }

               this.populateFieldsWithMetadata(md);
               this.connection.getFieldMetadataCache().putAll(md);
            }
         }
      }
   }

   public String getBaseSchemaName(int column) throws SQLException {
      this.fetchFieldMetaData();
      Field field = this.getField(column);
      FieldMetadata metadata = field.getMetadata();
      return metadata == null ? "" : metadata.schemaName;
   }

   public int getPrecision(int column) throws SQLException {
      Field field = this.getField(column);
      return this.connection.getTypeInfo().getPrecision(field.getOID(), field.getMod());
   }

   public int getScale(int column) throws SQLException {
      Field field = this.getField(column);
      return this.connection.getTypeInfo().getScale(field.getOID(), field.getMod());
   }

   public String getTableName(int column) throws SQLException {
      return this.getBaseTableName(column);
   }

   public String getBaseTableName(int column) throws SQLException {
      this.fetchFieldMetaData();
      Field field = this.getField(column);
      FieldMetadata metadata = field.getMetadata();
      return metadata == null ? "" : metadata.tableName;
   }

   public String getCatalogName(int column) throws SQLException {
      return "";
   }

   public int getColumnType(int column) throws SQLException {
      return this.getSQLType(column);
   }

   public int getFormat(int column) throws SQLException {
      return this.getField(column).getFormat();
   }

   public String getColumnTypeName(int column) throws SQLException {
      String type = this.getPGType(column);
      if (this.isAutoIncrement(column)) {
         if ("int4".equals(type)) {
            return "serial";
         }

         if ("int8".equals(type)) {
            return "bigserial";
         }

         if ("int2".equals(type) && this.connection.haveMinimumServerVersion(ServerVersion.v9_2)) {
            return "smallserial";
         }
      }

      return (String)Nullness.castNonNull(type);
   }

   public boolean isReadOnly(int column) throws SQLException {
      return false;
   }

   public boolean isWritable(int column) throws SQLException {
      return !this.isReadOnly(column);
   }

   public boolean isDefinitelyWritable(int column) throws SQLException {
      return false;
   }

   protected Field getField(int columnIndex) throws SQLException {
      if (columnIndex >= 1 && columnIndex <= this.fields.length) {
         return this.fields[columnIndex - 1];
      } else {
         throw new PSQLException(GT.tr("The column index is out of range: {0}, number of columns: {1}.", columnIndex, this.fields.length), PSQLState.INVALID_PARAMETER_VALUE);
      }
   }

   @Nullable
   protected String getPGType(int columnIndex) throws SQLException {
      return this.connection.getTypeInfo().getPGType(this.getField(columnIndex).getOID());
   }

   protected int getSQLType(int columnIndex) throws SQLException {
      return this.connection.getTypeInfo().getSQLType(this.getField(columnIndex).getOID());
   }

   public String getColumnClassName(int column) throws SQLException {
      Field field = this.getField(column);
      String result = this.connection.getTypeInfo().getJavaClass(field.getOID());
      if (result != null) {
         return result;
      } else {
         int sqlType = this.getSQLType(column);
         if (sqlType == 2003) {
            return "java.sql.Array";
         } else {
            String type = this.getPGType(column);
            return "unknown".equals(type) ? "java.lang.String" : "java.lang.Object";
         }
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
