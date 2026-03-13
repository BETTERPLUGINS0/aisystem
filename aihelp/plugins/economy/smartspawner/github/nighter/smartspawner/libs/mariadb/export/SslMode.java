package github.nighter.smartspawner.libs.mariadb.export;

public enum SslMode {
   DISABLE("disable", new String[]{"DISABLED", "0", "false"}),
   TRUST("trust", new String[]{"REQUIRED"}),
   VERIFY_CA("verify-ca", new String[]{"VERIFY_CA"}),
   VERIFY_FULL("verify-full", new String[]{"VERIFY_IDENTITY", "1", "true"});

   private final String value;
   private final String[] aliases;

   private SslMode(String param3, String[] param4) {
      this.value = value;
      this.aliases = aliases;
   }

   public String getValue() {
      return this.value;
   }

   public static SslMode from(String value) {
      SslMode[] var1 = values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         SslMode sslMode = var1[var3];
         if (sslMode.value.equalsIgnoreCase(value) || sslMode.name().equalsIgnoreCase(value)) {
            return sslMode;
         }

         String[] var5 = sslMode.aliases;
         int var6 = var5.length;

         for(int var7 = 0; var7 < var6; ++var7) {
            String alias = var5[var7];
            if (alias.equalsIgnoreCase(value)) {
               return sslMode;
            }
         }
      }

      throw new IllegalArgumentException(String.format("Wrong argument value '%s' for SslMode", value));
   }

   // $FF: synthetic method
   private static SslMode[] $values() {
      return new SslMode[]{DISABLE, TRUST, VERIFY_CA, VERIFY_FULL};
   }
}
