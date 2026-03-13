package github.nighter.smartspawner.libs.mariadb;

import github.nighter.smartspawner.libs.mariadb.client.DataType;
import github.nighter.smartspawner.libs.mariadb.client.ServerVersion;
import github.nighter.smartspawner.libs.mariadb.client.result.CompleteResult;
import github.nighter.smartspawner.libs.mariadb.util.VersionFactory;
import github.nighter.smartspawner.libs.mariadb.util.constants.CatalogTerm;
import github.nighter.smartspawner.libs.mariadb.util.constants.MetaExportedKeys;
import java.sql.ResultSet;
import java.sql.RowIdLifetime;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

public class DatabaseMetaData implements java.sql.DatabaseMetaData {
   public static final String DRIVER_NAME = "MariaDB Connector/J";
   private final Connection connection;
   private final Configuration conf;

   public DatabaseMetaData(Connection connection, Configuration conf) {
      this.connection = connection;
      this.conf = conf;
   }

   private static String DataTypeClause(Configuration conf) {
      String upperCaseWithoutSize = " UCASE(IF( COLUMN_TYPE LIKE '%(%)%', CONCAT(SUBSTRING( COLUMN_TYPE,1, LOCATE('(',COLUMN_TYPE) - 1 ), SUBSTRING(COLUMN_TYPE ,1+locate(')', COLUMN_TYPE))), COLUMN_TYPE))";
      if (conf.tinyInt1isBit()) {
         upperCaseWithoutSize = " IF(COLUMN_TYPE like 'tinyint(1)%', '" + (conf.transformedBitIsBoolean() ? "BOOLEAN" : "BIT") + "', " + upperCaseWithoutSize + ")";
      }

      return !conf.yearIsDateType() ? " IF(COLUMN_TYPE IN ('year(2)', 'year(4)'), 'SMALLINT', " + upperCaseWithoutSize + ")" : upperCaseWithoutSize;
   }

   private static int skipWhiteSpace(char[] part, int startPos) {
      for(int i = startPos; i < part.length; ++i) {
         if (!Character.isWhitespace(part[i])) {
            return i;
         }
      }

      return part.length;
   }

   private static int parseIdentifier(char[] part, int startPos, DatabaseMetaData.Identifier identifier) throws ParseException {
      int pos = skipWhiteSpace(part, startPos);
      if (part[pos] != '`') {
         throw new ParseException(new String(part), pos);
      } else {
         ++pos;
         StringBuilder sb = new StringBuilder();

         for(int quotes = 0; pos < part.length; ++pos) {
            char ch = part[pos];
            if (ch == '`') {
               ++quotes;
            } else {
               for(int j = 0; j < quotes / 2; ++j) {
                  sb.append('`');
               }

               if (quotes % 2 == 1) {
                  if (ch == '.') {
                     if (identifier.schema != null) {
                        throw new ParseException(new String(part), pos);
                     }

                     identifier.schema = sb.toString();
                     return parseIdentifier(part, pos + 1, identifier);
                  }

                  identifier.name = sb.toString();
                  return pos;
               }

               quotes = 0;
               sb.append(ch);
            }
         }

         throw new ParseException(new String(part), startPos);
      }
   }

   private static int skipKeyword(char[] part, int startPos, String keyword) throws ParseException {
      int pos = skipWhiteSpace(part, startPos);

      for(int i = 0; i < keyword.length(); ++pos) {
         if (part[pos] != keyword.charAt(i)) {
            throw new ParseException(new String(part), pos);
         }

         ++i;
      }

      return pos;
   }

   private static int getImportedKeyAction(String actionKey) {
      if (actionKey == null) {
         return 1;
      } else {
         byte var2 = -1;
         switch(actionKey.hashCode()) {
         case -2125576539:
            if (actionKey.equals("SET NULL")) {
               var2 = 2;
            }
            break;
         case 446081724:
            if (actionKey.equals("RESTRICT")) {
               var2 = 4;
            }
            break;
         case 1198185539:
            if (actionKey.equals("SET DEFAULT")) {
               var2 = 3;
            }
            break;
         case 1256228213:
            if (actionKey.equals("NO ACTION")) {
               var2 = 0;
            }
            break;
         case 1272812180:
            if (actionKey.equals("CASCADE")) {
               var2 = 1;
            }
         }

         switch(var2) {
         case 0:
            return 3;
         case 1:
            return 0;
         case 2:
            return 2;
         case 3:
            return 4;
         case 4:
            return 1;
         default:
            throw new IllegalArgumentException("Illegal key action '" + actionKey + "' specified.");
         }
      }
   }

   private static String quoteIdentifier(String string) {
      return "`" + string.replaceAll("`", "``") + "`";
   }

   public static String escapeString(String value, boolean noBackslashEscapes) {
      return noBackslashEscapes ? value.replace("'", "''") : value.replace("\\", "\\\\").replace("'", "\\'").replace("\u0000", "\\0").replace("\"", "\\\"");
   }

   private int parseIdentifierList(char[] part, int startPos, List<DatabaseMetaData.Identifier> list) throws ParseException {
      int pos = skipWhiteSpace(part, startPos);
      if (part[pos] != '(') {
         throw new ParseException(new String(part), pos);
      } else {
         ++pos;

         while(true) {
            pos = skipWhiteSpace(part, pos);
            char ch = part[pos];
            switch(ch) {
            case ')':
               return pos + 1;
            case ',':
               ++pos;
               break;
            case '`':
               DatabaseMetaData.Identifier id = new DatabaseMetaData.Identifier();
               pos = parseIdentifier(part, pos, id);
               list.add(id);
               break;
            default:
               throw new ParseException(new String(part, startPos, part.length - startPos), startPos);
            }
         }
      }
   }

   private void parseShowCreateTable(String tableDef, String database, String tableName, boolean importedKeysWithConstraintNames, List<String[]> data) throws ParseException, SQLException {
      String[] parts = tableDef.split("\n");
      String[] var7 = parts;
      int var8 = parts.length;

      for(int var9 = 0; var9 < var8; ++var9) {
         String part = var7[var9];
         part = part.trim();
         if (part.toUpperCase(Locale.ROOT).startsWith("CONSTRAINT") || part.toUpperCase(Locale.ROOT).contains("FOREIGN KEY")) {
            char[] partChar = part.toCharArray();
            DatabaseMetaData.Identifier constraintName = new DatabaseMetaData.Identifier();
            int pos = skipKeyword(partChar, 0, "CONSTRAINT");
            pos = parseIdentifier(partChar, pos, constraintName);
            pos = skipKeyword(partChar, pos, "FOREIGN KEY");
            List<DatabaseMetaData.Identifier> foreignKeyCols = new ArrayList();
            pos = this.parseIdentifierList(partChar, pos, foreignKeyCols);
            pos = skipKeyword(partChar, pos, "REFERENCES");
            DatabaseMetaData.Identifier pkTable = new DatabaseMetaData.Identifier();
            pos = parseIdentifier(partChar, pos, pkTable);
            List<DatabaseMetaData.Identifier> primaryKeyCols = new ArrayList();
            this.parseIdentifierList(partChar, pos, primaryKeyCols);
            int onUpdateReferenceAction = 1;
            int onDeleteReferenceAction = 1;
            String[] var19 = new String[]{"RESTRICT", "CASCADE", "SET NULL", "NO ACTION", "SET DEFAULT"};
            int i = var19.length;

            String ext;
            for(int var21 = 0; var21 < i; ++var21) {
               ext = var19[var21];
               if (part.toUpperCase(Locale.ROOT).contains("ON UPDATE " + ext)) {
                  onUpdateReferenceAction = getImportedKeyAction(ext);
               }

               if (part.toUpperCase(Locale.ROOT).contains("ON DELETE " + ext)) {
                  onDeleteReferenceAction = getImportedKeyAction(ext);
               }
            }

            Map<String, Map<String[], String>> externalInfos = new HashMap();

            for(i = 0; i < primaryKeyCols.size(); ++i) {
               String[] row = new String[]{this.conf.useCatalogTerm() == CatalogTerm.UseCatalog ? (pkTable.schema == null ? database : pkTable.schema) : "def", this.conf.useCatalogTerm() == CatalogTerm.UseSchema ? (pkTable.schema == null ? database : pkTable.schema) : null, pkTable.name, ((DatabaseMetaData.Identifier)primaryKeyCols.get(i)).name, this.conf.useCatalogTerm() == CatalogTerm.UseCatalog ? database : "def", this.conf.useCatalogTerm() == CatalogTerm.UseSchema ? database : null, tableName, ((DatabaseMetaData.Identifier)foreignKeyCols.get(i)).name, Integer.toString(i + 1), Integer.toString(onUpdateReferenceAction), Integer.toString(onDeleteReferenceAction), constraintName.name, null, null};
               if (!importedKeysWithConstraintNames) {
                  row[12] = null;
               } else {
                  ext = quoteIdentifier(pkTable.schema == null ? database : pkTable.schema) + "." + quoteIdentifier(pkTable.name);
                  if (!externalInfos.containsKey(ext)) {
                     externalInfos.put(ext, this.getExtImportedKeys(ext, this.connection));
                  }

                  row[12] = null;
                  Map<String[], String> externalInfo = (Map)externalInfos.get(ext);
                  if (externalInfo != null) {
                     Iterator var24 = externalInfo.entrySet().iterator();

                     label116:
                     while(true) {
                        Entry entry;
                        boolean foundAll;
                        ArrayList pkKeysFound;
                        do {
                           if (!var24.hasNext()) {
                              break label116;
                           }

                           entry = (Entry)var24.next();
                           foundAll = true;
                           pkKeysFound = new ArrayList();
                           String[] var28 = (String[])entry.getKey();
                           int var29 = var28.length;

                           for(int var30 = 0; var30 < var29; ++var30) {
                              String keyPart = var28[var30];
                              boolean foundKey = false;
                              Iterator var33 = primaryKeyCols.iterator();

                              while(var33.hasNext()) {
                                 DatabaseMetaData.Identifier keyCol = (DatabaseMetaData.Identifier)var33.next();
                                 if (keyCol.name.equals(keyPart)) {
                                    foundKey = true;
                                    pkKeysFound.add(keyCol);
                                    break;
                                 }
                              }

                              if (!foundKey) {
                                 foundAll = false;
                                 break;
                              }
                           }
                        } while(!foundAll && pkKeysFound.size() != primaryKeyCols.size());

                        row[12] = (String)entry.getValue();
                     }
                  }
               }

               row[13] = Integer.toString(7);
               data.add(row);
            }
         }
      }

   }

   private Map<String[], String> getExtImportedKeys(String tableName, Connection connection) throws SQLException {
      ResultSet rs = connection.createStatement().executeQuery("SHOW CREATE TABLE " + tableName);
      rs.next();
      String refTableDef = rs.getString(2);
      Map<String[], String> res = new HashMap();
      String[] parts = refTableDef.split("\n");

      for(int i = 1; i < parts.length - 1; ++i) {
         String part = parts[i].trim();
         if (!part.startsWith("`") && (part.startsWith("PRIMARY KEY") || part.startsWith("UNIQUE KEY") || part.startsWith("KEY"))) {
            String name = "PRIMARY";
            if (part.indexOf("`") < part.indexOf("(")) {
               int offset = part.indexOf("`");
               name = part.substring(offset + 1, part.indexOf("`", offset + 1));
            }

            String subPart = part.substring(part.indexOf("(") + 1, part.lastIndexOf(")"));
            List<String> cols = new ArrayList();

            int endpos;
            for(int pos = 0; pos < subPart.length(); pos = endpos + 1) {
               pos = subPart.indexOf("`", pos);
               endpos = subPart.indexOf("`", pos + 1);
               cols.add(subPart.substring(pos + 1, endpos));
            }

            res.put((String[])cols.toArray(new String[0]), name);
         }
      }

      return res;
   }

   public ResultSet getImportedKeys(String catalog, String schema, String table) throws SQLException {
      if (table != null && !table.isEmpty()) {
         String database = this.conf.useCatalogTerm() == CatalogTerm.UseCatalog ? catalog : schema;
         boolean getImportedKeysUsingIs = Boolean.parseBoolean(this.conf.nonMappedOptions().getProperty("getImportedKeysUsingIs", "false"));
         if (getImportedKeysUsingIs) {
            return this.getImportedKeysUsingInformationSchema(database, table);
         } else {
            try {
               return this.getImportedKeysUsingShowCreateTable(database, table);
            } catch (Exception var7) {
               return this.getImportedKeysUsingInformationSchema(database, table);
            }
         }
      } else {
         throw new SQLException("'table' parameter in getImportedKeys cannot be null");
      }
   }

