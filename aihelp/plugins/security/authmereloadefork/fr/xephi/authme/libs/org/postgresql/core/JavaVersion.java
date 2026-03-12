package fr.xephi.authme.libs.org.postgresql.core;

public enum JavaVersion {
   v1_8,
   other;

   private static final JavaVersion RUNTIME_VERSION = from(System.getProperty("java.version"));

   public static JavaVersion getRuntimeVersion() {
      return RUNTIME_VERSION;
   }

   public static JavaVersion from(String version) {
      return version.startsWith("1.8") ? v1_8 : other;
   }

   // $FF: synthetic method
   private static JavaVersion[] $values() {
      return new JavaVersion[]{v1_8, other};
   }
}
