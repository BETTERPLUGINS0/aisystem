package ac.grim.grimac.manager.violationdatabase;

public interface DatabaseDialect {
   String getUuidColumnType();

   String getAutoIncrementPrimaryKeySyntax();

   String getInsertOrIgnoreSyntax(String var1, String var2);

   String getUniqueConstraintViolationSQLState();

   int getUniqueConstraintViolationErrorCode();
}