   private String dataTypeClause(String fullTypeColumnName) {
      return " CASE data_type WHEN 'bit' THEN -7 WHEN 'tinyblob' THEN -3 WHEN 'mediumblob' THEN -4 WHEN 'longblob' THEN -4 WHEN 'blob' THEN -4 WHEN 'tinytext' THEN 12 WHEN 'mediumtext' THEN -1 WHEN 'longtext' THEN -1 WHEN 'text' THEN -1 WHEN 'date' THEN 91 WHEN 'datetime' THEN 93 WHEN 'decimal' THEN 3 WHEN 'double' THEN 8 WHEN 'enum' THEN 12 WHEN 'float' THEN 7 WHEN 'int' THEN IF( " + fullTypeColumnName + " like '%unsigned%', " + 4 + "," + 4 + ") WHEN 'bigint' THEN " + -5 + " WHEN 'mediumint' THEN " + 4 + " WHEN 'null' THEN " + 0 + " WHEN 'set' THEN " + 12 + " WHEN 'smallint' THEN IF( " + fullTypeColumnName + " like '%unsigned%', " + 5 + "," + 5 + ") WHEN 'varchar' THEN " + 12 + " WHEN 'varbinary' THEN " + -3 + " WHEN 'char' THEN " + 1 + " WHEN 'binary' THEN " + -2 + " WHEN 'time' THEN " + 92 + " WHEN 'timestamp' THEN " + 93 + " WHEN 'tinyint' THEN " + (this.conf.tinyInt1isBit() ? "IF(" + fullTypeColumnName + " like 'tinyint(1)%'," + (this.conf.transformedBitIsBoolean() ? 16 : -7) + "," + -6 + ") " : -6) + " WHEN 'year' THEN " + (this.conf.yearIsDateType() ? 91 : 5) + " ELSE " + 1111 + " END ";
   }

   private ResultSet executeQuery(String sql) throws SQLException {
      java.sql.Statement stmt = this.connection.createStatement(1004, 1007);
      stmt.setFetchSize(0);
      CompleteResult rs = (CompleteResult)stmt.executeQuery(sql);
      CompleteResult newRes = rs.newResultsetWithUseAliasAsName();
      newRes.setStatement((java.sql.Statement)null);
      return newRes;
   }

   private String escapeQuote(String value) {
      return value == null ? "null" : "'" + escapeString(value, (this.connection.getContext().getServerStatus() & 512) > 0) + "'";
   }

   private boolean databaseCond(boolean firstCondition, StringBuilder sb, String columnName, String database, boolean usePattern) {
      if ((database != null || this.conf.nullDatabaseMeansCurrent()) && (!"%".equals(database) || !usePattern)) {
         if ((database != null || !this.conf.nullDatabaseMeansCurrent()) && !database.isEmpty()) {
            sb.append(firstCondition ? " WHERE " : " AND ").append(columnName).append(usePattern && (database.indexOf(37) != -1 || database.indexOf(95) != -1) ? " LIKE " : "=").append(this.escapeQuote(database));
            return false;
         } else {
            sb.append(firstCondition ? " WHERE " : " AND ").append(columnName).append(" = database()");
            return false;
         }
      } else {
         return firstCondition;
      }
   }

   private boolean patternCond(boolean firstCondition, StringBuilder sb, String columnName, String tableName) {
      if (tableName != null && !"%".equals(tableName)) {
         sb.append(firstCondition ? " WHERE " : " AND ").append(columnName).append(tableName.indexOf(37) == -1 && tableName.indexOf(95) == -1 ? "=" : " LIKE ").append("'").append(escapeString(tableName, (this.connection.getContext().getServerStatus() & 512) != 0)).append("'");
         return false;
      } else {
         return firstCondition;
      }
   }

   public ResultSet getPrimaryKeys(String catalog, String schema, String table) throws SQLException {
      if (table != null && !table.isEmpty()) {
         StringBuilder sb = (new StringBuilder("SELECT ")).append(this.conf.useCatalogTerm() == CatalogTerm.UseCatalog ? "TABLE_SCHEMA TABLE_CAT, NULL TABLE_SCHEM" : "TABLE_CATALOG TABLE_CAT, TABLE_SCHEMA TABLE_SCHEM").append(", TABLE_NAME, COLUMN_NAME, SEQ_IN_INDEX KEY_SEQ, INDEX_NAME PK_NAME  FROM INFORMATION_SCHEMA.STATISTICS WHERE INDEX_NAME='PRIMARY'");
         String database = this.conf.useCatalogTerm() == CatalogTerm.UseCatalog ? catalog : schema;
         this.databaseCond(false, sb, "TABLE_SCHEMA", database, false);
         sb.append(" AND TABLE_NAME = ").append(this.escapeQuote(table)).append(" ORDER BY COLUMN_NAME");
         return this.executeQuery(sb.toString());
      } else {
         throw new SQLException("'table' parameter is mandatory in getPrimaryKeys()");
      }
   }

   public ResultSet getTables(String catalog, String schemaPattern, String tableNamePattern, String[] types) throws SQLException {
      StringBuilder sb = new StringBuilder("SELECT " + (this.conf.useCatalogTerm() == CatalogTerm.UseCatalog ? "TABLE_SCHEMA TABLE_CAT, NULL TABLE_SCHEM," : "TABLE_CATALOG TABLE_CAT, TABLE_SCHEMA TABLE_SCHEM,") + " TABLE_NAME,   IF(TABLE_SCHEMA IN ('mysql', 'performance_schema', 'sys'), IF(TABLE_TYPE='BASE TABLE', 'SYSTEM TABLE', IF(TABLE_TYPE='VIEW', 'SYSTEM VIEW', TABLE_TYPE)), IF(TABLE_TYPE='BASE TABLE' or TABLE_TYPE='SYSTEM VERSIONED', 'TABLE', IF(TABLE_TYPE='TEMPORARY', 'LOCAL TEMPORARY', TABLE_TYPE))) as TABLE_TYPE,  TABLE_COMMENT REMARKS, NULL TYPE_CAT, NULL TYPE_SCHEM, NULL TYPE_NAME, NULL SELF_REFERENCING_COL_NAME,  NULL REF_GENERATION FROM INFORMATION_SCHEMA.TABLES");
      String database = this.conf.useCatalogTerm() == CatalogTerm.UseCatalog ? catalog : schemaPattern;
      boolean firstCondition = this.databaseCond(true, sb, "TABLE_SCHEMA", database, this.conf.useCatalogTerm() == CatalogTerm.UseSchema);
      firstCondition = this.patternCond(firstCondition, sb, "TABLE_NAME", tableNamePattern);
      if (types != null && types.length > 0) {
         boolean mustAddType = false;
         StringBuilder sqlType = new StringBuilder((firstCondition ? " WHERE " : " AND ") + " TABLE_TYPE IN (");
         String[] var10 = types;
         int var11 = types.length;

         for(int var12 = 0; var12 < var11; ++var12) {
            String s = var10[var12];
            if (s != null) {
               if (mustAddType) {
                  sqlType.append(",");
               }

               mustAddType = true;
               byte var15 = -1;
               switch(s.hashCode()) {
               case -1703528548:
                  if (s.equals("LOCAL TEMPORARY")) {
                     var15 = 1;
                  }
                  break;
               case 79578030:
                  if (s.equals("TABLE")) {
                     var15 = 0;
                  }
               }

               switch(var15) {
               case 0:
                  sqlType.append("'BASE TABLE','SYSTEM VERSIONED'");
                  break;
               case 1:
                  sqlType.append("'TEMPORARY'");
                  break;
               default:
                  sqlType.append(this.escapeQuote(s));
               }
            }
         }

         sqlType.append(")");
         if (mustAddType) {
            sb.append(sqlType);
         }
      }

      sb.append(" ORDER BY TABLE_TYPE,TABLE_CAT,TABLE_SCHEMA,TABLE_NAME");
      return this.executeQuery(sb.toString());
   }

   public ResultSet getColumns(String catalog, String schemaPattern, String tableNamePattern, String columnNamePattern) throws SQLException {
      ServerVersion version = this.connection.getContext().getVersion();
      boolean supportsFractionalSeconds = version.isMariaDBServer() ? version.versionGreaterOrEqual(5, 3, 0) : version.versionGreaterOrEqual(5, 6, 4);
      StringBuilder sb = new StringBuilder();
      sb.append("SELECT ").append(this.conf.useCatalogTerm() == CatalogTerm.UseCatalog ? "TABLE_SCHEMA TABLE_CAT, NULL TABLE_SCHEM" : "TABLE_CATALOG TABLE_CAT, TABLE_SCHEMA TABLE_SCHEM").append(", TABLE_NAME, COLUMN_NAME,").append(this.dataTypeClause("COLUMN_TYPE")).append(" DATA_TYPE,").append(DataTypeClause(this.conf)).append(" TYPE_NAME, CASE DATA_TYPE WHEN 'date' THEN 10");
      if (supportsFractionalSeconds) {
         sb.append("  WHEN 'time' THEN IF(DATETIME_PRECISION = 0, 10, CAST(11 + DATETIME_PRECISION as signed integer))  WHEN 'datetime' THEN IF(DATETIME_PRECISION = 0, 19, CAST(20 + DATETIME_PRECISION as signed integer))  WHEN 'timestamp' THEN IF(DATETIME_PRECISION = 0, 19, CAST(20 + DATETIME_PRECISION as signed integer))");
      } else {
         sb.append(" WHEN 'time' THEN 10 WHEN 'datetime' THEN 19 WHEN 'timestamp' THEN 19");
      }

      sb.append(this.conf.yearIsDateType() ? "" : " WHEN 'year' THEN 5").append("  ELSE   IF(NUMERIC_PRECISION IS NULL, LEAST(CHARACTER_MAXIMUM_LENGTH,2147483647), NUMERIC_PRECISION)  END COLUMN_SIZE, 65535 BUFFER_LENGTH,  CONVERT (CASE DATA_TYPE WHEN 'year' THEN ").append(this.conf.yearIsDateType() ? "NUMERIC_SCALE" : "0").append(" WHEN 'tinyint' THEN ").append(this.conf.tinyInt1isBit() ? "0" : "NUMERIC_SCALE").append(" ELSE NUMERIC_SCALE END, UNSIGNED INTEGER) DECIMAL_DIGITS, 10 NUM_PREC_RADIX, IF(IS_NULLABLE = 'yes',1,0) NULLABLE,COLUMN_COMMENT REMARKS, COLUMN_DEFAULT COLUMN_DEF, 0 SQL_DATA_TYPE, 0 SQL_DATETIME_SUB,   LEAST(CHARACTER_OCTET_LENGTH,2147483647) CHAR_OCTET_LENGTH, ORDINAL_POSITION, IS_NULLABLE, NULL SCOPE_CATALOG, NULL SCOPE_SCHEMA, NULL SCOPE_TABLE, NULL SOURCE_DATA_TYPE, IF(EXTRA = 'auto_increment','YES','NO') IS_AUTOINCREMENT,  IF(EXTRA in ('VIRTUAL', 'PERSISTENT', 'VIRTUAL GENERATED', 'STORED GENERATED') ,'YES','NO') IS_GENERATEDCOLUMN  FROM INFORMATION_SCHEMA.COLUMNS");
      String database = this.conf.useCatalogTerm() == CatalogTerm.UseCatalog ? catalog : schemaPattern;
      boolean firstCondition = this.databaseCond(true, sb, "TABLE_SCHEMA", database, this.conf.useCatalogTerm() == CatalogTerm.UseSchema);
      firstCondition = this.patternCond(firstCondition, sb, "TABLE_NAME", tableNamePattern);
      this.patternCond(firstCondition, sb, "COLUMN_NAME", columnNamePattern);
      sb.append(" ORDER BY TABLE_CAT, TABLE_SCHEM, TABLE_NAME, ORDINAL_POSITION");
      return this.executeQuery(sb.toString());
   }

