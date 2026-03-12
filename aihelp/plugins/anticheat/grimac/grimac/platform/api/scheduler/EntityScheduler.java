package ac.grim.grimac.platform.api.scheduler;

import ac.grim.grimac.api.plugin.GrimPlugin;
import ac.grim.grimac.platform.api.entity.GrimEntity;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;

public interface EntityScheduler {
   void execute(@NotNull GrimEntity var1, @NotNull GrimPlugin var2, @NotNull Runnable var3, @Nullable Runnable var4, long var5);

   TaskHandle run(@NotNull GrimEntity var1, @NotNull GrimPlugin var2, @NotNull Runnable var3, @Nullable Runnable var4);

   TaskHandle runDelayed(@NotNull GrimEntity var1, @NotNull GrimPlugin var2, @NotNull Runnable var3, @Nullable Runnable var4, long var5);

   TaskHandle runAtFixedRate(@NotNull GrimEntity var1, @NotNull GrimPlugin var2, @NotNull Runnable var3, @Nullable Runnable var4, long var5, long var7);
}
