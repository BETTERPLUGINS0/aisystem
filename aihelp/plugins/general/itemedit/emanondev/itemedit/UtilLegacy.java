package emanondev.itemedit;

import emanondev.itemedit.utility.VersionUtils;
import org.jetbrains.annotations.NotNull;

public final class UtilLegacy {
   public static int readPotionEffectDurationSecondsToTicks(@NotNull String value) {
      double durationSecs = readPotionDurationSeconds(value);
      if (durationSecs >= 0.0D) {
         return (int)(durationSecs * 20.0D);
      } else {
         return VersionUtils.isVersionAfter(1, 19, 4) ? -1 : Integer.MAX_VALUE;
      }
   }

   private static double readPotionDurationSeconds(@NotNull String value) {
      if (!value.equalsIgnoreCase("infinite") && !value.equalsIgnoreCase("∞")) {
         return value.equalsIgnoreCase("instant") ? 0.0D : Double.parseDouble(value);
      } else {
         return -1.0D;
      }
   }
}