   public ResultSet getExportedKeys(String catalog, String schema, String table) throws SQLException {
      if (table != null && !table.isEmpty()) {
         MetaExportedKeys implType = this.conf.metaExportedKeys();
         String useIs = this.conf.nonMappedOptions().getProperty("getExportedKeysUsingIs");
         if ("true".equals(useIs)) {
            implType = MetaExportedKeys.UseInformationSchema;
         }

         if (implType == MetaExportedKeys.UseShowCreate || implType == MetaExportedKeys.Auto && Boolean.TRUE.equals(this.connection.getContext().isLoopbackAddress())) {
            try {
               return this.getExportedKeysUsingShowCreateTable(catalog, schema, table);
            } catch (Exception var7) {
            }
         }

         return this.getExportedKeysUsingInformationSchema(catalog, schema, table);
      } else {
         throw new SQLException("'table' parameter in getExportedKeys cannot be null");
      }
   }

   private ResultSet getExportedKeysUsingShowCreateTable(String catalog, String schema, String table) throws Exception {
      String[] columnNames = new String[]{"PKTABLE_CAT", "PKTABLE_SCHEM", "PKTABLE_NAME", "PKCOLUMN_NAME", "FKTABLE_CAT", "FKTABLE_SCHEM", "FKTABLE_NAME", "FKCOLUMN_NAME", "KEY_SEQ", "UPDATE_RULE", "DELETE_RULE", "FK_NAME", "PK_NAME", "DEFERRABILITY"};
      DataType[] dataTypes = new DataType[]{DataType.VARCHAR, DataType.NULL, DataType.VARCHAR, DataType.VARCHAR, DataType.VARCHAR, DataType.NULL, DataType.VARCHAR, DataType.VARCHAR, DataType.SMALLINT, DataType.SMALLINT, DataType.SMALLINT, DataType.VARCHAR, DataType.VARCHAR, DataType.SMALLINT};
      String database = this.conf.useCatalogTerm() == CatalogTerm.UseCatalog ? catalog : schema;
      List<String[]> data = new ArrayList();
      java.sql.Statement stmt = this.connection.createStatement();
      List<String> databases = new ArrayList();
      ResultSet rs = stmt.executeQuery("SHOW DATABASES");

      while(rs.next()) {
         databases.add(rs.getString(1));
      }

      String ext = (database == null ? "" : quoteIdentifier(database) + ".") + quoteIdentifier(table);
      Map<String[], String> externalInfo = this.getExtImportedKeys(ext, this.connection);
      Iterator var13 = databases.iterator();

      while(true) {
         String db;
         do {
            do {
               do {
                  do {
                     if (!var13.hasNext()) {
                        String[][] arr = (String[][])data.toArray(new String[0][]);
                        Arrays.sort(arr, (row1, row2) -> {
                           int result = 0;
                           if (row1[4] != null) {
                              result = row1[4].compareTo(row2[4]);
                           }

                           if (result == 0) {
                              if (row1[5] != null) {
                                 result = row1[5].compareTo(row2[5]);
                              }

                              if (result == 0) {
                                 result = row1[6].compareTo(row2[6]);
                                 if (result == 0) {
                                    if (row1[12] != null) {
                                       result = row1[12].compareTo(row2[12]);
                                    }

                                    if (result == 0) {
                                       result = row1[8].length() - row2[8].length();
                                       if (result == 0) {
                                          result = row1[8].compareTo(row2[8]);
                                       }
                                    }
                                 }
                              }
                           }

                           return result;
                        });
                        return CompleteResult.createResultSet((String[])columnNames, (DataType[])dataTypes, arr, this.connection.getContext(), 2, 1004);
                     }

                     db = (String)var13.next();
                  } while(db.equalsIgnoreCase("sys"));
               } while(db.equalsIgnoreCase("information_schema"));
            } while(db.equalsIgnoreCase("mysql"));
         } while(db.equalsIgnoreCase("performance_schema"));

         List<String> tables = new ArrayList();
         ResultSet rsTables = stmt.executeQuery("SHOW TABLES FROM " + quoteIdentifier(db));

         while(rsTables.next()) {
            tables.add(rsTables.getString(1));
         }

         Iterator var17 = tables.iterator();

         while(var17.hasNext()) {
            String tableName = (String)var17.next();

            try {
               rs = stmt.executeQuery("SHOW CREATE TABLE " + quoteIdentifier(db) + "." + quoteIdentifier(tableName));
               rs.next();
               String tableDef = rs.getString(2);
               String[] parts = tableDef.split("\n");
               String[] var21 = parts;
               int var22 = parts.length;

               for(int var23 = 0; var23 < var22; ++var23) {
                  String part = var21[var23];
                  part = part.trim();
                  if (part.toUpperCase(Locale.ROOT).startsWith("CONSTRAINT") || part.toUpperCase(Locale.ROOT).contains("FOREIGN KEY")) {
                     char[] partChar = part.toCharArray();
                     DatabaseMetaData.Identifier constraintName = new DatabaseMetaData.Identifier();
                     int pos = skipKeyword(partChar, 0, "CONSTRAINT");
                     pos = parseIdentifier(partChar, pos, constraintName);
                     pos = skipKeyword(partChar, pos, "FOREIGN KEY");
                     List<DatabaseMetaData.Identifier> foreignKeyCols = new ArrayList();
                     pos = this.parseIdentifierList(partChar, pos, foreignKeyCols);
                     pos = skipKeyword(partChar, pos, "REFERENCES");
                     DatabaseMetaData.Identifier pkTable = new DatabaseMetaData.Identifier();
                     pos = parseIdentifier(partChar, pos, pkTable);
                     List<DatabaseMetaData.Identifier> primaryKeyCols = new ArrayList();
                     this.parseIdentifierList(partChar, pos, primaryKeyCols);
                     String pkTableSchema = pkTable.schema != null ? pkTable.schema : db;
                     if ((database == null || database.equals(pkTableSchema)) && pkTable.name.equals(table)) {
                        int onUpdateReferenceAction = 1;
                        int onDeleteReferenceAction = 1;
                        String[] var34 = new String[]{"RESTRICT", "CASCADE", "SET NULL", "NO ACTION", "SET DEFAULT"};
                        int var35 = var34.length;

                        for(int var36 = 0; var36 < var35; ++var36) {
                           String referenceAction = var34[var36];
                           if (part.toUpperCase(Locale.ROOT).contains("ON UPDATE " + referenceAction)) {
                              onUpdateReferenceAction = getImportedKeyAction(referenceAction);
                           }

                           if (part.toUpperCase(Locale.ROOT).contains("ON DELETE " + referenceAction)) {
                              onDeleteReferenceAction = getImportedKeyAction(referenceAction);
                           }
                        }

                        label177:
                        for(int i = 0; i < primaryKeyCols.size(); ++i) {
                           String[] row = new String[]{this.conf.useCatalogTerm() == CatalogTerm.UseCatalog ? (pkTable.schema == null ? database : pkTable.schema) : "def", this.conf.useCatalogTerm() == CatalogTerm.UseSchema ? (pkTable.schema == null ? database : pkTable.schema) : null, pkTable.name, ((DatabaseMetaData.Identifier)primaryKeyCols.get(i)).name, this.conf.useCatalogTerm() == CatalogTerm.UseCatalog ? database : "def", this.conf.useCatalogTerm() == CatalogTerm.UseSchema ? database : null, tableName, ((DatabaseMetaData.Identifier)foreignKeyCols.get(i)).name, Integer.toString(i + 1), Integer.toString(onUpdateReferenceAction), Integer.toString(onDeleteReferenceAction), constraintName.name, null, null};
                           Iterator var51 = externalInfo.entrySet().iterator();

                           while(true) {
                              boolean foundAll;
                              ArrayList pkKeysFound;
                              Entry entry;
                              do {
                                 if (!var51.hasNext()) {
                                    row[13] = Integer.toString(7);
                                    data.add(row);
                                    continue label177;
                                 }

                                 entry = (Entry)var51.next();
                                 foundAll = true;
                                 pkKeysFound = new ArrayList();
                                 String[] var40 = (String[])entry.getKey();
                                 int var41 = var40.length;

                                 for(int var42 = 0; var42 < var41; ++var42) {
                                    String keyPart = var40[var42];
                                    boolean foundKey = false;
                                    Iterator var45 = primaryKeyCols.iterator();

                                    while(var45.hasNext()) {
                                       DatabaseMetaData.Identifier keyCol = (DatabaseMetaData.Identifier)var45.next();
                                       if (keyCol.name.equals(keyPart)) {
                                          foundKey = true;
                                          pkKeysFound.add(keyCol);
                                          break;
                                       }
                                    }

                                    if (!foundKey) {
                                       foundAll = false;
                                       break;
                                    }
                                 }
                              } while(!foundAll && pkKeysFound.size() != primaryKeyCols.size());

                              row[12] = (String)entry.getValue();
                           }
                        }
                     }
                  }
               }
            } catch (SQLException var47) {
            }
         }
      }
   }

   private ResultSet getExportedKeysUsingInformationSchema(String catalog, String schema, String table) throws SQLException {
      StringBuilder sb = (new StringBuilder("SELECT ")).append(this.conf.useCatalogTerm() == CatalogTerm.UseCatalog ? "KCU.REFERENCED_TABLE_SCHEMA PKTABLE_CAT, NULL PKTABLE_SCHEM" : "KCU.CONSTRAINT_CATALOG PKTABLE_CAT, KCU.REFERENCED_TABLE_SCHEMA PKTABLE_SCHEM").append(",  KCU.REFERENCED_TABLE_NAME PKTABLE_NAME, KCU.REFERENCED_COLUMN_NAME PKCOLUMN_NAME, ").append(this.conf.useCatalogTerm() == CatalogTerm.UseCatalog ? "KCU.TABLE_SCHEMA FKTABLE_CAT, NULL FKTABLE_SCHEM" : " TABLE_CATALOG FKTABLE_CAT, KCU.TABLE_SCHEMA FKTABLE_SCHEM").append(", KCU.TABLE_NAME FKTABLE_NAME, KCU.COLUMN_NAME FKCOLUMN_NAME, KCU.POSITION_IN_UNIQUE_CONSTRAINT KEY_SEQ, CASE update_rule    WHEN 'RESTRICT' THEN 1   WHEN 'NO ACTION' THEN 3   WHEN 'CASCADE' THEN 0   WHEN 'SET NULL' THEN 2   WHEN 'SET DEFAULT' THEN 4 END UPDATE_RULE, CASE DELETE_RULE  WHEN 'RESTRICT' THEN 1  WHEN 'NO ACTION' THEN 3  WHEN 'CASCADE' THEN 0  WHEN 'SET NULL' THEN 2  WHEN 'SET DEFAULT' THEN 4 END DELETE_RULE, RC.CONSTRAINT_NAME FK_NAME, RC.UNIQUE_CONSTRAINT_NAME PK_NAME,7 DEFERRABILITY FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE KCU INNER JOIN INFORMATION_SCHEMA.REFERENTIAL_CONSTRAINTS RC ON KCU.CONSTRAINT_CATALOG = RC.CONSTRAINT_CATALOG AND KCU.CONSTRAINT_SCHEMA = RC.CONSTRAINT_SCHEMA AND KCU.CONSTRAINT_NAME = RC.CONSTRAINT_NAME AND KCU.TABLE_NAME = RC.TABLE_NAME ");
      String database = this.conf.useCatalogTerm() == CatalogTerm.UseCatalog ? catalog : schema;
      boolean firstCondition = this.databaseCond(true, sb, "KCU.REFERENCED_TABLE_SCHEMA", database, this.conf.useCatalogTerm() == CatalogTerm.UseSchema);
      sb.append(firstCondition ? " WHERE " : " AND ").append("KCU.REFERENCED_TABLE_NAME = ").append(this.escapeQuote(table));
      sb.append(" ORDER BY FKTABLE_CAT, FKTABLE_SCHEM, FKTABLE_NAME, PK_NAME, KEY_SEQ");
      return this.executeQuery(sb.toString());
   }

