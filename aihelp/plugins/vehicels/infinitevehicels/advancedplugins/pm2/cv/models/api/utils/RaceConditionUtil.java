package advancedplugins.pm2.cv.models.api.utils;

import advancedplugins.pm2.cv.models.api.utils.ticker.DualTicker;
import java.util.ConcurrentModificationException;

public class RaceConditionUtil {
   public static void wrapConmod(Runnable var0) {
      wrapConmod(var0, 1);
   }

   public static void wrapConmod(Runnable var0, int var1) {
      wrapConmod(var0, var1, 1);
   }

   public static void wrapConmod(Runnable var0, int var1, int var2) {
      try {
         var0.run();
      } catch (ConcurrentModificationException var4) {
         if (var2 > 0) {
            DualTicker.queueDelayedSyncTask(() -> {
               wrapConmod(var0, var1, var2 - 1);
            }, var1);
         }
      } catch (Throwable var5) {
         var5.printStackTrace();
      }

   }

   public static void wrapAll(Runnable var0) {
      wrapConmod(var0, 1);
   }

   public static void wrapAll(Runnable var0, int var1) {
      wrapConmod(var0, var1, 1);
   }

   public static void wrapAll(Runnable var0, int var1, int var2) {
      try {
         var0.run();
      } catch (Throwable var4) {
         if (var2 > 0) {
            DualTicker.queueDelayedSyncTask(() -> {
               wrapConmod(var0, var1, var2 - 1);
            }, var1);
         } else {
            var4.printStackTrace();
         }
      }

   }
}
