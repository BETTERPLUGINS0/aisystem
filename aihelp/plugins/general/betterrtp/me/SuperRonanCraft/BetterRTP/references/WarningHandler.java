package me.SuperRonanCraft.BetterRTP.references;

import java.util.HashMap;
import me.SuperRonanCraft.BetterRTP.BetterRTP;

public class WarningHandler {
   HashMap<WarningHandler.WARNING, Long> lastWarning = new HashMap();

   public static void warn(WarningHandler.WARNING type, String str) {
      warn(type, str, true);
   }

   public static void warn(WarningHandler.WARNING type, String str, boolean auto_ignore) {
      WarningHandler handler = BetterRTP.getInstance().getWarningHandler();
      if (auto_ignore) {
         Long lastTime = (Long)handler.lastWarning.getOrDefault(type, 0L);
         if (lastTime <= System.currentTimeMillis()) {
            BetterRTP.getInstance().getLogger().info(str);
            lastTime = lastTime + System.currentTimeMillis() + 1800000L;
         }

         handler.lastWarning.put(type, lastTime);
      } else {
         BetterRTP.getInstance().getLogger().warning(str);
      }

   }

   public static enum WARNING {
      USELOCATION_ENABLED_NO_LOCATION_AVAILABLE,
      NO_WORLD_TYPE_DECLARED;

      // $FF: synthetic method
      private static WarningHandler.WARNING[] $values() {
         return new WarningHandler.WARNING[]{USELOCATION_ENABLED_NO_LOCATION_AVAILABLE, NO_WORLD_TYPE_DECLARED};
      }
   }
}