   public ResultSet getImportedKeysUsingInformationSchema(String database, String table) throws SQLException {
      StringBuilder sb = (new StringBuilder("SELECT ")).append(this.conf.useCatalogTerm() == CatalogTerm.UseCatalog ? "KCU.REFERENCED_TABLE_SCHEMA PKTABLE_CAT, NULL PKTABLE_SCHEM" : "KCU.TABLE_CATALOG PKTABLE_CAT, KCU.REFERENCED_TABLE_SCHEMA PKTABLE_SCHEM").append(",  KCU.REFERENCED_TABLE_NAME PKTABLE_NAME, KCU.REFERENCED_COLUMN_NAME PKCOLUMN_NAME, ").append(this.conf.useCatalogTerm() == CatalogTerm.UseCatalog ? "KCU.TABLE_SCHEMA FKTABLE_CAT, NULL FKTABLE_SCHEM" : "KCU.TABLE_CATALOG FKTABLE_CAT, KCU.TABLE_SCHEMA FKTABLE_SCHEM").append(", KCU.TABLE_NAME FKTABLE_NAME, KCU.COLUMN_NAME FKCOLUMN_NAME, KCU.POSITION_IN_UNIQUE_CONSTRAINT KEY_SEQ, CASE update_rule    WHEN 'RESTRICT' THEN 1   WHEN 'NO ACTION' THEN 3   WHEN 'CASCADE' THEN 0   WHEN 'SET NULL' THEN 2   WHEN 'SET DEFAULT' THEN 4 END UPDATE_RULE, CASE DELETE_RULE  WHEN 'RESTRICT' THEN 1  WHEN 'NO ACTION' THEN 3  WHEN 'CASCADE' THEN 0  WHEN 'SET NULL' THEN 2  WHEN 'SET DEFAULT' THEN 4 END DELETE_RULE, RC.CONSTRAINT_NAME FK_NAME, RC.UNIQUE_CONSTRAINT_NAME PK_NAME,7 DEFERRABILITY FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE KCU INNER JOIN INFORMATION_SCHEMA.REFERENTIAL_CONSTRAINTS RC ON KCU.CONSTRAINT_CATALOG = RC.CONSTRAINT_CATALOG AND KCU.CONSTRAINT_SCHEMA = RC.CONSTRAINT_SCHEMA AND KCU.CONSTRAINT_NAME = RC.CONSTRAINT_NAME AND KCU.TABLE_NAME = RC.TABLE_NAME ");
      boolean firstCondition = this.databaseCond(true, sb, "KCU.TABLE_SCHEMA", database, false);
      sb.append(firstCondition ? " WHERE " : " AND ").append("KCU.TABLE_NAME = ").append(this.escapeQuote(table));
      sb.append(" ORDER BY PKTABLE_CAT, PKTABLE_SCHEM, PKTABLE_NAME, KEY_SEQ");
      return this.executeQuery(sb.toString());
   }

   public ResultSet getImportedKeysUsingShowCreateTable(String database, String table) throws Exception {
      boolean importedKeysWithConstraintNames = Boolean.parseBoolean(this.conf.nonMappedOptions().getProperty("importedKeysWithConstraintNames", "true"));
      String[] columnNames = new String[]{"PKTABLE_CAT", "PKTABLE_SCHEM", "PKTABLE_NAME", "PKCOLUMN_NAME", "FKTABLE_CAT", "FKTABLE_SCHEM", "FKTABLE_NAME", "FKCOLUMN_NAME", "KEY_SEQ", "UPDATE_RULE", "DELETE_RULE", "FK_NAME", "PK_NAME", "DEFERRABILITY"};
      DataType[] dataTypes = new DataType[]{DataType.VARCHAR, DataType.NULL, DataType.VARCHAR, DataType.VARCHAR, DataType.VARCHAR, DataType.NULL, DataType.VARCHAR, DataType.VARCHAR, DataType.SMALLINT, DataType.SMALLINT, DataType.SMALLINT, DataType.VARCHAR, DataType.VARCHAR, DataType.SMALLINT};
      List<String[]> data = new ArrayList();
      java.sql.Statement stmt = this.connection.createStatement();
      if (database != null) {
         ResultSet rs = stmt.executeQuery("SHOW CREATE TABLE " + quoteIdentifier(database) + "." + quoteIdentifier(table));
         rs.next();
         String tableDef = rs.getString(2);
         this.parseShowCreateTable(tableDef, database, table, importedKeysWithConstraintNames, data);
      } else {
         List<String> databases = new ArrayList();
         ResultSet rs = stmt.executeQuery("SHOW DATABASES");

         while(rs.next()) {
            databases.add(rs.getString(1));
         }

         Iterator var10 = databases.iterator();

         while(var10.hasNext()) {
            String db = (String)var10.next();
            if (!db.equalsIgnoreCase("sys") && !db.equalsIgnoreCase("information_schema") && !db.equalsIgnoreCase("mysql") && !db.equalsIgnoreCase("performance_schema")) {
               try {
                  rs = stmt.executeQuery("SHOW CREATE TABLE " + quoteIdentifier(db) + "." + quoteIdentifier(table));
                  rs.next();
                  String tableDef = rs.getString(2);
                  this.parseShowCreateTable(tableDef, db, table, importedKeysWithConstraintNames, data);
               } catch (SQLException var13) {
               }
            }
         }
      }

      String[][] arr = (String[][])data.toArray(new String[0][]);
      Arrays.sort(arr, (row1, row2) -> {
         int result = 0;
         if (row1[0] != null) {
            result = row1[0].compareTo(row2[0]);
         }

         if (result == 0) {
            if (row1[1] != null) {
               result = row1[1].compareTo(row2[1]);
            }

            if (result == 0) {
               result = row1[2].compareTo(row2[2]);
               if (result == 0) {
                  if (row1[12] != null) {
                     result = row1[12].compareTo(row2[12]);
                  }

                  if (result == 0) {
                     result = row1[8].length() - row2[8].length();
                     if (result == 0) {
                        result = row1[8].compareTo(row2[8]);
                     }
                  }
               }
            }
         }

         return result;
      });
      return CompleteResult.createResultSet((String[])columnNames, (DataType[])dataTypes, arr, this.connection.getContext(), 2, 1004);
   }

   public ResultSet getBestRowIdentifier(String catalog, String schema, String table, int scope, boolean nullable) throws SQLException {
      if (table == null) {
         throw new SQLException("'table' parameter cannot be null in getBestRowIdentifier()");
      } else {
         boolean hasIsGeneratedCol = this.connection.getContext().getVersion().isMariaDBServer() && this.connection.getContext().getVersion().versionGreaterOrEqual(10, 2, 0);
         StringBuilder sbInner = new StringBuilder("SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE COLUMN_KEY = 'PRI'");
         String database = this.conf.useCatalogTerm() == CatalogTerm.UseCatalog ? catalog : schema;
         this.databaseCond(false, sbInner, "TABLE_SCHEMA", database, false);
         sbInner.append(" AND TABLE_NAME = ").append(this.escapeQuote(table));
         StringBuilder sb = new StringBuilder("SELECT 2 SCOPE, COLUMN_NAME," + this.dataTypeClause("COLUMN_TYPE") + " DATA_TYPE, DATA_TYPE TYPE_NAME, IF(NUMERIC_PRECISION IS NULL, CHARACTER_MAXIMUM_LENGTH, NUMERIC_PRECISION) COLUMN_SIZE, 0 BUFFER_LENGTH, NUMERIC_SCALE DECIMAL_DIGITS," + (hasIsGeneratedCol ? "IF(IS_GENERATED='NEVER',1,2)" : 1) + " PSEUDO_COLUMN FROM INFORMATION_SCHEMA.COLUMNS WHERE (COLUMN_KEY  = 'PRI' OR (COLUMN_KEY = 'UNI' AND NOT EXISTS (" + sbInner + " )))");
         this.databaseCond(false, sb, "TABLE_SCHEMA", database, false);
         sb.append(" AND TABLE_NAME = ").append(this.escapeQuote(table));
         if (!nullable) {
            sb.append(" AND IS_NULLABLE = 'NO'");
         }

         return this.executeQuery(sb.toString());
      }
   }

   public boolean generatedKeyAlwaysReturned() {
      return true;
   }

   public ResultSet getPseudoColumns(String catalog, String schemaPattern, String tableNamePattern, String columnNamePattern) throws SQLException {
      return this.connection.createStatement().executeQuery("SELECT ' ' TABLE_CAT, ' ' TABLE_SCHEM, ' ' TABLE_NAME, ' ' COLUMN_NAME, 0 DATA_TYPE, 0 COLUMN_SIZE, 0 DECIMAL_DIGITS, 10 NUM_PREC_RADIX, ' ' COLUMN_USAGE,  ' ' REMARKS, 0 CHAR_OCTET_LENGTH, 'YES' IS_NULLABLE FROM DUAL WHERE 1=0");
   }

   public boolean allProceduresAreCallable() {
      return true;
   }

   public boolean allTablesAreSelectable() {
      return true;
   }

   public String getURL() {
      return this.conf.initialUrl();
   }

   public String getUserName() {
      return this.conf.user();
   }

   public boolean isReadOnly() throws SQLException {
      java.sql.Statement st = this.connection.createStatement();
      ResultSet rs = st.executeQuery("SELECT @@READ_ONLY");
      rs.next();
      String readOnly = rs.getString(1);
      return "ON".equals(readOnly) || "1".equals(readOnly);
   }

   public boolean nullsAreSortedHigh() {
      return false;
   }

   public boolean nullsAreSortedLow() {
      return true;
   }

   public boolean nullsAreSortedAtStart() {
      return false;
   }

   public boolean nullsAreSortedAtEnd() {
      return true;
   }

   public String getDatabaseProductName() {
      return !this.conf.useMysqlMetadata() && this.connection.getContext().getVersion().isMariaDBServer() ? "MariaDB" : "MySQL";
   }

   public String getDatabaseProductVersion() {
      return this.connection.getContext().getVersion().getVersion();
   }

   public String getDriverName() {
      return "MariaDB Connector/J";
   }

   public String getDriverVersion() {
      return VersionFactory.getInstance().getVersion();
   }

   public int getDriverMajorVersion() {
      return VersionFactory.getInstance().getMajorVersion();
   }

   public int getDriverMinorVersion() {
      return VersionFactory.getInstance().getMinorVersion();
   }

   public boolean usesLocalFiles() {
      return false;
   }

   public boolean usesLocalFilePerTable() {
      return false;
   }

   public boolean supportsMixedCaseIdentifiers() throws SQLException {
      return this.connection.getLowercaseTableNames() == 0;
   }

   public boolean storesUpperCaseIdentifiers() {
      return false;
   }

   public boolean storesLowerCaseIdentifiers() throws SQLException {
      return this.connection.getLowercaseTableNames() == 1;
   }

   public boolean storesMixedCaseIdentifiers() throws SQLException {
      return this.connection.getLowercaseTableNames() == 2;
   }

   public boolean supportsMixedCaseQuotedIdentifiers() throws SQLException {
      return this.supportsMixedCaseIdentifiers();
   }

   public boolean storesUpperCaseQuotedIdentifiers() {
      return this.storesUpperCaseIdentifiers();
   }

   public boolean storesLowerCaseQuotedIdentifiers() throws SQLException {
      return this.storesLowerCaseIdentifiers();
   }

   public boolean storesMixedCaseQuotedIdentifiers() throws SQLException {
      return this.storesMixedCaseIdentifiers();
   }

   public String getIdentifierQuoteString() {
      return "`";
   }

