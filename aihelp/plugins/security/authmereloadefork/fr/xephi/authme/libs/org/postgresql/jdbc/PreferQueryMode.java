package fr.xephi.authme.libs.org.postgresql.jdbc;

public enum PreferQueryMode {
   SIMPLE("simple"),
   EXTENDED_FOR_PREPARED("extendedForPrepared"),
   EXTENDED("extended"),
   EXTENDED_CACHE_EVERYTHING("extendedCacheEverything");

   private final String value;

   private PreferQueryMode(String value) {
      this.value = value;
   }

   public static PreferQueryMode of(String mode) {
      PreferQueryMode[] var1 = values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         PreferQueryMode preferQueryMode = var1[var3];
         if (preferQueryMode.value.equals(mode)) {
            return preferQueryMode;
         }
      }

      return EXTENDED;
   }

   public String value() {
      return this.value;
   }

   // $FF: synthetic method
   private static PreferQueryMode[] $values() {
      return new PreferQueryMode[]{SIMPLE, EXTENDED_FOR_PREPARED, EXTENDED, EXTENDED_CACHE_EVERYTHING};
   }
}
