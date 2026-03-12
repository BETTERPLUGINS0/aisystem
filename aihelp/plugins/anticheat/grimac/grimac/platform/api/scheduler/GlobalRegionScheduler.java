package ac.grim.grimac.platform.api.scheduler;

import ac.grim.grimac.api.plugin.GrimPlugin;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;

public interface GlobalRegionScheduler {
   void execute(@NotNull GrimPlugin var1, @NotNull Runnable var2);

   TaskHandle run(@NotNull GrimPlugin var1, @NotNull Runnable var2);

   TaskHandle runDelayed(@NotNull GrimPlugin var1, @NotNull Runnable var2, long var3);

   TaskHandle runAtFixedRate(@NotNull GrimPlugin var1, @NotNull Runnable var2, long var3, long var5);

   void cancel(@NotNull GrimPlugin var1);
}
