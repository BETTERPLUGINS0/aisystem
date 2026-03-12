package fr.xephi.authme.libs.org.postgresql;

import java.util.HashMap;
import java.util.Map;
import org.checkerframework.checker.nullness.qual.Nullable;

public enum PGEnvironment {
   ORG_POSTGRESQL_PGPASSFILE("fr.xephi.authme.libs.org.postgresql.pgpassfile", (String)null, "Specified location of password file."),
   PGPASSFILE("PGPASSFILE", "pgpass", "Specified location of password file."),
   ORG_POSTGRESQL_PGSERVICEFILE("fr.xephi.authme.libs.org.postgresql.pgservicefile", (String)null, "Specifies the service resource to resolve connection properties."),
   PGSERVICEFILE("PGSERVICEFILE", "pg_service.conf", "Specifies the service resource to resolve connection properties."),
   PGSYSCONFDIR("PGSYSCONFDIR", (String)null, "Specifies the directory containing the PGSERVICEFILE file");

   private final String name;
   @Nullable
   private final String defaultValue;
   private final String description;
   private static final Map<String, PGEnvironment> PROPS_BY_NAME = new HashMap();

   private PGEnvironment(String name, @Nullable String defaultValue, String description) {
      this.name = name;
      this.defaultValue = defaultValue;
      this.description = description;
   }

   public String getName() {
      return this.name;
   }

   @Nullable
   public String getDefaultValue() {
      return this.defaultValue;
   }

   public String getDescription() {
      return this.description;
   }

   // $FF: synthetic method
   private static PGEnvironment[] $values() {
      return new PGEnvironment[]{ORG_POSTGRESQL_PGPASSFILE, PGPASSFILE, ORG_POSTGRESQL_PGSERVICEFILE, PGSERVICEFILE, PGSYSCONFDIR};
   }

   static {
      PGEnvironment[] var0 = values();
      int var1 = var0.length;

      for(int var2 = 0; var2 < var1; ++var2) {
         PGEnvironment prop = var0[var2];
         if (PROPS_BY_NAME.put(prop.getName(), prop) != null) {
            throw new IllegalStateException("Duplicate PGProperty name: " + prop.getName());
         }
      }

   }
}
