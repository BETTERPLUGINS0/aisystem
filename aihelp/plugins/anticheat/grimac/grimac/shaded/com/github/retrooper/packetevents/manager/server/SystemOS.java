package ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server;

import java.util.Locale;

public enum SystemOS {
   WINDOWS,
   MACOS,
   LINUX,
   OTHER;

   private static SystemOS CACHE;

   public static SystemOS getOSNoCache() {
      String os = System.getProperty("os.name").toLowerCase(Locale.ROOT);
      SystemOS[] var1 = values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         SystemOS sysos = var1[var3];
         if (os.contains(sysos.name().toLowerCase(Locale.ROOT))) {
            return sysos;
         }
      }

      return OTHER;
   }

   public static SystemOS getOS() {
      if (CACHE == null) {
         CACHE = getOSNoCache();
      }

      return CACHE;
   }

   // $FF: synthetic method
   private static SystemOS[] $values() {
      return new SystemOS[]{WINDOWS, MACOS, LINUX, OTHER};
   }
}