   public String getSQLKeywords() {
      return "ACCESSIBLE,ADD,ALL,ALTER,ANALYZE,AND,AS,ASC,ASENSITIVE,BEFORE,BETWEEN,BIGINT,BINARY,BLOB,BOTH,BY,CALL,CASCADE,CASE,CHANGE,CHAR,CHARACTER,CHECK,COLLATE,COLUMN,CONDITION,CONSTRAINT,CONTINUE,CONVERT,CREATE,CROSS,CURRENT_DATE,CURRENT_ROLE,CURRENT_TIME,CURRENT_TIMESTAMP,CURRENT_USER,CURSOR,DATABASE,DATABASES,DAY_HOUR,DAY_MICROSECOND,DAY_MINUTE,DAY_SECOND,DEC,DECIMAL,DECLARE,DEFAULT,DELAYED,DELETE,DELETE_DOMAIN_ID,DESC,DESCRIBE,DETERMINISTIC,DISTINCT,DISTINCTROW,DIV,DO_DOMAIN_IDS,DOUBLE,DROP,DUAL,EACH,ELSE,ELSEIF,ENCLOSEDESCAPED,EXCEPTEXISTS,EXIT,EXPLAIN,FALSE,FETCH,FLOAT,FLOAT4,FLOAT8,FOR,FORCE,FOREIGN,FROMFULLTEXT,GENERAL,GRANT,GROUP,HAVING,HIGH_PRIORITY,HOUR_MICROSECOND,HOUR_MINUTE,HOUR_SECOND,IF,IGNORE,IGNORE_DOMAIN_IDS,IGNORE_SERVER_IDS,IN,INDEX,INFILE,INNER,INOUT,INSENSITIVE,INSERT,INT,INT1,INT2,INT3,INT4,INT8,INTEGER,INTERSECTINTERVAL,INTO,IS,ITERATE,JOIN,KEY,KEYS,KILL,LEADING,LEAVE,LEFT,LIKE,LIMIT,LINEAR,LINES,LOAD,LOCALTIME,LOCALTIMESTAMP,LOCK,LONG,LONGBLOB,LONGTEXT,LOOP,LOW_PRIORITY,MASTER_HEARTBEAT_PERIOD,MASTER_SSL_VERIFY_SERVER_CERT,MATCH,MAXVALUE,MEDIUMBLOB,MEDIUMINT,MEDIUMTEXT,MIDDLEINT,MINUTE_MICROSECOND,MINUTE_SECOND,MOD,MODIFIES,NATURAL,NOT,NO_WRITE_TO_BINLOG,NULL,NUMERIC,OFFSETON,OPTIMIZE,OPTION,OPTIONALLY,OR,ORDER,OUT,OUTER,OUTFILE,OVER,PAGE_CHECKSUM,PARSE_VCOL_EXPR,PARTITION,PRECISION,PRIMARY,PROCEDURE,PURGE,RANGE,READ,READS,READ_WRITE,REALRECURSIVE,REF_SYSTEM_ID,REFERENCES,REGEXP,RELEASE,RENAME,REPEAT,REPLACE,REQUIRE,RESIGNAL,RESTRICT,RETURN,RETURNING,REVOKE,RIGHT,RLIKE,ROW_NUMBERROWS,SCHEMA,SCHEMAS,SECOND_MICROSECOND,SELECT,SENSITIVE,SEPARATOR,SET,SHOW,SIGNAL,SLOW,SMALLINT,SPATIAL,SPECIFIC,SQL,SQLEXCEPTION,SQLSTATE,SQLWARNING,SQL_BIG_RESULT,SQL_CALC_FOUND_ROWS,SQL_SMALL_RESULT,SSL,STARTING,STATS_AUTO_RECALC,STATS_PERSISTENT,STATS_SAMPLE_PAGES,STRAIGHT_JOIN,TABLE,TERMINATED,THEN,TINYBLOB,TINYINT,TINYTEXT,TO,TRAILING,TRIGGER,TRUE,UNDO,UNION,UNIQUE,UNLOCK,UNSIGNED,UPDATE,USAGE,USE,USING,UTC_DATE,UTC_TIME,UTC_TIMESTAMP,VALUES,VARBINARY,VARCHAR,VARCHARACTER,VARYING,WHEN,WHERE,WHILE,WINDOWWITH,WRITE,XOR,YEAR_MONTH,ZEROFILL";
   }

   public String getNumericFunctions() {
      return "DIV,ABS,ACOS,ASIN,ATAN,ATAN2,CEIL,CEILING,CONV,COS,COT,CRC32,DEGREES,EXP,FLOOR,GREATEST,LEAST,LN,LOG,LOG10,LOG2,MOD,OCT,PI,POW,POWER,RADIANS,RAND,ROUND,SIGN,SIN,SQRT,TAN,TRUNCATE";
   }

   public String getStringFunctions() {
      return "ASCII,BIN,BIT_LENGTH,CAST,CHARACTER_LENGTH,CHAR_LENGTH,CONCAT,CONCAT_WS,CONVERT,ELT,EXPORT_SET,EXTRACTVALUE,FIELD,FIND_IN_SET,FORMAT,FROM_BASE64,HEX,INSTR,LCASE,LEFT,LENGTH,LIKE,LOAD_FILE,LOCATE,LOWER,LPAD,LTRIM,MAKE_SET,MATCH AGAINST,MID,NOT LIKE,NOT REGEXP,OCTET_LENGTH,ORD,POSITION,QUOTE,REPEAT,REPLACE,REVERSE,RIGHT,RPAD,RTRIM,SOUNDEX,SOUNDS LIKE,SPACE,STRCMP,SUBSTR,SUBSTRING,SUBSTRING_INDEX,TO_BASE64,TRIM,UCASE,UNHEX,UPDATEXML,UPPER,WEIGHT_STRING";
   }

   public String getSystemFunctions() {
      return "DATABASE,USER,SYSTEM_USER,SESSION_USER,LAST_INSERT_ID,VERSION";
   }

   public String getTimeDateFunctions() {
      return "ADDDATE,ADDTIME,CONVERT_TZ,CURDATE,CURRENT_DATE,CURRENT_TIME,CURRENT_TIMESTAMP,CURTIME,DATEDIFF,DATE_ADD,DATE_FORMAT,DATE_SUB,DAY,DAYNAME,DAYOFMONTH,DAYOFWEEK,DAYOFYEAR,EXTRACT,FROM_DAYS,FROM_UNIXTIME,GET_FORMAT,HOUR,LAST_DAY,LOCALTIME,LOCALTIMESTAMP,MAKEDATE,MAKETIME,MICROSECOND,MINUTE,MONTH,MONTHNAME,NOW,PERIOD_ADD,PERIOD_DIFF,QUARTER,SECOND,SEC_TO_TIME,STR_TO_DATE,SUBDATE,SUBTIME,SYSDATE,TIMEDIFF,TIMESTAMPADD,TIMESTAMPDIFF,TIME_FORMAT,TIME_TO_SEC,TO_DAYS,TO_SECONDS,UNIX_TIMESTAMP,UTC_DATE,UTC_TIME,UTC_TIMESTAMP,WEEK,WEEKDAY,WEEKOFYEAR,YEAR,YEARWEEK";
   }

   public String getSearchStringEscape() {
      return "\\";
   }

   public String getExtraNameCharacters() {
      return "#@";
   }

   public boolean supportsAlterTableWithAddColumn() {
      return true;
   }

   public boolean supportsAlterTableWithDropColumn() {
      return true;
   }

   public boolean supportsColumnAliasing() {
      return true;
   }

   public boolean nullPlusNonNullIsNull() {
      return true;
   }

   public boolean supportsConvert() {
      return true;
   }

   public boolean supportsConvert(int fromType, int toType) {
      switch(fromType) {
      case -7:
      case -6:
      case -5:
      case 2:
      case 3:
      case 4:
      case 5:
      case 6:
      case 7:
      case 8:
      case 16:
         switch(toType) {
         case -7:
         case -6:
         case -5:
         case -4:
         case -3:
         case -2:
         case -1:
         case 1:
         case 2:
         case 3:
         case 4:
         case 5:
         case 6:
         case 7:
         case 8:
         case 12:
         case 16:
            return true;
         case 0:
         case 9:
         case 10:
         case 11:
         case 13:
         case 14:
         case 15:
         default:
            return false;
         }
      case -4:
      case -3:
      case -2:
      case -1:
      case 1:
      case 12:
      case 2005:
         switch(toType) {
         case -16:
         case -15:
         case -7:
         case -6:
         case -5:
         case -4:
         case -3:
         case -2:
         case -1:
         case 1:
         case 2:
         case 3:
         case 4:
         case 5:
         case 6:
         case 7:
         case 8:
         case 12:
         case 16:
         case 91:
         case 92:
         case 93:
         case 2004:
         case 2005:
         case 2011:
            return true;
         default:
            return false;
         }
      case 91:
         switch(toType) {
         case -4:
         case -3:
         case -2:
         case -1:
         case 1:
         case 12:
         case 91:
            return true;
         default:
            return false;
         }
      case 92:
         switch(toType) {
         case -4:
         case -3:
         case -2:
         case -1:
         case 1:
         case 12:
         case 92:
            return true;
         default:
            return false;
         }
      case 93:
         switch(toType) {
         case -4:
         case -3:
         case -2:
         case -1:
         case 1:
         case 12:
         case 91:
         case 92:
         case 93:
            return true;
         default:
            return false;
         }
      case 2004:
         switch(toType) {
         case -7:
         case -6:
         case -5:
         case -4:
         case -3:
         case -2:
         case -1:
         case 1:
         case 2:
         case 3:
         case 4:
         case 5:
         case 6:
         case 7:
         case 8:
         case 12:
         case 16:
            return true;
         case 0:
         case 9:
         case 10:
         case 11:
         case 13:
         case 14:
         case 15:
         default:
            return false;
         }
      default:
         return false;
      }
   }

   public boolean supportsTableCorrelationNames() {
      return true;
   }

   public boolean supportsDifferentTableCorrelationNames() {
      return true;
   }

   public boolean supportsExpressionsInOrderBy() {
      return true;
   }

   public boolean supportsOrderByUnrelated() {
      return true;
   }

   public boolean supportsGroupBy() {
      return true;
   }

   public boolean supportsGroupByUnrelated() {
      return true;
   }

   public boolean supportsGroupByBeyondSelect() {
      return true;
   }

   public boolean supportsLikeEscapeClause() {
      return true;
   }

   public boolean supportsMultipleResultSets() {
      return true;
   }

   public boolean supportsMultipleTransactions() {
      return true;
   }

   public boolean supportsNonNullableColumns() {
      return true;
   }

   public boolean supportsMinimumSQLGrammar() {
      return true;
   }

   public boolean supportsCoreSQLGrammar() {
      return true;
   }

   public boolean supportsExtendedSQLGrammar() {
      return true;
   }

   public boolean supportsANSI92EntryLevelSQL() {
      return true;
   }

   public boolean supportsANSI92IntermediateSQL() {
      return true;
   }

   public boolean supportsANSI92FullSQL() {
      return true;
   }

   public boolean supportsIntegrityEnhancementFacility() {
      return true;
   }

   public boolean supportsOuterJoins() {
      return true;
   }

   public boolean supportsFullOuterJoins() {
      return true;
   }

   public boolean supportsLimitedOuterJoins() {
      return true;
   }

   public String getSchemaTerm() {
      return "schema";
   }

   public String getProcedureTerm() {
      return "procedure";
   }

   public String getCatalogTerm() {
      return "database";
   }

   public boolean isCatalogAtStart() {
      return true;
   }

   public String getCatalogSeparator() {
      return ".";
   }

   public boolean supportsSchemasInDataManipulation() {
      return false;
   }

   public boolean supportsSchemasInProcedureCalls() {
      return false;
   }

   public boolean supportsSchemasInTableDefinitions() {
      return false;
   }

   public boolean supportsSchemasInIndexDefinitions() {
      return false;
   }

   public boolean supportsSchemasInPrivilegeDefinitions() {
      return false;
   }

   public boolean supportsCatalogsInDataManipulation() {
      return true;
   }

   public boolean supportsCatalogsInProcedureCalls() {
      return true;
   }

   public boolean supportsCatalogsInTableDefinitions() {
      return true;
   }

   public boolean supportsCatalogsInIndexDefinitions() {
      return true;
   }

   public boolean supportsCatalogsInPrivilegeDefinitions() {
      return true;
   }

   public boolean supportsPositionedDelete() {
      return false;
   }

   public boolean supportsPositionedUpdate() {
      return false;
   }

   public boolean supportsSelectForUpdate() {
      return true;
   }

   public boolean supportsStoredProcedures() {
      return true;
   }

   public boolean supportsSubqueriesInComparisons() {
      return true;
   }

   public boolean supportsSubqueriesInExists() {
      return true;
   }

   public boolean supportsSubqueriesInIns() {
      return true;
   }

   public boolean supportsSubqueriesInQuantifieds() {
      return true;
   }

   public boolean supportsCorrelatedSubqueries() {
      return true;
   }

   public boolean supportsUnion() {
      return true;
   }

   public boolean supportsUnionAll() {
      return true;
   }

   public boolean supportsOpenCursorsAcrossCommit() {
      return true;
   }

   public boolean supportsOpenCursorsAcrossRollback() {
      return true;
   }

   public boolean supportsOpenStatementsAcrossCommit() {
      return true;
   }

   public boolean supportsOpenStatementsAcrossRollback() {
      return true;
   }

   public int getMaxBinaryLiteralLength() {
      return Integer.MAX_VALUE;
   }

   public int getMaxCharLiteralLength() {
      return Integer.MAX_VALUE;
   }

   public int getMaxColumnNameLength() {
      return 64;
   }

   public int getMaxColumnsInGroupBy() {
      return 64;
   }

   public int getMaxColumnsInIndex() {
      return 16;
   }

   public int getMaxColumnsInOrderBy() {
      return 64;
   }

