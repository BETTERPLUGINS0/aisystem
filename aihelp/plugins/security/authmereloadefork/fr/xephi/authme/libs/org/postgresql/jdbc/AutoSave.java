package fr.xephi.authme.libs.org.postgresql.jdbc;

import java.util.Locale;

public enum AutoSave {
   NEVER,
   ALWAYS,
   CONSERVATIVE;

   private final String value;

   private AutoSave() {
      this.value = this.name().toLowerCase(Locale.ROOT);
   }

   public String value() {
      return this.value;
   }

   public static AutoSave of(String value) {
      return valueOf(value.toUpperCase(Locale.ROOT));
   }

   // $FF: synthetic method
   private static AutoSave[] $values() {
      return new AutoSave[]{NEVER, ALWAYS, CONSERVATIVE};
   }
}
