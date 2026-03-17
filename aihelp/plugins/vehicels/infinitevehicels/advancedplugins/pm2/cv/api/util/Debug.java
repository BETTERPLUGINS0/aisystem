package advancedplugins.pm2.cv.api.util;

import advancedplugins.pm2.cv.api.InfiniteVehicles;

public class Debug {
   public static void log(String... var0) {
      String[] var1 = var0;
      int var2 = var0.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         String var4 = var1[var3];
         InfiniteVehicles.getPlugin().getLogger().info("IV-DEBUG | " + var4);
      }

   }
}