   public int getMaxColumnsInSelect() {
      return 32767;
   }

   public int getMaxColumnsInTable() {
      return 0;
   }

   public int getMaxConnections() {
      return 0;
   }

   public int getMaxCursorNameLength() {
      return 0;
   }

   public int getMaxIndexLength() {
      return 256;
   }

   public int getMaxSchemaNameLength() {
      return 0;
   }

   public int getMaxProcedureNameLength() {
      return 64;
   }

   public int getMaxCatalogNameLength() {
      return 0;
   }

   public int getMaxRowSize() {
      return 0;
   }

   public boolean doesMaxRowSizeIncludeBlobs() {
      return false;
   }

   public int getMaxStatementLength() {
      return 0;
   }

   public int getMaxStatements() {
      return 0;
   }

   public int getMaxTableNameLength() {
      return 64;
   }

   public int getMaxTablesInSelect() {
      return 256;
   }

   public int getMaxUserNameLength() {
      return 0;
   }

   public int getDefaultTransactionIsolation() {
      return 4;
   }

   public boolean supportsTransactions() {
      return true;
   }

   public boolean supportsTransactionIsolationLevel(int level) {
      switch(level) {
      case 1:
      case 2:
      case 4:
      case 8:
         return true;
      case 3:
      case 5:
      case 6:
      case 7:
      default:
         return false;
      }
   }

   public boolean supportsDataDefinitionAndDataManipulationTransactions() {
      return true;
   }

   public boolean supportsDataManipulationTransactionsOnly() {
      return false;
   }

   public boolean dataDefinitionCausesTransactionCommit() {
      return true;
   }

   public boolean dataDefinitionIgnoredInTransactions() {
      return false;
   }

   public ResultSet getProcedures(String catalog, String schemaPattern, String procedureNamePattern) throws SQLException {
      StringBuilder sb = (new StringBuilder("SELECT ")).append(this.conf.useCatalogTerm() == CatalogTerm.UseCatalog ? "ROUTINE_SCHEMA PROCEDURE_CAT, NULL PROCEDURE_SCHEM" : "ROUTINE_CATALOG PROCEDURE_CAT, ROUTINE_SCHEMA PROCEDURE_SCHEM").append(", ROUTINE_NAME PROCEDURE_NAME, NULL RESERVED1, NULL RESERVED2, NULL RESERVED3, ROUTINE_COMMENT REMARKS, CASE ROUTINE_TYPE   WHEN 'FUNCTION' THEN 2  WHEN 'PROCEDURE' THEN 1  ELSE 0 END PROCEDURE_TYPE, SPECIFIC_NAME  FROM INFORMATION_SCHEMA.ROUTINES ");
      String database = this.conf.useCatalogTerm() == CatalogTerm.UseCatalog ? catalog : schemaPattern;
      boolean firstCondition = this.databaseCond(true, sb, "ROUTINE_SCHEMA", database, this.conf.useCatalogTerm() == CatalogTerm.UseSchema);
      this.patternCond(firstCondition, sb, "ROUTINE_NAME", procedureNamePattern);
      return this.executeQuery(sb.toString());
   }

   public ResultSet getProcedureColumns(String catalog, String schemaPattern, String procedureNamePattern, String columnNamePattern) throws SQLException {
      StringBuilder sb = new StringBuilder("SELECT SPECIFIC_SCHEMA PROCEDURE_CAT, NULL PROCEDURE_SCHEM, SPECIFIC_NAME PROCEDURE_NAME, PARAMETER_NAME COLUMN_NAME,  CASE PARAMETER_MODE   WHEN 'IN' THEN 1  WHEN 'OUT' THEN 4  WHEN 'INOUT' THEN 2  ELSE IF(PARAMETER_MODE IS NULL,5,0) END COLUMN_TYPE," + this.dataTypeClause("DTD_IDENTIFIER") + " DATA_TYPE,DATA_TYPE TYPE_NAME, CASE DATA_TYPE  WHEN 'time' THEN IF(DATETIME_PRECISION = 0, 10, CAST(11 + DATETIME_PRECISION as signed integer))  WHEN 'date' THEN 10  WHEN 'datetime' THEN IF(DATETIME_PRECISION = 0, 19, CAST(20 + DATETIME_PRECISION as signed integer))  WHEN 'timestamp' THEN IF(DATETIME_PRECISION = 0, 19, CAST(20 + DATETIME_PRECISION as signed integer))  ELSE   IF(NUMERIC_PRECISION IS NULL, LEAST(CHARACTER_MAXIMUM_LENGTH," + Integer.MAX_VALUE + "), NUMERIC_PRECISION)  END `PRECISION`, CASE DATA_TYPE  WHEN 'time' THEN IF(DATETIME_PRECISION = 0, 10, CAST(11 + DATETIME_PRECISION as signed integer))  WHEN 'date' THEN 10  WHEN 'datetime' THEN IF(DATETIME_PRECISION = 0, 19, CAST(20 + DATETIME_PRECISION as signed integer))  WHEN 'timestamp' THEN IF(DATETIME_PRECISION = 0, 19, CAST(20 + DATETIME_PRECISION as signed integer))  ELSE   IF(NUMERIC_PRECISION IS NULL, LEAST(CHARACTER_MAXIMUM_LENGTH," + Integer.MAX_VALUE + "), NUMERIC_PRECISION)  END `LENGTH`, CASE DATA_TYPE  WHEN 'time' THEN CAST(DATETIME_PRECISION as signed integer)  WHEN 'datetime' THEN CAST(DATETIME_PRECISION as signed integer)  WHEN 'timestamp' THEN CAST(DATETIME_PRECISION as signed integer)  ELSE NUMERIC_SCALE  END `SCALE`,10 RADIX," + 2 + " NULLABLE,NULL REMARKS,NULL COLUMN_DEF,0 SQL_DATA_TYPE,0 SQL_DATETIME_SUB,CHARACTER_OCTET_LENGTH CHAR_OCTET_LENGTH ,ORDINAL_POSITION, '' IS_NULLABLE, SPECIFIC_NAME  FROM INFORMATION_SCHEMA.PARAMETERS");
      String database = this.conf.useCatalogTerm() == CatalogTerm.UseCatalog ? catalog : schemaPattern;
      boolean firstCondition = this.databaseCond(true, sb, "SPECIFIC_SCHEMA", database, this.conf.useCatalogTerm() == CatalogTerm.UseCatalog);
      firstCondition = this.patternCond(firstCondition, sb, "SPECIFIC_NAME", procedureNamePattern);
      this.patternCond(firstCondition, sb, "PARAMETER_NAME", columnNamePattern);
      sb.append(" ORDER BY SPECIFIC_SCHEMA, SPECIFIC_NAME, ORDINAL_POSITION");
      return this.executeQuery(sb.toString());
   }

   public ResultSet getFunctionColumns(String catalog, String schemaPattern, String functionNamePattern, String columnNamePattern) throws SQLException {
      StringBuilder sb = (new StringBuilder("SELECT ")).append(this.conf.useCatalogTerm() == CatalogTerm.UseCatalog ? "SPECIFIC_SCHEMA `FUNCTION_CAT`, NULL `FUNCTION_SCHEM`" : "SPECIFIC_CATALOG `FUNCTION_CAT`, SPECIFIC_SCHEMA `FUNCTION_SCHEM`").append(", SPECIFIC_NAME FUNCTION_NAME, PARAMETER_NAME COLUMN_NAME,  CASE PARAMETER_MODE   WHEN 'IN' THEN 1  WHEN 'OUT' THEN 3  WHEN 'INOUT' THEN 2  ELSE 4 END COLUMN_TYPE,").append(this.dataTypeClause("DTD_IDENTIFIER")).append(" DATA_TYPE,DATA_TYPE TYPE_NAME,NUMERIC_PRECISION `PRECISION`,CHARACTER_MAXIMUM_LENGTH LENGTH,NUMERIC_SCALE SCALE,10 RADIX,2 NULLABLE,NULL REMARKS,CHARACTER_OCTET_LENGTH CHAR_OCTET_LENGTH ,ORDINAL_POSITION, '' IS_NULLABLE, SPECIFIC_NAME  FROM INFORMATION_SCHEMA.PARAMETERS");
      String database = this.conf.useCatalogTerm() == CatalogTerm.UseCatalog ? catalog : schemaPattern;
      boolean firstCondition = this.databaseCond(true, sb, "SPECIFIC_SCHEMA", database, this.conf.useCatalogTerm() == CatalogTerm.UseSchema);
      firstCondition = this.patternCond(firstCondition, sb, "SPECIFIC_NAME", functionNamePattern);
      firstCondition = this.patternCond(firstCondition, sb, "PARAMETER_NAME", columnNamePattern);
      sb.append(firstCondition ? " WHERE " : " AND ").append(" ROUTINE_TYPE='FUNCTION' ORDER BY FUNCTION_CAT, SPECIFIC_NAME, ORDINAL_POSITION");
      return this.executeQuery(sb.toString());
   }

   public ResultSet getSchemas() throws SQLException {
      return this.conf.useCatalogTerm() == CatalogTerm.UseSchema ? this.executeQuery("SELECT SCHEMA_NAME as TABLE_SCHEM, CATALOG_NAME as TABLE_CATALOG FROM information_schema.SCHEMATA ORDER BY TABLE_CATALOG, SCHEMA_NAME") : this.executeQuery("SELECT '' TABLE_SCHEM, '' TABLE_CATALOG  FROM DUAL WHERE 1=0");
   }

   public ResultSet getSchemas(String catalog, String schemaPattern) throws SQLException {
      if (this.conf.useCatalogTerm() == CatalogTerm.UseSchema) {
         StringBuilder sb = new StringBuilder("SELECT SCHEMA_NAME as TABLE_SCHEM, CATALOG_NAME as TABLE_CATALOG FROM information_schema.SCHEMATA ");
         this.databaseCond(true, sb, "SCHEMA_NAME", schemaPattern, true);
         sb.append(" ORDER BY SCHEMA_NAME");
         return this.executeQuery(sb.toString());
      } else {
         return this.executeQuery("SELECT  '' TABLE_SCHEM, '' TABLE_CATALOG FROM DUAL WHERE 1=0");
      }
   }

   public ResultSet getCatalogs() throws SQLException {
      return this.conf.useCatalogTerm() == CatalogTerm.UseSchema ? this.executeQuery("SELECT null TABLE_CAT FROM DUAL WHERE 1=0") : this.executeQuery("SELECT SCHEMA_NAME TABLE_CAT FROM INFORMATION_SCHEMA.SCHEMATA  ORDER BY SCHEMA_NAME");
   }

   public ResultSet getTableTypes() throws SQLException {
      return this.executeQuery("SELECT 'LOCAL TEMPORARY' TABLE_TYPE UNION SELECT 'SYSTEM TABLE' TABLE_TYPE UNION SELECT 'SYSTEM VIEW' TABLE_TYPE UNION SELECT 'TABLE' TABLE_TYPE UNION SELECT 'VIEW' TABLE_TYPE");
   }

   public ResultSet getColumnPrivileges(String catalog, String schema, String table, String columnNamePattern) throws SQLException {
      if (table == null) {
         throw new SQLException("'table' parameter must not be null");
      } else {
         StringBuilder sb = new StringBuilder("SELECT TABLE_SCHEMA TABLE_CAT, NULL TABLE_SCHEM, TABLE_NAME, COLUMN_NAME, NULL AS GRANTOR, GRANTEE, PRIVILEGE_TYPE AS PRIVILEGE, IS_GRANTABLE FROM INFORMATION_SCHEMA.COLUMN_PRIVILEGES");
         String database = this.conf.useCatalogTerm() == CatalogTerm.UseCatalog ? catalog : schema;
         boolean firstCondition = this.databaseCond(true, sb, "TABLE_SCHEMA", database, false);
         sb.append(firstCondition ? " WHERE " : " AND ").append(" TABLE_NAME = ").append(this.escapeQuote(table));
         this.patternCond(false, sb, "COLUMN_NAME", columnNamePattern);
         sb.append(" ORDER BY COLUMN_NAME, PRIVILEGE_TYPE");
         return this.executeQuery(sb.toString());
      }
   }

