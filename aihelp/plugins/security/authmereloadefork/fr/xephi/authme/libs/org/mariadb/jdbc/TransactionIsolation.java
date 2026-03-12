package fr.xephi.authme.libs.org.mariadb.jdbc;

public enum TransactionIsolation {
   REPEATABLE_READ("REPEATABLE-READ", 4),
   READ_COMMITTED("READ-COMMITTED", 2),
   READ_UNCOMMITTED("READ-UNCOMMITTED", 1),
   SERIALIZABLE("SERIALIZABLE", 8);

   private final String value;
   private final int level;

   private TransactionIsolation(String value, int level) {
      this.value = value;
      this.level = level;
   }

   public static TransactionIsolation from(String value) {
      TransactionIsolation[] var1 = values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         TransactionIsolation transactionIsolation = var1[var3];
         if (transactionIsolation.value.replaceAll("[ \\-_]", "").equalsIgnoreCase(value.replaceAll("[ \\-_]", ""))) {
            return transactionIsolation;
         }
      }

      throw new IllegalArgumentException(String.format("Wrong argument value '%s' for TransactionIsolation", value));
   }

   public String getValue() {
      return this.value;
   }

   public int getLevel() {
      return this.level;
   }
}
