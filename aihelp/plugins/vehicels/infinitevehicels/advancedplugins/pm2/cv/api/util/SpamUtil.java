package advancedplugins.pm2.cv.api.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SpamUtil {
   private static final Map<String, Long> spamStorage = new ConcurrentHashMap();

   public static boolean isSpam(String var0) {
      if (spamStorage.containsKey(var0)) {
         Long var1 = (Long)spamStorage.get(var0);
         if (var1 < System.currentTimeMillis()) {
            removeSpam(var0);
            return false;
         } else {
            return true;
         }
      } else {
         return false;
      }
   }

   public static void addSpam(String var0, long var1) {
      spamStorage.put(var0, System.currentTimeMillis() + var1 * 50L);
   }

   public static void removeSpam(String var0) {
      spamStorage.remove(var0);
   }

   public static int getRemainingSeconds(String var0) {
      if (spamStorage.containsKey(var0)) {
         Long var1 = (Long)spamStorage.get(var0);
         if (var1 < System.currentTimeMillis()) {
            removeSpam(var0);
            return 0;
         } else {
            return (int)((var1 - System.currentTimeMillis()) / 50L / 20L);
         }
      } else {
         return 0;
      }
   }
}
