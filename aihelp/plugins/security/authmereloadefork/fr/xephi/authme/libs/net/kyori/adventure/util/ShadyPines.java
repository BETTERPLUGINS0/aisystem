package fr.xephi.authme.libs.net.kyori.adventure.util;

import java.util.Set;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

public final class ShadyPines {
   private ShadyPines() {
   }

   /** @deprecated */
   @Deprecated
   @SafeVarargs
   @ApiStatus.ScheduledForRemoval(
      inVersion = "5.0.0"
   )
   @NotNull
   public static <E extends Enum<E>> Set<E> enumSet(final Class<E> type, @NotNull final E... constants) {
      return MonkeyBars.enumSet(type, constants);
   }

   public static boolean equals(final double a, final double b) {
      return Double.doubleToLongBits(a) == Double.doubleToLongBits(b);
   }

   public static boolean equals(final float a, final float b) {
      return Float.floatToIntBits(a) == Float.floatToIntBits(b);
   }
}
