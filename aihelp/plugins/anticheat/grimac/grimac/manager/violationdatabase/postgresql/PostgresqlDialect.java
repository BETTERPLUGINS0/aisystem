package ac.grim.grimac.manager.violationdatabase.postgresql;

import ac.grim.grimac.manager.violationdatabase.DatabaseDialect;

public class PostgresqlDialect implements DatabaseDialect {
   public String getUuidColumnType() {
      return "UUID";
   }

   public String getAutoIncrementPrimaryKeySyntax() {
      return "BIGSERIAL PRIMARY KEY";
   }

   public String getInsertOrIgnoreSyntax(String tableName, String columnNames) {
      return "INSERT INTO " + tableName + " (" + columnNames + ") VALUES (?) ON CONFLICT DO NOTHING";
   }

   public String getUniqueConstraintViolationSQLState() {
      return "23505";
   }

   public int getUniqueConstraintViolationErrorCode() {
      return 0;
   }
}