   public ResultSet getTablePrivileges(String catalog, String schemaPattern, String tableNamePattern) throws SQLException {
      StringBuilder sb = new StringBuilder("SELECT TABLE_SCHEMA TABLE_CAT, NULL TABLE_SCHEM, TABLE_NAME, NULL GRANTOR,GRANTEE, PRIVILEGE_TYPE PRIVILEGE, IS_GRANTABLE FROM INFORMATION_SCHEMA.TABLE_PRIVILEGES");
      String database = this.conf.useCatalogTerm() == CatalogTerm.UseCatalog ? catalog : schemaPattern;
      boolean firstCondition = this.databaseCond(true, sb, "TABLE_SCHEMA", database, this.conf.useCatalogTerm() == CatalogTerm.UseSchema);
      this.patternCond(firstCondition, sb, "TABLE_NAME", tableNamePattern);
      sb.append(" ORDER BY TABLE_SCHEMA, TABLE_NAME,  PRIVILEGE_TYPE ");
      return this.executeQuery(sb.toString());
   }

   public ResultSet getVersionColumns(String catalog, String schema, String table) throws SQLException {
      String sql = "SELECT 0 SCOPE, ' ' COLUMN_NAME, 0 DATA_TYPE, ' ' TYPE_NAME, 0 COLUMN_SIZE, 0 BUFFER_LENGTH, 0 DECIMAL_DIGITS, 0 PSEUDO_COLUMN  FROM DUAL WHERE 1 = 0";
      return this.executeQuery(sql);
   }

   public ResultSet getCrossReference(String parentCatalog, String parentSchema, String parentTable, String foreignCatalog, String foreignSchema, String foreignTable) throws SQLException {
      StringBuilder sb = (new StringBuilder("SELECT ")).append(this.conf.useCatalogTerm() == CatalogTerm.UseCatalog ? "KCU.REFERENCED_TABLE_SCHEMA PKTABLE_CAT, NULL PKTABLE_SCHEM" : "KCU.TABLE_CATALOG PKTABLE_CAT, KCU.REFERENCED_TABLE_SCHEMA PKTABLE_SCHEM").append(", KCU.REFERENCED_TABLE_NAME PKTABLE_NAME, KCU.REFERENCED_COLUMN_NAME PKCOLUMN_NAME, ").append(this.conf.useCatalogTerm() == CatalogTerm.UseCatalog ? "KCU.TABLE_SCHEMA FKTABLE_CAT, NULL FKTABLE_SCHEM" : "KCU.TABLE_CATALOG FKTABLE_CAT, KCU.TABLE_SCHEMA FKTABLE_SCHEM").append(", KCU.TABLE_NAME FKTABLE_NAME, KCU.COLUMN_NAME FKCOLUMN_NAME, KCU.POSITION_IN_UNIQUE_CONSTRAINT KEY_SEQ, CASE update_rule    WHEN 'RESTRICT' THEN 1   WHEN 'NO ACTION' THEN 3   WHEN 'CASCADE' THEN 0   WHEN 'SET NULL' THEN 2   WHEN 'SET DEFAULT' THEN 4 END UPDATE_RULE, CASE DELETE_RULE  WHEN 'RESTRICT' THEN 1  WHEN 'NO ACTION' THEN 3  WHEN 'CASCADE' THEN 0  WHEN 'SET NULL' THEN 2  WHEN 'SET DEFAULT' THEN 4 END DELETE_RULE, RC.CONSTRAINT_NAME FK_NAME, RC.UNIQUE_CONSTRAINT_NAME PK_NAME,7 DEFERRABILITY FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE KCU INNER JOIN INFORMATION_SCHEMA.REFERENTIAL_CONSTRAINTS RC ON KCU.CONSTRAINT_CATALOG = RC.CONSTRAINT_CATALOG AND KCU.CONSTRAINT_SCHEMA = RC.CONSTRAINT_SCHEMA AND KCU.CONSTRAINT_NAME = RC.CONSTRAINT_NAME AND KCU.TABLE_NAME = RC.TABLE_NAME ");
      String parentDatabase = this.conf.useCatalogTerm() == CatalogTerm.UseCatalog ? parentCatalog : parentSchema;
      String foreignDatabase = this.conf.useCatalogTerm() == CatalogTerm.UseCatalog ? foreignCatalog : foreignSchema;
      boolean firstCondition = this.databaseCond(true, sb, "KCU.REFERENCED_TABLE_SCHEMA", parentDatabase, false);
      firstCondition = this.databaseCond(firstCondition, sb, "KCU.TABLE_SCHEMA", foreignDatabase, false);
      firstCondition = this.patternCond(firstCondition, sb, "KCU.REFERENCED_TABLE_NAME", parentTable);
      this.patternCond(firstCondition, sb, "KCU.TABLE_NAME", foreignTable);
      sb.append("ORDER BY FKTABLE_CAT, FKTABLE_SCHEM, FKTABLE_NAME, KEY_SEQ");
      return this.executeQuery(sb.toString());
   }

   public ResultSet getTypeInfo() {
      String[] columnNames = new String[]{"TYPE_NAME", "DATA_TYPE", "PRECISION", "LITERAL_PREFIX", "LITERAL_SUFFIX", "CREATE_PARAMS", "NULLABLE", "CASE_SENSITIVE", "SEARCHABLE", "UNSIGNED_ATTRIBUTE", "FIXED_PREC_SCALE", "AUTO_INCREMENT", "LOCAL_TYPE_NAME", "MINIMUM_SCALE", "MAXIMUM_SCALE", "SQL_DATA_TYPE", "SQL_DATETIME_SUB", "NUM_PREC_RADIX"};
      DataType[] dataTypes = new DataType[]{DataType.VARCHAR, DataType.INTEGER, DataType.INTEGER, DataType.VARCHAR, DataType.VARCHAR, DataType.VARCHAR, DataType.INTEGER, DataType.BIT, DataType.SMALLINT, DataType.BIT, DataType.BIT, DataType.BIT, DataType.VARCHAR, DataType.SMALLINT, DataType.SMALLINT, DataType.INTEGER, DataType.INTEGER, DataType.INTEGER};
      String[][] baseData = new String[][]{{"BIT", "-7", "1", "", "", "[(M)]", "1", "\u0001", "3", "\u0000", "\u0000", "\u0000", "BIT", "0", "0", "0", "0", "10"}, {"TINYINT", "-6", "3", "", "", "[(M)] [UNSIGNED] [ZEROFILL]", "1", "\u0001", "3", "\u0000", "\u0000", "\u0001", "TINYINT", "0", "0", "0", "0", "10"}, {"TINYINT UNSIGNED", "-6", "3", "", "", "[(M)] [UNSIGNED] [ZEROFILL]", "1", "\u0001", "3", "\u0001", "\u0000", "\u0001", "TINYINT UNSIGNED", "0", "0", "0", "0", "10"}, {"BIGINT", "-5", "19", "", "", "[(M)] [UNSIGNED] [ZEROFILL]", "1", "\u0001", "3", "\u0000", "\u0000", "\u0001", "BIGINT", "0", "0", "0", "0", "10"}, {"BIGINT UNSIGNED", "-5", "20", "", "", "[(M)] [ZEROFILL]", "1", "\u0001", "3", "\u0001", "\u0000", "\u0001", "BIGINT UNSIGNED", "0", "0", "0", "0", "10"}, {"LONG VARBINARY", "-4", "16777215", "'", "'", "", "1", "\u0001", "3", "\u0000", "\u0000", "\u0000", "MEDIUMBLOB", "0", "0", "0", "0", "10"}, {"MEDIUMBLOB", "-4", "16777215", "'", "'", "", "1", "\u0001", "3", "\u0000", "\u0000", "\u0000", "MEDIUMBLOB", "0", "0", "0", "0", "10"}, {"LONGBLOB", "-4", "2147483647", "'", "'", "", "1", "\u0001", "3", "\u0000", "\u0000", "\u0000", "LONGBLOB", "0", "0", "0", "0", "10"}, {"BLOB", "-4", "65535", "'", "'", "", "1", "1", "3", "\u0000", "0", "0", "BLOB", "0", "0", "0", "0", "10"}, {"TINYBLOB", "-3", "255", "'", "'", "", "1", "\u0001", "3", "\u0000", "\u0000", "\u0000", "TINYBLOB", "0", "0", "0", "0", "10"}, {"VARBINARY", "-3", "255", "'", "'", "(M)", "1", "\u0001", "3", "\u0000", "\u0000", "\u0000", "VARBINARY", "0", "0", "0", "0", "10"}, {"BINARY", "-2", "255", "'", "'", "(M)", "1", "1", "3", "\u0000", "\u0000", "\u0000", "BINARY", "0", "0", "0", "0", "10"}, {"LONG VARCHAR", "-1", "16777215", "'", "'", "[CHARACTER SET charset_name] [COLLATE collation_name]", "1", "\u0000", "3", "\u0000", "\u0000", "\u0000", "MEDIUMTEXT", "0", "0", "0", "0", "10"}, {"MEDIUMTEXT", "-1", "16777215", "'", "'", "[CHARACTER SET charset_name] [COLLATE collation_name]", "1", "\u0000", "3", "\u0000", "\u0000", "\u0000", "MEDIUMTEXT", "0", "0", "0", "0", "10"}, {"LONGTEXT", "-1", "2147483647", "'", "'", "[CHARACTER SET charset_name] [COLLATE collation_name]", "1", "\u0000", "3", "\u0000", "\u0000", "\u0000", "LONGTEXT", "0", "0", "0", "0", "10"}, {"TEXT", "-1", "65535", "'", "'", "[CHARACTER SET charset_name] [COLLATE collation_name]", "1", "0", "3", "\u0000", "0", "0", "TEXT", "0", "0", "0", "0", "10"}, {"CHAR", "1", "255", "'", "'", "[(M)] [CHARACTER SET charset_name] [COLLATE collation_name]", "1", "0", "3", "\u0000", "0", "0", "CHAR", "0", "0", "0", "0", "10"}, {"NUMERIC", "2", "65", "", "", "[(M,D])] [ZEROFILL]", "1", "\u0001", "3", "\u0000", "\u0000", "\u0001", "DECIMAL", "-308", "308", "0", "0", "10"}, {"DECIMAL", "3", "65", "", "", "[(M,D])] [ZEROFILL]", "1", "\u0001", "3", "\u0000", "\u0000", "\u0001", "DECIMAL", "-308", "308", "0", "0", "10"}, {"INTEGER", "4", "10", "", "", "[(M)] [UNSIGNED] [ZEROFILL]", "1", "\u0001", "3", "\u0000", "\u0000", "\u0001", "INTEGER", "0", "0", "0", "0", "10"}, {"INTEGER UNSIGNED", "4", "10", "", "", "[(M)] [ZEROFILL]", "1", "\u0001", "3", "\u0001", "\u0000", "\u0001", "INTEGER UNSIGNED", "0", "0", "0", "0", "10"}, {"INT", "4", "10", "", "", "[(M)] [UNSIGNED] [ZEROFILL]", "1", "\u0001", "3", "\u0000", "\u0000", "\u0001", "INT", "0", "0", "0", "0", "10"}, {"INT UNSIGNED", "4", "10", "", "", "[(M)] [ZEROFILL]", "1", "\u0001", "3", "\u0001", "\u0000", "\u0001", "INT UNSIGNED", "0", "0", "0", "0", "10"}, {"MEDIUMINT", "4", "7", "", "", "[(M)] [UNSIGNED] [ZEROFILL]", "1", "\u0001", "3", "\u0000", "\u0000", "\u0001", "MEDIUMINT", "0", "0", "0", "0", "10"}, {"MEDIUMINT UNSIGNED", "4", "8", "", "", "[(M)] [ZEROFILL]", "1", "\u0001", "3", "\u0001", "\u0000", "\u0001", "MEDIUMINT UNSIGNED", "0", "0", "0", "0", "10"}, {"SMALLINT", "5", "5", "", "", "[(M)] [UNSIGNED] [ZEROFILL]", "1", "\u0001", "3", "\u0000", "\u0000", "\u0001", "SMALLINT", "0", "0", "0", "0", "10"}, {"SMALLINT UNSIGNED", "5", "5", "", "", "[(M)] [ZEROFILL]", "1", "\u0001", "3", "\u0001", "\u0000", "\u0001", "SMALLINT UNSIGNED", "0", "0", "0", "0", "10"}, {"FLOAT", "7", "10", "", "", "[(M|D)] [ZEROFILL]", "1", "\u0000", "3", "\u0000", "\u0000", "\u0001", "FLOAT", "-38", "38", "0", "0", "10"}, {"DOUBLE", "8", "17", "", "", "[(M|D)] [ZEROFILL]", "1", "\u0000", "3", "\u0000", "\u0000", "\u0001", "DOUBLE", "-308", "308", "0", "0", "10"}, {"DOUBLE PRECISION", "8", "17", "", "", "[(M,D)] [ZEROFILL]", "1", "\u0000", "3", "\u0000", "\u0000", "\u0001", "DOUBLE PRECISION", "-308", "308", "0", "0", "10"}, {"REAL", "8", "17", "", "", "[(M,D)] [ZEROFILL]", "1", "\u0000", "3", "\u0000", "\u0000", "\u0001", "DOUBLE", "-308", "308", "0", "0", "10"}, {"VARCHAR", "12", "255", "'", "'", "(M)", "1", "\u0000", "3", "\u0000", "\u0000", "\u0000", "VARCHAR", "0", "0", "0", "0", "10"}, {"ENUM", "12", "65535", "'", "'", "[CHARACTER SET charset_name] [COLLATE collation_name]", "1", "\u0000", "3", "\u0000", "\u0000", "\u0000", "ENUM", "0", "0", "0", "0", "10"}, {"TINYTEXT", "12", "255", "'", "'", "[CHARACTER SET charset_name] [COLLATE collation_name]", "1", "\u0000", "3", "\u0000", "\u0000", "\u0000", "TINYTEXT", "0", "0", "0", "0", "10"}, {"SET", "12", "64", "'", "'", "", "1", "\u0000", "3", "\u0000", "\u0000", "\u0000", "SET", "0", "0", "0", "0", "10"}, {"BOOL", "16", "1", "", "", "", "1", "\u0001", "3", "\u0000", "\u0000", "\u0000", "BOOLEAN", "0", "0", "0", "0", "10"}, {"BOOLEAN", "16", "1", "", "", "", "1", "\u0001", "3", "\u0000", "\u0000", "\u0000", "BOOLEAN", "0", "0", "0", "0", "10"}, {"DATE", "91", "10", "'", "'", "", "1", "\u0000", "3", "\u0000", "\u0000", "\u0000", "DATE", "0", "0", "0", "0", "10"}, {"TIME", "92", "18", "'", "'", "[(M)]", "1", "\u0000", "3", "\u0000", "\u0000", "\u0000", "TIME", "0", "0", "0", "0", "10"}, {"DATETIME", "93", "27", "'", "'", "[(M)]", "1", "\u0000", "3", "\u0000", "\u0000", "\u0000", "DATETIME", "0", "0", "0", "0", "10"}, {"TIMESTAMP", "93", "27", "'", "'", "[(M)]", "1", "\u0000", "3", "\u0000", "\u0000", "\u0000", "TIMESTAMP", "0", "0", "0", "0", "10"}};
      String[][] data = baseData;
      if (this.connection.getContext().getVersion().isMariaDBServer() && this.connection.getContext().getVersion().versionGreaterOrEqual(10, 7, 0)) {
         List<String[]> datalist = new ArrayList(Arrays.asList(baseData));
         datalist.add(new String[]{"UUID", "1111", "36", "'", "'", "", "1", "\u0000", "3", "\u0001", "\u0000", "\u0000", "UUID", "0", "0", "0", "0", "10"});
         if (this.connection.getContext().getVersion().isMariaDBServer() && this.connection.getContext().getVersion().versionGreaterOrEqual(11, 7, 1)) {
            datalist.add(new String[]{"VECTOR", "-3", "65532", "_binary '", "'", "(M)", "1", "\u0001", "3", "\u0000", "\u0000", "\u0000", "VECTOR", "0", "0", "0", "0", "10"});
         }

         data = new String[datalist.size()][];
         datalist.sort(Comparator.comparingInt((m) -> {
            return Integer.parseInt(m[1]);
         }));
         datalist.toArray(data);
      }

      return CompleteResult.createResultSet((String[])columnNames, (DataType[])dataTypes, data, this.connection.getContext(), 0, 1004);
   }

