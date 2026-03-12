package ac.grim.grimac.manager.violationdatabase.sqlite;

import ac.grim.grimac.manager.violationdatabase.DatabaseDialect;

public class SQLiteDialect implements DatabaseDialect {
   public String getUuidColumnType() {
      return "BLOB";
   }

   public String getAutoIncrementPrimaryKeySyntax() {
      return "INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT";
   }

   public String getInsertOrIgnoreSyntax(String tableName, String columnNames) {
      return "INSERT OR IGNORE INTO " + tableName + " (" + columnNames + ") VALUES (?)";
   }

   public String getUniqueConstraintViolationSQLState() {
      return "23000";
   }

   public int getUniqueConstraintViolationErrorCode() {
      return 19;
   }
}
