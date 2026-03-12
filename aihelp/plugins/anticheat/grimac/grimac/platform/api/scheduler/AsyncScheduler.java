package ac.grim.grimac.platform.api.scheduler;

import ac.grim.grimac.api.plugin.GrimPlugin;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import java.util.concurrent.TimeUnit;

public interface AsyncScheduler {
   TaskHandle runNow(@NotNull GrimPlugin var1, @NotNull Runnable var2);

   TaskHandle runDelayed(@NotNull GrimPlugin var1, @NotNull Runnable var2, long var3, @NotNull TimeUnit var5);

   TaskHandle runAtFixedRate(@NotNull GrimPlugin var1, @NotNull Runnable var2, long var3, long var5, @NotNull TimeUnit var7);

   TaskHandle runAtFixedRate(@NotNull GrimPlugin var1, @NotNull Runnable var2, long var3, long var5);

   void cancel(@NotNull GrimPlugin var1);
}