   public ResultSet getIndexInfo(String catalog, String schema, String table, boolean unique, boolean approximate) throws SQLException {
      if (table == null) {
         throw new SQLException("'table' parameter must not be null");
      } else {
         StringBuilder sb = (new StringBuilder("SELECT ")).append(this.conf.useCatalogTerm() == CatalogTerm.UseCatalog ? "TABLE_SCHEMA TABLE_CAT, NULL TABLE_SCHEM" : "TABLE_CATALOG TABLE_CAT, TABLE_SCHEMA TABLE_SCHEM").append(", TABLE_NAME, NON_UNIQUE, ").append(this.conf.useCatalogTerm() == CatalogTerm.UseCatalog ? "TABLE_SCHEMA INDEX_QUALIFIER" : "TABLE_CATALOG INDEX_QUALIFIER").append(", INDEX_NAME, 3 TYPE, SEQ_IN_INDEX ORDINAL_POSITION, COLUMN_NAME, COLLATION ASC_OR_DESC, CARDINALITY, NULL PAGES, NULL FILTER_CONDITION FROM INFORMATION_SCHEMA.STATISTICS");
         String database = this.conf.useCatalogTerm() == CatalogTerm.UseCatalog ? catalog : schema;
         boolean firstCondition = this.databaseCond(true, sb, "TABLE_SCHEMA", database, false);
         sb.append(firstCondition ? " WHERE " : " AND ").append("TABLE_NAME = ").append(this.escapeQuote(table));
         if (unique) {
            sb.append(" AND NON_UNIQUE = 0");
         }

         sb.append(" ORDER BY NON_UNIQUE, TYPE, INDEX_NAME, ORDINAL_POSITION");
         return this.executeQuery(sb.toString());
      }
   }

   public boolean supportsResultSetType(int type) {
      return type == 1004 || type == 1003;
   }

   public boolean supportsResultSetConcurrency(int type, int concurrency) {
      return type == 1004 || type == 1003;
   }

   public boolean ownUpdatesAreVisible(int type) {
      return this.supportsResultSetType(type);
   }

   public boolean ownDeletesAreVisible(int type) {
      return this.supportsResultSetType(type);
   }

   public boolean ownInsertsAreVisible(int type) {
      return this.supportsResultSetType(type);
   }

   public boolean othersUpdatesAreVisible(int type) {
      return false;
   }

   public boolean othersDeletesAreVisible(int type) {
      return false;
   }

   public boolean othersInsertsAreVisible(int type) {
      return false;
   }

   public boolean updatesAreDetected(int type) {
      return false;
   }

   public boolean deletesAreDetected(int type) {
      return false;
   }

   public boolean insertsAreDetected(int type) {
      return false;
   }

   public boolean supportsBatchUpdates() {
      return true;
   }

   public ResultSet getUDTs(String catalog, String schemaPattern, String typeNamePattern, int[] types) throws SQLException {
      String sql = "SELECT ' ' TYPE_CAT, NULL TYPE_SCHEM, ' ' TYPE_NAME, ' ' CLASS_NAME, 0 DATA_TYPE, ' ' REMARKS, 0 BASE_TYPE FROM DUAL WHERE 1=0";
      return this.executeQuery(sql);
   }

   public Connection getConnection() {
      return this.connection;
   }

   public boolean supportsSavepoints() {
      return true;
   }

   public boolean supportsNamedParameters() {
      return false;
   }

   public boolean supportsMultipleOpenResults() {
      return false;
   }

   public boolean supportsGetGeneratedKeys() {
      return true;
   }

   public ResultSet getSuperTypes(String catalog, String schemaPattern, String typeNamePattern) throws SQLException {
      String sql = "SELECT  ' ' TYPE_CAT, NULL TYPE_SCHEM, ' ' TYPE_NAME, ' ' SUPERTYPE_CAT, ' ' SUPERTYPE_SCHEM, ' '  SUPERTYPE_NAME FROM DUAL WHERE 1=0";
      return this.executeQuery(sql);
   }

   public ResultSet getSuperTables(String catalog, String schemaPattern, String tableNamePattern) throws SQLException {
      String sql = "SELECT  ' ' TABLE_CAT, ' ' TABLE_SCHEM, ' ' TABLE_NAME, ' ' SUPERTABLE_NAME FROM DUAL WHERE 1=0";
      return this.executeQuery(sql);
   }

   public ResultSet getAttributes(String catalog, String schemaPattern, String typeNamePattern, String attributeNamePattern) throws SQLException {
      String sql = "SELECT ' ' TYPE_CAT, ' ' TYPE_SCHEM, ' ' TYPE_NAME, ' ' ATTR_NAME, 0 DATA_TYPE, ' ' ATTR_TYPE_NAME, 0 ATTR_SIZE, 0 DECIMAL_DIGITS, 0 NUM_PREC_RADIX, 0 NULLABLE, ' ' REMARKS, ' ' ATTR_DEF,  0 SQL_DATA_TYPE, 0 SQL_DATETIME_SUB, 0 CHAR_OCTET_LENGTH, 0 ORDINAL_POSITION, ' ' IS_NULLABLE, ' ' SCOPE_CATALOG, ' ' SCOPE_SCHEMA, ' ' SCOPE_TABLE, 0 SOURCE_DATA_TYPE FROM DUAL  WHERE 1=0";
      return this.executeQuery(sql);
   }

   public boolean supportsResultSetHoldability(int holdability) {
      return holdability == 1;
   }

   public int getResultSetHoldability() {
      return 1;
   }

   public int getDatabaseMajorVersion() {
      return this.connection.getContext().getVersion().getMajorVersion();
   }

   public int getDatabaseMinorVersion() {
      return this.connection.getContext().getVersion().getMinorVersion();
   }

   public int getJDBCMajorVersion() {
      return 4;
   }

   public int getJDBCMinorVersion() {
      return 2;
   }

   public int getSQLStateType() {
      return 2;
   }

   public boolean locatorsUpdateCopy() {
      return false;
   }

   public boolean supportsStatementPooling() {
      return false;
   }

   public RowIdLifetime getRowIdLifetime() {
      return RowIdLifetime.ROWID_UNSUPPORTED;
   }

   public boolean supportsStoredFunctionsUsingCallSyntax() {
      return true;
   }

   public boolean autoCommitFailureClosesAllResultSets() {
      return false;
   }

   public ResultSet getClientInfoProperties() {
      String[] columnNames = new String[]{"NAME", "MAX_LEN", "DEFAULT_VALUE", "DESCRIPTION"};
      DataType[] types = new DataType[]{DataType.VARSTRING, DataType.INTEGER, DataType.VARSTRING, DataType.VARSTRING};
      String[][] data = new String[][]{{"ApplicationName", "16777215", "", "The name of the application currently utilizing the connection"}, {"ClientHostname", "16777215", "", "The hostname of the computer the application using the connection is running on"}, {"ClientUser", "16777215", "", "The name of the user that the application using the connection is performing work for. This may not be the same as the user name that was used in establishing the connection."}};
      return CompleteResult.createResultSet((String[])columnNames, (DataType[])types, data, this.connection.getContext(), 0, 1004);
   }

   public ResultSet getFunctions(String catalog, String schemaPattern, String functionNamePattern) throws SQLException {
      StringBuilder sb = new StringBuilder("SELECT ROUTINE_SCHEMA FUNCTION_CAT,NULL FUNCTION_SCHEM, ROUTINE_NAME FUNCTION_NAME, ROUTINE_COMMENT REMARKS, 1 FUNCTION_TYPE, SPECIFIC_NAME  FROM INFORMATION_SCHEMA.ROUTINES");
      String database = this.conf.useCatalogTerm() == CatalogTerm.UseCatalog ? catalog : schemaPattern;
      boolean firstCondition = this.databaseCond(true, sb, "ROUTINE_SCHEMA", database, this.conf.useCatalogTerm() == CatalogTerm.UseSchema);
      firstCondition = this.patternCond(firstCondition, sb, "ROUTINE_NAME", functionNamePattern);
      sb.append(firstCondition ? " WHERE " : " AND ").append(" ROUTINE_TYPE='FUNCTION'");
      sb.append(" ORDER BY FUNCTION_CAT,FUNCTION_SCHEM,FUNCTION_NAME,SPECIFIC_NAME");
      return this.executeQuery(sb.toString());
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

   public long getMaxLogicalLobSize() {
      return 4294967295L;
   }

   private static class Identifier {
      public String schema;
      public String name;

      private Identifier() {
      }

      // $FF: synthetic method
      Identifier(Object x0) {
         this();
      }
   }
}
