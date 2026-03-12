package ac.grim.grimac.manager.violationdatabase.mysql;

import ac.grim.grimac.manager.violationdatabase.DatabaseDialect;

public class MySQLDialect implements DatabaseDialect {
   public String getUuidColumnType() {
      return "BINARY(16)";
   }

   public String getAutoIncrementPrimaryKeySyntax() {
      return "BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT";
   }

   public String getInsertOrIgnoreSyntax(String tableName, String columnNames) {
      return "INSERT IGNORE INTO " + tableName + " (" + columnNames + ") VALUES (?)";
   }

   public String getUniqueConstraintViolationSQLState() {
      return "23000";
   }

   public int getUniqueConstraintViolationErrorCode() {
      return 1062;
   }
}
