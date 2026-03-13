package github.nighter.smartspawner.libs.mariadb.util.constants;

public enum MetaExportedKeys {
   UseInformationSchema,
   UseShowCreate,
   Auto;

   public static MetaExportedKeys from(String value) {
      MetaExportedKeys[] var1 = values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         MetaExportedKeys val = var1[var3];
         if (val.name().equalsIgnoreCase(value)) {
            return val;
         }
      }

      throw new IllegalArgumentException(String.format("Wrong argument value '%s' for MetaExportedKeys", value));
   }

   // $FF: synthetic method
   private static MetaExportedKeys[] $values() {
      return new MetaExportedKeys[]{UseInformationSchema, UseShowCreate, Auto};
   }
}
