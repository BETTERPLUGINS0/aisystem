package ac.grim.grimac.shaded.kyori.adventure.util;

import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import java.time.Duration;

public interface Ticks {
   int TICKS_PER_SECOND = 20;
   long SINGLE_TICK_DURATION_MS = 50L;

   @NotNull
   static Duration duration(final long ticks) {
      return Duration.ofMillis(ticks * 50L);
   }
}
