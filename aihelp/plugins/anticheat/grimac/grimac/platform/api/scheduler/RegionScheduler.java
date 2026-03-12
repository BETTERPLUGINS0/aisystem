package ac.grim.grimac.platform.api.scheduler;

import ac.grim.grimac.api.plugin.GrimPlugin;
import ac.grim.grimac.platform.api.world.PlatformWorld;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.utils.math.Location;

public interface RegionScheduler {
   void execute(@NotNull GrimPlugin var1, @NotNull PlatformWorld var2, int var3, int var4, @NotNull Runnable var5);

   void execute(@NotNull GrimPlugin var1, @NotNull Location var2, @NotNull Runnable var3);

   TaskHandle run(@NotNull GrimPlugin var1, @NotNull PlatformWorld var2, int var3, int var4, @NotNull Runnable var5);

   TaskHandle run(@NotNull GrimPlugin var1, @NotNull Location var2, @NotNull Runnable var3);

   TaskHandle runDelayed(@NotNull GrimPlugin var1, @NotNull PlatformWorld var2, int var3, int var4, @NotNull Runnable var5, long var6);

   TaskHandle runDelayed(@NotNull GrimPlugin var1, @NotNull Location var2, @NotNull Runnable var3, long var4);

   TaskHandle runAtFixedRate(@NotNull GrimPlugin var1, @NotNull PlatformWorld var2, int var3, int var4, @NotNull Runnable var5, long var6, long var8);

   TaskHandle runAtFixedRate(@NotNull GrimPlugin var1, @NotNull Location var2, @NotNull Runnable var3, long var4, long var6);
}
