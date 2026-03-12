package fr.xephi.authme.util;

import java.util.UUID;

public final class UuidUtils {
   private UuidUtils() {
   }

   public static UUID parseUuidSafely(String string) {
      try {
         return string == null ? null : UUID.fromString(string);
      } catch (IllegalArgumentException var2) {
         return null;
      }
   }
}
